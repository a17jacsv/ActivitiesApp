package com.example.brom.activitiesapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import static com.example.brom.activitiesapp.R.id.list_view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

    public class MainActivity extends AppCompatActivity {
        private String[] mountainNames = {"Matterhorn", "Mont Blanc", "Denali"};
        private String[] mountainLocations = {"Alps", "Alps", "Alaska"};
        private int[] mountainHeights = {4478, 4808, 6190};
        // Create ArrayLists from the raw data above and use these lists when populating your ListView.

        private Mountains m = new Mountains("K2", 5000, "Himalaya", "https://google.se");

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            final List<String> listData = new ArrayList<String>(Arrays.asList(mountainNames));

            final ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), R.layout.list_item_textview, R.id.my_item_textview, listData);

            ListView myListView = (ListView) findViewById(list_view);
            myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    //Toast nedan:
                    Toast.makeText(getApplicationContext(),mountainNames[position] + " is part of the " + mountainLocations[position] +  " mountains range and is " +  Integer.toString(mountainHeights[position]) + "m high.", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getApplicationContext(), MountainsDetailsActivity.class);

                    intent.putExtra("MOUNTAIN_NAMES", mountainNames[position]);
                    intent.putExtra("MOUNTAIN_LOCATIONS", mountainLocations[position]);
                    intent.putExtra("MOUNTAIN_HEIGHTS", Integer.toString(mountainHeights[position]));

                    startActivity(intent);
                }
            });

            myListView.setAdapter(adapter);

            new Brorsan().execute();
        }

        private class Brorsan extends AsyncTask {

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                String s = new String(o.toString());
                Log.d("Jacob","DataFetched"+s);
            }

            @Override
            protected Object doInBackground(Object[] params) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String jsonStr = null;

            try {
                // Construct the URL for the php-service
                URL url = new URL("https://wwwlab.iit.his.se/brom/kurser/mobilprog/dbservice/admin/getdataasjson.php?type=brom");

                // Create the request to the PHP-service, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                jsonStr = buffer.toString();
                return jsonStr;

            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;

            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("Network error", "Error closing stream", e);
                    }
                }
            }
        }
    }
}

// 1. Create a ListView as in previous assignment
// 2. Create a new activity named "MountainDetailsActivity
// 3. Create a new Layout file for the MountainDetailsActivity called
//    "activity_mountaindetails". MountainDetailsActivity must have MainActivity as its
//    ´parent activity.
// 4. The layout file created in step 3 must have a LinearLayout
// 5. The layout file created in step 3 must be able to display
//    * Mountain Name
//    * Mountain Location
//    * Mountain Height
// 6. When tapping on a list item: create an Intent that includes
//    * Mountain Name
//    * Mountain Location
//    * Mountain Height
// 7. Display the MountainDetailsActivity with the data from the Intent created in step
//    6
// 8. From the MountainDetailsActivity you should have an option to "go back" using an
//    left arro button. This is done by letting the MainActivity be the parent activity to
//    MountainDetailsActivity.