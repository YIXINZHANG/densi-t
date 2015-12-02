package org.intracode.contactmanager;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.UUID;

/**
 * Created by pbz18_000 on 12/1/2015.
 */
public class BusynessActivity extends Activity {

    public  Map<String, String> bldgnames = new HashMap<String, String>();
    public  Map<String, String> bldglocs = new HashMap<String, String>();
    public  Map<String, String> bldgbusyness = new HashMap<String, String>();
    public  Map<String, String> busynessTemp = new HashMap<String, String>();
    private ArrayList<String> hours = new ArrayList<String>();
    private ArrayList<String> buildingNames = new ArrayList<String>();
    private RoundCornerProgressBar progress1;
    private Bundle data;
    private String name;
    private Calendar c;
    private int dayOfWeek;
    private int currDate = 0;
    private int hour;
    private int minute;
    private String id;
    private double lat, lon;
    private MapView mapView;
    private GoogleMap map;
    private CameraUpdate cameraUpdate;
    private String[] theTimes;
    private String API = "http://densit-api.appspot.com/locations";
    private Spinner dateSpinner;
    private boolean first = true;

    private ArrayAdapter<String> adapter;
    private Map<Integer, String> timeStamps = new HashMap<Integer, String>();
    private String[] hourPoints = {"00:00", "01:00", "02:00", "03:00", "04:00", "05:00", "06:00", "07:00", "08:00", "09:00", "10:00",
            "11:00", "12:00", "13:00",  "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busyness);
        data = getIntent().getExtras();
        id = data.getString("ID");
        name = data.getString("NAME");
        lat = data.getDouble("LAT");
        lon = data.getDouble("LON");

        System.out.println("Activity " + id + " " + name + " " + lat + " "  + lon);
        Busyness busy = new Busyness();
        String[] params = new String[2];
        params[0] = API;
        params[1] = id;
        busy.execute(params);

        c = Calendar.getInstance();
        dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        currDate = dayOfWeek;
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);

        TextView textV = (TextView) findViewById(R.id.bName);
        textV.setText(name);

        dateSpinner = (Spinner) findViewById(R.id.date_spinner4);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapters2 = ArrayAdapter.createFromResource(this, R.array.day_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapters2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        dateSpinner.setAdapter(adapters2);
        dateSpinner.setSelection(dayOfWeek - 1);
        dateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                currDate = position;
                hours.clear();
                if (currDate == 0) {
                    for (int i = 0; i < 24 * 60; i = i + 60) {
                        hours.add(timeStamps.get(i));
                    }

                } else if (currDate == 1) {
                    for (int i = 24 * 60; i < 48 * 60; i = i + 60) {
                        hours.add(timeStamps.get(i));
                    }

                } else if (currDate == 2) {
                    for (int i = 48 * 60; i < 72 * 60; i = i + 60) {
                        hours.add(timeStamps.get(i));
                    }

                } else if (currDate == 3) {
                    for (int i = 72 * 60; i < 96 * 60; i = i + 60) {
                        hours.add(timeStamps.get(i));
                    }

                } else if (currDate == 4) {
                    for (int i = 96 * 60; i < 120 * 60; i = i + 60) {
                        hours.add(timeStamps.get(i));
                    }

                } else if (currDate == 5) {
                    for (int i = 120 * 60; i < 144 * 60; i = i + 60) {
                        hours.add(timeStamps.get(i));
                    }

                } else if (currDate == 6) {
                    for (int i = 144 * 60; i < 168 * 60; i = i + 60) {
                        hours.add(timeStamps.get(i));
                    }
                }

                display();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });



    }

    public void display() {
        adapter = new LocationListAdapter(this, R.layout.listview_hours, hours);
        ListView tListView = (ListView) findViewById(R.id.listviewHours);
        tListView.setAdapter(adapter);
        tListView.setTextFilterEnabled(true);
    }
    private class LocationListAdapter extends ArrayAdapter<String> {
        public LocationListAdapter(Context c, int r, List<String> o) {
            super(c, r, o);

        }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            view = getLayoutInflater().inflate(R.layout.listview_hours, parent, false);
            TextView tx = (TextView) view.findViewById(R.id.hours2);
            System.out.println("Printing" + hourPoints[position]);
            tx.setText(hourPoints[position]);
            try {
                double dHours = Double.parseDouble(hours.get(position));
                progress1 = (RoundCornerProgressBar) view.findViewById(R.id.progress2);
                if (dHours < 35) {
                    progress1.setProgressColor(Color.parseColor("#86BA4B"));
                } else if (dHours < 70) {
                    progress1.setProgressColor(Color.parseColor("#F29317"));
                } else {
                    progress1.setProgressColor(Color.parseColor("#E64436"));
                }
                progress1.setProgressBackgroundColor(Color.parseColor("#fff3f3f3"));
                progress1.setMax(100);
                progress1.setProgress((float) dHours);
            } catch (Exception e ){
                System.out.println("pass");
            }

            return view;
        }

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

