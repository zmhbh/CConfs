package cmu.cconfs.model.parseModel;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by zmhbh on 8/24/15.
 */
@ParseClassName("Session_Room")
public class Session_Room extends ParseObject {

    public static final String PIN_TAG = "ALL_SESSION_ROOMS";

    public void setChair(String chair) {
        put("chair", chair);
    }

    public String getChair() {
        return getString("chair");
    }

    public void setPapers(String papers) {
        put("papers", papers);
    }

    public String getPapers() {
        return getString("papers");
    }

    public void setRoomId(int id) {
        put("room_id", id);
    }

    public int getRoomId() {
        return getInt("room_id");
    }

    public void setSelected(int selected) {
        put("selected", selected);
    }
    public String getSessionTitle() {
        return getString("session_title");
    }
    public String getValue() {
        return getString("value");
    }



    public int getSelected() {
        return getInt("selected");
    }

    public void setSessionId(int id) {
        put("session_id", id);
    }

    public int getSessionId() {
        return getInt("session_id");
    }
    public String getTimeslot() {
        return getString("timeslot");
    }

    public static ParseQuery<Session_Room> getQuery() {
        ParseQuery<Session_Room> parseQuery = ParseQuery.getQuery(Session_Room.class);
        parseQuery.setLimit(1000);
        return parseQuery;
    }
}
