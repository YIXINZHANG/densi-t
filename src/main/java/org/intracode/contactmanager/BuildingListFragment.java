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
import android.widget.ImageButton;
import android.widget.ImageView;
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
import java.util.Arrays;
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
    public  ArrayList<Building> buildings = new ArrayList<Building>();
    private TextView name;
//    private TextView testPrint;
//    private Random rand;
    private ListView listView;
    private RoundCornerProgressBar progress1;
    private ArrayAdapter<String> adapter;
    private ActionBar actionBar;
    private GlobalVariables gv;
    private int position = 0;
    private int currTime = 0;
    private Calendar c;
    private int dayOfWeek;
    private int hour;
    private int minute;
    private Boolean fav = false;
    private Boolean foo = false;
    private Boolean stu = false;
    private Boolean rec = false;

    private String API = "http://densit-api.appspot.com/locations";

    UpdateListener mCallback;

    public interface UpdateListener {
        public void onArticleSelected(int position, String id, String name);
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
//        String[] params = new String[4];
//        params[0] = API;
//        params[1] = "1";
//        params[2] = Long.toString(System.currentTimeMillis() / 1000L);
//        params[3] = Long.toString(System.currentTimeMillis() / 1000L);
//        Log.d("TIME", params[3]);
//        Busyness busy = new Busyness();
//        busy.execute(API);
//        busy.execute(params);//TESTING
        int[] t1 = {21,18,10,7,5,5,10,29,38,58,73,85,87,83,71,53,46,42,62,65,53,51,42,36};
        int[] t2 = {18,10,4,2,3,4,9,24,34,49,73,87,93,81,67,42,37,39,44,31,26,27,23,20};
        int[] t3 = {32,25,11,8,6,5,4,22,29,32,42,59,62,78,75,61,73,55,78,72,64,58,47,39};
        int[] t4 = {0,0,0,0,25,34,30,21,29,35,33,28,39,35,49,52,67,61,72,74,13,2,0,0};
        int[] t5 = {19,17,11,6,4,5,8,28,37,56,68,71,70,68,65,59,42,38,40,43,47,36,31,20};
        int[] t6 = {24,19,15,9,4,5,5,25,42,51,62,87,82,71,65,61,44,41,43,49,39,37,31,29};


        Building newBuilding1 = new Building("Culc", 33.774599, -84.396372, 39, t1, true, false, true, false);
        Building newBuilding2 = new Building("Student Center", 33.774028, -84.398818, 35, t2, true, true, true, true);
        Building newBuilding3 = new Building("Library", 33.774327, -84.395825, 31, t3, false, false, true, false);
        Building newBuilding4 = new Building("CRC", 33.77562, -84.403753, 46, t4, true, true, false, true);
        Building newBuilding5 = new Building("Klaus", 33.777212, -84.396281, 33, t5, false, false, true, false);
        Building newBuilding6 = new Building("CoC", 33.777386, -84.396281, 52, t6, false, true, true, false);
        buildings.add(newBuilding1);
        buildings.add(newBuilding2);
        buildings.add(newBuilding3);
        buildings.add(newBuilding4);
        buildings.add(newBuilding5);
        buildings.add(newBuilding6);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        gv = (GlobalVariables) getActivity().getApplication();
        bldgnames.putAll(gv.getBldgnames());
        bldglocs.putAll(gv.getBldglocs());
        bldgbusyness.putAll(gv.getBldgbusyness());
        busynessTemp.putAll(gv.getBusynessTemp());
//        buildingNames = new ArrayList<String>(gv.getBuildingNames());

//        Log.d("2nd", "Here");
//        Log.d("JSON", "Buildings " + bldgnames);
//        Log.d("JSON", "Buildbusyness " + bldgbusyness);
//        Log.d("JSON", "Buildbldglocs " + bldglocs);
//        Log.d("JSON", "Builddmanes " + buildingNames);
//        Log.d("JSON", "BuildTemp " + busynessTemp);


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
                    currTime = position;
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // TODO Auto-generated method stub

                }
            });

        //filters
        final ImageButton fa = (ImageButton) getActivity().findViewById(R.id.favourite);
        fa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!fav) {
                    fav = true;
                    fa.setImageResource(R.drawable.favourite_selected);
                    ViewFav();
                } else {
                    fav = false;
                    fa.setImageResource(R.drawable.favourite);
                    ViewAll();
                }
                displayListView();

            }
        });

        final ImageButton food = (ImageButton) getActivity().findViewById(R.id.food);
        food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!foo){
                    foo = true;
                    food.setImageResource(R.drawable.food_selected);
                    ViewFood();
                } else {
                    foo = false;
                    food.setImageResource(R.drawable.food);
                    ViewAll();
                }
                displayListView();

            }
        });

        final ImageButton study = (ImageButton) getActivity().findViewById(R.id.study);
        study.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!stu){
                    stu = true;
                    study.setImageResource(R.drawable.study_selected);
                    ViewStudy();
                } else {
                    stu = false;
                    study.setImageResource(R.drawable.study);
                    ViewAll();
                }
                displayListView();

            }
        });

        final ImageButton recreation = (ImageButton) getActivity().findViewById(R.id.recreation);
        recreation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!rec){
                    rec = true;
                    recreation.setImageResource(R.drawable.recreation_selected);
                    ViewRec();
                } else {
                    rec = false;
                    recreation.setImageResource(R.drawable.recreation);
                    ViewAll();
                }
                displayListView();
            }
        });
