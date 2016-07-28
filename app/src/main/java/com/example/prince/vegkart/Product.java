package com.example.prince.vegkart;

/**
 * Created by aniPC on 16-Jun-16.
 */
public class Product {
  private String productName;
  private String description;
  private String price;
  private String quantity;
  
  public Product() {
    this.quantity = "0";

  }

  public Product(String productName, String description, String price) {
    this.productName = productName;
    this.description = description;
    this.price = price;
    this.quantity = "0";
  }

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getPrice() {
    return price;
  }

  public void setPrice(String price) {
    this.price = price;
  }

  public String getQuantity() {
    return quantity;
  }

  public void setQuantity(String quantity) {
    this.quantity = quantity;
  }
}

