package cmu.cconfs.model.parseModel;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by zmhbh on 8/24/15.
 */
@ParseClassName("Session_Timeslot")
public class Session_Timeslot extends ParseObject {

    public static final String PIN_TAG = "ALL_SESSION_TIMESLOTS";

    public void setSessionId(int id) {
        put("session_id", id);
    }

    public int getSessionId() {
        return getInt("session_id");
    }

    public void setValue(String value) {
        put("value", value);
    }

    public String getValue() {
        return getString("value");
    }

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

    public void setRoom(String room) {
        put("room", room);
    }

    public String getRoom() {
        return getString("room");
    }

    public void setSelected(int selected) {
        put("selected", selected);
    }

    public int getSelected() {
        return getInt("selected");
    }

    public void setSessionTitle(String sessionTitle) {
        put("session_title", sessionTitle);
    }

    public String getSessionTitle() {
        return getString("session_title");
    }

    public void setTimeslotId(int id) {
        put("timeslot_id", id);
    }

    public int getTimeslotId() {
        return getInt("timeslot_id");
    }

    public static ParseQuery<Session_Timeslot> getQuery() {
        return ParseQuery.getQuery(Session_Timeslot.class);
    }
}
