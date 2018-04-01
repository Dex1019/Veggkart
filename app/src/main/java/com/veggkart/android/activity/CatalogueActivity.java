package com.veggkart.android.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.veggkart.android.util.UserHelper;
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

        initialize();
    }

    private void initialize() {
        setContentView(R.layout.activity_catalogue);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//    ActionBar actionBar = this.getSupportActionBar();
//
//    if (actionBar != null) {
//      actionBar.setDisplayHomeAsUpEnabled(false);
//      actionBar.setTitle(this.getResources().getString(R.string.app_name));
//    }

        cartButton = findViewById(R.id.imageButton_catalogue_cart);
        orderTotalTextView = findViewById(R.id.textView_catalogue_orderTotal);
        checkoutButton = findViewById(R.id.imageButton_catalogue_checkout);

        cartButton.setOnClickListener(this);
        checkoutButton.setOnClickListener(this);

        orderTotalTextView.setText(this.getResources().getString(R.string.order_total, 0.0, 0));

        categoryAdapter = CategoryAdapter.readFromSharedPreferences(this, this.getSupportFragmentManager(), this);

        if (this.categoryAdapter == null) {
            String productsUrl = APIHelper.getEndpointProducts();
            JsonObjectRequest productsRequest = new JsonObjectRequest(Request.Method.GET, productsUrl, new JSONObject(), this, this);
            VolleySingleton.getInstance(this).addToRequestQueue(productsRequest);

            this.categoryAdapter = new CategoryAdapter(this.getSupportFragmentManager(), new ArrayList<Product>(), this);
        } else {
//      findViewById(R.id.container_content).setVisibility(View.VISIBLE);
            findViewById(R.id.placeholder_progress).setVisibility(View.GONE);
            findViewById(R.id.placeHolder_error).setVisibility(View.GONE);
        }

        int position = CategoryAdapter.readPositionFromSharedPreferences(this);

        tabLayout = findViewById(R.id.tabs_subBrokers);
        viewPager = findViewById(R.id.viewPager_subBrokers);
        viewPager.setAdapter(categoryAdapter);
        tabLayout.setupWithViewPager(viewPager, true);
        viewPager.setCurrentItem(position);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = this.getMenuInflater();
        menuInflater.inflate(R.menu.menu_catalogue, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        switch (itemId) {
            case R.id.menu_catalogue_profile:
                openProfile();
                break;
            case R.id.menu_catalogue_signOut:
                signOut();
                break;
        }

        return true;
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        switch (viewId) {
            case R.id.imageButton_catalogue_cart:
//         The button is put just as an icon
                break;
            case R.id.imageButton_catalogue_checkout:
                CheckoutActivity.launchActivity(this, this.categoryAdapter.getCheckoutProducts());
                break;
        }
    }

    @Override
    public void onResponse(JSONObject response) {
        try {
            ArrayList<Product> products = new ArrayList<>(Arrays.asList((new Gson()).fromJson(response.getJSONArray("UArray").toString(), Product[].class)));
            this.categoryAdapter.refreshProducts(products);

//      findViewById(R.id.container_content).setVisibility(View.VISIBLE);
            findViewById(R.id.placeholder_progress).setVisibility(View.GONE);
            findViewById(R.id.placeHolder_error).setVisibility(View.GONE);
            findViewById(R.id.tabs_subBrokers).setVisibility(View.VISIBLE);
            findViewById(R.id.item_price_detail_layout).setVisibility(View.VISIBLE);
        } catch (JSONException e) {
            e.printStackTrace();
            onErrorResponse(new VolleyError("Corrupted response"));
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
//    findViewById(R.id.container_content).setVisibility(View.GONE);
        findViewById(R.id.placeholder_progress).setVisibility(View.GONE);
        findViewById(R.id.placeHolder_error).setVisibility(View.VISIBLE);
    }

    @Override
    public void onAdapterInteraction(double orderTotal, int numberOfProducts) {
        this.orderTotalTextView.setText(this.getResources().getString(R.string.order_total, orderTotal, numberOfProducts));
    }

    private void openProfile() {
        categoryAdapter.storePositionToSharedPreferences(this, this.viewPager.getCurrentItem());
        categoryAdapter.storeToSharedPreferences(this);

        ProfileActivity.launchActivity(this);
    }

    private void signOut() {
        AlertDialog.Builder confirmationDialogBuilder = new AlertDialog.Builder(this);
        confirmationDialogBuilder.setTitle(R.string.label_sign_out);
        confirmationDialogBuilder.setMessage("Are you sure you want to sign-out?");
        confirmationDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                UserHelper.clearUserDetails(CatalogueActivity.this);
                SignInActivity.launchActivity(CatalogueActivity.this);
            }
        });
        confirmationDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        confirmationDialogBuilder.setCancelable(true);
        confirmationDialogBuilder.setIcon(R.drawable.ic_logout_black);

        AlertDialog confirmationDialog = confirmationDialogBuilder.create();
        confirmationDialog.show();
    }
}
