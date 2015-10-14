package cmu.cconfs;

import android.content.ContentValues;
import android.content.Context;
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
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;

import cmu.cconfs.model.parseModel.Photo;

/**
 * Created by mangobin on 15/10/14.
 */
public class ImageDetailsActivity extends AppCompatActivity {

    private String sessionKey;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_details);
        ImageView imageView = (ImageView) findViewById(R.id.grid_item_image);
        Button shareButton = (Button)findViewById(R.id.sharebutton);
        Intent incomeIntent = getIntent();
        sessionKey = incomeIntent.getStringExtra("sessionKey");

        if (incomeIntent.hasExtra("byteArray")) {
            byte[] bytes = incomeIntent.getByteArrayExtra("byteArray");
            bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            shareButton.setVisibility(View.INVISIBLE);
        } else {
            String imagePath = incomeIntent.getStringExtra("image");
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            bitmap = BitmapFactory.decodeFile(imagePath, options);
        }

        imageView.setImageBitmap(bitmap);
        imageView.setVisibility(View.VISIBLE);

    }

    public void didTapShareButton(View view) {
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
        photo.saveInBackground();
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
