package com.veggkart.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.veggkart.android.R;
import com.veggkart.android.adapter.CategoryAdapter;
import com.veggkart.android.eventlistener.OnAdapterInteractionListener;
import com.veggkart.android.model.Product;
import com.veggkart.android.util.APIHelper;
import com.veggkart.android.util.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class CatalogueActivity extends AppCompatActivity implements View.OnClickListener, Response.Listener<JSONObject>, Response.ErrorListener, OnAdapterInteractionListener {

  private TabLayout tabLayout;
  private ViewPager viewPager;
  private CategoryAdapter categoryAdapter;

  private AppCompatImageButton cartButton;
  private AppCompatTextView orderTotalTextView;
  private AppCompatImageButton checkoutButton;

  public static void launchActivity(AppCompatActivity currentActivity) {
    Intent intent = new Intent(currentActivity, CatalogueActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    currentActivity.startActivity(intent);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    this.initialize();
  }

  private void initialize() {
    setContentView(R.layout.activity_catalogue);

    ActionBar actionBar = this.getSupportActionBar();

    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(false);
      actionBar.setTitle(this.getResources().getString(R.string.app_name));
    }

    this.tabLayout = (TabLayout) this.findViewById(R.id.tabs_subBrokers);
    this.viewPager = (ViewPager) this.findViewById(R.id.viewPager_subBrokers);
    this.categoryAdapter = new CategoryAdapter(this.getSupportFragmentManager(), new ArrayList<Product>(), this);
    viewPager.setAdapter(categoryAdapter);
    tabLayout.setupWithViewPager(viewPager, true);

    this.cartButton = (AppCompatImageButton) this.findViewById(R.id.imageButton_catalogue_cart);
    this.orderTotalTextView = (AppCompatTextView) this.findViewById(R.id.textView_catalogue_orderTotal);
    this.checkoutButton = (AppCompatImageButton) this.findViewById(R.id.imageButton_catalogue_checkout);

    this.cartButton.setOnClickListener(this);
    this.checkoutButton.setOnClickListener(this);

    this.orderTotalTextView.setText(this.getResources().getString(R.string.order_total, 0.0, 0));

    String productsUrl = APIHelper.getEndpointProducts();
    JsonObjectRequest productsRequest = new JsonObjectRequest(Request.Method.GET, productsUrl, new JSONObject(), this, this);
    VolleySingleton.getInstance(this).addToRequestQueue(productsRequest);
  }

  @Override
  public void onClick(View view) {
    int viewId = view.getId();
    switch (viewId) {
      case R.id.imageButton_catalogue_cart:
        //ToDo: Implement Cart-Details page
        break;
      case R.id.imageButton_catalogue_checkout:
        //ToDo: Implement Checkout page
        CheckoutActivity.launchActivity(this, this.categoryAdapter.getCheckoutProducts());
        break;
    }
  }

  @Override
  public void onResponse(JSONObject response) {
    try {
      ArrayList<Product> products = new ArrayList<>(Arrays.asList((new Gson()).fromJson(response.getJSONArray("UArray").toString(), Product[].class)));
      this.categoryAdapter.refreshProducts(products);

      this.findViewById(R.id.container_content).setVisibility(View.VISIBLE);
      this.findViewById(R.id.placeholder_progress).setVisibility(View.GONE);
      this.findViewById(R.id.placeHolder_error).setVisibility(View.GONE);
    } catch (JSONException e) {
      e.printStackTrace();
      this.onErrorResponse(new VolleyError("Corrupted response"));
    }
  }

  @Override
  public void onErrorResponse(VolleyError error) {
    this.findViewById(R.id.container_content).setVisibility(View.GONE);
    this.findViewById(R.id.placeholder_progress).setVisibility(View.GONE);
    this.findViewById(R.id.placeHolder_error).setVisibility(View.VISIBLE);
  }

  @Override
  public void onAdapterInteraction(double orderTotal, int numberOfProducts) {
    this.orderTotalTextView.setText(this.getResources().getString(R.string.order_total, orderTotal, numberOfProducts));
  }
}
