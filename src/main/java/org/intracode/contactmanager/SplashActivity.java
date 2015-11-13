package org.intracode.contactmanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SplashActivity extends Activity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 2000;

    public  Map<String, String> bldgnames = new HashMap<String, String>();
    public  Map<String, String> bldglocs = new HashMap<String, String>();
    public  Map<String, String> bldgbusyness = new HashMap<String, String>();
    public  Map<String, String> busynessTemp = new HashMap<String, String>();
    private ArrayList<String> buildingNames = new ArrayList<String>();
    private String API = "http://densit-api.appspot.com/locations";
    private GlobalVariables gb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);
        if (!isNetworkAvailable()) {
            new AlertDialog.Builder(this)
                    .setTitle("WARNING")
                    .setMessage("NEED INTERNET CONNECTION")
                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                            System.exit(0);
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        } else {
            new Handler().postDelayed(new Runnable() {

                /*
                 * Showing splash screen with a timer. This will be useful when you
                 * want to show case your app logo / company
                 */

                public void run() {
                    // This method will be executed once the timer is over
                    // Start your app main activity

//                    server = ServerUtility.getInstance();
                    Busyness busy = new Busyness();
                    busy.execute(API);

                    gb = (GlobalVariables) getApplication();




                    Intent i = new Intent(SplashActivity.this, MainActivity.class);

                    startActivity(i);
                    finish();
                }
            }, SPLASH_TIME_OUT);
        }

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    class Wrapper
    {
        public int numOfParams;
        public String result;
    }

    public class Busyness extends AsyncTask<String, String, Wrapper> {
        public String test = "123";
        @Override
        protected Wrapper doInBackground(String... params) {
            String urlString = params[0]; // URL to call
            InputStream in = null;
            // HTTP Get
            int paramLength = params.length; //storing [0] url, [1] id [2,3]starttime/endtime
            if (paramLength == 3 || paramLength >=5) {
                Log.d("ERROR", "params length error");
            }
            if (paramLength >=2) {
                urlString = urlString + "/" + params[1];
                Log.d("URL", urlString);
            }
            if (paramLength ==4) {
                urlString = urlString + "/busyness/" + params[2] + "/" + params[3];
                Log.d("URL", urlString);
            }
            try {
                URL url = new URL(urlString);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                in = new BufferedInputStream(urlConnection.getInputStream());
            } catch (Exception e ) {
                Log.d("ERROR", e.getMessage());
            }
            String result = convertStreamToString(in);

            Log.d("API", result);
            Wrapper w = new Wrapper();
            w.numOfParams = paramLength;
            w.result = result;
            return w;
        }

        private void getLocations(String response) {
            //parse json
            try {
                JSONObject js = new JSONObject(response);
                JSONArray bldgs = js.getJSONArray("locations");
                for(int i = 0 ; i < bldgs.length() ; i++){
                    String value = "0";
                    String name = bldgs.getJSONObject(i).getString("name");
                    String id = bldgs.getJSONObject(i).getString("id");
                    JSONObject geo = bldgs.getJSONObject(i).getJSONObject("geocode");
                    if (bldgs.getJSONObject(i).has("busyness")) {
                        JSONObject keys = bldgs.getJSONObject(i).getJSONObject("busyness");
                        value = keys.toString();


                        bldgnames.put(id, name);

                        String[]tokens = value.split(":");
                        String percent = tokens[tokens.length-1];
                        Log.d("JSON", "key =" + id + "busyness" + percent);
                        bldgbusyness.put(id, percent.substring(0, percent.length() - 1));
                    } else {
                        bldgnames.put(id, name);
                        bldgbusyness.put(id, "0");
                    }
                    bldglocs.put(id, geo.toString());
                    Log.d("JSON", "key =" + id + "name" + name);
                    Log.d("JSON", "key =" + id + "geo" + geo.toString());

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private void getBusynessNow(String response) {
            //parse json
            try {
                JSONObject js = new JSONObject(response);
                JSONObject keys = js.getJSONObject("busyness");
                String id = js.getString("id");
                String value = keys.toString();
                String[]tokens = value.split(":");
                String percent = tokens[tokens.length-1];
                bldgbusyness.put(id, percent.substring(0, percent.length() - 1));
                Log.d("JSON", "key =" + id + "busyness" + percent);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private void getBusynessAtTime(String response) {
            //parse json
            try {
                JSONObject js = new JSONObject(response);

                for(int i = 0; i<js.names().length(); i++){
                    String ts = js.names().getString(i);
                    String bs = js.get(ts).toString();
                    busynessTemp.put(ts, bs);
                    Log.d("JSON", ts+", "+bs);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private String convertStreamToString(InputStream is) {

            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();

            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return sb.toString();
        }

        protected void onPostExecute(Wrapper w) {
//            Intent intent = new Intent(getApplicationContext(), BuildingListFragment.class);
//
//            intent.putExtra(EXTRA_MESSAGE, result);
//
//            startActivity(intent);
//            testPrint.setText(result);

//            if (w.numOfParams == 1)   {
                getLocations(w.result);
                buildingNames.clear();
                for (Map.Entry<String, String> entry : bldgbusyness.entrySet())
                {
//                Log.d("MAPPING", bldgnames.get(entry.getKey()));
                    buildingNames.add(entry.getKey());
                }
//            }
//            getBusynessNow(w.result);
            getBusynessAtTime(w.result);

            Log.d("JSON", "Buildings " + bldgnames);
            Log.d("JSON", "Buildbusyness " + bldgbusyness);
            Log.d("JSON", "Buildbldglocs " + bldglocs);
            Log.d("JSON", "Builddmanes " + buildingNames);
            Log.d("JSON", "BuildTemp " + busynessTemp);
            gb.addBldglocs(bldglocs);
            gb.addBusynessTemp(busynessTemp);
            gb.addBldgames(bldgnames);
            gb.addBuildingNames(buildingNames);
            gb.addBldgames(bldgnames);
//            if (w.numOfParams == 2)
//            if (w.numOfParams == 4)
//            buildingNames.clear();
//            for (Map.Entry<String, String> entry : bldgbusyness.entrySet())
//            {
////                Log.d("MAPPING", bldgnames.get(entry.getKey()));
//                buildingNames.add(entry.getKey());
//            }

        }
    }
}