//
//        Spinner dateSpinner = (Spinner) getActivity().findViewById(R.id.date_spinner);
//        // Create an ArrayAdapter using the string array and a default spinner layout
//        ArrayAdapter<CharSequence> dayAdapters = ArrayAdapter.createFromResource(getActivity(), R.array.day_array, android.R.layout.simple_spinner_item);
//        // Specify the layout to use when the list of choices appears
//        dayAdapters.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        // Apply the adapter to the spinner
//        dateSpinner.setAdapter(dayAdapters);
//        dateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view,
//                                       int position, long id) {
//                // TODO Auto-generated method stub
//
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                // TODO Auto-generated method stub
//
//            }
//        });
////        int position = hour/2;
//        timeSpinner.setSelection(position);
        ViewAll();
        displayListView();
//
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

    private void ViewAll() {
        buildingNames.clear();
        for (Building b : buildings) {
            buildingNames.add(b.getName());
        }
    }

    private void ViewFav() {
        buildingNames.clear();
        for (Building b : buildings){
            if (b.getFavorite()) {
                Log.d("FAV1", Boolean.toString(b.getFavorite())+","+b.getName());
                buildingNames.add(b.getName());
            }
//            buildingNames.add(b.getName());
        }
    }

    private void ViewFood() {
        buildingNames.clear();
        for (Building b : buildings){
            if (b.getFood()) {
//                Log.d("FAV1", Boolean.toString(b.getFavorite())+","+b.getName());
                buildingNames.add(b.getName());
            }
//            buildingNames.add(b.getName());
        }
    }

    private void ViewStudy() {
        buildingNames.clear();
        for (Building b : buildings){
            if (b.getStudy()) {
//                Log.d("FAV1", Boolean.toString(b.getFavorite())+","+b.getName());
                buildingNames.add(b.getName());
            }
//            buildingNames.add(b.getName());
        }
    }

    private void ViewRec() {
        buildingNames.clear();
        for (Building b : buildings){
            if (b.getRec()) {
//                Log.d("FAV1", Boolean.toString(b.getFavorite())+","+b.getName());
                buildingNames.add(b.getName());
            }
//            buildingNames.add(b.getName());
        }
    }

    private void displayListView() {
        //create an ArrayAdaptar from the String Array
//        buildingNames.clear();
//        for (Building b : buildings){
//            buildingNames.add(b.getName());
//        }
        adapter = new LocationListAdapter(getActivity(), R.layout.listview_item, buildingNames);
        listView = (ListView) getView().findViewById(R.id.listview);
        listView.setAdapter(adapter);
//        listView.setTextFilterEnabled(true);
//        listView.setOnItemClickListener(new BLDGOnClickListener());
        AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Log.d("ID", Integer.toString(position));
            }
        };
        listView.setOnItemClickListener(listener);
    }

    private class LocationListAdapter extends ArrayAdapter<String> {
        public LocationListAdapter(Context c, int r, List<String> o) {
            super(c, r, o);
        }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            if (view == null)
                view = getActivity().getLayoutInflater().inflate(R.layout.listview_item, parent, false);

//            String buildingName = buildingNames.get(position);
//            Log.d("NAMES", buildingName);
//            final Building b = buildings.get(position);
            for (Building temp : buildings){
                if (temp.getName() == buildingNames.get(position)){
                    final Building b = temp;
                    name = (TextView) view.findViewById(R.id.buildingName);
                    name.setText(b.getName());
                    Log.d("MAPPING", b.getName());
                    int percent = 0;
//            String name = bldgbusyness.get(buildingName);
//            if (name != null) {
//                percent = Integer.parseInt(bldgbusyness.get(buildingName));
//            }
                    int[] times = b.getBusynessArray();
                    percent = times[currTime];
//            busyness = (TextView) view.findViewById(R.id.busyness);
//            busyness.setText(Integer.toString(percent) + "%");
                    progress1 = (RoundCornerProgressBar) view.findViewById(R.id.progress);
                    progress1.setProgressColor(Color.parseColor("#000000"));
                    progress1.setProgressBackgroundColor(Color.parseColor("#fff3f3f3"));
                    progress1.setMax(100);
                    progress1.setProgress(percent);
                    final ImageView iv = (ImageView) view.findViewById(R.id.clickfavourite);
                    Log.d("FAV", Boolean.toString(b.getFavorite())+", "+b.getName());
                    if (b.getFavorite()) {
                        iv.setImageResource(R.drawable.favourite);
                    }
                    iv.setOnClickListener(new View.OnClickListener() {
                        //@Override
                        public void onClick(View v) {
//                            Log.d("ACT", "click");
//                            Log.d("POS", Boolean.toString(b.getFavorite()));
                            if (b.getFavorite()){
                                b.setFavorite();
                                iv.setImageResource(R.drawable.unfavourite);
                            } else {
                                b.setFavorite();
                                Log.d("POS", Integer.toString(iv.getId()));
                                iv.setImageResource(R.drawable.favourite);
                            }
                        }
                    });
                }
            }


            return view;
        }
    }

