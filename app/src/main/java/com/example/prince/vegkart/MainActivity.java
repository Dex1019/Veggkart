package com.example.prince.vegkart;

import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar1;
    private TabLayout tabLayout;
    private String Category;
    private TextView textView1;
    private Button frag_button;
    private ClipData.Item item;

    FragmentManager fm = getSupportFragmentManager();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        frag_button = (Button) findViewById(R.id.frag_button);
//        frag_button.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View arg0) {
//                Contact_Us_Activity dFragment = new Contact_Us_Activity();
//                // Show DialogFragment
//                dFragment.show(fm, "Dialog Fragment");
//            }
//        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startActivity(new Intent(MainActivity.this,cart_activity.class));
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    textView1 = (TextView) findViewById(R.id.textView1);
    tabLayout = (TabLayout) findViewById(R.id.tabs);

    bindWidgetWithEvent();


    tabLayout.addTab(tabLayout.newTab().setText("ONE"), true);
    tabLayout.addTab(tabLayout.newTab().setText("TWO"));
    tabLayout.addTab(tabLayout.newTab().setText("THREE"));
    tabLayout.addTab(tabLayout.newTab().setText("FOUR"));
    tabLayout.addTab(tabLayout.newTab().setText("FIVE"));
    tabLayout.addTab(tabLayout.newTab().setText("SIX"));
    tabLayout.addTab(tabLayout.newTab().setText("SEVEN"));


}

    private void bindWidgetWithEvent() {
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setCurrentCategory(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }


        });
    }


    private void setCurrentCategory(int tabPosition) {
        switch (tabPosition) {
            case 0:
                Category = "cat1";

                break;
            case 1:
                Category = "cate2";
                break;
            case 2:
                Category = "cate3";
                break;
            case 3:
                Category = "cate4";
                break;
            case 4:
                Category = "cate5";
                break;
            case 5:
                Category = "cate6";
                break;
            case 6:
                Category = "cate7";
                break;
            case 7:
                Category = "cate8";
                break;

        }



        textView1.setText(Category);
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //@Override
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
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            FragmentManager fm= getSupportFragmentManager();

            Contact_Us_Activity dFragment = new Contact_Us_Activity();
            // Show DialogFragment
            dFragment.show(fm, "Dialog Fragment");

        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {
            FragmentManager fm= getSupportFragmentManager();

            Contact_Us_Activity dFragment = new Contact_Us_Activity();
            // Show DialogFragment
            dFragment.show(fm, "Dialog Fragment");

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
