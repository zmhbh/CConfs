package cmu.cconfs.instantMessage.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import cmu.cconfs.instantMessage.imlib.controller.HXSDKHelper;

/**
 * Created by zmhbh on 10/6/15.
 */
public class IMBaseActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // during onResume，disable notification
        HXSDKHelper.getInstance().getNotifier().reset();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    /**
     *return
     *
     * @param view
     */
    public void back(View view) {
        finish();
    }
}
