package cmu.cconfs.utils;

import android.graphics.drawable.Drawable;

/**
 * Created by zmhbh on 7/29/15.
 */
public class DrawableUtils {
    private static final int[] EMPTY_STATE = new int[]{};

    public static void clearState(Drawable drawable) {
        if (drawable != null) {
            drawable.setState(EMPTY_STATE);
        }
    }
}
