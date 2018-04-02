package edu.purdue.entreemobile.entreeproject.util.asyctask;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import edu.purdue.entreemobile.entreeproject.util.db.CacheImageHelper;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

    ImageView bmImage;
    Context context;

    public DownloadImageTask(Context context, ImageView bmImage) {
        this.bmImage = bmImage;
        this.context = context;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        String filename = "";
        boolean cacheIsExisted = false;

        // search if the url has been cached
        CacheImageHelper mDbHelper = new CacheImageHelper(context, CacheImageHelper.DATABASE_ID_IMAGE_CACHE);
        // Gets the data repository in write mode
        SQLiteDatabase dbReader = mDbHelper.getReadableDatabase();
        Log.d("TEST", "Database path: " + dbReader.getPath());

        String[] projection = {CacheImageHelper.IMAGE_CACHE_COLUMN_FILENAME};
        String selection = CacheImageHelper.IMAGE_CACHE_COLUMN_URL + " = ?";
        String[] selectionArgs = {urldisplay};
        String sortOrder = "";

        Cursor res = dbReader.query(
                CacheImageHelper.IMAGE_CACHE_TABLE_NAME,  // The table to query
                projection,                       // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );
        if(res.moveToFirst()) {
            filename = res.getString(res.getColumnIndex(CacheImageHelper.IMAGE_CACHE_COLUMN_FILENAME));
            cacheIsExisted = true;
            Log.d("TEST", "SQLITE: Load image filename: "+filename);
        }

        if(!res.isClosed()) {
            res.close();
        }
        dbReader.close();

        Bitmap mIcon11 = null;
        try {
            InputStream in;
            if(cacheIsExisted) {
                String filepath = context.getFilesDir().getPath().toString() + "/" + filename + ".small.jpeg";
                Log.d("TEST", "Load cache image: "+filepath);
                in = new FileInputStream(new File(filepath));
                Log.d("TEST", "Load cache image success!");
            }
            else {
                in = new java.net.URL(urldisplay).openStream();
                Log.d("TEST", "Download image from "+urldisplay);
            }

            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        // if image was not existed, cache the bitmap
        if(!cacheIsExisted) {
            filename =
                Long.toString(System.currentTimeMillis())+
                "_"+
                Integer.toString(new Random().nextInt(10000000));

            String filepath = context.getFilesDir().getPath().toString() + "/" + filename;
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(filepath+".small.jpeg");
                mIcon11.compress(Bitmap.CompressFormat.JPEG, 15, out);
                out = new FileOutputStream(filepath+".large.jpeg");
                mIcon11.compress(Bitmap.CompressFormat.JPEG, 100, out);

                mIcon11 = BitmapFactory.decodeFile(filepath+".small.jpeg");
                Log.d("TEST", "Save cache image: " + filepath + " from " + urldisplay);
                // bmp is your Bitmap instance
                // PNG is a lossless format, the compression factor (100) is ignored

                SQLiteDatabase dbWriter = mDbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(CacheImageHelper.IMAGE_CACHE_COLUMN_URL, urldisplay);
                values.put(CacheImageHelper.IMAGE_CACHE_COLUMN_FILENAME, filename);
                long newRowId;
                newRowId = dbWriter.insert(
                        CacheImageHelper.IMAGE_CACHE_TABLE_NAME,
                        null,
                        values);
                dbWriter.close();
                Log.d("TEST", "SQLITE: Add local image: ["+newRowId+"] "+filename+" from "+urldisplay);

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    }
}