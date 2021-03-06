package com.example.brom.activitiesapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.brom.activitiesapp.R.id.list_view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

    public class MainActivity extends AppCompatActivity {

        protected ArrayList<Mountains> mountainlist = new ArrayList<>();
        ListView myListView;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            Brorsan getJson = new Brorsan();
            getJson.execute();

            ListView myListView = (ListView) findViewById(list_view);

            myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    //Toast nedan:
                    Toast.makeText(getApplicationContext(), mountainlist.get(position).utmatare(), Toast.LENGTH_SHORT).show();

                }
            });


            myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {


                    Intent intent = new Intent(getApplicationContext(), hio.class);
                    Bundle extras = new Bundle();

                    String name = mountainlist.get(position).getName();
                    String location = mountainlist.get(position).getLocation();
                    String height = Integer.toString(mountainlist.get(position).getHeight());
                    String url = mountainlist.get(position).getImage();

                    extras.putString("MOUNTAIN_NAME", name);
                    extras.putString("MOUNTAIN_LOCATION", location);
                    extras.putString("MOUNTAIN_HEIGHT", height);
                    extras.putString("MOUNTAIN_IMAGE", url);

                    intent.putExtras(extras);
                    getApplicationContext().startActivity(intent);

                }
            });

        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu){
            getMenuInflater().inflate(R.menu.menu_main, menu);
            return super.onCreateOptionsMenu(menu);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();

            if (id == R.id.action_refresh) {
                mountainlist.clear();
                new Brorsan().execute();
                Toast refreshed = Toast.makeText(this, "List have been refreshed", Toast.LENGTH_SHORT);
                refreshed.show();

                return true;
            }

            return super.onOptionsItemSelected(item);
        }

        private class Brorsan extends AsyncTask {

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                String s = new String(o.toString());
                Log.d("Jacob","DataFetched"+s);

                try {
                    JSONArray mountaindata = new JSONArray(s);

                    for(int i = 0; i < mountaindata.length(); i++){
                        JSONObject mountain = mountaindata.getJSONObject(i);

                        String name = mountain.getString("name");
                        String location = mountain.getString("location");
                        int height = mountain.getInt("size");

                        String auxdata = mountain.getString("auxdata");
                        JSONObject aux = new JSONObject(auxdata);
                        String url = aux.getString("img");
                        Mountains m = new Mountains(name, height, location, url);
                        mountainlist.add(m);

                    }
                }
                    catch (JSONException e) {
                    e.printStackTrace();
                    }


                    ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), R.layout.list_item_textview, R.id.my_item_textview, mountainlist);

                    myListView = (ListView)findViewById(R.id.list_view);
                    myListView.setAdapter(adapter);
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
                URL url = new URL("http://wwwlab.iit.his.se/brom/kurser/mobilprog/dbservice/admin/getdataasjson.php?type=brom");

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
