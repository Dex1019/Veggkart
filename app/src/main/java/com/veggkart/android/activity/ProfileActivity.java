package com.veggkart.android.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.util.Patterns;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.veggkart.android.R;
import com.veggkart.android.model.User;
import com.veggkart.android.util.APIHelper;
import com.veggkart.android.util.UserHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener, Response.Listener<JSONObject>, Response.ErrorListener {

  private TextInputEditText editTextName;
  private TextInputEditText editTextEmail;
  private TextInputEditText editTextMobile;
  private TextInputEditText editTextAddress;
  private TextInputEditText editTextCity;
  private TextInputEditText editTextZipCode;
  private AppCompatSpinner spinnerState;
  private AppCompatButton buttonUpdateProfile;

  private ProgressDialog progressDialog;

  public static void launchActivity(AppCompatActivity currentActivity) {
    Intent updateProfileIntent = new Intent(currentActivity, ProfileActivity.class);
    currentActivity.startActivity(updateProfileIntent);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    this.initialize();
  }

  private void initialize() {
    this.setContentView(R.layout.activity_profile);

    ActionBar actionBar = this.getSupportActionBar();

    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
      actionBar.setTitle(R.string.title_update_profile);
    }

    this.editTextName = (TextInputEditText) this.findViewById(R.id.editText_profile_fullName);
    this.editTextEmail = (TextInputEditText) this.findViewById(R.id.editText_profile_email);
    this.editTextMobile = (TextInputEditText) this.findViewById(R.id.editText_profile_mobile);
    this.editTextAddress = (TextInputEditText) this.findViewById(R.id.editText_profile_address);
    this.editTextCity = (TextInputEditText) this.findViewById(R.id.editText_profile_city);
    this.editTextZipCode = (TextInputEditText) this.findViewById(R.id.editText_profile_zip);
    this.spinnerState = (AppCompatSpinner) this.findViewById(R.id.spinner_profile_state);
    this.buttonUpdateProfile = (AppCompatButton) this.findViewById(R.id.button_profile_updateProfile);

    this.buttonUpdateProfile.setOnClickListener(this);

    this.fillInitialValues();
  }

  private void fillInitialValues() {
    User user = UserHelper.getUserDetails(this);

    String[] states = this.getResources().getStringArray(R.array.india_states);
    int position = 0;
    for (int i = 0; i < states.length; i++) {
      if (user.getState().equals(states[i])) {
        position = i;
        break;
      }
    }

    this.editTextName.setText(user.getName());
    this.editTextEmail.setText(user.getEmail());
    this.editTextMobile.setText(user.getMobile());
    this.editTextAddress.setText(user.getAddress());
    this.editTextCity.setText(user.getCity());
    this.editTextZipCode.setText(user.getZip());
    this.spinnerState.setSelection(position);
  }

  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.button_profile_updateProfile:
        this.updateProfile();
        break;
    }
  }

  private void updateProfile() {
    String fullName = this.editTextName.getText().toString().trim();
    String email = this.editTextEmail.getText().toString().trim();
    String mobile = this.editTextMobile.getText().toString().trim();
    String address = this.editTextAddress.getText().toString().trim();
    String city = this.editTextCity.getText().toString().trim();
    String zipCode = this.editTextZipCode.getText().toString().trim();
    String state = this.spinnerState.getSelectedItem().toString().trim();

    boolean isInputValid = true;

    if (!fullName.matches("^(\\w+\\s)+\\w+$")) {
      isInputValid = false;
      this.editTextName.setError("Please enter your full name");
    }

    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
      isInputValid = false;
      this.editTextEmail.setError("Please enter valid email");
    }

    if (!mobile.matches("^[7-9]\\d{9}$")) {
      isInputValid = false;
      this.editTextMobile.setError("Enter full mobile number(10 digits)");
    }

    if (!address.matches("^.+$")) {
      isInputValid = false;
      this.editTextAddress.setError("Address can\'t be empty");
    }

    if (!city.matches("^.+$")) {
      isInputValid = false;
      this.editTextCity.setError("City can't be empty");
    }

    if (!zipCode.matches("^[1-9]\\d{5}$")) {
      isInputValid = false;
      this.editTextZipCode.setError("Enter valid zip-code");
    }

    if (isInputValid) {
      if (this.progressDialog != null && this.progressDialog.isShowing()) {
        this.progressDialog.dismiss();
      }
      this.progressDialog = new ProgressDialog(this);
      this.progressDialog.setIndeterminate(true);
      this.progressDialog.setTitle("VegGKart");
      this.progressDialog.setMessage("Updating Profile...");
      this.progressDialog.show();

      String userId = UserHelper.getUserDetails(this).getUserId();
      User user = new User(userId, email, fullName, address, city, state, mobile, zipCode);

      APIHelper.userUpdateProfile(user, this, this, this);
    }
  }

  @Override
  public void onResponse(JSONObject response) {
    if (this.progressDialog != null && this.progressDialog.isShowing()) {
      this.progressDialog.dismiss();
    }

    try {
      int updateCheck = response.getInt("success");

      switch (updateCheck) {
        case 0:
          this.onErrorResponse(new VolleyError("Corrupt network response"));
          break;
        case 10:
          this.onErrorResponse(new VolleyError("Please provide valid inputs"));
          break;
        case 11:
          this.successfulUpdate();
          break;
      }
    } catch (JSONException e) {
      e.printStackTrace();
      this.onErrorResponse(new VolleyError("Corrupt network response"));
    }
  }

  private void successfulUpdate() {
    String userId = UserHelper.getUserDetails(this).getUserId();
    String name = this.editTextName.getText().toString().trim();
    String address = this.editTextAddress.getText().toString().trim();
    String city = this.editTextCity.getText().toString().trim();
    String state = this.spinnerState.getSelectedItem().toString().trim();
    String zip = this.editTextZipCode.getText().toString().trim();
    String mobile = this.editTextMobile.getText().toString().trim();
    String email = this.editTextEmail.getText().toString().trim();

    User user = new User(userId, email, name, address, city, state, mobile, zip);

    UserHelper.storeUserDetails(user, this);

    this.onBackPressed();
  }

  @Override
  public void onErrorResponse(VolleyError error) {
    if (error.networkResponse != null) {
      Log.e("Update-Profile", (new String(error.networkResponse.data, Charset.defaultCharset())));
    } else {
      Log.e("Update-Profile", error.getMessage());
    }
    Snackbar.make(this.editTextName, "Some error occurred\nTry again after some time", Snackbar.LENGTH_LONG).show();
  }
}
