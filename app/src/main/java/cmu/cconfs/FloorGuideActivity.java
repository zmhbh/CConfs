package cmu.cconfs;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

import com.xgc1986.parallaxPagerTransformer.ParallaxPagerTransformer;

import cmu.cconfs.adapter.FloorGuideAdapter;
import cmu.cconfs.fragment.FloorGuideFragment;


public class FloorGuideActivity extends FragmentActivity {

    ViewPager mPager;
    FloorGuideAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floor_guide);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setBackgroundColor(0xFF000000);

        ParallaxPagerTransformer pt = new ParallaxPagerTransformer(R.id.image);
        pt.setBorder(20);
        // pt.setSpeed(0.8f);

        mPager.setPageTransformer(false, pt);

        mAdapter = new FloorGuideAdapter(getSupportFragmentManager());
        mAdapter.setPager(mPager);


        Bundle thirdFloor = new Bundle();
        thirdFloor.putInt("image", R.drawable.third);
        thirdFloor.putString("name", "Third Floor");
        FloorGuideFragment thirdFloorFragment = new FloorGuideFragment();
        thirdFloorFragment.setArguments(thirdFloor);

        Bundle fourthFloor = new Bundle();
        fourthFloor.putInt("image", R.drawable.fourth);
        fourthFloor.putString("name", "Fourth Floor");
        FloorGuideFragment fourthFloorFragment = new FloorGuideFragment();
        fourthFloorFragment.setArguments(fourthFloor);

        Bundle seventhFloor = new Bundle();
        seventhFloor.putInt("image", R.drawable.seventh);
        seventhFloor.putString("name", "Seventh Floor");
        FloorGuideFragment seventhFloorFragment = new FloorGuideFragment();
        seventhFloorFragment.setArguments(seventhFloor);

        mAdapter.add(thirdFloorFragment);
        mAdapter.add(fourthFloorFragment);
        mAdapter.add(seventhFloorFragment);

        mPager.setAdapter(mAdapter);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();

        return super.onOptionsItemSelected(item);
    }
}
