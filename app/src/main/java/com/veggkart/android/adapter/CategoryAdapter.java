package com.veggkart.android.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.ArrayMap;

import com.google.gson.Gson;
import com.veggkart.android.R;
import com.veggkart.android.eventlistener.OnAdapterInteractionListener;
import com.veggkart.android.fragment.ProductsListFragment;
import com.veggkart.android.model.Product;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

/**
 * 
 * Project: veggkart
 */
public class CategoryAdapter extends FragmentPagerAdapter {
  private final static String KEY_POSITION = "catalogue-position";
  private final static String KEY_PRODUCTS = "catalogue-products";
  private final static String KEY_ORDER_TOTAL_PRICE = "catalogue-order-total-price";
  private final static String KEY_NUMBER_OF_PRODUCTS = "catalogue-number-of-products";

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

  private CategoryAdapter(FragmentManager fragmentManager, ArrayList<Product> products, OnAdapterInteractionListener adapterInteractionListener, double orderTotalPrice, int numberOfProducts) {
    super(fragmentManager);

    this.sortAndCategorizeProducts(products);

    this.orderTotalPrice = orderTotalPrice;
    this.numberOfProducts = numberOfProducts;

    this.adapterInteractionListener = adapterInteractionListener;
  }

  public static int readPositionFromSharedPreferences(Context context) {
    SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.CONST_PREFS_FILE), Context.MODE_PRIVATE);
    return sharedPreferences.getInt(KEY_POSITION, 0);
  }

  public static CategoryAdapter readFromSharedPreferences(Context context, FragmentManager fragmentManager, OnAdapterInteractionListener adapterInteractionListener) {
    CategoryAdapter categoryAdapter;

    SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.CONST_PREFS_FILE), Context.MODE_PRIVATE);

    if (sharedPreferences.contains(KEY_PRODUCTS)) {
      String productsJson = sharedPreferences.getString(KEY_PRODUCTS, "");
      double orderTotalPrice = sharedPreferences.getFloat(KEY_ORDER_TOTAL_PRICE, 0.0f);
      int numberOfProducts = sharedPreferences.getInt(KEY_NUMBER_OF_PRODUCTS, 0);

      sharedPreferences
          .edit()
          .remove(KEY_PRODUCTS)
          .remove(KEY_ORDER_TOTAL_PRICE)
          .remove(KEY_NUMBER_OF_PRODUCTS)
          .apply();

      Gson gson = new Gson();
      ArrayList<Product> products = new ArrayList<>(Arrays.asList(gson.fromJson(productsJson, Product[].class)));

      categoryAdapter = new CategoryAdapter(fragmentManager, products, adapterInteractionListener, orderTotalPrice, numberOfProducts);
    } else {
      categoryAdapter = null;
    }

    return categoryAdapter;
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

      try {
        Double.valueOf(products.get(i).getPrice());
        this.products.get(products.get(i).getCategory()).add(products.get(i));
      } catch (NumberFormatException e) {
        e.printStackTrace();
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

  public ArrayList<Product> getCheckoutProducts() {
    ArrayList<Product> checkoutProducts = new ArrayList<>();
    for (int i = 0; i < this.categories.size(); i++) {
      ArrayList<Product> temp = this.products.get(this.categories.get(i));
      for (int j = 0; j < temp.size(); j++) {
        Product tempProduct = temp.get(j);
        if (tempProduct.getQuantity() > 0) {
          checkoutProducts.add(tempProduct);
        }
      }
    }
    return checkoutProducts;
  }

  public void storePositionToSharedPreferences(Context context, int position) {
    SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.CONST_PREFS_FILE), Context.MODE_PRIVATE);
    sharedPreferences
        .edit()
        .putInt(KEY_POSITION, position)
        .apply();
  }

  public void storeToSharedPreferences(Context context) {
    ArrayList<Product> products = new ArrayList<>();
    for (ArrayList<Product> p : this.products.values()) {
      products.addAll(p);
    }

    Gson gson = new Gson();
    String productsJson = gson.toJson(products);

    SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.CONST_PREFS_FILE), Context.MODE_PRIVATE);
    sharedPreferences
        .edit()
        .putString(KEY_PRODUCTS, productsJson)
        .apply();
  }
}
