package cmu.cconfs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import org.askerov.dynamicgrid.DynamicGridView;

import java.util.ArrayList;
import java.util.Arrays;

import cmu.cconfs.adapter.HomeGridDynamicAdapter;


public class HomeActivity extends AppCompatActivity {

    private static final String TAG = HomeActivity.class.getName();
    private DynamicGridView gridView;
    private String[] titles = {"Program", "My Schedule", "Map", "Floor Guide", "Sponsor", "Notification", "About", "Setting", "Chat"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        gridView = (DynamicGridView) findViewById(R.id.dynamic_grid);
        gridView.setAdapter(new HomeGridDynamicAdapter(this
                , new ArrayList<String>(Arrays.asList(titles))
                , getResources().getInteger(R.integer.column_count)));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(HomeActivity.this, parent.getAdapter().getItem(position).toString(),
                        Toast.LENGTH_SHORT).show();

                Intent intent = new Intent();
                switch (position) {
                    //agenda
                    case 0:
                        intent.setClass(getApplicationContext(), AgendaActivity.class);
                        startActivity(intent);
                        break;

                    //schedule
                    case 1:
                        intent.setClass(getApplicationContext(), ScheduleActivity.class);
                        startActivity(intent);
                        break;

                    //map
                    case 2:
                        intent.setClass(getApplicationContext(), MapActivity.class);
                        startActivity(intent);
                        break;

                    //Floor Guide
                    case 3:
                        intent.setClass(getApplicationContext(), FloorGuideActivity.class);
                        startActivity(intent);
                        break;

                    //Sponsor
                    case 4:
                        intent.setClass(getApplicationContext(), SponsorActivity.class);
                        startActivity(intent);
                        break;

                    //Notification
                    case 5:
                        intent.setClass(getApplicationContext(), NotificationActivity.class);
                        startActivity(intent);
                        break;

                    //About
                    case 6:
                        intent.setClass(getApplicationContext(), AboutActivity.class);
                        intent.putExtra("link", "about");
                        startActivity(intent);
                        break;

                    //Setting
                    case 7:

//                        intent.setClass(getApplicationContext(),PaperActivity.class);
//                        startActivity(intent);
                        break;

                    case 8:
                        intent.setClass(getApplicationContext(), IMActivity.class);
                        startActivity(intent);
                        break;

                    default:
                        break;
                }
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_home);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setTitle("Home");
    }


}
