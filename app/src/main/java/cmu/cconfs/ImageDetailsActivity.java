package cmu.cconfs;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;

import cmu.cconfs.model.parseModel.Photo;
import cmu.cconfs.utils.PreferencesManager;

/**
 * Created by mangobin on 15/10/14.
 */
public class ImageDetailsActivity extends AppCompatActivity {

    private String sessionKey;
    private Bitmap bitmap;
    private PreferencesManager mPreferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_details);
        mPreferencesManager = new PreferencesManager(this);
        ImageView imageView = (ImageView) findViewById(R.id.grid_item_image);
        TextView publisherTextView = (TextView) findViewById(R.id.photoUploader);
        Button shareButton = (Button) findViewById(R.id.sharebutton);
        Intent incomeIntent = getIntent();
        sessionKey = incomeIntent.getStringExtra("sessionKey");

        if (incomeIntent.hasExtra("byteArray")) {
            byte[] bytes = incomeIntent.getByteArrayExtra("byteArray");
            bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            shareButton.setVisibility(View.INVISIBLE);
            publisherTextView.setText(incomeIntent.getStringExtra("Publisher"));
            publisherTextView.setVisibility(View.VISIBLE);
        } else {
            String imagePath = incomeIntent.getStringExtra("image");
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            bitmap = BitmapFactory.decodeFile(imagePath, options);
            publisherTextView.setVisibility(View.INVISIBLE);
        }

        imageView.setImageBitmap(bitmap);
        imageView.setVisibility(View.VISIBLE);

    }

    public void didTapShareButton(View view) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(ImageDetailsActivity.this);
        alert.setMessage("Do you really want to share this photo with public?");
        alert.setTitle("Message");

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                boolean loggedIn = mPreferencesManager.getBooleanPreference("LoggedIn", false);

                if (!loggedIn) {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                } else {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
                    byte[] scaledData = bos.toByteArray();

                    ParseFile file = new ParseFile("photo.png", scaledData);
                    file.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e != null) {
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Upload Success!", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    Photo photo = new Photo();
                    photo.setSessionKey(sessionKey);
                    photo.setPhotoFile(file);
                    photo.setPublisher(ParseUser.getCurrentUser().getUsername());
                    photo.saveInBackground();
                }
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
            }
        });

        alert.show();

    }

    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            cursor.close();
            return Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }
}
