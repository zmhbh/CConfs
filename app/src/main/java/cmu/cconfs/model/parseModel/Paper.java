package cmu.cconfs.model.parseModel;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by zmhbh on 8/24/15.
 */

@ParseClassName("Paper")
public class Paper extends ParseObject {
    public static final String PIN_TAG = "ALL_PAPERS";

    public void setPaperId(int id) {
        put("paper_id", id);
    }

    public int getPaperId() {
        return getInt("paper_id");
    }

    public void setUniqueId(String uniqueId) {
        put("unique_id", uniqueId);
    }

    public String getUniqueId() {
        return getString("unique_id");
    }

    public void setAffiliation(String aff) {
        put("affiliation", aff);
    }

    public String getAffiliation() {
        return getString("affiliation");
    }

    public void setAuhtor(String author) {
        put("author", author);
    }

    public String getAuthor() {
        return getString("author");
    }

    public void setAuthorWithAff(String authorWithAff) {
        put("authorwithaffiliation", authorWithAff);
    }

    public String getAuthorWithAff() {
        return getString("authorwithaffiliation");
    }

    public void setTitle(String title) {
        put("title", title);
    }

    public String getTitle() {
        return getString("title");
    }

    public void setAbstract(String abs) {
        put("abstract", abs);
    }

    public String getAbstract() {
        return getString("abstract");
    }

    public static ParseQuery<Paper> getQuery() {
        return ParseQuery.getQuery(Paper.class);
    }
}
