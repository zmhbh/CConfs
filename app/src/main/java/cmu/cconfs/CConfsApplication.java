package cmu.cconfs;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseUser;

import cmu.cconfs.model.Todo;
import cmu.cconfs.model.parseModel.Message;
import cmu.cconfs.model.parseModel.Paper;
import cmu.cconfs.model.parseModel.Program;
import cmu.cconfs.model.parseModel.Room;
import cmu.cconfs.model.parseModel.Session_Room;
import cmu.cconfs.model.parseModel.Session_Timeslot;
import cmu.cconfs.model.parseModel.Timeslot;
import cmu.cconfs.model.parseModel.Version;
import cmu.cconfs.utils.data.DataProvider;
import cmu.cconfs.utils.data.UnityDataProvider;

/**
 * Created by zmhbh on 8/23/15.
 */
public class CConfsApplication extends Application {
    public static final String TODO_GROUP_NAME = "ALL_TODOS";

    private static DataProvider dataProvider;

    public void setDataProvider(DataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    public UnityDataProvider getUnityDataProvider(int dateIndex) {
        return dataProvider.getUnityDataProvider(dateIndex);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // facebook image library
        Fresco.initialize(this);

        // add Todo subclass
        ParseObject.registerSubclass(Todo.class);

        ParseObject.registerSubclass(Paper.class);
        ParseObject.registerSubclass(Program.class);
        ParseObject.registerSubclass(Room.class);
        ParseObject.registerSubclass(Session_Room.class);
        ParseObject.registerSubclass(Session_Timeslot.class);
        ParseObject.registerSubclass(Timeslot.class);
        ParseObject.registerSubclass(Version.class);
        ParseObject.registerSubclass(Message.class);

        // enable the Local Datastore
        Parse.enableLocalDatastore(getApplicationContext());
        Parse.initialize(this, "XHVlAsAd6VC4VcL0FaTuRO3HuhpDlN7AlkWoJgLE", "kjyUc3p4BTrb2MRBwz6IrsohhTW3EyPtAoXaAXFu");
        ParseInstallation.getCurrentInstallation().saveInBackground();
        ParsePush.subscribeInBackground("CConfs");

        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        ParseACL.setDefaultACL(defaultACL, true);

    }


}
