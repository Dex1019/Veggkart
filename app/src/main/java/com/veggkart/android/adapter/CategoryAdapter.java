package com.veggkart.android.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.ArrayMap;

import com.veggkart.android.eventlistener.OnAdapterInteractionListener;
import com.veggkart.android.fragment.ProductsListFragment;
import com.veggkart.android.model.Product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Creator: vbarad
 * Date: 2016-08-07
 * Project: veggkart
 */
public class CategoryAdapter extends FragmentPagerAdapter {
  private ArrayMap<String, ArrayList<Product>> products;
  private ArrayList<String> categories;
  private OnAdapterInteractionListener adapterInteractionListener;

  private double orderTotalPrice;
  private int numberOfProducts;

  public CategoryAdapter(FragmentManager fragmentManager, ArrayList<Product> products, OnAdapterInteractionListener adapterInteractionListener) {
    super(fragmentManager);

    this.sortAndCategorizeProducts(products);

    this.orderTotalPrice = 0.0;
    this.numberOfProducts = 0;

    this.adapterInteractionListener = adapterInteractionListener;
  }

  private void sortAndCategorizeProducts(ArrayList<Product> products) {
    Collections.sort(products, new Comparator<Product>() {
      @Override
      public int compare(Product p1, Product p2) {
        return p1.getName().compareTo(p2.getName());
      }
    });

    this.products = new ArrayMap<>();
    int numberOfProducts = products.size();
    for (int i = 0; i < numberOfProducts; i++) {
      if (!this.products.containsKey(products.get(i).getCategory())) {
        this.products.put(products.get(i).getCategory(), new ArrayList<Product>());
      }
      //ToDo: Get the price "VARIABLE" fixed
      if (!products.get(i).getId().equals("81")) {
        this.products.get(products.get(i).getCategory()).add(products.get(i));
      }
    }

    this.categories = new ArrayList<>();
    this.categories.addAll(this.products.keySet());
    Collections.sort(this.categories);
  }

  @Override
  public Fragment getItem(int position) {
    return ProductsListFragment.newInstance(this, this.categories.get(position));
  }

  @Override
  public int getCount() {
    return this.categories.size();
  }

  @Override
  public CharSequence getPageTitle(int position) {
    return this.categories.get(position);
  }

  public void refreshProducts(ArrayList<Product> products) {
    this.sortAndCategorizeProducts(products);
    this.notifyDataSetChanged();
  }

  public void setProductQuantity(String category, int position, int quantity) {
    if (quantity < 0) {
      quantity = 0;
    }
    int originalQuantity = this.products.get(category).get(position).getQuantity();
    this.products.get(category).get(position).setQuantity(quantity);

    double rate = Double.parseDouble(this.products.get(category).get(position).getPrice());
    double price = ((double) (quantity - originalQuantity)) * rate;

    if (originalQuantity == 0) {
      if (quantity > 0) {
        numberOfProducts++;
      }
    } else if (quantity == 0) {
      numberOfProducts--;
    }

    this.orderTotalPrice += price;

    this.adapterInteractionListener.onAdapterInteraction(this.getOrderTotalPrice(), this.getNumberOfProducts());
  }

  public double getOrderTotalPrice() {
    return orderTotalPrice;
  }

  public int getNumberOfProducts() {
    return numberOfProducts;
  }

  public ArrayList<Product> getProducts(String category) {
    ArrayList<Product> products;
    if (this.products.containsKey(category)) {
      products = this.products.get(category);
    } else {
      products = null;
    }

    return products;
  }

  public ArrayMap<String, ArrayList<Product>> getCategorizedProducts() {
    return this.products;
  }
}
