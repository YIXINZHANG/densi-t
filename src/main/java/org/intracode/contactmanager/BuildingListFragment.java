package org.intracode.contactmanager;


import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AbsListView;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.google.android.gms.games.Players;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class BuildingListFragment extends Fragment {

    private List<String> urlList = new ArrayList<String>();
    private ArrayList<String> buildingNames = new ArrayList<String>();
    private ArrayList<LatLng> positions = new ArrayList<LatLng>();
    private TextView busyness;
    public  Map<String, String> bldgnames = new HashMap<String, String>();
    public  Map<String, String> bldglocs = new HashMap<String, String>();
    public  Map<String, String> bldgbusyness = new HashMap<String, String>();
    public  Map<String, String> busynessTemp = new HashMap<String, String>();
    private TextView name;
//    private TextView testPrint;
//    private Random rand;
    private ListView listView;
    private RoundCornerProgressBar progress1;
    private ArrayAdapter<String> adapter;
    private ActionBar actionBar;
    private GlobalVariables gv;
    private int position = 0;

    private Calendar c;
    private int dayOfWeek;
    private int hour;
    private int minute;

    private String API = "http://densit-api.appspot.com/locations";

    UpdateListener mCallback;

    public interface UpdateListener {
        public void onArticleSelected(int position);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (UpdateListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String[] params = new String[2];
        params[0] = API;
        params[1] = "1";
        Busyness busy = new Busyness();
        busy.execute(API);
//        busy.execute(params);//TESTING

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        gv = (GlobalVariables) getActivity().getApplication();

//        buildingNames.clear();
//        for (Map.Entry<String, String> entry : bldgmap.entrySet())
//        {
//            buildingNames.add(entry.getKey());
//        }
//        buildingNames.add("Culc");
//        buildingNames.add("Student Center");
//        buildingNames.add("Library");
//        buildingNames.add("CRC");
//        buildingNames.add("Klaus");
//        buildingNames.add("CoC");

        c = Calendar.getInstance();
        dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
//        Toast.makeText(getActivity().getApplicationContext(), dayOfWeek, Toast.LENGTH_LONG).show();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);

        Spinner timeSpinner = (Spinner) getActivity().findViewById(R.id.time_spinner);
            // Create an ArrayAdapter using the string array and a default spinner layout
            ArrayAdapter<CharSequence> adapters = ArrayAdapter.createFromResource(getActivity(), R.array.time_array, android.R.layout.simple_spinner_item);
            // Specify the layout to use when the list of choices appears
            adapters.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // Apply the adapter to the spinner
            timeSpinner.setAdapter(adapters);
            timeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

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

        Spinner dateSpinner = (Spinner) getActivity().findViewById(R.id.date_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> dayAdapters = ArrayAdapter.createFromResource(getActivity(), R.array.day_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        dayAdapters.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        dateSpinner.setAdapter(dayAdapters);
        dateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

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
//        int position = hour/2;
        timeSpinner.setSelection(position);
        displayListView();

        }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
            if (container == null) {
            return null;
            }
            View view = inflater.inflate(R.layout.fragment_buildinglist, container, false);
            return view;
            }

    private void displayListView() {
        //create an ArrayAdaptar from the String Array
        adapter = new LocationListAdapter(getActivity(), R.layout.listview_item, buildingNames);
        listView = (ListView) getView().findViewById(R.id.listview);
        listView.setAdapter(adapter);
        listView.setTextFilterEnabled(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                actionBar = ((ActionBarActivity)getActivity()).getSupportActionBar();
                gv.setSelected(position);
                mCallback.onArticleSelected(position);
                actionBar.setSelectedNavigationItem(1);


            }
        });
    }

    private class LocationListAdapter extends ArrayAdapter<String> {
        public LocationListAdapter(Context c, int r, List<String> o) {
            super(c, r, o);
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            if (view == null)
                view = getActivity().getLayoutInflater().inflate(R.layout.listview_item, parent, false);

            String buildingName = buildingNames.get(position);
//            Log.d("NAMES", buildingName);
            name = (TextView) view.findViewById(R.id.buildingName);
            name.setText(bldgnames.get(buildingName));
            Log.d("MAPPING", bldgnames.get(buildingName));


//            rand = new Random();
            int percent = Integer.parseInt(bldgbusyness.get(buildingName));
            busyness = (TextView) view.findViewById(R.id.busyness);
            busyness.setText(Integer.toString(percent) + "%");
            progress1 = (RoundCornerProgressBar) view.findViewById(R.id.progress);
            progress1.setProgressColor(Color.parseColor("#6960ec"));
            progress1.setBackgroundColor(Color.parseColor("#e5e4e2"));
            progress1.setMax(100);
            progress1.setProgress(percent);

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
            if (paramLength ==2) {
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
                JSONArray bldgs = js.getJSONArray("locations");
                for(int i = 0 ; i < bldgs.length() ; i++){
                    JSONObject keys = bldgs.getJSONObject(i).getJSONObject("busyness");
                    String id = bldgs.getJSONObject(i).getString("id");
                    String value = keys.toString();
                    String[]tokens = value.split(":");
                    String percent = tokens[tokens.length-1];
                    bldgbusyness.put(id, percent.substring(0, percent.length() - 1));
                    Log.d("JSON", "key =" + id + "busyness" + percent);
                }

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

            if (w.numOfParams == 1)   getLocations(w.result);
            if (w.numOfParams == 2)   getBusynessNow(w.result);
            if (w.numOfParams == 4)   getBusynessAtTime(w.result);
            buildingNames.clear();
            for (Map.Entry<String, String> entry : bldgbusyness.entrySet())
            {
                Log.d("MAPPING", bldgnames.get(entry.getKey()));
                buildingNames.add(entry.getKey());
            }

        }
    }


}
