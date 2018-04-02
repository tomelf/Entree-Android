package edu.purdue.entreemobile.entreeproject;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Random;

import edu.purdue.entreemobile.entreeproject.util.asyctask.DownloadImageTask;
import edu.purdue.entreemobile.entreeproject.util.asyctask.ServerQuery;
import edu.purdue.entreemobile.entreeproject.util.asyctask.UpdateAsync;


public class ResultListActivity extends ActionBarActivity {

    private int itemsPerPage = 2000;
    private int currentPage = 1;
    private int totalPage = 0;
    private String currentQuery = "";
    private boolean shouldExecuteOnResume;

    @Override
    public void onResume() {
        super.onResume();

        if(shouldExecuteOnResume){
            onSearch(currentQuery);
        } else{
            shouldExecuteOnResume = true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_list);

        shouldExecuteOnResume = false;

        String sText = getIntent().getStringExtra("searchText");

        this.currentQuery = sText.trim();

        final EditText searchText = (EditText) findViewById(R.id.searchText);
        searchText.setText(sText);
        searchText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction()==KeyEvent.ACTION_DOWN) {
                    String queryString = searchText.getText().toString();
                    currentPage = 1;
                    onSearch(queryString);
                }
                return false;
            }
        });

        updateUI(sText);
    }

    public void onSearchClick(View v) {
        EditText searchText = (EditText) findViewById(R.id.searchText);
        String queryString = searchText.getText().toString();
        if(!queryString.equals("")) {
            currentPage = 1;
            onSearch(queryString.trim());
        }
    }

    public void onSearch(String queryString) {
        this.currentQuery = queryString.trim();
        if(!this.currentQuery.equals("")) {
            updateUI(this.currentQuery);
        }
    }

    private void updateUI(String sText) {
        final Context context = getApplicationContext();
        final LinearLayout listArea = (LinearLayout) findViewById(R.id.linearLayout_02);
        // clear previous child views
        listArea.removeAllViews();

        String encSText = "";
        try {
            encSText = URLEncoder.encode(sText, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        final String serverUrl = "http://foodielist.herokuapp.com/api/menu/list/"+ encSText +"/nitems/"+itemsPerPage+"/"+currentPage;
        final LayoutInflater inflater = LayoutInflater.from(this.getApplicationContext());

        ServerQuery myQuery = new ServerQuery(context, serverUrl) {
            @Override
            protected void customPreExecute(String url, String... params) {
                Log.d(this.getClass().getName(), "customPreExecute: " + url);
            }

            @Override
            protected void customPostExecute(String queryResult) {
                Log.d(this.getClass().getName(), "customPostExecute" + queryResult);

                try {
                    JSONObject jRootObj = new JSONObject(queryResult);
                    int total_pages = jRootObj.getInt("totalPages");
                    totalPage = total_pages;

                    int numItemsInPage = jRootObj.length()-1;

                    for (int i=0; i<numItemsInPage; ++i) {
                        JSONObject jObj = jRootObj.getJSONObject(Integer.toString(i));

                        final String menuName = jObj.getString("menuName");
                        final String restaurantName = jObj.getString("restaurantName");
                        final JSONArray pricesArr = jObj.getJSONArray("prices");

                        String sImgUrl = "";
                        JSONArray urlArr = jObj.getJSONArray("url");
                        if(urlArr.length()>0) {
                            try {
                                sImgUrl = URLDecoder.decode(urlArr.getString(0), "utf-8");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }

                        final String imgUrl = sImgUrl;

                        UpdateAsync updateTask = new UpdateAsync(context) {
                            private LinearLayout listItem;

                            @Override
                            protected void onUpdate() {
                                Log.d("TEST", "onUpdate");
                                listItem = (LinearLayout) inflater.inflate(R.layout.menu_item, null, false);
                                final TextView name = (TextView) listItem.findViewById(R.id.name);
                                final TextView restaurant = (TextView) listItem.findViewById(R.id.restraunt);

                                name.setText(menuName);
                                restaurant.setText("@ "+restaurantName);

                                TextView priceView = (TextView) listItem.findViewById(R.id.money_value);
                                LinearLayout yummyImageLayout = (LinearLayout) listItem.findViewById(R.id.yummy_image_layout);

                                Random randObj = new Random();
                                int randomCount = randObj.nextInt(5) + 1;

                                for(int j=0;j<randomCount;++j) {
                                    switch(randomCount) {
                                        case 5:
                                            yummyImageLayout.findViewById(R.id.yummy_image_5).setVisibility(View.VISIBLE);
                                        case 4:
                                            yummyImageLayout.findViewById(R.id.yummy_image_4).setVisibility(View.VISIBLE);
                                        case 3:
                                            yummyImageLayout.findViewById(R.id.yummy_image_3).setVisibility(View.VISIBLE);
                                        case 2:
                                            yummyImageLayout.findViewById(R.id.yummy_image_2).setVisibility(View.VISIBLE);
                                        case 1:
                                            yummyImageLayout.findViewById(R.id.yummy_image_1).setVisibility(View.VISIBLE);
                                            break;
                                    }
                                }

//                                ImageView moneyChangeImage = (ImageView) listItem.findViewById(R.id.money_change);
//                                Drawable arrow_down = getResources().getDrawable(R.drawable.price_arrow_down);
//                                Drawable arrow_up = getResources().getDrawable(R.drawable.price_arrow_up);
//                                int rand = randObj.nextInt(3);
//                                switch(rand){
//                                    case 1:
//                                        moneyChangeImage.setBackground(arrow_down);
//                                        break;
//                                    case 2:
//                                        moneyChangeImage.setBackground(arrow_up);
//                                        break;
//                                    default:
//                                        moneyChangeImage.setBackground(null);
//                                        break;
//                                }

                                randomCount = new Random().nextInt(200);
                                TextView reviewCount = new TextView(yummyImageLayout.getContext());
                                reviewCount.setTextColor(Color.BLACK);
                                reviewCount.setText(" ("+ randomCount +")");
                                yummyImageLayout.addView(reviewCount);

                                LinearLayout moneyLayout = (LinearLayout) listItem.findViewById(R.id.money_layout);
                                if(pricesArr.length()>0) {
                                    try {
                                        if (pricesArr.length() > 1) {
                                            priceView.setText("$ " + pricesArr.get(0) + " ~ " + "$ " + pricesArr.get(pricesArr.length() - 1));
                                        } else {
                                            if (Float.parseFloat(pricesArr.get(0).toString()) == 0) {
                                                moneyLayout.setVisibility(View.GONE);
                                            } else {
                                                priceView.setText("$ " + pricesArr.get(0));
                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                // load image url
                                ImageView image = (ImageView) listItem.findViewById(R.id.image);
                                if(imgUrl!=null && !imgUrl.equals("")) {
                                    Log.d("TEST", "DownloadImageTask: "+imgUrl);
                                    new DownloadImageTask(context, image).execute(imgUrl);
                                }
                                listItem.setOnTouchListener(new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View v, MotionEvent event) {
                                        //name.setText(menuId);

                                        return true;
                                    }
                                });
                            }

                            @Override
                            protected void afterUpdate() {
                                Log.d("TEST", "afterUIUpdate");
                                listArea.addView(listItem);
                            }
                        };
                        updateTask.execute();
                    }
//                    if(total_pages>1) {
//                        final LinearLayout paginator = (LinearLayout) inflater.inflate(R.layout.paginator, null, false);
//                        Button preBt = (Button) paginator.findViewById(R.id.bt_previous);
//                        Button postBt = (Button) paginator.findViewById(R.id.bt_next);
//                        preBt.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                goPrevious(v);
//                            }
//                        });
//                        postBt.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                goNext(v);
//                            }
//                        });
//                        listArea.addView(paginator);
//                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        myQuery.execute();
    }

    public void goPrevious(View v) {
        if(currentPage <= 1)
            return;
        currentPage--;
        onSearch(currentQuery);

    }

    public void goNext(View v) {
        if(currentPage >= totalPage)
            return;
        currentPage++;
        onSearch(currentQuery);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_result_list, menu);


        return true;
    }
}
