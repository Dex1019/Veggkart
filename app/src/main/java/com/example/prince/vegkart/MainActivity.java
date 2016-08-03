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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener {

  private TabLayout tabLayout;
  private String Category;
  private ArrayList<ArrayList<Product>> items = new ArrayList<>();
  private RecyclerView productListView;
  private MyRecyclerViewAdapter adapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    try {
      setContentView(R.layout.activity_main);

      sendRequest();

      Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
      setSupportActionBar(toolbar);

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
      tabLayout = (TabLayout) findViewById(R.id.tabs);

      for (int j = 0; j < 7; j++) {
        items.add(new ArrayList<Product>());
        for (int i = 1; i < 25; i++) {
          items.get(j).add(new Product((j + 1) + "Name" + i, "1Description" + i, String.valueOf(i), ""));
        }
      }

      productListView = (RecyclerView) findViewById(R.id.mainListView);
      productListView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  /**
   * This Method sendRequest(), makes a request queue using volley.
   */
  private void sendRequest() {
    try {
      JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, getString(R.string.API_BASE_URL) + getString(R.string.API_JSON_URL), null, new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
          try {
            if (items.size() < 1) {
              items.add(new ArrayList<>(Arrays.asList((new Gson()).fromJson(response.getJSONArray("UArray").toString(), Product[].class))));
              adapter.setItems(items.get(0));
            } else {
              items.set(0, new ArrayList<>(Arrays.asList((new Gson()).fromJson(response.getJSONArray("UArray").toString(), Product[].class))));
              adapter.setItems(items.get(0));
            }
          } catch (JSONException e) {
            e.printStackTrace();
          }
        }
      }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {

        }
      });

      VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
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
    switch (tabPosition) {
      case 0:
        Category = "cate1";
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
    }
    adapter = new MyRecyclerViewAdapter(MainActivity.this, items.get(tabPosition));

    productListView.setAdapter(adapter);
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
  protected void onStart() {
    super.onStart();
    bindWidgetWithEvent();

    if (tabLayout != null && tabLayout.getTabCount() == 0) {
      tabLayout.addTab(tabLayout.newTab().setText("ONE"), true);
      tabLayout.addTab(tabLayout.newTab().setText("TWO"));
      tabLayout.addTab(tabLayout.newTab().setText("THREE"));
      tabLayout.addTab(tabLayout.newTab().setText("FOUR"));
      tabLayout.addTab(tabLayout.newTab().setText("FIVE"));
    }
  }
}
