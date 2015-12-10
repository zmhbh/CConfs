package cmu.cconfs;

import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.GsonBuilder;
import com.parse.ParseObject;
import com.parse.ParsePush;

import org.json.JSONException;
import org.json.JSONObject;

import cmu.cconfs.model.parseModel.Message;

public class SendNotificationActivity extends AppCompatActivity {
    public EditText mEditText;
    public EditText mEditText1;

    public Button mButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_notification);
        mEditText = (EditText) findViewById(R.id.notification_text);
        mEditText1= (EditText) findViewById(R.id.notification_title);
        mButton = (Button) findViewById(R.id.send_Button);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Notification");
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setHomeButtonEnabled(false);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String notification = mEditText.getText().toString();
                String title = mEditText1.getText().toString();
                ParsePush push = new ParsePush();
                ParsePush.subscribeInBackground("CConfs");
                push.setChannel("CConfs");
                JSONObject data = new JSONObject();
                JSONObject json = new JSONObject();
                try {
                    data.put("message",notification);
                    data.put("title",title);
                    json.put("data", data);
                    json.put("is_background",false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                push.setData(json);
                //push.setMessage(send);
                push.sendInBackground();

                Message n = new Message();
                n.setMessage(notification);
                n.setTitle(title);
                n.saveInBackground();
                finish();
            }
        });
        
    }

}
