package com.example.prince.vegkart;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String JSON_URL = "http://anas.netau.net/api/crud_api.php";

    private JSONParse pj;

    private TabLayout tabLayout;
    private String Category;
    private List<Product>[] items = new ArrayList[7];
    private ListView productListView;
    private MyListViewAdapter adapter, tAdapter;
    //private RequestQueue requestQueue = Volley.newRequestQueue(this);

    FragmentManager fm = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_main);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

/*
        frag_button = (Button) findViewById(R.id.frag_button);
        frag_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Contact_Us_Activity dFragment = new Contact_Us_Activity();
//                 Show DialogFragment
                dFragment.show(fm, "Dialog Fragment");
            }
        });
*/
            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(MainActivity.this, cart_activity.class));
                }
            });

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

//        textView1 = (TextView) findViewById(R.id.textView1);
            tabLayout = (TabLayout) findViewById(R.id.tabs);
            productListView = (ListView) findViewById(R.id.mainListView);
//        items2 = new ArrayList<>();
//        items3 = new ArrayList<>();

            for (int j = 0; j < 7; j++) {
                items[j] = new ArrayList<>();
                for (int i = 1; i < 25; i++) {
                    items[j].add(new Product((j + 1) + "Name" + i, "1Description" + i, String.valueOf(i)));

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /*
* This Method sendRequest(), makes a request queue using volley.
* */
    private void sendRequest() {

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);

            StringRequest stringRequest = new StringRequest(0,JSON_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("TAG", response);
                            showJSON(response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("Error",error.toString());
                            Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
            );

            requestQueue.add(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
* This method parses data from jason object to listView using custom adapter.
* */
    private void showJSON(String json) {
        try {
            pj = new JSONParse(json);
            pj.parseJson();
            tAdapter=new MyListViewAdapter(MainActivity.this, JSONParse.productObj);

//            productListView.setAdapter(cl);
        } catch (Exception e) {
            e.printStackTrace();
        }

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

        try {


            if (adapter != null) {
                adapter.clear();
            }
            switch (tabPosition) {
                case 0:
                    Category = "cate1";
                    adapter = tAdapter;

                    break;
                case 1:
                    Category = "cate2";
                    adapter = new MyListViewAdapter(MainActivity.this, items[tabPosition]);

                    break;
                case 2:
                    Category = "cate3";
                    adapter = new MyListViewAdapter(MainActivity.this, items[tabPosition]);

                    break;
                case 3:
                    Category = "cate4";
                    adapter = new MyListViewAdapter(MainActivity.this, items[tabPosition]);

                    break;
                case 4:
                    Category = "cate5";
                    adapter = new MyListViewAdapter(MainActivity.this, items[tabPosition]);

                    break;

            }

            //passing items for list view to adapter then to listview
            productListView.setAdapter(adapter);

//        textView1.setText(Category);
            //populating main view
        }catch (Exception e){
            e.printStackTrace();
        }


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
            FragmentManager fm = getSupportFragmentManager();

            Contact_Us_Activity dFragment = new Contact_Us_Activity();
            // Show DialogFragment
            dFragment.show(fm, "Dialog Fragment");

        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {
            FragmentManager fm = getSupportFragmentManager();

            Contact_Us_Activity dFragment = new Contact_Us_Activity();
            // Show DialogFragment
            dFragment.show(fm, "Dialog Fragment");

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onStart(){
        super.onStart();
        try {

//            Log.d("Test","here it is!!");

            sendRequest();

            bindWidgetWithEvent();
            tabLayout.addTab(tabLayout.newTab().setText("ONE"), true);
            tabLayout.addTab(tabLayout.newTab().setText("TWO"));
            tabLayout.addTab(tabLayout.newTab().setText("THREE"));
            tabLayout.addTab(tabLayout.newTab().setText("FOUR"));
            tabLayout.addTab(tabLayout.newTab().setText("FIVE"));
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
