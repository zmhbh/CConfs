package cmu.cconfs.model.parseModel;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by zmhbh on 8/24/15.
 */

@ParseClassName("Program")
public class Program extends ParseObject {

    public static final String PIN_TAG = "ALL_PROGRAMS";

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


    public static ParseQuery<Program> getQuery() {
        return ParseQuery.getQuery(Program.class);
    }


}
