package cmu.cconfs;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import cmu.cconfs.utils.ResetPasswordActivity;

public class LoginActivity extends AppCompatActivity {
    protected Button mLoginButton;
    protected TextView mSignUpTextView;
    protected TextView mResetPasswordTextView;

    EditText mUsername;
    EditText mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login3);


        mSignUpTextView = (TextView) findViewById(R.id.signUpText);
        mResetPasswordTextView = (TextView) findViewById(R.id.resetPasswordText);
        mSignUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
        mResetPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
                startActivity(intent);
            }
        });


        mLoginButton = (Button) findViewById(R.id.loginButton);
        mUsername = (EditText) findViewById(R.id.usernameField);
        mPassword = (EditText) findViewById(R.id.passwordField);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = mUsername.getText().toString();
                String password = mPassword.getText().toString();

                username = username.trim();
                password = password.trim();
                if (username.isEmpty() || password.isEmpty()) {
                    postError("Invalid username/password");

                } else {
                    // Login
                    ParseUser.logInInBackground(username, password, new LogInCallback() {
                        public void done(ParseUser user, ParseException e) {
                            if (user != null) {
                                Log.e("login", "success");
                                Toast.makeText(getApplicationContext(),
                                        "You have been successfully logged in!",
                                        Toast.LENGTH_LONG).show();
                                finish();
                                // Hooray! The user is logged in.
                            } else {
                                postError(e.getMessage());
                            }
                        }
                    });

                }
            }
        });



    }
    private void postError(String error){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(error)
                .setTitle("Please try again")
                .setPositiveButton(android.R.string.ok, null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }


}
