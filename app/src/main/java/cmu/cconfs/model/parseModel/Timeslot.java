package cmu.cconfs.model.parseModel;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by zmhbh on 8/24/15.
 */
@ParseClassName("Timeslot")
public class Timeslot extends ParseObject {

    public static final String PIN_TAG = "ALL_TIMESLOTS";

    public void setTimeslotId(int id) {
        put("timeslot_id", id);
    }

    public int getTimeslotId() {
        return getInt("timeslot_id");
    }

    public void setProgramId(int id) {
        put("program_id", id);
    }

    public int getProgramId() {
        return getInt("program_id");
    }

    public void setValue(String value) {
        put("value", value);
    }

    public String getValue() {
        return getString("value");
    }

    public static ParseQuery<Timeslot> getQuery() {
        return ParseQuery.getQuery(Timeslot.class);
    }
}
