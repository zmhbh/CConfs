package cmu.cconfs.model.parseModel;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by zmhbh on 8/25/15.
 */
@ParseClassName("Version")
public class Version extends ParseObject {
    public static final String PIN_TAG = "ALL_VERSIONS";

    public void setVersion(String version) {
        put("version", version);
    }

    public String getVersion() {
        return getString("version");
    }

    public static ParseQuery<Version> getQuery() {
        return ParseQuery.getQuery(Version.class);
    }

}
