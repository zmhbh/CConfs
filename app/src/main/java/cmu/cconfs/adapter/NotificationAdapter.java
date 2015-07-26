package cmu.cconfs.adapter;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cmu.cconfs.R;
import cmu.cconfs.model.parseModel.Message;

/**
 * Created by zmhbh on 8/27/15.
 */
public class NotificationAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<Message> messages;

    public NotificationAdapter(Context context, List<Message> messages) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.messages = messages;
    }

    public void updateDataSet(List<Message> messages) {
        this.messages = messages;
    }

    @Override
    public int getCount() {
        if (messages == null)
            return 0;

        return messages.size();
    }

    @Override
    public Object getItem(int position) {
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.list_notification, null);
        }

        TextView textTitle = (TextView) view.findViewById(R.id.title);
        TextView textMessage = (TextView) view.findViewById(R.id.message);
        TextView textTimestamp = (TextView) view.findViewById(R.id.timestamp);

        Message message = messages.get(position);
        textTitle.setText(message.getTitle());
        textMessage.setText(message.getMessage());

        CharSequence ago = DateUtils.getRelativeTimeSpanString(message.getCreatedAt().getTime(), System.currentTimeMillis(), 0L, DateUtils.FORMAT_ABBREV_ALL);
        textTimestamp.setText(String.valueOf(ago));
        return view;
    }

}
