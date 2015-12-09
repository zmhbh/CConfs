package cmu.cconfs.model.parseModel;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by zmhbh on 8/21/15.
 */
@ParseClassName("Sponsor")
public class Sponsor extends ParseObject {
    public static final String PIN_TAG = "ALL_SPONSORS";

    public Sponsor() {

    }

    public String getName() {
        return getString("name");
    }

    public String getLink() {
        return getString("link");
    }

    public String getImageUrl() {
        return getString("imageUrl");
    }

    public static ParseQuery<Sponsor> getQuery() {
        return ParseQuery.getQuery(Sponsor.class);
    }
}
