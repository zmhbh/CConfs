package cmu.cconfs.utils.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.wnafee.vector.MorphButton;

import cmu.cconfs.R;

/**
 * Created by zmhbh on 7/29/15.
 */
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
class ExpandableItemIndicatorImplAnim extends ExpandableItemIndicator.Impl {
    private MorphButton mMorphButton;

    @Override
    public void onInit(Context context, AttributeSet attrs, int defStyleAttr, ExpandableItemIndicator thiz) {
        View v = LayoutInflater.from(context).inflate(R.layout.widget_expandable_item_indicator_anim, thiz, true);
        mMorphButton = (MorphButton) v.findViewById(R.id.morph_button);
    }

    @Override
    public void setExpandedState(boolean isExpanded, boolean animate) {
        MorphButton.MorphState indicatorState = (isExpanded) ? MorphButton.MorphState.END : MorphButton.MorphState.START;

        if (mMorphButton.getState() != indicatorState) {
            mMorphButton.setState(indicatorState, animate);
        }
    }
}