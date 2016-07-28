package com.example.prince.vegkart;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prince on 4/23/2016.
 */
public class JSONParse {
  public static final String JSON_ARRAY = "UArray";
  public static final String KEY_PRODUCTNAME = "proname";
  public static final String KEY_PRODUCTPRICE = "price";
  public static final String KEY_CATEGORY = "catname";
  public static List<Product> productObj = null;
  private static Product productItem;
  private JSONArray products = null;

  private String json;

  public JSONParse(String json) {
    this.json = json;
  }

  protected void parseJson() {
    JSONObject jsonObject = null;

    try {
      jsonObject = new JSONObject(json);
      products = jsonObject.getJSONArray(JSON_ARRAY);

      productObj = new ArrayList<>(products.length());

      for (int i = 0; i < products.length(); i++) {
        JSONObject JObj = products.getJSONObject(i);

        productItem = new Product();
        productItem.setProductName(JObj.getString(KEY_PRODUCTNAME));
        productItem.setPrice(JObj.getString(KEY_PRODUCTPRICE));
        productItem.setDescription(JObj.getString(KEY_CATEGORY));

        productObj.add(productItem);
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
