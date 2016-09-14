package com.veggkart.android.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.veggkart.android.R;
import com.veggkart.android.adapter.CartAdapter;
import com.veggkart.android.model.Product;
import com.veggkart.android.model.User;
import com.veggkart.android.util.APIHelper;
import com.veggkart.android.util.UserHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class CheckoutActivity extends AppCompatActivity implements View.OnClickListener, Response.Listener<JSONObject>, Response.ErrorListener {
  private static final String PRODUCTS = "products";

  private RecyclerView cartRecyclerView;
  private CartAdapter cartAdapter;

  private AppCompatTextView textViewNumberOfProducts;
  private AppCompatTextView textViewOrderTotal;
  private AppCompatButton buttonPlaceOrder;

  private ArrayList<Product> products;

  private ProgressDialog progressDialog;

  public static void launchActivity(AppCompatActivity currentActivity, ArrayList<Product> products) {
    String productsJson = (new Gson()).toJson(products);
    Intent checkoutIntent = new Intent(currentActivity, CheckoutActivity.class);
    checkoutIntent.putExtra(CheckoutActivity.PRODUCTS, productsJson);
    currentActivity.startActivity(checkoutIntent);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    this.initialize(this.getIntent().getExtras());
  }

  private void initialize(Bundle extras) {
    setContentView(R.layout.activity_checkout);

    this.products = new ArrayList<>(Arrays.asList((new Gson()).fromJson(extras.getString(CheckoutActivity.PRODUCTS), Product[].class)));

    this.cartRecyclerView = (RecyclerView) this.findViewById(R.id.recyclerView_cart);
    this.cartRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    this.cartAdapter = new CartAdapter(this.products);
    this.cartRecyclerView.setAdapter(this.cartAdapter);

    this.textViewNumberOfProducts = (AppCompatTextView) this.findViewById(R.id.textView_checkout_quantity);
    this.textViewOrderTotal = (AppCompatTextView) this.findViewById(R.id.textView_checkout_orderTotal);

    this.textViewNumberOfProducts.setText(String.valueOf(this.cartAdapter.getNumberOfProducts()) + " products");
    this.textViewOrderTotal.setText(this.getString(R.string.price, this.cartAdapter.getOrderTotal()));

    this.buttonPlaceOrder = (AppCompatButton) this.findViewById(R.id.button_checkout_placeOrder);
    this.buttonPlaceOrder.setOnClickListener(this);
  }

  @Override
  public void onClick(View view) {
    int viewId = view.getId();

    switch (viewId) {
      case R.id.button_checkout_placeOrder:
        this.confirmContactDetailsAndPlaceOrder();
        break;
    }
  }

  private void confirmContactDetailsAndPlaceOrder() {
    User user = UserHelper.getUserDetails(this);

    AlertDialog.Builder confirmationDialogBuilder = new AlertDialog.Builder(this);
    confirmationDialogBuilder
        .setCancelable(false)
        .setTitle(this.getString(R.string.label_contact_details))
        .setMessage("Are these your contact details?\n" +
            "Email: " + user.getEmail() + "\n" +
            "Mobile: " + user.getMobile() + "\n" +
            "Address: " + user.getAddress())
        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialogInterface, int i) {
            CheckoutActivity.this.placeOrder();
          }
        })
        .setNegativeButton("No", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialogInterface, int i) {
            Toast.makeText(CheckoutActivity.this, "Please update your contact details and then place the order", Toast.LENGTH_SHORT).show();
          }
        })
        .setIcon(R.drawable.ic_person_outline_black);

    AlertDialog confirmationDialog = confirmationDialogBuilder.create();
    confirmationDialog.show();
  }

  private void placeOrder() {
    if (this.progressDialog != null && this.progressDialog.isShowing()) {
      this.progressDialog.dismiss();
    }
    this.progressDialog = new ProgressDialog(this);
    this.progressDialog.setIndeterminate(true);
    this.progressDialog.setTitle("VegGKart");
    this.progressDialog.setMessage("Placing order...");
    this.progressDialog.setCancelable(false);
    this.progressDialog.show();

    APIHelper.placeOrder(this.products, this, this, this);
  }

  @Override
  public void onResponse(JSONObject response) {
    if (this.progressDialog != null && this.progressDialog.isShowing()) {
      this.progressDialog.dismiss();
    }

    try {
      int status = response.getInt("success");
      if (status == 11) {
        Snackbar.make(this.cartRecyclerView, "Order placed successfully", Snackbar.LENGTH_LONG).show();
        CatalogueActivity.launchActivity(this);
      } else {
        this.onErrorResponse(new VolleyError("Server error"));
      }
    } catch (JSONException e) {
      e.printStackTrace();
      this.onErrorResponse(new VolleyError("Corrupt response"));
    }
  }

  @Override
  public void onErrorResponse(VolleyError error) {
    Snackbar.make(this.cartRecyclerView, "Error placing order", Snackbar.LENGTH_LONG).show();
    Log.e("Order placement", error.getMessage());
  }
}
