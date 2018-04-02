package edu.purdue.entreemobile.entreeproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import edu.purdue.entreemobile.entreeproject.util.asyctask.UpdateAsync;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        updateUI();
    }

    private void updateUI() {

        // add hamburger
        addCategoryItem(getString(R.string.category_hamburger), R.drawable.category_hamburger);

        // add chinese food category
        addCategoryItem(getString(R.string.category_chinesefoods), R.drawable.category_chinese);

        // add coffee food category
        addCategoryItem(getString(R.string.category_cafetea), R.drawable.category_coffee);

        // add japanese food
        addCategoryItem(getString(R.string.category_japanfoods), R.drawable.category_japan);

        // add pasta
        addCategoryItem(getString(R.string.category_pasta), R.drawable.category_pasta);

        final EditText searchTextView = (EditText) findViewById(R.id.searchText);
        searchTextView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    String queryString = searchTextView.getText().toString();
                    if (!queryString.equals(""))
                        onSearch(queryString);
                }
                return false;
            }
        });
    }

    public void addCategoryItem(final String name, final int imageid) {
        final LinearLayout linear = (LinearLayout) findViewById(R.id.linearLayout_02);

        UpdateAsync asyncUpdateTask = new UpdateAsync(this.getApplicationContext()) {
            private LinearLayout layout;

            @Override
            protected void onUpdate() {
                TextView text;
                ImageView image;
                LayoutInflater inflater;

                inflater = LayoutInflater.from(this.getCurrentContext());
                layout = (LinearLayout) inflater.inflate(R.layout.category_item, null, false);
                text = (TextView) layout.findViewById(R.id.name);
                text.setText(name);
                image = (ImageView) layout.findViewById(R.id.image);
                image.setBackgroundResource(imageid);
                layout.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        onSearch(name);
                        return false;
                    }
                });
            }

            @Override
            protected void afterUpdate() {
                linear.addView(layout);
            }
        };

        asyncUpdateTask.execute();
    }

    public void onSearchClick(View v) {
        EditText searchText = (EditText) findViewById(R.id.searchText);
        String queryString = searchText.getText().toString();
        if(queryString!=null && !queryString.equals(""))
            onSearch(queryString);
    }

    public void onSearch(String queryString) {
        Intent intent = new Intent(MainActivity.this, ResultListActivity.class);
        intent.putExtra("searchText", queryString.trim());
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, UserProfileActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return true;
    }
}
