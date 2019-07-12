package com.example.parstagram;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.parstagram.model.User;
import com.parse.ParseException;
import com.parse.SignUpCallback;

public class SignupActivity extends AppCompatActivity {

    private EditText fullNameInput;
    private EditText userNameInput;
    private EditText emailInput;
    private EditText passwordInput;
    private EditText confirmPasswordInput;
    private Button signupBtn;
    private Button loginBtn;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        fullNameInput = findViewById(R.id.fullNameInput);
        userNameInput = findViewById(R.id.userNameInput);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput);
        signupBtn = findViewById(R.id.signupBtn);
        loginBtn = findViewById(R.id.loginBtn);

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Get input values
                final String password = passwordInput.getText().toString();
                final String confirmPassword = confirmPasswordInput.getText().toString();
                final String username = userNameInput.getText().toString();
                final String fullname = fullNameInput.getText().toString();
                final String email = emailInput.getText().toString();

                // Check password validity
                if(!password.equals(confirmPassword)){
                    confirmPasswordInput.setError("Confirmation does not match password.");
                    return;
                } else { // Register user
                    registerUser(username, fullname, email, password);
                }
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Initialize progress dialog
        pd = new ProgressDialog(this);
        pd.setTitle("Loading...");
        pd.setMessage("Please wait.");
        pd.setCancelable(false);
    }

    private void registerUser(String username, String fullname, String email, String password){
        // Show progress dialog
        pd.show();
        // Create the ParseUser
        User user = new User();
        // Set core properties
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        // Set custom properties
        user.setFullName(fullname);
        // Invoke signUpInBackground
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    // Success -- Redirect to home activity
                    final Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // Signup failed
                    e.printStackTrace();
                }

                // Dismiss progress dialog
                pd.dismiss();
            }
        });
    }
}
