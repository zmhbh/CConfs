package cmu.cconfs;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

public class ResetPasswordActivity extends AppCompatActivity {

    protected Button mResetButton;
    EditText mEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        mResetButton = (Button) findViewById(R.id.resetButton);

        mEmail = (EditText) findViewById(R.id.email_reset);

        mResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();
                if (email.isEmpty()) {
                    postError("Invalid email address");

                } else {
                    resetPassword(email);
                }
            }
        });


    }

    private void postError(String error) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(error)
                .setTitle("Please enter a valid email address")
                .setPositiveButton(android.R.string.ok, null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void resetPassword(String email) {
        ParseUser user = new ParseUser();
        user.setEmail(email);

// other fields can be set just like with ParseObject
//        user.put("phone", "650-253-0000");


        user.requestPasswordResetInBackground(email, new RequestPasswordResetCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(getApplicationContext(),
                            "Email has been sent, please check your mailbox",
                            Toast.LENGTH_LONG).show();
                    finish();



                } else {
                    postError(e.getMessage());
                    // Something went wrong. Look at the ParseException to see what's up.
                }
            }
        });
    }
}