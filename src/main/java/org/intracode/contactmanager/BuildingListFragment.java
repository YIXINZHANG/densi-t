package org.intracode.contactmanager;


import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.LinearLayout;
import android.widget.ListAdapter;
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
    private ArrayList<String> buildingNamesFavorite = new ArrayList<String>();
    private ArrayList<String> buildingNamesSuggestion = new ArrayList<String>();
    private ArrayList<String> buildingNamesOthers = new ArrayList<String>();
    private ArrayList<String> buildingNamesDummy = new ArrayList<String>();
    private ArrayList<LatLng> positions = new ArrayList<LatLng>();
    private TextView busyness;
    public  Map<String, String> bldgnames = new HashMap<String, String>();
    public  Map<String, String> bldglocs = new HashMap<String, String>();
    public  Map<String, String> bldgbusyness = new HashMap<String, String>();
    public  Map<String, String> busynessTemp = new HashMap<String, String>();
    public  ArrayList<Building> buildings = new ArrayList<Building>();
    public  ArrayList<Building> buildingsFinal = new ArrayList<Building>();
    private TextView name;
//    private TextView testPrint;
//    private Random rand;
    private ListView listViewFavorite, listViewSuggestion, listViewOthers;
    private RoundCornerProgressBar progress1;
    private ArrayAdapter<String> adapter;
    private ActionBar actionBar;
    private GlobalVariables gv;
    private int currDate = 0;
    private int currTime = 0;
    private Calendar c;
    private int dayOfWeek;
    private int hour;
    private int minute;
    private Boolean fav = false;
    private Boolean foo = false;
    private Boolean stu = false;
    private Boolean rec = false;
    private ImageButton fa, food, study, recreation;

    private TextView favText, othText;
    private View favLine, favLine2;
    private Spinner timeSpinner, dateSpinner;

    private String API = "http://densit-api.appspot.com/locations";
    public static final String PREFS_NAME = "MyPrefsFile";

    UpdateListener mCallback;
    private SharedPreferences settings;

    public interface UpdateListener {
        public void onArticleSelected(String name, boolean check);
        public void onDayTimeSelected(int day, int time);
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

    public String timeToT(int hour, int day) {
        int res = (day*24+hour)*60;
        Log.d("TIME", Integer.toString(res));
        return Integer.toString(res);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // get current time
        Calendar c = Calendar.getInstance();
        dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        hour = c.get(Calendar.HOUR_OF_DAY);
        // make api call
        String[] params = new String[2];
        params[0] = API;
        params[1] = timeToT(hour,dayOfWeek);
        Busyness busy = new Busyness();
        busy.execute(params);

        int[] t1 = {21,18,10,7,5,5,10,29,38,58,73,85,87,83,71,53,46,42,62,65,53,51,42,36};
        int[] t2 = {18,10,4,2,3,4,9,24,34,49,73,87,93,81,67,42,37,39,44,31,26,27,23,20};
        int[] t3 = {32,25,11,8,6,5,4,22,29,32,42,59,62,78,75,61,73,55,78,72,64,58,47,39};
        int[] t4 = {0,0,0,0,25,34,30,21,29,35,33,28,39,35,49,52,67,61,72,74,13,2,0,0};
        int[] t5 = {19,17,11,6,4,5,8,28,37,56,68,71,70,68,65,59,42,38,40,43,47,36,31,20};
        int[] t6 = {24,19,15,9,4,5,5,25,42,51,62,87,82,71,65,61,44,41,43,49,39,37,31,29};


        Building newBuilding1 = new Building("1", "Culc", 33.774599, -84.396372, 39, t1, true, false, true, false);
        Building newBuilding2 = new Building("2", "Student Center", 33.774028, -84.398818, 35, t2, true, true, true, true);
        Building newBuilding3 = new Building("3", "Library", 33.774327, -84.395825, 31, t3, true, false, true, false);
        Building newBuilding4 = new Building("4", "CRC", 33.77562, -84.403753, 46, t4, false, true, false, true);
        Building newBuilding5 = new Building("5","Klaus", 33.777212, -84.396281, 33, t5, false, false, true, false);
        Building newBuilding6 = new Building("6", "CoC", 33.777386, -84.396281, 52, t6, false, true, true, false);
        buildings.add(newBuilding1);
        buildings.add(newBuilding2);
        buildings.add(newBuilding3);
        buildings.add(newBuilding4);
        buildings.add(newBuilding5);
        buildings.add(newBuilding6);
        buildingsFinal.addAll(buildings);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);




//        settings = getActivity().getSharedPreferences(PREFS_NAME, 0);
//        Map<String, ?> whatList = settings.getAll();
//        for (int i=0; i < buildings.size(); i++) {
//            System.out.println(settings.getBoolean(buildings.get(i).getName(), false) + " " + buildings.get(i).getName()+ " here");
//        }
        gv = (GlobalVariables) getActivity().getApplication();
        bldgnames.putAll(gv.getBldgnames());
        bldglocs.putAll(gv.getBldglocs());
        bldgbusyness.putAll(gv.getBldgbusyness());
        busynessTemp.putAll(gv.getBusynessTemp());
        favText = (TextView) getActivity().findViewById(R.id.favText);
        favLine = (View) getActivity().findViewById(R.id.favView);
        othText = (TextView) getActivity().findViewById(R.id.othText);
        favLine2 = (View) getActivity().findViewById(R.id.favView2);

        c = Calendar.getInstance();
        dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
//        Toast.makeText(getActivity().getApplicationContext(), dayOfWeek, Toast.LENGTH_LONG).show();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        changeList();
        for (Building b:buildings) {
            b.setBusyneesNow(b.getBusynessArray()[hour]);
        }

        timeSpinner = (Spinner) getActivity().findViewById(R.id.time_spinner);
            // Create an ArrayAdapter using the string array and a default spinner layout
            ArrayAdapter<CharSequence> adapters = ArrayAdapter.createFromResource(getActivity(), R.array.time_array, android.R.layout.simple_spinner_item);
            // Specify the layout to use when the list of choices appears
            adapters.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // Apply the adapter to the spinner
            timeSpinner.setAdapter(adapters);
            timeSpinner.setSelection(hour);
            timeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int position, long id) {
                    // TODO Auto-generated method stub
                    currTime = position;
                    for (Building b : buildings) {
                        b.setBusyneesNow(b.getBusynessArray()[currTime]);
                    }
                    mCallback.onDayTimeSelected(dateSpinner.getSelectedItemPosition(), position);
                    displayListView();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // TODO Auto-generated method stub

                }
            });

        dateSpinner = (Spinner) getActivity().findViewById(R.id.date_spinner);
            // Create an ArrayAdapter using the string array and a default spinner layout
            ArrayAdapter<CharSequence> adapters2 = ArrayAdapter.createFromResource(getActivity(), R.array.day_array, android.R.layout.simple_spinner_item);
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
                    Random r = new Random();
                    currDate = position;
                    mCallback.onDayTimeSelected(position, timeSpinner.getSelectedItemPosition());
                    if (currDate == 0 || currDate == 6) {
                        for (Building b : buildings) {
                            int a = r.nextInt(40) - 20;
                            if ((b.getBusynessArray()[currTime])/2 - a < 0) {
                                a = 0;
                            } else {
                                a = ((b.getBusynessArray()[currTime]) - a)/2;
                            }
                            b.setBusyneesNow(a);
                        }
                        displayListView2();
                    } else {
                        for (Building b : buildings) {
                            int a = r.nextInt(40) - 20;
                            if (b.getBusynessArray()[currTime] - a < 0) {
                                a = 0;
                            } else {
                                a = b.getBusynessArray()[currTime] - a;
                            }
                            b.setBusyneesNow(a);
                        }
                        displayListView2();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // TODO Auto-generated method stub

                }
            });

        food = (ImageButton) getActivity().findViewById(R.id.food);
        food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!foo) {
                    foo = true;
                    food.setImageResource(R.drawable.food_selected);

                    stu = false;
                    study.setImageResource(R.drawable.study);
                    rec = false;
                    recreation.setImageResource(R.drawable.recreation);

                    ViewFood();

                }
                displayListView();

            }
        });

        study = (ImageButton) getActivity().findViewById(R.id.study);
        study.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!stu) {
                    stu = true;
                    study.setImageResource(R.drawable.study_selected);

                    foo = false;
                    food.setImageResource(R.drawable.food);
                    rec = false;
                    recreation.setImageResource(R.drawable.recreation);
                    ViewStudy();
                }
                displayListView();

            }
        });

        recreation = (ImageButton) getActivity().findViewById(R.id.recreation);
        recreation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!rec) {
                    rec = true;
                    recreation.setImageResource(R.drawable.recreation_selected);
                    foo = false;
                    food.setImageResource(R.drawable.food);
                    stu = false;
                    study.setImageResource(R.drawable.study);
                    ViewRec();
                }
                displayListView();
            }
        });
        ViewStudy();
        stu = true;
        study.setImageResource(R.drawable.study_selected);
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

    private void ViewFood() {
        buildingNames.clear();
        for (Building b : buildingsFinal){
            if (b.getFood()) {
//                Log.d("FAV1", Boolean.toString(b.getFavorite())+","+b.getName());
                buildingNames.add(b.getName());
            }
//            buildingNames.add(b.getName());
        }
    }

    private void ViewStudy() {
        buildingNames.clear();
        for (Building b : buildingsFinal){
            if (b.getStudy()) {
//                Log.d("FAV1", Boolean.toString(b.getFavorite())+","+b.getName());
                buildingNames.add(b.getName());
            }
//            buildingNames.add(b.getName());
        }
    }

    private void ViewRec() {
        buildingNames.clear();
        for (Building b : buildingsFinal){
            if (b.getRec()) {
//                Log.d("FAV1", Boolean.toString(b.getFavorite())+","+b.getName());
                buildingNames.add(b.getName());
            }
//            buildingNames.add(b.getName());
        }
    }


    private void displayListView() {
        changeList();
//        System.out.println("Favorite " +buildingNamesFavorite);
//        System.out.println("Suggest " +buildingNamesSuggestion);
//        System.out.println("Others " +buildingNamesOthers);

        buildingNamesDummy.clear();
        buildingNamesDummy.addAll(buildingNamesFavorite);
        adapter = new LocationListAdapter(getActivity(), R.layout.listview_item, buildingNamesFavorite);
        listViewFavorite = (ListView) getView().findViewById(R.id.listviewFavorite);
        listViewFavorite.setAdapter(adapter);
//        listView.setTextFilterEnabled(true);
//        listView.setOnItemClickListener(new BLDGOnClickListener());
//        AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView<?> parent, View view, int position,
//                                    long id) {
//                Log.d("ID", Integer.toString(position));
//            }
//        };
//        listViewFavorite.setOnItemClickListener(listener);


        buildingNamesDummy.clear();
        buildingNamesDummy.addAll(buildingNamesSuggestion);
        adapter = new LocationListAdapter2(getActivity(), R.layout.listview_item, buildingNamesSuggestion);
        listViewSuggestion = (ListView) getView().findViewById(R.id.listviewSuggestion);
        listViewSuggestion.setAdapter(adapter);
//        listView.setTextFilterEnabled(true);
//        listView.setOnItemClickListener(new BLDGOnClickListener());
//        AdapterView.OnItemClickListener listener2 = new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView<?> parent, View view, int position,
//                                    long id) {
//                Log.d("ID", Integer.toString(position));
//            }
//        };
//        listViewSuggestion.setOnItemClickListener(listener2);


        buildingNamesDummy.clear();
        buildingNamesDummy.addAll(buildingNamesOthers);
        adapter = new LocationListAdapter3(getActivity(), R.layout.listview_item, buildingNamesOthers);
        listViewOthers = (ListView) getView().findViewById(R.id.listviewOthers);
        listViewOthers.setAdapter(adapter);
//        listView.setTextFilterEnabled(true);
//        listView.setOnItemClickListener(new BLDGOnClickListener());
//        AdapterView.OnItemClickListener listener3 = new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView<?> parent, View view, int position,
//                                    long id) {
//                Log.d("ID", Integer.toString(position));
//            }
//        };
//        listViewOthers.setOnItemClickListener(listener3);



        setDynamicHeight(listViewFavorite);
        setDynamicHeight(listViewSuggestion);
        setDynamicHeight(listViewOthers);


        if (buildingNamesFavorite.size() == 0) {
            favText.setVisibility(View.INVISIBLE);
            favText.setHeight(0);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)favText.getLayoutParams();
            params.setMargins(0, 0, 0, 0); //substitute parameters for left, top, right, bottom
            favText.setLayoutParams(params);
            favLine.setVisibility(View.INVISIBLE);
            LinearLayout.LayoutParams param = (LinearLayout.LayoutParams)favLine.getLayoutParams();
            param.setMargins(0, 0, 0, 0); //substitute parameters for left, top, right, bottom
            favLine.setLayoutParams(param);
            listViewFavorite.setVisibility(View.INVISIBLE);
        } else {
            favText.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)favText.getLayoutParams();
            params.setMargins(0, 30, 0, 0); //substitute parameters for left, top, right, bottom
            favText.setLayoutParams(params);
            favText.setHeight(70);
            favLine.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams param = (LinearLayout.LayoutParams)favLine.getLayoutParams();
            param.setMargins(0, 20, 0, 0); //substitute parameters for left, top, right, bottom
            favLine.setLayoutParams(param);
            listViewFavorite.setVisibility(View.VISIBLE);
        }

        if (buildingNamesOthers.size() == 0) {
            othText.setVisibility(View.INVISIBLE);
            favLine2.setVisibility(View.INVISIBLE);
        } else {
            othText.setVisibility(View.VISIBLE);
            favLine2.setVisibility(View.VISIBLE);
        }

    }

    private void displayListView2() {
        changeList();
//        System.out.println("Favorite " +buildingNamesFavorite);
//        System.out.println("Suggest " +buildingNamesSuggestion);
//        System.out.println("Others " +buildingNamesOthers);


        buildingNamesDummy.clear();
        buildingNamesDummy.addAll(buildingNamesFavorite);
        adapter = new LocationListAdapter4(getActivity(), R.layout.listview_item, buildingNamesFavorite);
        listViewFavorite = (ListView) getView().findViewById(R.id.listviewFavorite);
        listViewFavorite.setAdapter(adapter);
//        listView.setTextFilterEnabled(true);
//        listView.setOnItemClickListener(new BLDGOnClickListener());
        AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Log.d("ID", Integer.toString(position));
            }
        };
        listViewFavorite.setOnItemClickListener(listener);


        buildingNamesDummy.clear();
        buildingNamesDummy.addAll(buildingNamesSuggestion);
        adapter = new LocationListAdapter5(getActivity(), R.layout.listview_item, buildingNamesSuggestion);
        listViewSuggestion = (ListView) getView().findViewById(R.id.listviewSuggestion);
        listViewSuggestion.setAdapter(adapter);
