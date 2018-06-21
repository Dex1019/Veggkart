package com.veggkart.android.adapter;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.veggkart.android.R;
import com.veggkart.android.model.Product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * 
 * Project: veggkart
 */
public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
  private ArrayList<Product> products;
  private int numberOfProducts;
  private double orderTotal;

  public CartAdapter(ArrayList<Product> products) {
    this.products = products;

    this.initialize();
  }

  private void initialize() {
    Collections.sort(this.products, new Comparator<Product>() {
      @Override
      public int compare(Product p1, Product p2) {
        return p1.getName().compareTo(p2.getName());
      }
    });

    this.numberOfProducts = this.products.size();
    this.orderTotal = 0.0;

    for (int i = 0; i < this.products.size(); i++) {
      this.orderTotal += (Double.parseDouble(this.products.get(i).getPrice()) * ((double) this.products.get(i).getQuantity()));
    }
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_cart, parent, false);
    ViewHolder viewHolder = new ViewHolder(rootView);
    return viewHolder;
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    holder.textViewQuantity.setText(String.valueOf(this.products.get(position).getQuantity()));
    holder.textViewName.setText(this.products.get(position).getName());
    holder.textViewPrice.setText(String.valueOf(this.products.get(position).getPrice()));
  }

  @Override
  public int getItemCount() {
    return this.products.size();
  }

  public int getNumberOfProducts() {
    return numberOfProducts;
  }

  public double getOrderTotal() {
    return orderTotal;
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    private AppCompatTextView textViewQuantity;
    private AppCompatTextView textViewName;
    private AppCompatTextView textViewPrice;

    public ViewHolder(View itemView) {
      super(itemView);

      this.textViewQuantity = (AppCompatTextView) itemView.findViewById(R.id.listItem_cart_quantity);
      this.textViewName = (AppCompatTextView) itemView.findViewById(R.id.listItem_cart_name);
      this.textViewPrice = (AppCompatTextView) itemView.findViewById(R.id.listItem_cart_price);
    }
  }
}
