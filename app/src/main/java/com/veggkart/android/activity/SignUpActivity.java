package com.veggkart.android.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
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
import com.veggkart.android.util.Helper;
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

        initialize();
    }

    private void initialize() {
        setContentView(R.layout.activity_sign_up);

//    ActionBar actionBar = this.getSupportActionBar();
//
//    if (actionBar != null) {
//      actionBar.setDisplayHomeAsUpEnabled(true);
//      actionBar.setTitle(this.getResources().getString(R.string.title_sign_up));
//    }

        editTextName = findViewById(R.id.editText_signUp_fullName);
        editTextEmail = findViewById(R.id.editText_signUp_email);
        editTextUsername = findViewById(R.id.editText_signUp_username);
        editTextPassword = findViewById(R.id.editText_signUp_password);
        editTextMobile = findViewById(R.id.editText_signUp_mobile);
        editTextAddress = findViewById(R.id.editText_signUp_address);
        editTextCity = findViewById(R.id.editText_signUp_city);
        editTextZipCode = findViewById(R.id.editText_signUp_zip);
        spinnerState = findViewById(R.id.spinner_signUp_state);
        buttonSignUp = findViewById(R.id.button_signUp_signUp);

        buttonSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();

        switch (viewId) {
            case R.id.button_signUp_signUp:
                signUp();
                break;
        }
    }

    private void signUp() {
        String fullName = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String mobile = editTextMobile.getText().toString().trim();
        String address = editTextAddress.getText().toString().trim();
        String city = editTextCity.getText().toString().trim();
        String zipCode = editTextZipCode.getText().toString().trim();
        String state = spinnerState.getSelectedItem().toString().trim();

        boolean isInputValid = true;

        if (!fullName.matches("^(\\w+\\s)+\\w+$")) {
            isInputValid = false;
            editTextName.setError("Please enter your full name");
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            isInputValid = false;
            editTextEmail.setError("Please enter valid email");
        }

        if (!username.matches("^[a-zA-Z0-9]+$")) {
            isInputValid = false;
            editTextUsername.setError("Letters & numbers only");
        }

        if (!password.matches("^.{6,}$")) {
            isInputValid = false;
            editTextPassword.setError("Should be at-least 6 characters long");
        }

        if (!mobile.matches("^[7-9]\\d{9}$")) {
            isInputValid = false;
            editTextMobile.setError("Enter full mobile number(10 digits)");
        }

        if (!address.matches("^.+$")) {
            isInputValid = false;
            editTextAddress.setError("Address can\'t be empty");
        }

        if (!city.matches("^.+$")) {
            isInputValid = false;
            editTextCity.setError("City can't be empty");
        }

        if (!zipCode.matches("^[1-9]\\d{5}$")) {
            isInputValid = false;
            editTextZipCode.setError("Enter valid zip-code");
        }

        if (isInputValid) {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            progressDialog = new ProgressDialog(this);
            progressDialog.setIndeterminate(true);
            progressDialog.setTitle("VegGKart");
            progressDialog.setMessage("Signing Up...");
            progressDialog.show();

            User user = new User(email, fullName, address, city, state, mobile, zipCode);
            password = Helper.stringToMD5Hex(password);

            APIHelper.userSignUp(user, username, password, this, this, this);
        }
    }

    @Override
    public void onResponse(JSONObject response) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        try {
            int signUpCheck = response.getInt("signupcheck");

            switch (signUpCheck) {
                case 0:
                    onErrorResponse(new VolleyError("Corrupt network response"));
                    break;
                case 12:
                    editTextUsername.setError("This username is unavailable");
                    break;
                case 13:
                    editTextEmail.setError("Email address already in use");
                    break;
                case 14:
                    editTextEmail.setError("An account with this credentials exists");
                    break;
                case 11:
                    String userId = response.getString("userid");
                    if (userId != null && !userId.equals("null")) {
                        successfulSignUp(userId);
                    } else {
                        onErrorResponse(new VolleyError("Corrupt network response"));
                    }
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            onErrorResponse(new VolleyError("Corrupt network response"));
        }
    }

    private void successfulSignUp(String userId) {
        String username = editTextUsername.getText().toString().trim();
        String name = editTextName.getText().toString().trim();
        String address = editTextAddress.getText().toString().trim();
        String city = editTextCity.getText().toString().trim();
        String state = spinnerState.getSelectedItem().toString().trim();
        String zip = editTextZipCode.getText().toString().trim();
        String mobile = editTextMobile.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();

        User user = new User(userId, email, name, address, city, state, mobile, zip);

        UserHelper.storeUserDetails(user, this);
        UserHelper.storeUsername(username, this);

        CatalogueActivity.launchActivity(this);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.e("SIGN-UP", (new String(error.networkResponse.data, Charset.defaultCharset())));
        Snackbar.make(this.editTextUsername, "Some error occurred\nTry again after some time", Snackbar.LENGTH_LONG).show();
    }
}
