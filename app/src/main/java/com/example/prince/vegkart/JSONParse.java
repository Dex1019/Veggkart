package com.example.prince.vegkart;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prince on 4/23/2016.
 */
public class JSONParse {
    public static  List<Product> productObj = null;
    private static Product productItem;

    public static final String JSON_ARRAY = "UArray";
    public static final String KEY_PRODUCTNAME = "proname";
    public static final String KEY_PRODUCTPRICE = "price";
    public static final String KEY_CATEGORY = "catname";


    private JSONArray products = null;

    private String json;

    public JSONParse(String _json) {
        this.json = _json;
    }

    protected void parseJson() {
        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject(json);
            products = jsonObject.getJSONArray(JSON_ARRAY);

            productObj = new ArrayList<>(products.length());

            for (int i = 0; i < products.length(); i++) {
                JSONObject JObj = products.getJSONObject(i);

                productItem=new Product();
                productItem.setProname(JObj.getString(KEY_PRODUCTNAME));
                productItem.setPrice(JObj.getString(KEY_PRODUCTPRICE));
                productItem.setDescription(JObj.getString(KEY_CATEGORY));

                productObj.add(productItem);
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}
