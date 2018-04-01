package com.veggkart.android.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.veggkart.android.R;
import com.veggkart.android.model.User;

/**
 *
 * Project: veggkart
 */
public class UserHelper {
  private static final String KEY_USERNAME = "user-username";
  private static final String KEY_USER = "user-user";

  public static void storeUsername(String username, Context context) {
    SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.CONST_PREFS_FILE), Context.MODE_PRIVATE);
    preferences
        .edit()
        .putString(KEY_USERNAME, username)
        .apply();
  }

  public static String getUsername(Context context) {
    SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.CONST_PREFS_FILE), Context.MODE_PRIVATE);
    String username = preferences.getString(KEY_USERNAME, null);
    return username;
  }

  public static void storeUserDetails(User user, Context context) {
    Gson gson = new Gson();
    String userJson = gson.toJson(user);

    SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.CONST_PREFS_FILE), Context.MODE_PRIVATE);
    preferences
        .edit()
        .putString(KEY_USER, userJson)
        .apply();
  }

  public static User getUserDetails(Context context) {
    SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.CONST_PREFS_FILE), Context.MODE_PRIVATE);
    String userJson = preferences.getString(KEY_USER, null);
    return User.getInstance(userJson);
  }

  public static void clearUserDetails(Context context) {
    SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.CONST_PREFS_FILE), Context.MODE_PRIVATE);
    preferences
        .edit()
        .remove(KEY_USERNAME)
        .remove(KEY_USER)
        .apply();
  }
}
