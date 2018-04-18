
package com.example.brom.activitiesapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;


import java.io.InputStream;
/**
 * Created by jacobsvensson on 2018-04-18.
 */

public class hio extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mountain_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        String message1 = extras.getString("MOUNTAIN_NAME");
        String message2 = extras.getString("MOUNTAIN_LOCATION");
        String message3 = extras.getString("MOUNTAIN_HEIGHT");
        String message4 = extras.getString("MOUNTAIN_IMAGE");

        TextView locations = (TextView) findViewById(R.id.textView2);
        TextView names = (TextView) findViewById(R.id.textView3);
        TextView heights = (TextView) findViewById(R.id.textView4);

        names.setText(message1);
        locations.setText(message2);
        heights.setText(message3);

        class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
            ImageView mountainImage;

            public DownloadImageTask(ImageView mountainImage) {
                this.mountainImage = mountainImage;
            }

            protected Bitmap doInBackground(String... urls) {
                String urldisplay = urls[0];
                Bitmap mountain = null;
                try {
                    InputStream in = new java.net.URL(urldisplay).openStream();
                    mountain = BitmapFactory.decodeStream(in);
                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
                }
                return mountain;
            }

            protected void onPostExecute(Bitmap result) {
                mountainImage.setImageBitmap(result);
            }
        }

        new DownloadImageTask((ImageView) findViewById(R.id.mountainImage)).execute(message4);

    }
}
