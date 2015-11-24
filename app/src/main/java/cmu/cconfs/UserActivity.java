package cmu.cconfs;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.parse.ParseUser;

import cmu.cconfs.utils.PreferencesManager;

public class UserActivity extends Activity {
    PreferencesManager mPreferencesManager;


    protected Button mSignOutButton;
    protected TextView mCurrentUserTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        mCurrentUserTextView = (TextView) findViewById(R.id.current_username);
        mCurrentUserTextView.setText(ParseUser.getCurrentUser().getUsername());
        mPreferencesManager = new PreferencesManager(this);



        mSignOutButton = (Button) findViewById(R.id.signOutButton);
        mSignOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                mPreferencesManager.writeBooleanPreference("LoggedIn", false);
                finish();
            }
        });
    }

}
