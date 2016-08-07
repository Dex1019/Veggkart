package com.veggkart.android.model;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Creator: vbarad
 * Date: 2016-08-07
 * Project: veggkart
 */
public class Product {
  @Expose
  @SerializedName("proid")
  private String id;
  @Expose
  @SerializedName("proname")
  private String name;
  @Expose
  @SerializedName("catname")
  private String category;
  @Expose
  @SerializedName("price")
  private String price;
  @Expose
  @SerializedName("stock")
  private String stock;
  @Expose
  @SerializedName("photo")
  private String photoUrl;

  public Product(String id, String name, String category, String price, String stock, String photoUrl) {
    this.id = id;
    this.name = name;
    this.category = category;
    this.price = price;
    this.stock = stock;
    this.photoUrl = photoUrl;
  }

  public static Product getInstance(String productJson) {
    return (new Gson()).fromJson(productJson, Product.class);
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public String getPrice() {
    return price;
  }

  public void setPrice(String price) {
    this.price = price;
  }

  public boolean getStock() {
    if (this.stock != null && this.stock.trim().length() > 0 && this.stock.trim().equals("YES")) {
      return true;
    } else {
      return false;
    }
  }

  public void setStock(boolean stock) {
    if (stock) {
      this.stock = "YES";
    } else {
      this.stock = "";
    }
  }

  public String getPhotoUrl() {
    return photoUrl;
  }

  public void setPhotoUrl(String photoUrl) {
    this.photoUrl = photoUrl;
  }
}