//    class Wrapper
//    {
//        public int numOfParams;
//        public String result;
//    }
//
//    public class Busyness extends AsyncTask<String, String, Wrapper> {
//        public String test = "123";
//        @Override
//        protected Wrapper doInBackground(String... params) {
//            String urlString = params[0]; // URL to call
//            InputStream in = null;
//            // HTTP Get
//            int paramLength = params.length; //storing [0] url, [1] id [2,3]starttime/endtime
//            if (paramLength == 3 || paramLength >=5) {
//                Log.d("ERROR", "params length error");
//            }
//            if (paramLength >=2) {
//                urlString = urlString + "/" + params[1];
//                Log.d("URL", urlString);
//            }
//            if (paramLength ==4) {
//                urlString = urlString + "/busyness/" + params[2] + "/" + params[3];
//                Log.d("URL", urlString);
//            }
//            try {
//                URL url = new URL(urlString);
//                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//                in = new BufferedInputStream(urlConnection.getInputStream());
//            } catch (Exception e ) {
//                Log.d("ERROR", e.getMessage());
//            }
//            String result = convertStreamToString(in);
//
//            Log.d("API", result);
//            Wrapper w = new Wrapper();
//            w.numOfParams = paramLength;
//            w.result = result;
//            return w;
//        }
//
//        private void getLocations(String response) {
//            //parse json
//            try {
//                JSONObject js = new JSONObject(response);
//                JSONArray bldgs = js.getJSONArray("locations");
//                for(int i = 0 ; i < bldgs.length() ; i++){
//                    String value = "0";
//                    String name = bldgs.getJSONObject(i).getString("name");
//                    String id = bldgs.getJSONObject(i).getString("id");
//                    JSONObject geo = bldgs.getJSONObject(i).getJSONObject("geocode");
//                    if (bldgs.getJSONObject(i).has("busyness")) {
//                        JSONObject keys = bldgs.getJSONObject(i).getJSONObject("busyness");
//                        value = keys.toString();
//
//
//                        bldgnames.put(id, name);
//
//                        String[]tokens = value.split(":");
//                        String percent = tokens[tokens.length-1];
//                        Log.d("JSON", "key =" + id + "busyness" + percent);
//                        bldgbusyness.put(id, percent.substring(0, percent.length() - 1));
//                    } else {
//                        bldgnames.put(id, name);
//                        bldgbusyness.put(id, "0");
//                    }
//                    bldglocs.put(id, geo.toString());
//                    Log.d("JSON", "key =" + id + "name" + name);
//                    Log.d("JSON", "key =" + id + "geo" + geo.toString());
//
//                }
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//
//        private void getBusynessNow(String response) {
//            //parse json
//            try {
//                JSONObject js = new JSONObject(response);
//                JSONObject keys = js.getJSONObject("busyness");
//                String id = js.getString("id");
//                String value = keys.toString();
//                String[]tokens = value.split(":");
//                String percent = tokens[tokens.length-1];
//                bldgbusyness.put(id, percent.substring(0, percent.length() - 1));
//                Log.d("JSON", "key =" + id + "busyness" + percent);
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//
//        private void getBusynessAtTime(String response) {
//            //parse json
//            try {
//                JSONObject js = new JSONObject(response);
//
//                for(int i = 0; i<js.names().length(); i++){
//                    String ts = js.names().getString(i);
//                    String bs = js.get(ts).toString();
//                    busynessTemp.put(ts, bs);
//                    Log.d("JSON", ts+", "+bs);
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//
//        private String convertStreamToString(InputStream is) {
//
//            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
//            StringBuilder sb = new StringBuilder();
//
//            String line = null;
//            try {
//                while ((line = reader.readLine()) != null) {
//                    sb.append(line + "\n");
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            } finally {
//                try {
//                    is.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            return sb.toString();
//        }
//
//        protected void onPostExecute(Wrapper w) {
////            Intent intent = new Intent(getApplicationContext(), BuildingListFragment.class);
////
////            intent.putExtra(EXTRA_MESSAGE, result);
////
////            startActivity(intent);
////            testPrint.setText(result);
//
//            if (w.numOfParams == 1)   {
//                getLocations(w.result);
//                buildingNames.clear();
//                for (Map.Entry<String, String> entry : bldgbusyness.entrySet())
//                {
////                Log.d("MAPPING", bldgnames.get(entry.getKey()));
//                    buildingNames.add(entry.getKey());
//                }
//            }
//            if (w.numOfParams == 2)   getBusynessNow(w.result);
//            if (w.numOfParams == 4)   getBusynessAtTime(w.result);
////            buildingNames.clear();
////            for (Map.Entry<String, String> entry : bldgbusyness.entrySet())
////            {
//////                Log.d("MAPPING", bldgnames.get(entry.getKey()));
////                buildingNames.add(entry.getKey());
////            }
//
//        }
//    }


}
