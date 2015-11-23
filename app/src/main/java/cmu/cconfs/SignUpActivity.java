package cmu.cconfs;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.easemob.EMError;
import com.easemob.chat.EMChatManager;
import com.easemob.exceptions.EaseMobException;
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
        final ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);

// other fields can be set just like with ParseObject
//        user.put("phone", "650-253-0000");

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage(getResources().getString(R.string.Is_the_registered));
        pd.show();

        new Thread(new Runnable() {
            public void run() {
                try {
                    // 调用sdk注册方法
                    EMChatManager.getInstance().createAccountOnServer(mUsername.getText().toString().trim(), mPassword.getText().toString().trim());
                    runOnUiThread(new Runnable() {
                        public void run() {
                            if (!SignUpActivity.this.isFinishing())
                                pd.dismiss();
                            // 保存用户名
                            CConfsApplication.getInstance().setUserName(mUsername.getText().toString().trim());
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.Registered_successfully), Toast.LENGTH_SHORT).show();

                            user.signUpInBackground(new SignUpCallback() {
                                public void done(ParseException e) {
                                    if (e == null) {
                                        Log.e("sign up", "success");
                                        Toast.makeText(getApplicationContext(), "Sign up Success!", Toast.LENGTH_LONG).show();
                                        try {
                                            EMChatManager.getInstance().createAccountOnServer(mUsername.getText().toString().trim(), mPassword.getText().toString().trim());
                                            // 保存用户名
                                            CConfsApplication.getInstance().setUserName(mUsername.getText().toString().trim());
                                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.Registered_successfully), Toast.LENGTH_SHORT).show();
                                            finish();
                                        } catch (final EaseMobException ee) {
                                            int errorCode = ee.getErrorCode();
                                            if (errorCode == EMError.NONETWORK_ERROR) {
                                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.network_anomalies), Toast.LENGTH_SHORT).show();
                                            } else if (errorCode == EMError.USER_ALREADY_EXISTS) {
                                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.User_already_exists), Toast.LENGTH_SHORT).show();
                                            } else if (errorCode == EMError.UNAUTHORIZED) {
                                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.registration_failed_without_permission), Toast.LENGTH_SHORT).show();
                                            } else if (errorCode == EMError.ILLEGAL_USER_NAME) {
                                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.illegal_user_name), Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.Registration_failed) + ee.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }


                                        // Hooray! Let them use the app now.
                                    } else {
                                        if (e.getMessage().contains("This email has already been registered")) {
                                            postError("This email has already been registered. You can reset your password at the login page.");
                                        } else
                                            postError(e.getMessage());
                                    }
                                }
                            });

                            finish();
                        }
                    });
                } catch (final EaseMobException e) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            if (!SignUpActivity.this.isFinishing())
                                pd.dismiss();
                            int errorCode=e.getErrorCode();
                            if(errorCode== EMError.NONETWORK_ERROR){
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.network_anomalies), Toast.LENGTH_SHORT).show();
                            }else if(errorCode == EMError.USER_ALREADY_EXISTS){
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.User_already_exists), Toast.LENGTH_SHORT).show();
                            }else if(errorCode == EMError.UNAUTHORIZED){
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.registration_failed_without_permission), Toast.LENGTH_SHORT).show();
                            }else if(errorCode == EMError.ILLEGAL_USER_NAME){
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.illegal_user_name), Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.Registration_failed) + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        }).start();


    }

}
