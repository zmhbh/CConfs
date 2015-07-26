package cmu.cconfs.model.parseModel;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by zmhbh on 8/27/15.
 */
@ParseClassName("Message")
public class Message extends ParseObject {
    public static final String PIN_TAG = "ALL_MESSAGES";

    public void setTitle(String title) {
        put("title", title);
    }

    public String getTitle() {
        return getString("title");
    }

    public void setMessage(String message) {
        put("message", message);
    }

    public String getMessage() {
        return getString("message");
    }

    public static ParseQuery<Message> getQuery() {
        return ParseQuery.getQuery(Message.class);
    }
}
