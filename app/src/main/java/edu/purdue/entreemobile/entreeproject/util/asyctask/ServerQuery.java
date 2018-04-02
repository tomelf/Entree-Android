package edu.purdue.entreemobile.entreeproject.util.asyctask;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import edu.purdue.entreemobile.entreeproject.util.db.CacheImageHelper;

/**
 * Created by kane on 3/7/15.
 */
public class ServerQuery extends AsyncTask<Void, Void, Void> {
    String queryResult;
    String url;
    Context context;

    public ServerQuery(Context context, String url, String... params) {
        customPreExecute(url,params);
        this.url = url;
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... params) {
        queryResult = queryServer(url);
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        customPostExecute(queryResult);
    }

    protected void customPreExecute(String url, String... params) {
        return;
    }

    protected void customPostExecute(String queryResult) {
        return;
    }

    private String queryServer(String url, String... params) {
        StringBuilder builder = new StringBuilder();
        String queryResult = "";

        boolean cacheIsExisted = false;

        // search if the url has been cached
        CacheImageHelper mDbHelper = new CacheImageHelper(context, CacheImageHelper.DATABASE_ID_QUERY_CACHE);
        // Gets the data repository in write mode
        SQLiteDatabase dbReader = mDbHelper.getReadableDatabase();
        Log.d("TEST", "Database path: " + dbReader.getPath());

        String[] projection = {CacheImageHelper.QUERY_RESULT_COLUMN_RESULT};
        String selection = CacheImageHelper.QUERY_RESULT_COLUMN_QUERYURL + " = ?";
        String[] selectionArgs = {url};
        String sortOrder = "";

        Cursor res = dbReader.query(
                CacheImageHelper.QUERY_RESULT_TABLE_NAME,  // The table to query
                projection,                       // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );
        if(res.moveToFirst()) {
            queryResult = res.getString(res.getColumnIndex(CacheImageHelper.QUERY_RESULT_COLUMN_RESULT));
            cacheIsExisted = true;
            Log.d("TEST", "SQLITE: Load query "+url+" result: "+queryResult);
        }

        if(!res.isClosed()) {
            res.close();
        }
        dbReader.close();

        if(cacheIsExisted) {
            return queryResult;
        }

        // if cacheIsExisted is false
        String result = "";
        HttpClient client = new DefaultHttpClient();
        HttpGet httpPost = new HttpGet(url);
        try{
            HttpResponse response = client.execute(httpPost);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if(statusCode == 200){
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while((line = reader.readLine()) != null){
                    builder.append(line);
                }

                result = builder.toString();

                SQLiteDatabase dbWriter = mDbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(CacheImageHelper.QUERY_RESULT_COLUMN_QUERYURL, url);
                values.put(CacheImageHelper.QUERY_RESULT_COLUMN_RESULT, result);
                long newRowId;
                newRowId = dbWriter.insert(
                        CacheImageHelper.QUERY_RESULT_TABLE_NAME,
                        null,
                        values);
                dbWriter.close();
                Log.d("TEST", "SQLITE: Add query result: ["+newRowId+"] "+url+" >> "+result);

            } else {
                Log.e("ServerQuery", "Failed JSON object: "+statusCode);
            }
        } catch (ClientProtocolException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }

        return result;
    }
}