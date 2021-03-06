package cmu.cconfs.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.askerov.dynamicgrid.BaseDynamicGridAdapter;

import java.util.List;

import cmu.cconfs.R;

/**
 * Created by zmhbh on 7/26/15.
 */
public class HomeGridDynamicAdapter extends BaseDynamicGridAdapter {

    public HomeGridDynamicAdapter(Context context, List<?> items, int columnCount) {
        super(context, items, columnCount);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GridViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.home_grid_item, null);
            holder = new GridViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (GridViewHolder) convertView.getTag();
        }
        int imageResource = -1;
        switch (position) {
            case 0:
                imageResource = R.drawable.ic_event_available_black_48dp;
                break;
            case 1:
                imageResource = R.drawable.ic_alarm_on_black_48dp;
                break;
            case 2:
                imageResource = R.drawable.room;
                break;
            case 3:
                imageResource = R.drawable.ic_directions_black_48dp;
                break;
            case 4:
                imageResource = R.drawable.ic_event_seat_black_48dp;
                break;
            case 5:
                imageResource = R.drawable.ic_stars_black_48dp;
                break;
            case 6:
                imageResource = R.drawable.ic_notifications_active_black_48dp;
                break;
            case 7:
                imageResource = R.drawable.ic_info_outline_black_48dp;
                break;
            case 8:
                imageResource = R.drawable.ic_settings_black_48dp;
                break;
            case 9:
                imageResource = R.drawable.ic_supervisor_account_black_48dp;
                break;

        }
        holder.build(getItem(position).toString(), imageResource);
        return convertView;
    }

    @Override
    public int getCount() {
        return 10;
    }


    private class GridViewHolder {
        private TextView titleText;
        private ImageView image;

        private GridViewHolder(View view) {
            titleText = (TextView) view.findViewById(R.id.item_title);
            image = (ImageView) view.findViewById(R.id.item_img);
        }

        void build(String title, int imageResource) {
            titleText.setText(title);
            image.setImageResource(imageResource);
        }


    }
}
