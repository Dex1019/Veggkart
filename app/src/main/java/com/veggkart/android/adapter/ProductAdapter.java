package com.veggkart.android.adapter;

import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;
import com.veggkart.android.R;
import com.veggkart.android.eventlistener.ItemUpdateListener;
import com.veggkart.android.model.Product;
import com.veggkart.android.util.APIHelper;

import java.util.ArrayList;

/**
 * 
 * Project: veggkart
 */
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
  private ArrayList<Product> products;
  private ItemUpdateListener itemUpdateListener;

  public ProductAdapter(ArrayList<Product> products, ItemUpdateListener itemUpdateListener) {
    this.products = products;
    this.itemUpdateListener = itemUpdateListener;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_products, parent, false);
    ViewHolder viewHolder = new ViewHolder(rootView, this);
    return viewHolder;
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    Drawable placeHolderDrawable = ResourcesCompat.getDrawable(holder.imageViewProductImage.getResources(), R.drawable.ic_cloud_download_black_24dp, null);
    Picasso
        .with(holder.imageViewProductImage.getContext())
        .load(APIHelper.getProductImageUrl(this.products.get(position).getPhotoUrl()))
        .fit()
        .centerInside()
        .placeholder(placeHolderDrawable)
        .error(R.drawable.ic_cloud_off_black_24dp)
        .into(holder.imageViewProductImage);
    holder.textViewRate.setText(holder.textViewRate.getContext().getResources().getString(R.string.price, Double.parseDouble(this.products.get(position).getPrice())));
    holder.textViewName.setText(this.products.get(position).getName());
    holder.editTextQuantity.setText(String.valueOf(this.products.get(position).getQuantity()));
  }

  @Override
  public int getItemCount() {
    return this.products.size();
  }

  public void onItemUpdate(int position, int quantity) {
    this.itemUpdateListener.onItemUpdate(position, quantity);
  }

  public class ViewHolder extends RecyclerView.ViewHolder implements TextWatcher, AppCompatImageButton.OnClickListener {
    private ProductAdapter adapter;

    private AppCompatImageView imageViewProductImage;
    private AppCompatTextView textViewRate;
    private AppCompatTextView textViewName;
    private AppCompatImageButton buttonDecrement;
    private AppCompatImageButton buttonIncrement;
    private AppCompatEditText editTextQuantity;

    public ViewHolder(View itemView, ProductAdapter adapter) {
      super(itemView);
      this.adapter = adapter;

      this.imageViewProductImage = (AppCompatImageView) itemView.findViewById(R.id.imageView_products_product);
      this.textViewRate = (AppCompatTextView) itemView.findViewById(R.id.textView_products_price);
      this.textViewName = (AppCompatTextView) itemView.findViewById(R.id.textView_products_name);
      this.buttonDecrement = (AppCompatImageButton) itemView.findViewById(R.id.imageButton_products_decrement);
      this.buttonIncrement = (AppCompatImageButton) itemView.findViewById(R.id.imageButton_products_increment);
      this.editTextQuantity = (AppCompatEditText) itemView.findViewById(R.id.editText_products_quantity);

      this.buttonDecrement.setOnClickListener(this);
      this.buttonIncrement.setOnClickListener(this);
      this.editTextQuantity.addTextChangedListener(this);
    }

    @Override
    public void onClick(View view) {
      int viewId = view.getId();

      int quantity = Integer.parseInt(this.editTextQuantity.getText().toString());

      switch (viewId) {
        case R.id.imageButton_products_decrement:
          if (quantity > 0) {
            quantity--;
          }
          break;
        case R.id.imageButton_products_increment:
          quantity++;
          break;
      }

      this.editTextQuantity.setText(String.valueOf(quantity));
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
      String text = editable.toString();
      if (!(text.length() > 0)) {
        editable.clear();
        editable.append('0');
      }
      text = editable.toString();
      int quantity = Integer.parseInt(text);
      this.adapter.onItemUpdate(this.getAdapterPosition(), quantity);
    }
  }
}
