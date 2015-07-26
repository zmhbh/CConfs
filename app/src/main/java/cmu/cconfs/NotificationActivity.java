package cmu.cconfs;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

import cmu.cconfs.adapter.NotificationAdapter;
import cmu.cconfs.model.parseModel.Message;
import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;

public class NotificationActivity extends AppCompatActivity implements WaveSwipeRefreshLayout.OnRefreshListener {

    private ListView mListview;
    private Toolbar toolbar;

    private WaveSwipeRefreshLayout mWaveSwipeRefreshLayout;

    private List<Message> messages;
    private NotificationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().setNavigationBarColor(Color.BLACK);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_notification);
        initView();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Notification");

    }

    private void initView() {
        mWaveSwipeRefreshLayout = (WaveSwipeRefreshLayout) findViewById(R.id.main_swipe);
        mWaveSwipeRefreshLayout.setColorSchemeColors(Color.WHITE, Color.WHITE);
        mWaveSwipeRefreshLayout.setOnRefreshListener(this);
        mWaveSwipeRefreshLayout.setWaveColor(0xff32cd80);

        //mWaveSwipeRefreshLayout.setMaxDropHeight(1500);

        mListview = (ListView) findViewById(R.id.main_list);
        adapter = new NotificationAdapter(this, messages);
        mListview.setAdapter(adapter);

    }


    private void refresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ParseQuery<Message> query = Message.getQuery();
                query.orderByDescending("createdAt");
                try {
                    messages = query.find();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                adapter.updateDataSet(messages);
                adapter.notifyDataSetChanged();
                mWaveSwipeRefreshLayout.setRefreshing(false);

            }
        }, 3000);
    }

    @Override
    protected void onResume() {

        super.onResume();
        mWaveSwipeRefreshLayout.setRefreshing(true);

        refresh();


    }

    @Override
    public void onRefresh() {
        refresh();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            mWaveSwipeRefreshLayout.setRefreshing(true);
            refresh();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
