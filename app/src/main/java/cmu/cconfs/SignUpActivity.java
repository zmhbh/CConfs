package cmu.cconfs;

import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends AppCompatActivity {
    protected Button mSignUpButton;
    EditText mUsername;
    EditText mPassword ;
    EditText mConfirmPassword;
    EditText mEmail ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        mSignUpButton = (Button) findViewById(R.id.signupButton);

        mUsername = (EditText) findViewById(R.id.usernameField);
        mPassword = (EditText) findViewById(R.id.passwordField);
        mConfirmPassword = (EditText) findViewById(R.id.passwordConfirmField);
        mEmail = (EditText) findViewById(R.id.emailField);

        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = mUsername.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String confirmPassword = mConfirmPassword.getText().toString().trim();
                String email = mEmail.getText().toString().trim();


                if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || email.isEmpty()) {
                    Log.e("name",username);
                    Log.e("pass",password);
                    Log.e("conf",confirmPassword);
                    Log.e("em",email);
                    postError("Invalid input information");

                }
                else if (! password.equals(confirmPassword)){
                    postError("Password don not match");
                }
                else {
                    signUp(username, password, email);
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
    private void signUp(String username, String password, String email){
        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);

// other fields can be set just like with ParseObject
//        user.put("phone", "650-253-0000");

        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    Log.e("sign up", "success");
                    Toast.makeText(getApplicationContext(), "Sign up Success!", Toast.LENGTH_LONG).show();
                    finish();

                    // Hooray! Let them use the app now.
                } else {
                    if(e.getMessage().contains("This email has already been registered")) {
                        postError("This email has already been registered. You can reset your password at the login page.");
                    }
                    else
                        postError(e.getMessage());
                }
            }
        });
    }

}