//        listView.setTextFilterEnabled(true);
//        listView.setOnItemClickListener(new BLDGOnClickListener());
        AdapterView.OnItemClickListener listener2 = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Log.d("ID", Integer.toString(position));
            }
        };
        listViewSuggestion.setOnItemClickListener(listener2);


        buildingNamesDummy.clear();
        buildingNamesDummy.addAll(buildingNamesOthers);
        adapter = new LocationListAdapter6(getActivity(), R.layout.listview_item, buildingNamesOthers);
        listViewOthers = (ListView) getView().findViewById(R.id.listviewOthers);
        listViewOthers.setAdapter(adapter);
//        listView.setTextFilterEnabled(true);
//        listView.setOnItemClickListener(new BLDGOnClickListener());
        AdapterView.OnItemClickListener listener3 = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Log.d("ID", Integer.toString(position));
            }
        };
        listViewOthers.setOnItemClickListener(listener3);



        setDynamicHeight(listViewFavorite);
        setDynamicHeight(listViewSuggestion);
        setDynamicHeight(listViewOthers);
        if (buildingNamesFavorite.size() == 0) {
            favText.setVisibility(View.INVISIBLE);
            favText.setHeight(0);
            favLine.setVisibility(View.INVISIBLE);
            listViewFavorite.setVisibility(View.INVISIBLE);

        } else {
            favText.setVisibility(View.VISIBLE);
            favText.setHeight(70);
            favLine.setVisibility(View.VISIBLE);
            listViewFavorite.setVisibility(View.VISIBLE);
        }

        if (buildingNamesOthers.size() == 0) {
            othText.setVisibility(View.INVISIBLE);
            favLine2.setVisibility(View.INVISIBLE);
        } else {
            othText.setVisibility(View.VISIBLE);
            favLine2.setVisibility(View.VISIBLE);
        }

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
                if (temp.getName() == buildingNamesFavorite.get(position)){
                    final Building b = temp;
                    name = (TextView) view.findViewById(R.id.buildingName);
                    name.setText(b.getName());
//                    Log.d("MAPPING", b.getName());
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
                    if (percent < 30) {
                        progress1.setProgressColor(Color.parseColor("#86BA4B"));
                    } else if (percent < 70) {
                        progress1.setProgressColor(Color.parseColor("#F29317"));
                    } else {
                        progress1.setProgressColor(Color.parseColor("#E64436"));
                    }
                    progress1.setProgressBackgroundColor(Color.parseColor("#fff3f3f3"));
                    progress1.setMax(100);
                    progress1.setProgress(percent);
                    final ImageView iv = (ImageView) view.findViewById(R.id.clickfavourite);
//                    Log.d("FAV", Boolean.toString(b.getFavorite())+", "+b.getName());
                    if (b.getFavorite()) {
                        iv.setImageResource(R.drawable.favourite);
                    }
                    iv.setOnClickListener(new View.OnClickListener() {
                        //@Override
                        public void onClick(View v) {
//                            Log.d("ACT", "click");
//                            Log.d("POS", Boolean.toString(b.getFavorite()));
                            if (b.getFavorite()) {
                                b.setFavorite();
                                iv.setImageResource(R.drawable.unfavourite);
//                                SharedPreferences.Editor editor = getActivity().getSharedPreferences(PREFS_NAME, 0).edit();
//                                editor.putBoolean(b.getName(), false);
//                                editor.commit();
                                mCallback.onArticleSelected(b.getName(), false);
                                displayListView();
                            } else {
                                b.setFavorite();
                                Log.d("POS", Integer.toString(iv.getId()));
                                iv.setImageResource(R.drawable.favourite);
//                                SharedPreferences.Editor editor = getActivity().getSharedPreferences(PREFS_NAME, 0).edit();
//                                editor.putBoolean(b.getName(), true);
//                                editor.commit();
                                mCallback.onArticleSelected(b.getName(), true);
                                displayListView();
                            }
                        }
                    });
                }
            }
            return view;
        }

    }

    private class LocationListAdapter2 extends ArrayAdapter<String> {
        public LocationListAdapter2(Context c, int r, List<String> o) {
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
                if (temp.getName() == buildingNamesSuggestion.get(position)){
                    final Building b = temp;
                    name = (TextView) view.findViewById(R.id.buildingName);
                    name.setText(b.getName());
//                    Log.d("MAPPING", b.getName());
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
                    if (percent < 30) {
                        progress1.setProgressColor(Color.parseColor("#86BA4B"));
                    } else if (percent < 70) {
                        progress1.setProgressColor(Color.parseColor("#F29317"));
                    } else {
                        progress1.setProgressColor(Color.parseColor("#E64436"));
                    }
                    progress1.setProgressBackgroundColor(Color.parseColor("#fff3f3f3"));
                    progress1.setMax(100);
                    progress1.setProgress(percent);
                    final ImageView iv = (ImageView) view.findViewById(R.id.clickfavourite);
//                    Log.d("FAV", Boolean.toString(b.getFavorite())+", "+b.getName());
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
//                                SharedPreferences.Editor editor = getActivity().getSharedPreferences(PREFS_NAME, 0).edit();
//                                editor.putBoolean(b.getName(), false);
//                                editor.commit();
                                mCallback.onArticleSelected(b.getName(), false);
                                displayListView();
                            } else {
                                b.setFavorite();
                                Log.d("POS", Integer.toString(iv.getId()));
                                iv.setImageResource(R.drawable.favourite);
//                                SharedPreferences.Editor editor = getActivity().getSharedPreferences(PREFS_NAME, 0).edit();
//                                editor.putBoolean(b.getName(), true);
//                                editor.commit();
                                mCallback.onArticleSelected(b.getName(), true);
                                displayListView();
                            }
                        }
                    });
                }
            }
            return view;
        }
    }

    private class LocationListAdapter3 extends ArrayAdapter<String> {
        public LocationListAdapter3(Context c, int r, List<String> o) {
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
                if (temp.getName() == buildingNamesOthers.get(position)){
                    final Building b = temp;
                    name = (TextView) view.findViewById(R.id.buildingName);
                    name.setText(b.getName());
//                    Log.d("MAPPING", b.getName());
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
                    if (percent < 30) {
                        progress1.setProgressColor(Color.parseColor("#86BA4B"));
                    } else if (percent < 70) {
                        progress1.setProgressColor(Color.parseColor("#F29317"));
                    } else {
                        progress1.setProgressColor(Color.parseColor("#E64436"));
                    }

                    progress1.setProgressBackgroundColor(Color.parseColor("#fff3f3f3"));
                    progress1.setMax(100);
                    progress1.setProgress(percent);
                    final ImageView iv = (ImageView) view.findViewById(R.id.clickfavourite);
//                    Log.d("FAV", Boolean.toString(b.getFavorite())+", "+b.getName());
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
//                                SharedPreferences.Editor editor = getActivity().getSharedPreferences(PREFS_NAME, 0).edit();
//                                editor.putBoolean(b.getName(), false);
//                                editor.commit();
                                mCallback.onArticleSelected(b.getName(), false);
                                displayListView();
                            } else if (!b.getFavorite()) {
                                b.setFavorite();
                                iv.setImageResource(R.drawable.favourite);
//                                SharedPreferences.Editor editor = getActivity().getSharedPreferences(PREFS_NAME, 0).edit();
//                                editor.putBoolean(b.getName(), true);
//                                editor.commit();
                                mCallback.onArticleSelected(b.getName(), true);
                                displayListView();
                            }
                        }
                    });
                }
            }
            return view;
        }
    }

    public static void setDynamicHeight(ListView listView) {
        ListAdapter adapter = listView.getAdapter();
        //check adapter if null
        if (adapter == null) {
            return;
        }
        int height = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            height += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams layoutParams = listView.getLayoutParams();
        layoutParams.height = height + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(layoutParams);
        listView.requestLayout();
    }

    private class LocationListAdapter4 extends ArrayAdapter<String> {
        public LocationListAdapter4(Context c, int r, List<String> o) {
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
                if (temp.getName() == buildingNamesFavorite.get(position)){
                    final Building b = temp;
                    name = (TextView) view.findViewById(R.id.buildingName);
                    name.setText(b.getName());
//                    Log.d("MAPPING", b.getName());
                    int percent = 0;
//            String name = bldgbusyness.get(buildingName);
//            if (name != null) {
//                percent = Integer.parseInt(bldgbusyness.get(buildingName));
//            }
                    int[] times = b.getBusynessArray();
                    percent = b.getBusynessNow();
//            busyness = (TextView) view.findViewById(R.id.busyness);
//            busyness.setText(Integer.toString(percent) + "%");
                    progress1 = (RoundCornerProgressBar) view.findViewById(R.id.progress);
                    if (percent < 30) {
                        progress1.setProgressColor(Color.parseColor("#86BA4B"));
                    } else if (percent < 70) {
                        progress1.setProgressColor(Color.parseColor("#F29317"));
                    } else {
                        progress1.setProgressColor(Color.parseColor("#E64436"));
                    }
                    progress1.setProgressBackgroundColor(Color.parseColor("#fff3f3f3"));
                    progress1.setMax(100);
                    progress1.setProgress(percent);
                    final ImageView iv = (ImageView) view.findViewById(R.id.clickfavourite);
//                    Log.d("FAV", Boolean.toString(b.getFavorite())+", "+b.getName());
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
                                mCallback.onArticleSelected(b.getName(), false);
                                displayListView();
                            } else {
                                b.setFavorite();
                                Log.d("POS", Integer.toString(iv.getId()));
                                iv.setImageResource(R.drawable.favourite);
                                mCallback.onArticleSelected(b.getName(), false);
                                displayListView();
                            }
                        }
                    });
                }
            }
            return view;
        }
    }

    private class LocationListAdapter5 extends ArrayAdapter<String> {
        public LocationListAdapter5(Context c, int r, List<String> o) {
            super(c, r, o);

        }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            if (view == null)
                view = getActivity().getLayoutInflater().inflate(R.layout.listview_item, parent, false);

            for (Building temp : buildings){
                if (temp.getName() == buildingNamesSuggestion.get(position)){
                    final Building b = temp;
                    name = (TextView) view.findViewById(R.id.buildingName);
                    name.setText(b.getName());
                    int percent = 0;
                    int[] times = b.getBusynessArray();
                    percent = b.getBusynessNow();
                    progress1 = (RoundCornerProgressBar) view.findViewById(R.id.progress);
                    if (percent < 30) {
                        progress1.setProgressColor(Color.parseColor("#86BA4B"));
                    } else if (percent < 70) {
                        progress1.setProgressColor(Color.parseColor("#F29317"));
                    } else {
                        progress1.setProgressColor(Color.parseColor("#E64436"));
                    }
                    progress1.setProgressBackgroundColor(Color.parseColor("#fff3f3f3"));
                    progress1.setMax(100);
                    progress1.setProgress(percent);
                    final ImageView iv = (ImageView) view.findViewById(R.id.clickfavourite);
                    if (b.getFavorite()) {
                        iv.setImageResource(R.drawable.favourite);
                    }
                    iv.setOnClickListener(new View.OnClickListener() {
                        //@Override
                        public void onClick(View v) {
                            if (b.getFavorite()){
                                b.setFavorite();
                                iv.setImageResource(R.drawable.unfavourite);
                                mCallback.onArticleSelected(b.getName(), false);
                                displayListView();
                            } else {
                                b.setFavorite();
                                Log.d("POS", Integer.toString(iv.getId()));
                                iv.setImageResource(R.drawable.favourite);
                                mCallback.onArticleSelected(b.getName(), true);
                                displayListView();
                            }
                        }
                    });
                }
            }
            return view;
        }
    }

    private class LocationListAdapter6 extends ArrayAdapter<String> {
        public LocationListAdapter6(Context c, int r, List<String> o) {
            super(c, r, o);

        }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            if (view == null)
                view = getActivity().getLayoutInflater().inflate(R.layout.listview_item, parent, false);
            for (Building temp : buildings){
                if (temp.getName() == buildingNamesOthers.get(position)){
                    final Building b = temp;
                    name = (TextView) view.findViewById(R.id.buildingName);
                    name.setText(b.getName());
                    int percent = 0;
                    int[] times = b.getBusynessArray();
                    percent = b.getBusynessNow();
                    progress1 = (RoundCornerProgressBar) view.findViewById(R.id.progress);
                    if (percent < 30) {
                        progress1.setProgressColor(Color.parseColor("#86BA4B"));
                    } else if (percent < 70) {
                        progress1.setProgressColor(Color.parseColor("#F29317"));
                    } else {
                        progress1.setProgressColor(Color.parseColor("#E64436"));
                    }

                    progress1.setProgressBackgroundColor(Color.parseColor("#fff3f3f3"));
                    progress1.setMax(100);
                    progress1.setProgress(percent);
                    final ImageView iv = (ImageView) view.findViewById(R.id.clickfavourite);
//                    Log.d("FAV", Boolean.toString(b.getFavorite())+", "+b.getName());
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
                                mCallback.onArticleSelected(b.getName(), false);
                                displayListView();
                            } else if (!b.getFavorite()) {
                                b.setFavorite();
                                iv.setImageResource(R.drawable.favourite);
                                mCallback.onArticleSelected(b.getName(), true);
                                displayListView();
                            }
                        }
                    });
                }
            }
            return view;
        }
    }

    public void changeList() {
        int lowest = 100;
        String lowestName ="";
        buildingNamesFavorite.clear();
        buildingNamesOthers.clear();
        buildingNamesSuggestion.clear();
        for (Building b: buildings) {
            for (String name:buildingNames) {
                if (b.getName() == name) {
                    if (lowest > b.getBusynessNow()) {
                        lowest = b.getBusynessNow();
                        lowestName = name;
                    }
                }
            }
        }
        buildingNamesSuggestion.add(lowestName);
        for (Building b: buildings) {
            for (String name:buildingNames) {
                if (b.getName() == name) {
                    if (b.getFavorite()) {
                        if (b.getName() != lowestName) {
                            buildingNamesFavorite.add(b.getName());
                        }
                    } else {
                        if (b.getName() != lowestName) {
                            buildingNamesOthers.add(b.getName());
                        }
                    }
                }
            }
        }


    }

    public static void setMargins (View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }

    public void reload(int day, int time) {
//        LatLng position = positions.get(p);
//        cameraUpdate = CameraUpdateFactory.newLatLngZoom(position, 15.0f);
//        map.animateCamera(cameraUpdate);
        // update the view
        dateSpinner.setSelection(day);
        timeSpinner.setSelection(time);
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
            int paramLength = params.length; //storing [0] url, [1] timestamp, no other param needed
            if (paramLength == 3 || paramLength >=5) {
                Log.d("ERROR", "params length error");
            }
            if (paramLength >=2) {
                urlString = urlString + "?t=" + params[1];
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
                        String roundPercent = Integer.toString(Math.round(Float.parseFloat(percent.substring(0,percent.length()-1))));
                        bldgbusyness.put(id, roundPercent);
                        Log.d("JSON", "key =" + id + ", busyness=" + roundPercent);
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
            for (Map.Entry<String, String> entry : bldgbusyness.entrySet()) {
//                Log.d("MAPPING", bldgnames.get(entry.getKey()));
                buildingNames.add(entry.getKey());
            }
//            getBusynessAtTime(w.result);

            Log.d("bldgnames", "Buildings " + bldgnames);
            Log.d("bldgbusyness", "Buildbusyness " + bldgbusyness);
            Log.d("bldglocs", "Buildbldglocs " + bldglocs);
            Log.d("buildingNames", "Builddmanes " + buildingNames);
            Log.d("busynessTemp", "BuildTemp " + busynessTemp);
//            gb.addBldglocs(bldglocs);
//            gb.addBusynessTemp(busynessTemp);
//            gb.addBldgames(bldgnames);
//            gb.addBuildingNames(buildingNames);
//            gb.addBldgames(bldgnames);
//            if (w.numOfParams == 2)
//            if (w.numOfParams == 4)
//            buildingNames.clear();
//            for (Map.Entry<String, String> entry : bldgbusyness.entrySet())
//            {
////                Log.d("MAPPING", bldgnames.get(entry.getKey()));
//                buildingNames.add(entry.getKey());
//            }
//            gb = (GlobalVariables) getApplication();
//            gb.addBldglocs(bldglocs);
//            gb.addBusynessTemp(busynessTemp);
//            gb.addBldgames(bldgnames);
//            gb.addBuildingNames(buildingNames);
//            gb.addBldgames(bldgnames);
//
//
//
//            Intent i = new Intent(SplashActivity.this, MainActivity.class);
//
//            startActivity(i);
        }
    }
}
