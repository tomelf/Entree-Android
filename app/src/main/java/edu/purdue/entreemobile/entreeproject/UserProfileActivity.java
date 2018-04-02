package edu.purdue.entreemobile.entreeproject;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class UserProfileActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        TextView text;
        ImageView image;
        LinearLayout linear;
        LayoutInflater inflater;
        LinearLayout layout;
        LinearLayout userinfoLayout;

        linear = (LinearLayout) findViewById(R.id.review_layout);
        inflater = LayoutInflater.from(this.getApplicationContext());
        layout = (LinearLayout) inflater.inflate(R.layout.review_item, null, false);
        userinfoLayout = (LinearLayout) layout.findViewById(R.id.userinfo_layout);
        userinfoLayout.setVisibility(View.GONE);
        linear.addView(layout);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void clickReviews(View v) {
        LinearLayout layout1 = (LinearLayout) findViewById(R.id.review_layout);
        LinearLayout layout2 = (LinearLayout) findViewById(R.id.follower_layout);
        LinearLayout layout3 = (LinearLayout) findViewById(R.id.following_layout);

        layout1.setVisibility(View.VISIBLE);
        layout2.setVisibility(View.GONE);
        layout3.setVisibility(View.GONE);
    }

    public void clickFollowers(View v) {
        LinearLayout layout1 = (LinearLayout) findViewById(R.id.review_layout);
        LinearLayout layout2 = (LinearLayout) findViewById(R.id.follower_layout);
        LinearLayout layout3 = (LinearLayout) findViewById(R.id.following_layout);

        layout1.setVisibility(View.GONE);
        layout2.setVisibility(View.VISIBLE);
        layout3.setVisibility(View.GONE);
    }

    public void clickFollowing(View v) {
        LinearLayout layout1 = (LinearLayout) findViewById(R.id.review_layout);
        LinearLayout layout2 = (LinearLayout) findViewById(R.id.follower_layout);
        LinearLayout layout3 = (LinearLayout) findViewById(R.id.following_layout);

        layout1.setVisibility(View.GONE);
        layout2.setVisibility(View.GONE);
        layout3.setVisibility(View.VISIBLE);
    }
}
