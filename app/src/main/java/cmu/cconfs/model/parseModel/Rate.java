package cmu.cconfs.model.parseModel;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by mangobin on 15/11/19.
 */
@ParseClassName("Rate")
public class Rate extends ParseObject {
    public Rate() {
    }

    public String getPaperKey() {
        return getString("paperKey");
    }

    public void setPaperKey(String paperKey) {
        put("paperKey", paperKey);
    }

    public void setRate(double rate) {
        put("rate", rate);
    }

    public double getRate() {
        return getDouble("rate");
    }

    public void setRater(String rater) {
        put("rater", rater);
    }

    public String getRater() {
        return getString("rater");
    }
}
