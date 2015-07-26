package cmu.cconfs.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import cmu.cconfs.utils.data.DataProvider;


/**
 * Created by zmhbh on 7/28/15.
 */
@Deprecated
public class DataProviderFragment extends Fragment {

    private DataProvider mDataProvider;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
        //    mDataProvider=new DataProviderTime();
    }

    public DataProvider getDataProvider() {
        return mDataProvider;
    }
}
