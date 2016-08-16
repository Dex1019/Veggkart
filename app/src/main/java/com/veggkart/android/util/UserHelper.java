package com.veggkart.android.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Creator: vbarad
 * Date: 2016-08-10
 * Project: veggkart
 */
public class UserHelper {
  private static final String PREFS_FILE = "veggkart-prefs";
  private static final String KEY_USERNAME = "username";
  private static final String KEY_USER_ID = "user-id";

  public static void storeUsername(String username, Context context) {
    SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putString(KEY_USERNAME, username);
    editor.apply();
  }

  public static void storeUserId(String userId, Context context) {
    SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putString(KEY_USER_ID, userId);
    editor.apply();
  }

  public static String getUsername(Context context) {
    SharedPreferences preferences = context.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
    String username = preferences.getString(KEY_USERNAME, null);
    return username;
  }

  public static String getUserId(Context context) {
    SharedPreferences preferences = context.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
    String userId = preferences.getString(KEY_USER_ID, null);
    return userId;
  }

  public static void clearUserDetails(Context context) {
    SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.remove(KEY_USERNAME);
    editor.remove(KEY_USER_ID);
    editor.apply();
  }
}
