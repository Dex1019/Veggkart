package com.veggkart.android.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.veggkart.android.R;
import com.veggkart.android.model.User;
import com.veggkart.android.util.APIHelper;
import com.veggkart.android.util.UserHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener, Response.Listener<JSONObject>, Response.ErrorListener {

    private AppCompatEditText editTextUsername;
    private AppCompatEditText editTextPassword;
    private AppCompatButton buttonSignIn;
    private AppCompatButton buttonSignUp;
    private TextView textViewSkip;

    private ProgressDialog progressDialog;

    public static void launchActivity(AppCompatActivity currentActivity) {
        Intent intent = new Intent(currentActivity, SignInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        currentActivity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        initialize();
    }

    private void initialize() {
        if (UserHelper.getUserDetails(this) != null) {
            CatalogueActivity.launchActivity(this);
        } else {

            setContentView(R.layout.activity_sign_in);
            // getSupportActionBar().hide();
            // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

//            ActionBar actionBar = this.getSupportActionBar();
//
//            if (actionBar != null) {
//                actionBar.setDisplayHomeAsUpEnabled(false);
//                actionBar.setTitle(this.getResources().getString(R.string.app_name));
//            }


            editTextUsername = findViewById(R.id.editText_signIn_username);
            editTextPassword = findViewById(R.id.editText_signIn_password);
            buttonSignIn = findViewById(R.id.button_signIn_signIn);
            buttonSignUp = findViewById(R.id.button_signIn_signUp);
            textViewSkip = findViewById(R.id.button_skip);

            textViewSkip.setOnClickListener(this);
            buttonSignIn.setOnClickListener(this);
            buttonSignUp.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();

        switch (viewId) {
            case R.id.button_signIn_signIn:
                signIn();
                break;
            case R.id.button_signIn_signUp:
                signUp();
                break;
            case R.id.button_skip:
                skip_listener();
                break;
        }
    }

    private void skip_listener() {
        CatalogueActivity.launchActivity(this);
    }

    private void signIn() {
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        boolean isInputValid = true;

        if (!username.matches("^[a-zA-Z0-9]+$")) {
            isInputValid = false;
            editTextUsername.setError("Invalid username");
        }

        if (!password.matches("^.{6,}$")) {
            isInputValid = false;
            editTextPassword.setError("Invalid password");
        }

        if (isInputValid) {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            progressDialog = new ProgressDialog(this);
            progressDialog.setIndeterminate(true);
            progressDialog.setTitle("VegGKart");
            progressDialog.setMessage("Signing In...");
            progressDialog.show();

            APIHelper.userSignIn(username, password, this, this, this);
        }
    }

    private void signUp() {
        SignUpActivity.launchActivity(this);
    }

    @Override
    public void onResponse(JSONObject response) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        try {
            String userId = response.getString("userid");

            if (userId != null && !userId.equals("null")) {
                String username = editTextUsername.getText().toString().trim();

                User user = User.getInstance(response.toString());

                UserHelper.storeUserDetails(user, this);
                UserHelper.storeUsername(username, this);

                CatalogueActivity.launchActivity(this);
            } else {
                editTextPassword.setText("");
                editTextUsername.setError("Username or password invalid");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            onErrorResponse(new VolleyError("Corrupt network response"));
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.e("SIGN-IN", (new String(error.networkResponse.data, Charset.defaultCharset())));
        Snackbar.make(this.editTextUsername, "Some error occurred\nTry again after some time", Snackbar.LENGTH_LONG).show();
    }
}
