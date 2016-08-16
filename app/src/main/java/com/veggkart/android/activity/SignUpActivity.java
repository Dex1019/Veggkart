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

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener, Response.Listener<JSONObject>, Response.ErrorListener {

  private TextInputEditText editTextName;
  private TextInputEditText editTextEmail;
  private TextInputEditText editTextUsername;
  private TextInputEditText editTextPassword;
  private TextInputEditText editTextMobile;
  private TextInputEditText editTextAddress;
  private TextInputEditText editTextCity;
  private TextInputEditText editTextZipCode;
  private AppCompatSpinner spinnerState;
  private AppCompatButton buttonSignUp;

  private ProgressDialog progressDialog;

  public static void launchActivity(AppCompatActivity currentActivity) {
    Intent signUpIntent = new Intent(currentActivity, SignUpActivity.class);
    currentActivity.startActivity(signUpIntent);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    this.initialize();
  }

  private void initialize() {
    setContentView(R.layout.activity_sign_up);

    ActionBar actionBar = this.getSupportActionBar();

    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
      actionBar.setTitle(this.getResources().getString(R.string.title_sign_up));
    }

    this.editTextName = (TextInputEditText) this.findViewById(R.id.editText_signUp_fullName);
    this.editTextEmail = (TextInputEditText) this.findViewById(R.id.editText_signUp_email);
    this.editTextUsername = (TextInputEditText) this.findViewById(R.id.editText_signUp_username);
    this.editTextPassword = (TextInputEditText) this.findViewById(R.id.editText_signUp_password);
    this.editTextMobile = (TextInputEditText) this.findViewById(R.id.editText_signUp_mobile);
    this.editTextAddress = (TextInputEditText) this.findViewById(R.id.editText_signUp_address);
    this.editTextCity = (TextInputEditText) this.findViewById(R.id.editText_signUp_city);
    this.editTextZipCode = (TextInputEditText) this.findViewById(R.id.editText_signUp_zip);
    this.spinnerState = (AppCompatSpinner) this.findViewById(R.id.spinner_signUp_state);
    this.buttonSignUp = (AppCompatButton) this.findViewById(R.id.button_signUp_signUp);

    this.buttonSignUp.setOnClickListener(this);
  }

  @Override
  public void onClick(View view) {
    int viewId = view.getId();

    switch (viewId) {
      case R.id.button_signUp_signUp:
        this.signUp();
        break;
    }
  }

  private void signUp() {
    String fullName = this.editTextName.getText().toString().trim();
    String email = this.editTextEmail.getText().toString().trim();
    String username = this.editTextUsername.getText().toString().trim();
    String password = this.editTextPassword.getText().toString().trim();
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

    if (!username.matches("^[a-zA-Z0-9]+$")) {
      isInputValid = false;
      this.editTextUsername.setError("Letters & numbers only");
    }

    if (!password.matches("^.{6,}$")) {
      isInputValid = false;
      this.editTextPassword.setError("Should be at-least 6 characters long");
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
      this.progressDialog.setMessage("Signing Up...");
      this.progressDialog.show();

      User user = new User(username, password, email, fullName, address, city, state, mobile, zipCode);
      user.encryptPassword();

      APIHelper.userSignUp(user, this, this, this);
    }
  }

  @Override
  public void onResponse(JSONObject response) {
    if (this.progressDialog != null && this.progressDialog.isShowing()) {
      this.progressDialog.dismiss();
    }

    try {
      int signUpCheck = response.getInt("signupcheck");

      switch (signUpCheck) {
        case 0:
          this.onErrorResponse(new VolleyError("Corrupt network response"));
          break;
        case 12:
          this.editTextUsername.setError("This username is unavailable");
          break;
        case 13:
          this.editTextEmail.setError("Email address already in use");
          break;
        case 14:
          this.editTextEmail.setError("An account with this credentials exists");
          break;
        case 11:
          String userId = response.getString("userid");
          if (userId != null && !userId.equals("null")) {
            this.successfulSignUp(userId);
          } else {
            this.onErrorResponse(new VolleyError("Corrupt network response"));
          }
          break;
      }
    } catch (JSONException e) {
      e.printStackTrace();
      this.onErrorResponse(new VolleyError("Corrupt network response"));
    }
  }
  
  private void successfulSignUp(String userId) {
    String username = this.editTextUsername.getText().toString().trim();
    UserHelper.storeUserId(userId, this);
    UserHelper.storeUsername(username, this);

    CatalogueActivity.launchActivity(this);
  }
  
  @Override
  public void onErrorResponse(VolleyError error) {
    Log.e("SIGN-UP", (new String(error.networkResponse.data, Charset.defaultCharset())));
    Snackbar.make(this.editTextUsername, "Some error occurred\nTry again after some time", Snackbar.LENGTH_LONG).show();
  }
}
