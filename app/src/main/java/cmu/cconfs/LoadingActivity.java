package cmu.cconfs;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.jlmd.animatedcircleloadingview.AnimatedCircleLoadingView;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import cmu.cconfs.model.CheckVersion;
import cmu.cconfs.model.parseModel.Paper;
import cmu.cconfs.model.parseModel.Program;
import cmu.cconfs.model.parseModel.Room;
import cmu.cconfs.model.parseModel.Session_Room;
import cmu.cconfs.model.parseModel.Session_Timeslot;
import cmu.cconfs.model.parseModel.Timeslot;
import cmu.cconfs.model.parseModel.Version;
import cmu.cconfs.utils.data.DataProvider;

public class LoadingActivity extends AppCompatActivity {

    private AnimatedCircleLoadingView animatedCircleLoadingView;

    private boolean success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        animatedCircleLoadingView = (AnimatedCircleLoadingView) findViewById(R.id.circle_loading_view);
        animatedCircleLoadingView.startIndeterminate();

        startProcessingThread();

    }

    private void startProcessingThread() {
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {


            }

            @Override
            protected Void doInBackground(Void... arg0) {

                try {
                    //Do something...
                    preProcessing();

                    Thread.sleep(3000);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {


                if (success) {
                    Intent intent = new Intent();
                    intent.setClass(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();


//                    Toast.makeText(getApplicationContext(), "Loading Data finished",
//                            Toast.LENGTH_SHORT).show();
                }

            }

        };
        task.execute((Void[]) null);
    }


    private void preProcessing() {
        if (!checkLocalExists()) {
            if (checkNetwork()) {
                loadFromParse();
            } else {
                //enable network

                //if failure
                animatedCircleLoadingView.stopFailure();
                return;
            }
        } else {
            if (!checkNetwork()) {
                // for future
                //if network re-connect
                if (true) {
                    if (!isUpToDate())
                        loadFromParse();
                }
            } else {
                if (!isUpToDate())
                    loadFromParse();
            }
        }


        success = true;
        populateDataProvider();
//        Toast.makeText(LoadingActivity.this, "populateDataProvider finished:",
//                Toast.LENGTH_SHORT).show();
        animatedCircleLoadingView.stopOk();
    }

    private boolean checkNetwork() {
        return true;
    }

    private boolean checkLocalExists() {
        Version local = null;
        ParseQuery<Version> query = Version.getQuery();
        query.fromLocalDatastore();
        query.fromPin(Version.PIN_TAG);
        try {
            local = query.getFirst();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return local != null;
    }

    private boolean isUpToDate() {
        Version local = null;
        Version remote = null;

        ParseQuery<Version> query = Version.getQuery();
        query.fromLocalDatastore();
        query.fromPin(Version.PIN_TAG);
        try {
            local = query.getFirst();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        query = Version.getQuery();
        try {
            remote = query.getFirst();
            ;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        CheckVersion checkVersion = new CheckVersion(local);


        return checkVersion.equals(remote);
    }


    private void loadFromParse() {
        ParseQuery paperQuery = Paper.getQuery();
        ParseQuery programQuery = Program.getQuery();
        ParseQuery roomQuery = Room.getQuery();
        ParseQuery sessionRoomQuery = Session_Room.getQuery();
        ParseQuery sessionTimeslotQuery = Session_Timeslot.getQuery();
        ParseQuery timeslotQuery = Timeslot.getQuery();
        ParseQuery versionQuery = Version.getQuery();
        try {
            ParseObject.pinAll(Paper.PIN_TAG, paperQuery.find());
            ParseObject.pinAll(Program.PIN_TAG, programQuery.find());
            ParseObject.pinAll(Room.PIN_TAG, roomQuery.find());
            ParseObject.pinAll(Session_Room.PIN_TAG, sessionRoomQuery.find());
            ParseObject.pinAll(Session_Timeslot.PIN_TAG, sessionTimeslotQuery.find());
            ParseObject.pinAll(Timeslot.PIN_TAG, timeslotQuery.find());
            ParseObject.pinAll(Version.PIN_TAG, versionQuery.find());
        } catch (ParseException e) {
            animatedCircleLoadingView.stopFailure();
        }
    }

    private void populateDataProvider() {
        CConfsApplication application = new CConfsApplication();
        DataProvider dataProvider = new DataProvider();
        application.setDataProvider(dataProvider);
    }
}
