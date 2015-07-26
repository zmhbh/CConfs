package cmu.cconfs;

import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cmu.cconfs.adapter.PaperListViewAdapter;
import cmu.cconfs.model.parseModel.Paper;

public class SessionActivity extends BaseActivity implements ObservableScrollViewCallbacks {

    private View mHeaderView;
    private View mToolbarView;
    private ObservableScrollView mScrollView;
    private int mBaseTranslationY;

    private List<Paper> papers = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);

        String pNames = getIntent().getStringExtra("papers");
        //populate papers
        if (pNames != null && !pNames.isEmpty() && !pNames.trim().equals("0")) {
            pNames.trim();
            String[] paperNames = pNames.split(",");
            ParseQuery<Paper> query = Paper.getQuery();
            query.fromLocalDatastore();
            query.whereContainedIn("unique_id", new ArrayList<String>(Arrays.asList(paperNames)));
            query.fromPin(Paper.PIN_TAG);
            try {
                papers = query.find();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        String sessionTime = getIntent().getStringExtra("sessionTime");
        String sessionName = getIntent().getStringExtra("sessionName");
        String sessionRoom = getIntent().getStringExtra("sessionRoom");
        String sessionChair = getIntent().getStringExtra("sessionChair");

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        // session title, time and room
        getSupportActionBar().setTitle(sessionName);
        TextView stickyTextView = (TextView) findViewById(R.id.sticky);
        stickyTextView.setText(sessionTime + ", Room " + sessionRoom);
        // session chairman
        if (sessionChair == null || sessionChair.length() == 0) {
            CardView cardView = (CardView) findViewById(R.id.container);
            cardView.setVisibility(View.GONE);
        } else {
            TextView textView_chairman = (TextView) findViewById(R.id.chairman);
            textView_chairman.setText(sessionChair);
        }
        // session paper
        ListView listView_paper = (ListView) findViewById(R.id.listView_paper);
        PaperListViewAdapter adapter = new PaperListViewAdapter(this, papers);
        listView_paper.setAdapter(adapter);

        mHeaderView = findViewById(R.id.header);
        ViewCompat.setElevation(mHeaderView, getResources().getDimension(R.dimen.toolbar_elevation));
        mToolbarView = findViewById(R.id.toolbar);

        mScrollView = (ObservableScrollView) findViewById(R.id.scroll);
        mScrollView.setScrollViewCallbacks(this);

    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        if (dragging) {
            int toolbarHeight = mToolbarView.getHeight();
            if (firstScroll) {
                float currentHeaderTranslationY = ViewHelper.getTranslationY(mHeaderView);
                if (-toolbarHeight < currentHeaderTranslationY) {
                    mBaseTranslationY = scrollY;
                }
            }
            float headerTranslationY = ScrollUtils.getFloat(-(scrollY - mBaseTranslationY), -toolbarHeight, 0);
            ViewPropertyAnimator.animate(mHeaderView).cancel();
            ViewHelper.setTranslationY(mHeaderView, headerTranslationY);
        }
    }

    @Override
    public void onDownMotionEvent() {
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        mBaseTranslationY = 0;

        if (scrollState == ScrollState.DOWN) {
            showToolbar();
        } else if (scrollState == ScrollState.UP) {
            int toolbarHeight = mToolbarView.getHeight();
            int scrollY = mScrollView.getCurrentScrollY();
            if (toolbarHeight <= scrollY) {
                hideToolbar();
            } else {
                showToolbar();
            }
        } else {
            // Even if onScrollChanged occurs without scrollY changing, toolbar should be adjusted
            if (!toolbarIsShown() && !toolbarIsHidden()) {
                // Toolbar is moving but doesn't know which to move:
                // you can change this to hideToolbar()
                showToolbar();
            }
        }
    }

    private boolean toolbarIsShown() {
        return ViewHelper.getTranslationY(mHeaderView) == 0;
    }

    private boolean toolbarIsHidden() {
        return ViewHelper.getTranslationY(mHeaderView) == -mToolbarView.getHeight();
    }

    private void showToolbar() {
        float headerTranslationY = ViewHelper.getTranslationY(mHeaderView);
        if (headerTranslationY != 0) {
            ViewPropertyAnimator.animate(mHeaderView).cancel();
            ViewPropertyAnimator.animate(mHeaderView).translationY(0).setDuration(200).start();
        }
    }

    private void hideToolbar() {
        float headerTranslationY = ViewHelper.getTranslationY(mHeaderView);
        int toolbarHeight = mToolbarView.getHeight();
        if (headerTranslationY != -toolbarHeight) {
            ViewPropertyAnimator.animate(mHeaderView).cancel();
            ViewPropertyAnimator.animate(mHeaderView).translationY(-toolbarHeight).setDuration(200).start();
        }
    }
}
