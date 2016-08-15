package com.veggkart.android.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.veggkart.android.R;
import com.veggkart.android.util.APIHelper;
import com.veggkart.android.util.UserHelper;

import org.json.JSONException;
import org.json.JSONObject;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener, Response.Listener<JSONObject>, Response.ErrorListener {

  private AppCompatEditText editTextUsername;
  private AppCompatEditText editTextPassword;
  private AppCompatTextView textViewForgotPassword;
  private AppCompatButton buttonSignIn;
  private AppCompatButton buttonSignUp;

  private ProgressDialog progressDialog;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    this.initialize();
  }

  private void initialize() {
    setContentView(R.layout.activity_sign_in);

    ActionBar actionBar = this.getSupportActionBar();

    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(false);
      actionBar.setTitle(this.getResources().getString(R.string.app_name));
    }

    this.editTextUsername = (AppCompatEditText) this.findViewById(R.id.editText_signIn_username);
    this.editTextPassword = (AppCompatEditText) this.findViewById(R.id.editText_signIn_password);
    this.textViewForgotPassword = (AppCompatTextView) this.findViewById(R.id.textView_signIn_forgotPassword);
    this.buttonSignIn = (AppCompatButton) this.findViewById(R.id.button_signIn_signIn);
    this.buttonSignUp = (AppCompatButton) this.findViewById(R.id.button_signIn_signUp);

    this.textViewForgotPassword.setOnClickListener(this);
    this.buttonSignIn.setOnClickListener(this);
    this.buttonSignUp.setOnClickListener(this);
  }

  @Override
  public void onClick(View view) {
    int viewId = view.getId();

    switch (viewId) {
      case R.id.textView_signIn_forgotPassword:
        this.forgotPassword();
        break;
      case R.id.button_signIn_signIn:
        this.signIn();
        break;
      case R.id.button_signIn_signUp:
        this.signUp();
        break;
    }
  }

  private void forgotPassword() {
    //ToDo: Implement actual logic
  }

  private void signIn() {
    String username = this.editTextUsername.getText().toString().trim();
    String password = this.editTextPassword.getText().toString().trim();

    boolean isInputValid = true;

    if (!username.matches("^[a-zA-Z0-9]+$")) {
      isInputValid = false;
      this.editTextUsername.setError("Invalid username");
    }

    if (!password.matches("\\S{6,}")) {
      isInputValid = false;
      this.editTextPassword.setError("Invalid password");
    }

    if (isInputValid) {
      if (this.progressDialog != null && this.progressDialog.isShowing()) {
        this.progressDialog.dismiss();
      }
      this.progressDialog = new ProgressDialog(this);
      this.progressDialog.setIndeterminate(true);
      this.progressDialog.setTitle("VegGKart");
      this.progressDialog.setMessage("Signing In...");
      this.progressDialog.show();

      APIHelper.userSignIn(username, password, this, this, this);
    }
  }

  private void signUp() {
    //ToDo: Implement actual logic
  }

  @Override
  public void onResponse(JSONObject response) {
    if (this.progressDialog != null && this.progressDialog.isShowing()) {
      this.progressDialog.dismiss();
    }

    try {
      String userId = response.getString("key");

      if (userId != null && !userId.equals("null")) {
        String username = this.editTextUsername.getText().toString().trim();

        UserHelper.storeUserId(userId, this);
        UserHelper.storeUsername(username, this);

        CatalogueActivity.launchActivity(this);
      } else {
        if (this.progressDialog != null && this.progressDialog.isShowing()) {
          this.progressDialog.dismiss();
        }
        this.editTextPassword.setText("");
        this.editTextUsername.setError("Username or password invalid");
      }
    } catch (JSONException e) {
      e.printStackTrace();
      this.onErrorResponse(new VolleyError("Corrupt network response"));
    }
  }

  @Override
  public void onErrorResponse(VolleyError error) {
    Snackbar.make(this.editTextUsername, "Some error occurred\nTry again after some time", Snackbar.LENGTH_LONG).show();
  }
}
