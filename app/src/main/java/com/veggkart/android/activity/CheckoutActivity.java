package com.veggkart.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.veggkart.android.R;
import com.veggkart.android.adapter.CartAdapter;
import com.veggkart.android.model.Product;

import java.util.ArrayList;
import java.util.Arrays;

public class CheckoutActivity extends AppCompatActivity implements View.OnClickListener {
  private static final String PRODUCTS = "products";

  private RecyclerView cartRecyclerView;
  private CartAdapter cartAdapter;

  private AppCompatTextView textViewNumberOfProducts;
  private AppCompatTextView textViewOrderTotal;
  private AppCompatButton buttonPlaceOrder;

  private ArrayList<Product> products;

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
    Toast.makeText(this, "Order placed", Toast.LENGTH_LONG).show();
    //ToDo: Call API to place order
  }
}
