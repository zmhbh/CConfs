package cmu.cconfs.adapter;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemViewHolder;
import com.rey.material.widget.CheckBox;

import cmu.cconfs.R;
import cmu.cconfs.SessionActivity;
import cmu.cconfs.utils.data.AbstractExpandableDataProvider;
import cmu.cconfs.utils.data.UnityDataProvider;
import cmu.cconfs.utils.widget.ExpandableItemIndicator;

/**
 * Created by zmhbh on 7/30/15.
 */
public class ExpandableItemAdapter extends AbstractExpandableItemAdapter<ExpandableItemAdapter.MyGroupViewHolder, ExpandableItemAdapter.MyChildViewHolder> {

    private UnityDataProvider unityDataProvider;

    public ExpandableItemAdapter(UnityDataProvider unityDataProvider) {
        this.unityDataProvider = unityDataProvider;
        // ExpandableItemAdapter requires stable ID, and also
        // have to implement the getGroupItemId()/getChildItemId() methods appropriately.
        setHasStableIds(true);
    }

    @Override
    public int getGroupCount() {
        return unityDataProvider.getGroupCount() + 1;
    }

    @Override
    public int getChildCount(int groupPosition) {
        if (groupPosition == 0)
            return 0;
        return unityDataProvider.getChildCount(groupPosition - 1);
    }

    @Override
    public long getGroupId(int groupPosition) {
        if (groupPosition == 0)
            return Integer.MIN_VALUE;
        return unityDataProvider.getGroupItem(groupPosition - 1).getGroupId();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        if (groupPosition == 0)
            return Integer.MIN_VALUE;

        return unityDataProvider.getChildItem(groupPosition - 1, childPosition).getChildId();
    }

    @Override
    public int getGroupItemViewType(int groupPosition) {
        if (groupPosition == 0)
            return 100;
        return 0;
    }

    @Override
    public int getChildItemViewType(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public MyGroupViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == 100) {
            final View view = inflater.inflate(R.layout.material_view_pager_placeholder, parent, false);
            return new MyGroupViewHolder(view, 100);
        }


        final View v = inflater.inflate(R.layout.list_expandable_group_item, parent, false);
        return new MyGroupViewHolder(v);
    }

    @Override
    public MyChildViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.list_expandable_item, parent, false);
        return new MyChildViewHolder(v);
    }

    @Override
    public void onBindGroupViewHolder(MyGroupViewHolder holder, int groupPosition, int viewType) {

        if (groupPosition == 0)
            return;

        // child item
        final AbstractExpandableDataProvider.BaseData item = unityDataProvider.getGroupItem(groupPosition - 1);

        // set text
        holder.mTextView.setText(item.getText());

        // mark as clickable
        holder.itemView.setClickable(true);

        // set background resource (target view ID: container)
        final int expandState = holder.getExpandStateFlags();

        if ((expandState & RecyclerViewExpandableItemManager.STATE_FLAG_IS_UPDATED) != 0) {
            int bgResId;
            boolean isExpanded;

            if ((expandState & RecyclerViewExpandableItemManager.STATE_FLAG_IS_EXPANDED) != 0) {
                bgResId = R.drawable.bg_group_item_expanded_state;
                isExpanded = true;
            } else {
                bgResId = R.drawable.bg_group_item_normal_state;
                isExpanded = false;
            }

//            holder.mContainer.setCardBackgroundColor(Color.rgb(224,224,224));
            holder.mIndicator.setExpandedState(isExpanded, true);
        }
    }

    @Override
    public void onBindChildViewHolder(final MyChildViewHolder holder, final int groupPosition, final int childPosition, int viewType) {
        if (groupPosition == 0)
            return;
        // group item
        final UnityDataProvider.ConcreteChildData childItem = unityDataProvider.getChildItem(groupPosition - 1, childPosition);

        // set text
        holder.firstTextView.setText(childItem.getFirstText());
        holder.secondTextView.setText(childItem.getSecondText());
        holder.checkBox.setChecked(childItem.getSelected());
        holder.checkBox.setTag(childItem);


        holder.firstTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SessionActivity.class);
                intent.putExtra("sessionChair", childItem.getChair());
                intent.putExtra("sessionRoom", childItem.getSessionRoom());
                intent.putExtra("sessionName", childItem.getSessionName());
                intent.putExtra("papers", childItem.getPaperIdArray());

                intent.putExtra("sessionTime", unityDataProvider.getGroupItem(groupPosition - 1).getText());
                v.getContext().startActivity(intent);
            }
        });

        holder.secondTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SessionActivity.class);
                intent.putExtra("sessionChair", childItem.getChair());
                intent.putExtra("sessionRoom", childItem.getSessionRoom());
                intent.putExtra("sessionName", childItem.getSessionName());
                intent.putExtra("papers", childItem.getPaperIdArray());
                intent.putExtra("sessionTime", unityDataProvider.getGroupItem(groupPosition - 1).getText());
                v.getContext().startActivity(intent);
            }
        });

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox view = (CheckBox) v;
                if (view.isChecked()) {
                    ((UnityDataProvider.ConcreteChildData) view.getTag()).setSelected(true);
                } else {
                    ((UnityDataProvider.ConcreteChildData) view.getTag()).setSelected(false);
                }
            }
        });
        // set background resource (target view ID: container)
        int bgResId;
        bgResId = R.drawable.bg_item_normal_state;
//        holder.mContainer.setBackgroundResource(bgResId);
//        holder.mContainer.setCardBackgroundColor(Color.LTGRAY);
    }

    @Override
    public boolean onCheckCanExpandOrCollapseGroup(MyGroupViewHolder holder, int groupPosition, int x, int y, boolean expand) {
        if (groupPosition == 0)
            return true;

        // check is enabled
        if (!(holder.itemView.isEnabled() && holder.itemView.isClickable())) {
            return false;
        }

        return true;
    }


    public static abstract class MyBaseViewHolder extends AbstractExpandableItemViewHolder {
        public CardView mContainer;
        public TextView mTextView;

        public MyBaseViewHolder(View v) {
            super(v);
            mContainer = (CardView) v.findViewById(R.id.container);
            mTextView = (TextView) v.findViewById(android.R.id.text1);
        }

        public MyBaseViewHolder(View v, int viewType) {
            super(v);
        }
    }

    public static class MyGroupViewHolder extends MyBaseViewHolder {
        public ExpandableItemIndicator mIndicator;

        public MyGroupViewHolder(View v) {
            super(v);
            mIndicator = (ExpandableItemIndicator) v.findViewById(R.id.indicator);
        }

        public MyGroupViewHolder(View v, int viewType) {
            super(v, viewType);
        }

    }

    public static class MyChildViewHolder extends MyBaseViewHolder {
        public TextView firstTextView;
        public TextView secondTextView;
        public CheckBox checkBox;

        public MyChildViewHolder(View v) {
            super(v);
            firstTextView = (TextView) v.findViewById(android.R.id.text1);
            secondTextView = (TextView) v.findViewById(android.R.id.text2);
            checkBox = (CheckBox) v.findViewById(R.id.switches_cb1);
        }
    }
}
