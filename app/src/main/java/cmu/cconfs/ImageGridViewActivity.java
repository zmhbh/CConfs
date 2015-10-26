package cmu.cconfs;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.parse.ParseFile;
import com.parse.ParseQuery;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cmu.cconfs.adapter.ImageGridViewAdapter;
import cmu.cconfs.model.parseModel.Photo;

/**
 * Created by mangobin on 15/10/14.
 */
public class ImageGridViewActivity extends BaseActivity {

    private ImageGridViewAdapter mGridAdapter;
    private List<Bitmap> remotePhotosList;
    private List<String> remotePhotosPublisher;
    private ProgressBar mProgressBar;
    private String sessionKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_grid);

        GridView mGridView = (GridView) findViewById(R.id.gridView);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        Intent incomeIntent = getIntent();
        sessionKey = incomeIntent.getStringExtra("sessionKey");
        if (incomeIntent.hasExtra("isPublic")) {
            // show downloaded photos from Network
            remotePhotosList = new ArrayList<>();
            mProgressBar.setVisibility(View.VISIBLE);
            mGridAdapter = new ImageGridViewAdapter(this, R.layout.grid_image_item);
            new AsyncHttpTask().execute();
        } else {
            // show local photos
            String imagesPaths = incomeIntent.getStringExtra("imagesPaths");
            ArrayList<String> localPhotosList = new ArrayList<>();
            String[] str = imagesPaths.split(",");
            localPhotosList.addAll(Arrays.asList(str));
            mGridAdapter = new ImageGridViewAdapter(this, R.layout.grid_image_item, localPhotosList);

        }
        mGridView.setAdapter(mGridAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                //Pass the image sessionKey and url to DetailsActivity
                Intent intent = new Intent(ImageGridViewActivity.this, ImageDetailsActivity.class);
                intent.putExtra("sessionKey", sessionKey);

                //Get item at position
                Object item = parent.getItemAtPosition(position);
                if (item instanceof String) {
                    intent.putExtra("image", (String) item);
                } else if (item instanceof Bitmap) {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    ((Bitmap) item).compress(Bitmap.CompressFormat.PNG, 0, bos);
                    byte[] scaledData = bos.toByteArray();
                    intent.putExtra("byteArray", scaledData);
                    intent.putExtra("Publisher",remotePhotosPublisher.get(position));
                }

                //Start details activity
                startActivity(intent);
            }
        });

    }

    public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            Integer result = 0;
            try {
                ParseQuery<Photo> query = ParseQuery.getQuery(Photo.class);
                query.whereEqualTo("sessionKey", sessionKey);
                List<Photo> photos = query.find();
                remotePhotosPublisher = new ArrayList<>();
                for (Photo photo : photos) {
                    ParseFile photoFile = photo.getPhotoFile();
                    byte[] response = photoFile.getData();
                    Bitmap bitmap = BitmapFactory.decodeByteArray(response, 0, response.length);

                    remotePhotosList.add(bitmap);
                    remotePhotosPublisher.add(photo.getPublisher());
                }
                result = 1;
                return result;

            } catch (Exception e) {
                Log.e("photo", "Error: " + e.getMessage());
            }
            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            // Download complete. Let us update UI
            if (result == 1) {
                mGridAdapter.setGridData(remotePhotosList);
            } else {
                Toast.makeText(ImageGridViewActivity.this, "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }
            mProgressBar.setVisibility(View.GONE);
        }
    }


}
