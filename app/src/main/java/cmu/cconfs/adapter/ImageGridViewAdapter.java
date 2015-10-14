package cmu.cconfs.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cmu.cconfs.R;

/**
 * Created by mangobin on 15/10/14.
 */
public class ImageGridViewAdapter extends BaseAdapter {


    private Context mContext;
    private int layoutResourceId;
    private List<String> localPhotosFilePathList;
    private List<Bitmap> remotePhotosBitmapList;

    public ImageGridViewAdapter(Context mContext, int layoutResourceId, List<String> localPhotosFilePathList) {
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.localPhotosFilePathList = localPhotosFilePathList;
    }

    public ImageGridViewAdapter(Context mContext, int layoutResourceId) {
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        if (localPhotosFilePathList == null) {
            if (remotePhotosBitmapList == null) {
                return 0;
            } else {
                return remotePhotosBitmapList.size();
            }
        } else {
            return localPhotosFilePathList.size();
        }
    }

    @Override
    public Object getItem(int position) {
        if (localPhotosFilePathList == null) {
            if (remotePhotosBitmapList == null) {
                return null;
            } else {
                return remotePhotosBitmapList.get(position);
            }
        } else {
            return localPhotosFilePathList.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * Updates grid data and refresh grid items.
     *
     * @param mGridData
     */
    public void setGridData(ArrayList<String> mGridData) {
        this.localPhotosFilePathList = mGridData;
        notifyDataSetChanged();
    }

    public void setGridData(List<Bitmap> mGridData) {
        this.remotePhotosBitmapList = mGridData;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.titleTextView = (TextView) row.findViewById(R.id.grid_item_title);
            holder.imageView = (ImageView) row.findViewById(R.id.grid_item_image);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        // downsizing image as it throws OutOfMemory Exception for larger images
        options.inSampleSize = 8;
        if (localPhotosFilePathList != null) {
            String imagePath = localPhotosFilePathList.get(position);
            bitmap = BitmapFactory.decodeFile(imagePath, options);
        } else if (remotePhotosBitmapList != null) {
            bitmap = remotePhotosBitmapList.get(position);
        }

        holder.imageView.setImageBitmap(bitmap);
        holder.imageView.setVisibility(View.VISIBLE);
        // holder.titleTextView.setText(Html.fromHtml(item.getTitle()));
        return row;
    }

    static class ViewHolder {
        TextView titleTextView;
        ImageView imageView;
    }


}