//            Log.d("API", result);
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
                    JSONObject keys = bldgs.getJSONObject(i).getJSONObject("busyness");
                    String name = bldgs.getJSONObject(i).getString("name");
                    String id = bldgs.getJSONObject(i).getString("id");
                    JSONObject geo = bldgs.getJSONObject(i).getJSONObject("geocode");
                    bldgnames.put(id, name);
                    String value = keys.toString();
                    String[]tokens = value.split(":");
                    String percent = tokens[tokens.length-1];
                    bldgbusyness.put(id, percent.substring(0, percent.length() - 1));
                    bldglocs.put(id, geo.toString());
                    Log.d("JSON", "key =" + id + "name" + name);
                    Log.d("JSON", "key =" + id + "geo" + geo.toString());
                    Log.d("JSON", "key =" + id + "busyness" + percent);
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
                busynessTemp.clear();
//                hours.clear();
                String times;
                for(int i = 0; i<js.names().length(); i++){
//                    busynessTemp.clear();
//                    hours.clear();
                    String ts = js.names().getString(i);
                    String bs = js.get(ts).toString();
                    if (ts.equals("busyness")) {
                        times = js.get(ts).toString();
                        times = times.replace("{", "");
                        times = times.replace("}", "");
                        theTimes = times.split(",");
                        for (String s:theTimes) {
//                            System.out.println("theTime " + s);
                        }

                    }
//                    System.out.println(js.names().getString(i) + " and " +js.get(ts).toString() );
//                    if (((Long.valueOf(ts) % 86400) % 7200) == 0 ) {
//                        String hour = Long.toString(Long.valueOf(ts) % 86400 / 3600);
//                        busynessTemp.put(hour + ":00", bs);
//                        hours.add(hour + ":00");
//                        Log.d("JSON", hour+":00");
//                        Log.d("JSON", ts+", "+bs);
//                    }
//                    busynessTemp.put(ts, bs);
//                    Log.d("JSON", ts+", "+bs);
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

            if (w.numOfParams == 1)   {
                getLocations(w.result);
                buildingNames.clear();
                for (Map.Entry<String, String> entry : bldgbusyness.entrySet())
                {
//                Log.d("MAPPING", bldgnames.get(entry.getKey()));
                    buildingNames.add(entry.getKey());
                }
            }
            if (w.numOfParams == 2) getBusynessAtTime(w.result);
            if (w.numOfParams == 4)   getBusynessAtTime(w.result);
            timeStamps.clear();
            hours.clear();
            for (String s:theTimes) {
                String[] timeZ = s.split(":");
                String t = timeZ[0];
                t = t.substring(1, t.length() -1);
                timeStamps.put(Integer.parseInt(t), timeZ[1]);
            }

            if (currDate == 0) {
                for (int i=0; i < 24*60; i = i + 60) {
                    hours.add(timeStamps.get(i));
                }

            } else if (currDate == 1) {
                for (int i=24*60; i < 48*60; i = i + 60) {
                    hours.add(timeStamps.get(i));
                }

            } else if (currDate == 2) {
                for (int i=48*60; i < 72*60; i = i + 60) {
                    hours.add(timeStamps.get(i));
                }

            } else if (currDate == 3) {
                for (int i=72*60; i < 96*60; i = i + 60) {
                    hours.add(timeStamps.get(i));
                }

            } else if (currDate == 4) {
                for (int i=96*60; i < 120*60; i = i + 60) {
                    hours.add(timeStamps.get(i));
                }

            } else if (currDate == 5) {
                for (int i=120*60; i < 144*60; i = i + 60) {
                    hours.add(timeStamps.get(i));
                }

            } else if (currDate == 6) {
                for (int i=144*60; i < 168*60; i = i + 60) {
                    hours.add(timeStamps.get(i));
                }
            }
            display();
//            for (String d:hours) {
//                System.out.println("timeStamps " + d);
//            }
//            buildingNames.clear();
//            for (Map.Entry<String, String> entry : bldgbusyness.entrySet())
//            {
////                Log.d("MAPPING", bldgnames.get(entry.getKey()));
//                buildingNames.add(entry.getKey());
//            }

        }
    }
}