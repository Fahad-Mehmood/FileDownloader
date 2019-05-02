package com.example.filedownloaderapp.helper;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.filedownloaderapp.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.graphics.Bitmap.CompressFormat.PNG;
import static com.example.filedownloaderapp.MainActivity.EXTRA_LIKES;
import static com.example.filedownloaderapp.MainActivity.EXTRA_URL;

public class DetailActivity extends AppCompatActivity {

    ImageView imageView, fabBtn, downloadBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //permission for storage
        ActivityCompat.requestPermissions(DetailActivity.this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        //use save intent data
        Intent intent = getIntent();
        final String imageURL = intent.getStringExtra(EXTRA_URL);
        String likes = intent.getStringExtra(EXTRA_LIKES);

        //initialize value
        imageView = findViewById(R.id.image_detail);
        downloadBtn = findViewById(R.id.download);
        fabBtn = findViewById(R.id.upButton);
        TextView textView = findViewById(R.id.text_like);

        //load image
        Picasso.with(this).load(imageURL).fit().centerInside().into(imageView);
        textView.setText("Likes:" + likes);

        //add click listner to fabBtn
        fabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DetailActivity.this, "Data Move Up", Toast.LENGTH_SHORT).show();
            }
        });


        //listner to downloadBtn
        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileOutputStream fileOutputStream = null;
                File file = getDisc();
                if (!file.exists() && !file.mkdirs()) {
                    Toast.makeText(DetailActivity.this, "Can't create directory", Toast.LENGTH_SHORT).show();
                    return;
                }
                //image name format
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyymmsshhmmss");
                String date = simpleDateFormat.format(new Date());
                String name = "imag" + date+ ".jpg";
                String fileName = file.getAbsolutePath()+"/"+name;
                File newFile = new File(fileName);
                try {
                    fileOutputStream = new FileOutputStream(newFile);
                    Bitmap bitmap = viewToBitmap(imageView,imageView.getWidth(),imageView.getHeight());
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);
                    fileOutputStream.flush();
                    fileOutputStream.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                refreshGallery(newFile);
                Toast.makeText(DetailActivity.this, "Image has been saved to gallery", Toast.LENGTH_SHORT).show();
            }
        });


    }

    //refresh gallery with new image
    public void refreshGallery(File file) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(file));
        sendBroadcast(intent);
    }


    public static Bitmap viewToBitmap(View view, int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    //create image folder
    private File getDisc() {
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        return new File(file, "Image Demo");
    }
}
