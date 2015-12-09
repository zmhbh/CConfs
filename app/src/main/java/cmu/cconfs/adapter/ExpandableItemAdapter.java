package cmu.cconfs.adapter;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemViewHolder;
import com.rey.material.widget.CheckBox;

import java.util.Calendar;
import java.util.HashMap;
import java.util.TimeZone;

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
    HashMap<Integer, String> map = new HashMap<Integer, String>();

    public ExpandableItemAdapter(UnityDataProvider unityDataProvider) {
        this.unityDataProvider = unityDataProvider;
        // ExpandableItemAdapter requires stable ID, and also
        // have to implement the getGroupItemId()/getChildItemId() methods appropriately.
        setHasStableIds(true);
        map.put(252, "6-27");
        map.put(201, "6-28");
        map.put(151, "6-29");
        map.put(103, "6-30");
        map.put(63, "7-1");
        map.put(0, "7-2");
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
                    if(childItem.getSessionId() >= 252){
                        scheduleNotification(v.getContext(),
                                getNotification("Next session starts in 10 minutes at " +
                                        childItem.getSessionRoom(), v.getContext()),
                                map.get(252) + "-" + unityDataProvider.getGroupItem(groupPosition - 1).getText());
                    }
                    else if(childItem.getSessionId() >= 201){
                        scheduleNotification(v.getContext(),
                                getNotification("Next session starts in 10 minutes at " +
                                        childItem.getSessionRoom(), v.getContext()),
                                map.get(201) + "-" + unityDataProvider.getGroupItem(groupPosition - 1).getText());
                    }else if(childItem.getSessionId() >= 151){
                        scheduleNotification(v.getContext(),
                                getNotification("Next session starts in 10 minutes at " +
                                        childItem.getSessionRoom(), v.getContext()),
                                map.get(151) + "-" + unityDataProvider.getGroupItem(groupPosition - 1).getText());
                    }else if(childItem.getSessionId() >= 103){
                        scheduleNotification(v.getContext(),
                                getNotification("Next session starts in 10 minutes at " +
                                        childItem.getSessionRoom(), v.getContext()),
                                map.get(103) + "-" + unityDataProvider.getGroupItem(groupPosition - 1).getText());
                    }else if(childItem.getSessionId() >= 63){
                        scheduleNotification(v.getContext(),
                                getNotification("Next session starts in 10 minutes at " +
                                        childItem.getSessionRoom(), v.getContext()),
                                map.get(63) + "-" + unityDataProvider.getGroupItem(groupPosition - 1).getText());
                    }else{
                        scheduleNotification(v.getContext(),
                                getNotification("Next session starts in 10 minutes at " +
                                        childItem.getSessionRoom(), v.getContext()),
                                map.get(0) + "-" + unityDataProvider.getGroupItem(groupPosition - 1).getText());
                    }
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
    private void scheduleNotification(Context context, Notification notification, String time) {
        Intent notificationIntent = new Intent(context, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, notificationIntent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getDefault());
        calendar.set(Calendar.MONTH, Integer.parseInt(time.split("-")[0]) - 1);
        calendar.set(Calendar.YEAR, 2015);
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(time.split("-")[1]));
        Log.e("Month", Integer.parseInt(time.split("-")[0]) - 1 + "");
        Log.e("Year", 2015 + "");
        Log.e("Date", Integer.parseInt(time.split("-")[1]) + "");


        if(Integer.parseInt(time.split("-")[2].split(":")[1]) < 10) {
            Log.e("HOUR_OF_DAY", Integer.parseInt(time.split("-")[2].split(":")[0]) - 1 + "");
            Log.e("MINUTE", 50 + "");

            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time.split("-")[2].split(":")[0]) - 1);
            calendar.set(Calendar.MINUTE, 50);
            if(Integer.parseInt(time.split("-")[2].split(":")[0]) - 1 >= 12)
                calendar.set(Calendar.AM_PM,Calendar.PM);
            else
                calendar.set(Calendar.AM_PM,Calendar.AM);
        }
        else {
            Log.e("HOUR_OF_DAY",Integer.parseInt(time.split("-")[2].split(":")[0]) + "");
            Log.e("MINUTE", Integer.parseInt(time.split("-")[2].split(":")[1]) - 10 + "");

            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time.split("-")[2].split(":")[0]));
            calendar.set(Calendar.MINUTE, Integer.parseInt(time.split("-")[2].split(":")[1]) - 10);
            if(Integer.parseInt(time.split("-")[2].split(":")[0]) >= 12)
                calendar.set(Calendar.AM_PM,Calendar.PM);
            else
                calendar.set(Calendar.AM_PM,Calendar.AM);
        }
        calendar.set(Calendar.SECOND, 0);

        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        alarmManager.set(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                pendingIntent);
    }
    public static long getStampFromDate(int year, int month, int day, int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, minute);
        return calendar.getTimeInMillis();
    }
    private Notification getNotification(String content, Context context) {
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle("Session Reminder");
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.ic_launcher);
        return builder.build();
    }
}
