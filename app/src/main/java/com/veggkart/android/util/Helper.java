package com.veggkart.android.util;

import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;

/**
 * Creator: vbarad
 * Date: 2016-08-02
 * Project: veggkart
 */
public class Helper {
  public static int getScreenWidth(View view) {
    int screenSize = view.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
    int width;
    if (view.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
      switch (screenSize) {
        case Configuration.SCREENLAYOUT_SIZE_SMALL:
          width = 320;
          break;
        case Configuration.SCREENLAYOUT_SIZE_NORMAL:
          width = 320;
          break;
        case Configuration.SCREENLAYOUT_SIZE_LARGE:
          width = 480;
          break;
        case Configuration.SCREENLAYOUT_SIZE_XLARGE:
          width = 720;
          break;
        default:
          width = 480;
          break;
      }
    } else {
      switch (screenSize) {
        case Configuration.SCREENLAYOUT_SIZE_SMALL:
          width = 426;
          break;
        case Configuration.SCREENLAYOUT_SIZE_NORMAL:
          width = 470;
          break;
        case Configuration.SCREENLAYOUT_SIZE_LARGE:
          width = 640;
          break;
        case Configuration.SCREENLAYOUT_SIZE_XLARGE:
          width = 960;
          break;
        default:
          width = 640;
          break;
      }
    }
    return width;
  }

  public static int getScreenHeight(View view) {
    int screenSize = view.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
    int height;
    if (view.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
      switch (screenSize) {
        case Configuration.SCREENLAYOUT_SIZE_SMALL:
          height = 320;
          break;
        case Configuration.SCREENLAYOUT_SIZE_NORMAL:
          height = 320;
          break;
        case Configuration.SCREENLAYOUT_SIZE_LARGE:
          height = 480;
          break;
        case Configuration.SCREENLAYOUT_SIZE_XLARGE:
          height = 720;
          break;
        default:
          height = 480;
          break;
      }
    } else {
      switch (screenSize) {
        case Configuration.SCREENLAYOUT_SIZE_SMALL:
          height = 426;
          break;
        case Configuration.SCREENLAYOUT_SIZE_NORMAL:
          height = 470;
          break;
        case Configuration.SCREENLAYOUT_SIZE_LARGE:
          height = 640;
          break;
        case Configuration.SCREENLAYOUT_SIZE_XLARGE:
          height = 960;
          break;
        default:
          height = 640;
          break;
      }
    }
    return height;
  }

  public static boolean isConnectedToInternet(Context context) {
    ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

    boolean isConnected;

    if ((activeNetwork != null) && activeNetwork.isConnected()) {
      isConnected = true;
    } else {
      isConnected = false;
    }

    return isConnected;
  }
}
