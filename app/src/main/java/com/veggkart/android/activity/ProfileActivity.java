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
import android.widget.Toast;

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
        initialize();
    }

    private void initialize() {
        this.setContentView(R.layout.activity_profile);

        ActionBar actionBar = this.getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.title_update_profile);
        }

        editTextName = findViewById(R.id.editText_profile_fullName);
        editTextEmail = findViewById(R.id.editText_profile_email);
        editTextMobile = findViewById(R.id.editText_profile_mobile);
        editTextAddress = findViewById(R.id.editText_profile_address);
        editTextCity = findViewById(R.id.editText_profile_city);
        editTextZipCode = findViewById(R.id.editText_profile_zip);
        spinnerState = findViewById(R.id.spinner_profile_state);
        buttonUpdateProfile = findViewById(R.id.button_profile_updateProfile);

        buttonUpdateProfile.setOnClickListener(this);

        fillInitialValues();
    }

    private void fillInitialValues() {
        try {
            User user = UserHelper.getUserDetails(this);

            String[] states = this.getResources().getStringArray(R.array.india_states);
            int position = 0;
            for (int i = 0; i < states.length; i++) {
                if (user.getState().equals(states[i])) {
                    position = i;
                    break;
                }
            }

            editTextName.setText(user.getName());
            editTextEmail.setText(user.getEmail());
            editTextMobile.setText(user.getMobile());
            editTextAddress.setText(user.getAddress());
            editTextCity.setText(user.getCity());
            spinnerState.setSelection(position);
        } catch (NullPointerException e) {
            Toast.makeText(this, "Please Login In", Toast.LENGTH_SHORT).show();
        }

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
        String fullName = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
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
            progressDialog.setMessage("Updating Profile...");
            progressDialog.show();

            String userId = UserHelper.getUserDetails(this).getUserId();
            User user = new User(userId, email, fullName, address, city, state, mobile, zipCode);

            APIHelper.userUpdateProfile(user, this, this, this);
        }
    }

    @Override
    public void onResponse(JSONObject response) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        try {
            int updateCheck = response.getInt("success");

            switch (updateCheck) {
                case 0:
                    onErrorResponse(new VolleyError("Corrupt network response"));
                    break;
                case 10:
                    onErrorResponse(new VolleyError("Please provide valid inputs"));
                    break;
                case 11:
                    successfulUpdate();
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            onErrorResponse(new VolleyError("Corrupt network response"));
        }
    }

    private void successfulUpdate() {
        String userId = UserHelper.getUserDetails(this).getUserId();
        String name = editTextName.getText().toString().trim();
        String address = editTextAddress.getText().toString().trim();
        String city = editTextCity.getText().toString().trim();
        String state = spinnerState.getSelectedItem().toString().trim();
        String zip = editTextZipCode.getText().toString().trim();
        String mobile = editTextMobile.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();

        User user = new User(userId, email, name, address, city, state, mobile, zip);

        UserHelper.storeUserDetails(user, this);

        this.onBackPressed();
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.e("Update-Profile", (new String(error.networkResponse.data, Charset.defaultCharset())));
        Snackbar.make(editTextName, "Some error occurred\nTry again after some time", Snackbar.LENGTH_LONG).show();
    }
}
