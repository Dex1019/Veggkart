package com.veggkart.android.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.veggkart.android.R;
import com.veggkart.android.adapter.CategoryAdapter;
import com.veggkart.android.adapter.ProductAdapter;
import com.veggkart.android.eventlistener.ItemUpdateListener;
import com.veggkart.android.model.Product;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductsListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductsListFragment extends Fragment implements ItemUpdateListener {
  private static final String CATEGORY = "category";

  static {
    AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
  }

  private String category;
  private CategoryAdapter categoryAdapter;
  private ArrayList<Product> products;
  private View rootView;
  private RecyclerView recyclerViewProducts;
  private ProductAdapter productAdapter;

  public ProductsListFragment() {
    // Required empty public constructor
  }

  /**
   * Use this factory method to create a new instance of
   * this fragment using the provided parameters.
   *
   * @param categoryAdapter Category-Adapter holding all products
   * @param category        Category of the products in this tab
   * @return A new instance of fragment ProductsListFragment.
   */
  public static ProductsListFragment newInstance(CategoryAdapter categoryAdapter, String category) {
    ProductsListFragment fragment = new ProductsListFragment();
    Bundle args = new Bundle();
    args.putString(CATEGORY, category);
    fragment.setArguments(args);
    fragment.categoryAdapter = categoryAdapter;
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      category = getArguments().getString(CATEGORY);
    }
    products = this.categoryAdapter.getProducts(this.category);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    rootView = inflater.inflate(R.layout.fragment_products_list, container, false);

    recyclerViewProducts = rootView.findViewById(R.id.recyclerView_products);
    recyclerViewProducts.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    productAdapter = new ProductAdapter(products, this);
    recyclerViewProducts.setAdapter(productAdapter);

    return rootView;
  }

  @Override
  public void onItemUpdate(int position, int quantity) {
    categoryAdapter.setProductQuantity(category, position, quantity);
  }
}
