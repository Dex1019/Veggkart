package com.veggkart.android.model;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Creator: vbarad
 * Date: 2016-08-16
 * Project: veggkart
 */
public class User {
  @Expose
  @SerializedName("userid")
  private String userId;
  @Expose
  @SerializedName("email")
  private String email;
  @Expose
  @SerializedName("name")
  private String name;
  @Expose
  @SerializedName("address")
  private String address;
  @Expose
  @SerializedName("city")
  private String city;
  @Expose
  @SerializedName("state")
  private String state;
  @Expose
  @SerializedName("mobile")
  private String mobile;
  @Expose
  @SerializedName("zip")
  private String zip;

  public User() {

  }

  public User(String email, String name, String address, String city, String state, String mobile, String zip) {
    this.email = email;
    this.name = name;
    this.address = address;
    this.city = city;
    this.state = state;
    this.mobile = mobile;
    this.zip = zip;
  }

  public User(String userId, String email, String name, String address, String city, String state, String mobile, String zip) {
    this.userId = userId;
    this.email = email;
    this.name = name;
    this.address = address;
    this.city = city;
    this.state = state;
    this.mobile = mobile;
    this.zip = zip;
  }

  public static User getInstance(String userJson) {
    return (new Gson()).fromJson(userJson, User.class);
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getMobile() {
    return mobile;
  }

  public void setMobile(String mobile) {
    this.mobile = mobile;
  }

  public String getZip() {
    return zip;
  }

  public void setZip(String zip) {
    this.zip = zip;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }
}
