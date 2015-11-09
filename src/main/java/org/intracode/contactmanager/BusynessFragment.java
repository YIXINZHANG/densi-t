package org.intracode.contactmanager;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by pbz18_000 on 10/28/2015.
 */
public class BusynessFragment extends Fragment {
    private ArrayList<String> hours = new ArrayList<String>();
    private ArrayList<String> buildingNames = new ArrayList<String>();
    private TextView busyness;
    private TextView hour;
    private Random rand;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private GlobalVariables gv;
    private int position = 0;
    private Spinner spinner;
    private RoundCornerProgressBar progress1;
    private ArrayAdapter<CharSequence> floorAdapters;
    private Spinner floorSpinner;

    private Calendar c;
    private Map<String, String> timestamps = new HashMap<String, String>();
    private String[] hourPoints = {"00:00", "02:00", "04:00", "06:00", "08:00", "10:00", "12:00", "14:00", "16:00", "18:00", "20:00", "22:00", "24:00"};
    private String API = "http://densit-api.appspot.com/locations";
    public  Map<String, String> bldgnames = new HashMap<String, String>();
    public  Map<String, String> bldglocs = new HashMap<String, String>();
    public  Map<String, String> bldgbusyness = new HashMap<String, String>();
    public  Map<String, String> busynessTemp = new HashMap<String, String>();
//    private int dayOfWeek;
//    private int hour;
//    private int minute;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Busyness busy = new Busyness();
        busy.execute(API);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Busyness b = new Busyness();
        b.execute(API);
//        b.cancel();
//        for (int i = 0; i<hourPoints.length-1; i++) {
//            hours.add(hourPoints[i]+" - "+hourPoints[i+1]);
//        }

        buildingNames.clear();

        c = Calendar.getInstance();
        spinner = (Spinner) getActivity().findViewById(R.id.building_spinner);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapters = ArrayAdapter.createFromResource(getActivity(), R.array.buildings_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapters.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapters);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub


                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });
        spinner.setSelection(position);

        floorSpinner = (Spinner) getActivity().findViewById(R.id.floor_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        floorAdapters = ArrayAdapter.createFromResource(getActivity(), R.array.culc_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        floorAdapters.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        floorSpinner.setAdapter(floorAdapters);
        floorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });
//        dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
//        hour = c.get(Calendar.HOUR_OF_DAY);
//        minute = c.get(Calendar.MINUTE);
        displayListView();

    }

    @Override
    public void onResume() {
        super.onResume();
//        spinner.setSelection(position);
//        adapter.notifyDataSetChanged();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        View view = inflater.inflate(R.layout.fragment_busyness, container, false);
        return view;
    }

    public void reload(int p, String id, String name) {
//        spinner.setSelection(position);
//        adapter.notifyDataSetChanged();
        this.position = p;
        spinner.setSelection(position);
        // make api call to get today's all timestamps
        Calendar date0 = new GregorianCalendar();
        ////// reset hour, minutes, seconds and millis
        date0.set(Calendar.HOUR_OF_DAY, 0);
        date0.set(Calendar.MINUTE, 0);
        date0.set(Calendar.SECOND, 0);
        date0.set(Calendar.MILLISECOND, 0);
        long unixTime0 = date0.getTimeInMillis() / 1000;
        date0.add(Calendar.DAY_OF_MONTH, 1);
        long unixTime1 = date0.getTimeInMillis() / 1000;
        String[] params = {API, id, Long.toString(unixTime0), Long.toString(unixTime1)};
        Busyness bs = new Busyness();
        bs.execute(params);
//        Log.d("LOC", Integer.toString(p));
        /////////////////////////////////
        floorSpinner.setAdapter(null);
        adapter.notifyDataSetChanged();
        if (position == 0) {
            floorAdapters = ArrayAdapter.createFromResource(getActivity(), R.array.howey_array, android.R.layout.simple_spinner_item);
        } else if (position == 1) {
            floorAdapters = ArrayAdapter.createFromResource(getActivity(), R.array.library_array, android.R.layout.simple_spinner_item);
        } else if (position == 2) {
            floorAdapters = ArrayAdapter.createFromResource(getActivity(), R.array.sc_array, android.R.layout.simple_spinner_item);
        } else if (position == 3) {
            floorAdapters = ArrayAdapter.createFromResource(getActivity(), R.array.crc_array, android.R.layout.simple_spinner_item);
        } else if (position == 4) {
            floorAdapters = ArrayAdapter.createFromResource(getActivity(), R.array.klaus_array, android.R.layout.simple_spinner_item);
        } else if (position == 5) {
            floorAdapters = ArrayAdapter.createFromResource(getActivity(), R.array.coc_array, android.R.layout.simple_spinner_item);
        }
        floorAdapters = ArrayAdapter.createFromResource(getActivity(), R.array.culc_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        floorAdapters.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        floorSpinner.setAdapter(floorAdapters);
        floorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });
        adapter.notifyDataSetChanged();
//        this.onResume();
    }

    private void displayListView() {
        //create an ArrayAdaptar from the String Array
        adapter = new HourListAdapter(getActivity(), R.layout.listview_hours, hours);
//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.listview_item, urlList);
        listView = (ListView) getView().findViewById(R.id.listview2);
        listView.setAdapter(adapter);
        listView.setTextFilterEnabled(true);
    }

    private class HourListAdapter extends ArrayAdapter<String> {
        public HourListAdapter(Context c, int r, List<String> o) {
            super(c, r, o);
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            if (view == null)
                view = getActivity().getLayoutInflater().inflate(R.layout.listview_hours, parent, false);

            String buildingName = hours.get(position);
            Log.d("NAME", buildingName + " " + busynessTemp.get(buildingName));
            hour = (TextView) view.findViewById(R.id.hours);
            hour.setText(hourPoints[position]);
            busyness = (TextView) view.findViewById(R.id.busynessHours);
//            rand = new Random();
            String percent = busynessTemp.get(hours.get(position));
            busyness.setText(percent + "%");
//            Log.d("JSON", percent);
            progress1 = (RoundCornerProgressBar) view.findViewById(R.id.progress2);
            progress1.setProgressColor(Color.parseColor("#6960ec"));
            progress1.setBackgroundColor(Color.parseColor("#e5e4e2"));
            progress1.setMax(100);
            progress1.setProgress(Integer.valueOf(percent));

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
                hours.clear();
                for(int i = 0; i<js.names().length(); i++){
//                    busynessTemp.clear();
//                    hours.clear();
                    String ts = js.names().getString(i);
                    String bs = js.get(ts).toString();
                    if (((Long.valueOf(ts) % 86400) % 7200) == 0 ) {
                        String hour = Long.toString(Long.valueOf(ts) % 86400 / 3600);
                        busynessTemp.put(hour + ":00", bs);
                        hours.add(hour + ":00");
                        Log.d("JSON", hour+":00");
                        Log.d("JSON", ts+", "+bs);
                    }
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
            if (w.numOfParams == 2)   getBusynessNow(w.result);
            if (w.numOfParams == 4)   getBusynessAtTime(w.result);
//            buildingNames.clear();
//            for (Map.Entry<String, String> entry : bldgbusyness.entrySet())
//            {
////                Log.d("MAPPING", bldgnames.get(entry.getKey()));
//                buildingNames.add(entry.getKey());
//            }

        }
    }
}
