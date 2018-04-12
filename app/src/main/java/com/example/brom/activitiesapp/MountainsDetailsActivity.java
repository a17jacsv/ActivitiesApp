package com.example.brom.activitiesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MountainsDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mountain_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String message1 = intent.getStringExtra("MOUNTAIN_NAMES");
        String message2 = intent.getStringExtra("MOUNTAIN_LOCATIONS");
        String message3 = intent.getStringExtra("MOUNTAIN_HEIGHTS");

        //Toast.makeText(getApplicationContext(),message, Toast.LENGTH_SHORT).show();


        TextView locations = (TextView)findViewById(R.id.textView2);
        TextView names = (TextView)findViewById(R.id.textView3);
        TextView heights = (TextView)findViewById(R.id.textView4);

        names.setText(message1);
        locations.setText(message2);
        heights.setText(message3);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
