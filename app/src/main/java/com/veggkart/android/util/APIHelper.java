package com.veggkart.android.util;

/**
 * Creator: vbarad
 * Date: 2016-08-02
 * Project: veggkart
 */
public class APIHelper {
  private static final String baseUrl = "http://nullsquad.in";
  private static final String endpointProducts = "/api/crud_api.php";
  private static final String endpointPlaceOrder = "/api/postTest.php";

  public static String getEndpointProducts() {
    return APIHelper.baseUrl + APIHelper.endpointProducts;
  }

  public static String getEndpointPlaceOrder() {
    return APIHelper.baseUrl + APIHelper.endpointPlaceOrder;
  }

  public static String getProductImageUrl(String imageUrlStub) {
    return APIHelper.baseUrl + imageUrlStub;
  }
}
