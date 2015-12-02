package org.intracode.contactmanager;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.MatrixCursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Spinner;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.intracode.contactmanager.R;
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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by pbz18_000 on 10/18/2015.
 */
public class MapFragment extends Fragment {

    private MapView mapView;
    private GoogleMap map;
    private Activity parentActivity;
    private Location cLocation;
    private double pLong, pLat;
    private LatLng currentLocation;
    private View fragment;
    private CameraUpdate cameraUpdate;
    private ActionBar actionBar;
    private Button allButton;
    private ImageButton foodButton, studyButton, recButton, favButton;
    public  ArrayList<Building> buildings = new ArrayList<Building>();


    private ArrayList<String> buildingNames = new ArrayList<String>();
    private ArrayList<LatLng> positions = new ArrayList<LatLng>();
    private ArrayList<Marker> mapMarker = new ArrayList<Marker>();
    public  Map<String, String> bldgbusyness = new HashMap<String, String>();
    public  Map<String, String> busynessTemp = new HashMap<String, String>();
    UpdateListener2 mCallback2;
    private Calendar c;
    private int dayOfWeek;
    private int currDate = 0;
    private int hour;
    private int minute;
    private int currTime = 0;
    private Marker md;
    private ArrayList<Marker> mdList = new ArrayList<Marker>();
    private Spinner dateSpinner, timeSpinner;
    private String API = "http://densit-api.appspot.com/locations";

    private boolean study, food, rec;
    private boolean first = true;
    private boolean dateS, timeS;
    private boolean check;
    private float dZoom = 15.1f;
    public interface UpdateListener2 {
        public void onDayTimeSelected2(int date, int day);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback2 = (UpdateListener2) activity;
        } catch (ClassCastException e) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // inflat and return the layout

        fragment = inflater.inflate(R.layout.fragment_map, container, false);
//        getActivity().setContentView(R.layout.fragment_map);
        setSpinnerContent(fragment);
        mapView = (MapView) fragment.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        c = Calendar.getInstance();
        dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        currTime = hour;
        currDate = dayOfWeek;

        parentActivity = getActivity();

        foodButton = (ImageButton) fragment.findViewById(R.id.buttonFood);
        studyButton = (ImageButton) fragment.findViewById(R.id.buttonStudy);
        recButton = (ImageButton) fragment.findViewById(R.id.buttonRec);

        foodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                foodButton.setImageResource(R.drawable.food_selected);

                studyButton.setImageResource(R.drawable.study);
                recButton.setImageResource(R.drawable.recreation);

                updateFoodView();

            }
        });
        studyButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                studyButton.setImageResource(R.drawable.study_selected);

                foodButton.setImageResource(R.drawable.food);
                recButton.setImageResource(R.drawable.recreation);
                updateStudyView();
            }
        });
        recButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                recButton.setImageResource(R.drawable.recreation_selected);

                foodButton.setImageResource(R.drawable.food);
                studyButton.setImageResource(R.drawable.study);
                updateRecView();
            }
        });


//        favButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                favButton.setImageResource(R.drawable.favourite_selected);
//
//                foodButton.setImageResource(R.drawable.food);
//                studyButton.setImageResource(R.drawable.study);
//                recButton.setImageResource(R.drawable.recreation);
//                updateFavView();
//
//            }
//        });




        MapsInitializer.initialize(parentActivity.getApplicationContext());
        map = mapView.getMap();
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.setMyLocationEnabled(true);
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        map.setOnInfoWindowClickListener(new WindowHandler());
        study = true;
        studyButton.setImageResource(R.drawable.study_selected);

        foodButton.setImageResource(R.drawable.food);
        recButton.setImageResource(R.drawable.recreation);
//        updateStudyView();

        String[] params = new String[2];
        params[0] = API;
        params[1] = timeToT(hour,dayOfWeek);
        Busyness busy = new Busyness();
        busy.execute(params);
        return fragment;

    }

    private void setSpinnerContent( View view ) {
        c = Calendar.getInstance();
        dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
//        System.out.println(dayOfWeek);
//        Toast.makeText(getActivity().getApplicationContext(), dayOfWeek, Toast.LENGTH_LONG).show();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        currTime = hour;
        currDate = dayOfWeek;

        timeSpinner = (Spinner) view.findViewById(R.id.time_spinner2);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapters3 = ArrayAdapter.createFromResource(getActivity(), R.array.time_array2, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapters3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        timeSpinner.setAdapter(adapters3);
        timeSpinner.setSelection(hour);
        timeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                currTime = position;
                if (!check) {
                    mCallback2.onDayTimeSelected2(dateSpinner.getSelectedItemPosition(), position);
//                    System.out.println("calling");
                    check = true;
                    String[] params = new String[2];
                    params[0] = API;
                    params[1] = timeToT(currTime,currDate);
                    timeS = true;
                    check = true;
                    Busyness busy = new Busyness();
                    busy.execute(params);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        dateSpinner = (Spinner) view.findViewById(R.id.date_spinner2);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapters4 = ArrayAdapter.createFromResource(getActivity(), R.array.day_array2, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapters4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        dateSpinner.setAdapter(adapters4);
        dateSpinner.setSelection(dayOfWeek - 1);
        dateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                currDate = position;
                if (!check) {
                    mCallback2.onDayTimeSelected2(position, timeSpinner.getSelectedItemPosition());
//                    System.out.println("calling2");
                    check = true;
                    String[] params = new String[2];
                    params[0] = API;
                    params[1] = timeToT(currTime,currDate);
                    timeS = true;
                    check = true;
                    Busyness busy = new Busyness();
                    busy.execute(params);
                }

//                displayListView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });
    }

    public void reload(int day, int time) {
//        LatLng position = positions.get(p);
//        cameraUpdate = CameraUpdateFactory.newLatLngZoom(position, 15.0f);
//        map.animateCamera(cameraUpdate);
        // update the view
//        System.out.println("check " +  check);
        dateSpinner.setSelection(day);
        timeSpinner.setSelection(time);
        if (!check) {
            if (study) {
                updateStudyView();
            } else if (food) {
                updateFoodView();
            } else if (rec) {
                updateRecView();
            }
        }
    }

    @Override
    public void onResume() {

        super.onResume();

        mapView.onResume();

    }

    @Override
    public void onPause() {

        super.onPause();

        mapView.onPause();

    }

    @Override
    public void onDestroy() {

        super.onDestroy();

        mapView.onDestroy();

    }

    @Override
    public void onLowMemory() {

        super.onLowMemory();

        mapView.onLowMemory();

    }

    public String timeToT(int hour, int day) {
        int res = (day*24+hour)*60;
        Log.d("TIME", Integer.toString(res));
        return Integer.toString(res);
    }

    private class WindowHandler implements GoogleMap.OnInfoWindowClickListener {

        @Override
        public void onInfoWindowClick(Marker marker) {
            // TODO Auto-generated method stub
            String buidlingName = marker.getTitle();
            int number = buildingNames.indexOf(buidlingName);
//            System.out.println(buidlingName + number);
            ///////////////////////////////////////////////////////////////////////////
            String name = "Something"; // please add the name of building
            String id = "ID"; // add the ID
            ///////////////////////////////////////////////////////////////////////////
//            actionBar = ((ActionBarActivity)getActivity()).getSupportActionBar();
//            actionBar.setSelectedNavigationItem(1);
        }
    }



    //Update study area
    public void updateStudyView() {
        study = true;
        food = false;
        rec = false;
        map.clear();

        ArrayList<Double> lat = new ArrayList<Double>();
        ArrayList<Double> lon = new ArrayList<Double>();
        mdList.clear();
        for (int i=0; i < buildings.size(); i++) {
            if (buildings.get(i).getStudy()) {
                lat.add(buildings.get(i).getLat());
                lon.add(buildings.get(i).getLon());
                if (buildings.get(i).getBusynessNow() < 35) {
                    if (buildings.get(i).getName().equals("Klaus")) {
                        md = map.addMarker(new MarkerOptions()
                                .position(new LatLng(buildings.get(i).getLat(), buildings.get(i).getLon()))
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.greenklaus))); // changing ICON
                    } else if (buildings.get(i).getName().equals("Student Center")) {
                        md = map.addMarker(new MarkerOptions()
                                .position(new LatLng(buildings.get(i).getLat(), buildings.get(i).getLon()))
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.greensc))); // changing ICON
                    } else if (buildings.get(i).getName().equals("College of Computing")) {
                        md = map.addMarker(new MarkerOptions()
                                .position(new LatLng(buildings.get(i).getLat(), buildings.get(i).getLon()))
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.greencoc))); // changing ICON
                    } else if (buildings.get(i).getName().equals("Clough")) {
                        md = map.addMarker(new MarkerOptions()
                                .position(new LatLng(buildings.get(i).getLat(), buildings.get(i).getLon()))
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.greenculc))); // changing ICON
                    } else if (buildings.get(i).getName().equals("Library")) {
                        md = map.addMarker(new MarkerOptions()
                                .position(new LatLng(buildings.get(i).getLat(), buildings.get(i).getLon()))
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.greenlib))); // changing ICON
                    } else if (buildings.get(i).getName().equals("College of Architecture")) {
                        md = map.addMarker(new MarkerOptions()
                                .position(new LatLng(buildings.get(i).getLat(), buildings.get(i).getLon()))
                                .title(buildings.get(i).getName())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.greenmarker))); // changing ICON
                    } else if (buildings.get(i).getName().equals("Campus Recreation Center")) {
                        md = map.addMarker(new MarkerOptions()
                                .position(new LatLng(buildings.get(i).getLat(), buildings.get(i).getLon()))
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.greencrc))); // changing ICON
                    } else {
                        md = map.addMarker(new MarkerOptions()
                                .position(new LatLng(buildings.get(i).getLat(), buildings.get(i).getLon()))
                                .title(buildings.get(i).getName())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.greenmarker))); // changing ICON
                    }
                } else if(buildings.get(i).getBusynessNow() >= 35 && buildings.get(i).getBusynessNow() < 70) {
                    if (buildings.get(i).getName().equals("Klaus")) {
                        md = map.addMarker(new MarkerOptions()
                                .position(new LatLng(buildings.get(i).getLat(), buildings.get(i).getLon()))
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.yellowklaus))); // changing ICON
                    } else if (buildings.get(i).getName().equals("Student Center")) {
                        md = map.addMarker(new MarkerOptions()
                                .position(new LatLng(buildings.get(i).getLat(), buildings.get(i).getLon()))
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.yellowsc))); // changing ICON
                    } else if (buildings.get(i).getName().equals("College of Computing")) {
                        md = map.addMarker(new MarkerOptions()
                                .position(new LatLng(buildings.get(i).getLat(), buildings.get(i).getLon()))
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.yellowcoc))); // changing ICON
                    } else if (buildings.get(i).getName().equals("Clough")) {
                        md = map.addMarker(new MarkerOptions()
                                .position(new LatLng(buildings.get(i).getLat(), buildings.get(i).getLon()))
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.yellowculc))); // changing ICON
                    } else if (buildings.get(i).getName().equals("Library")) {
                        md = map.addMarker(new MarkerOptions()
                                .position(new LatLng(buildings.get(i).getLat(), buildings.get(i).getLon()))
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.yellowlib))); // changing ICON
                    } else if (buildings.get(i).getName().equals("College of Architecture")) {
                        md = map.addMarker(new MarkerOptions()
                                .position(new LatLng(buildings.get(i).getLat(), buildings.get(i).getLon()))
                                .title(buildings.get(i).getName())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.yellowmarker))); // changing ICON
                    } else if (buildings.get(i).getName().equals("Campus Recreation Center")) {
                        md = map.addMarker(new MarkerOptions()
                                .position(new LatLng(buildings.get(i).getLat(), buildings.get(i).getLon()))
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.yellowcrc))); // changing ICON
                    } else {
                        md = map.addMarker(new MarkerOptions()
                                .position(new LatLng(buildings.get(i).getLat(), buildings.get(i).getLon()))
                                .title(buildings.get(i).getName())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.yellowmarker))); // changing ICON
                    }
                } else {
                    if (buildings.get(i).getName().equals("Klaus")) {
                        md = map.addMarker(new MarkerOptions()
                                .position(new LatLng(buildings.get(i).getLat(), buildings.get(i).getLon()))
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.redklaus))); // changing ICON
                    } else if (buildings.get(i).getName().equals("Student Center")) {
                        md = map.addMarker(new MarkerOptions()
                                .position(new LatLng(buildings.get(i).getLat(), buildings.get(i).getLon()))
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.redsc))); // changing ICON
                    } else if (buildings.get(i).getName().equals("College of Computing")) {
                        md = map.addMarker(new MarkerOptions()
                                .position(new LatLng(buildings.get(i).getLat(), buildings.get(i).getLon()))
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.redcoc))); // changing ICON
                    } else if (buildings.get(i).getName().equals("Clough")) {
                        md = map.addMarker(new MarkerOptions()
                                .position(new LatLng(buildings.get(i).getLat(), buildings.get(i).getLon()))
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.redculc))); // changing ICON
                    } else if (buildings.get(i).getName().equals("Library")) {
                        md = map.addMarker(new MarkerOptions()
                                .position(new LatLng(buildings.get(i).getLat(), buildings.get(i).getLon()))
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.redlib))); // changing ICON
                    } else if (buildings.get(i).getName().equals("College of Architecture")) {
                        md = map.addMarker(new MarkerOptions()
                                .position(new LatLng(buildings.get(i).getLat(), buildings.get(i).getLon()))
                                .title(buildings.get(i).getName())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.redmarker))); // changing ICON
                    } else if (buildings.get(i).getName().equals("Campus Recreation Center")) {
                        md = map.addMarker(new MarkerOptions()
                                .position(new LatLng(buildings.get(i).getLat(), buildings.get(i).getLon()))
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.redcrc))); // changing ICON
                    } else {
                        md = map.addMarker(new MarkerOptions()
                                .position(new LatLng(buildings.get(i).getLat(), buildings.get(i).getLon()))
                                .title(buildings.get(i).getName())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.redmarker))); // changing ICON
                    }
                }
                mdList.add(md);
            }
        }
        try {
            double newLat = (Collections.max(lat) + Collections.min(lat)) / 2;
            double newLon = (Collections.max(lon) + Collections.min(lon)) / 2;
            double maxLat = Math.abs(Collections.max(lat) - Collections.min(lat));
            double maxLon = Math.abs(Collections.max(lon) - Collections.min(lon));
            LatLng newPosition = new LatLng(newLat, newLon);
            float zoom;
            if (maxLat > maxLon) {
                zoom = (float) (dZoom + (1 - (maxLat - 0.001099) / 0.006829) * 2);
            } else {
                zoom = (float) (dZoom + (1 - (maxLon - 0.001099) / 0.006829) * 2);
            }
            cameraUpdate = CameraUpdateFactory.newLatLngZoom(newPosition, zoom);
            map.animateCamera(cameraUpdate);
        } catch (Exception e ) {
            Log.d("ERROR", "error");
        }
    }

    //Update food Area
    public void updateFoodView() {
        study = false;
        food = true;
        rec = false;
        map.clear();
        ArrayList<Double> lat = new ArrayList<Double>();
        ArrayList<Double> lon = new ArrayList<Double>();
        mdList.clear();
        for (int i=0; i < buildings.size(); i++) {
            if (buildings.get(i).getFood()) {
                lat.add(buildings.get(i).getLat());
                lon.add(buildings.get(i).getLon());
                if (buildings.get(i).getBusynessNow() < 35) {
                    if (buildings.get(i).getName().equals("Klaus")) {
                        md = map.addMarker(new MarkerOptions()
                                .position(new LatLng(buildings.get(i).getLat(), buildings.get(i).getLon()))
                                .title(buildings.get(i).getName())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.greenklaus))); // changing ICON
                    } else if (buildings.get(i).getName().equals("Student Center")) {
                        md = map.addMarker(new MarkerOptions()
                                .position(new LatLng(buildings.get(i).getLat(), buildings.get(i).getLon()))
                                .title(buildings.get(i).getName())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.greensc))); // changing ICON
                    } else if (buildings.get(i).getName().equals("College of Computing")) {
                        md = map.addMarker(new MarkerOptions()
                                .position(new LatLng(buildings.get(i).getLat(), buildings.get(i).getLon()))
                                .title(buildings.get(i).getName())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.greencoc))); // changing ICON
                    } else if (buildings.get(i).getName().equals("Clough")) {
                        md = map.addMarker(new MarkerOptions()
                                .position(new LatLng(buildings.get(i).getLat(), buildings.get(i).getLon()))
                                .title(buildings.get(i).getName())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.greenculc))); // changing ICON
                    } else if (buildings.get(i).getName().equals("Library")) {
                        md = map.addMarker(new MarkerOptions()
                                .position(new LatLng(buildings.get(i).getLat(), buildings.get(i).getLon()))
                                .title(buildings.get(i).getName())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.greenlib))); // changing ICON
                    } else if (buildings.get(i).getName().equals("College of Architecture")) {
                        md = map.addMarker(new MarkerOptions()
                                .position(new LatLng(buildings.get(i).getLat(), buildings.get(i).getLon()))
                                .title(buildings.get(i).getName())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.greenmarker))); // changing ICON
                    } else if (buildings.get(i).getName().equals("Campus Recreation Center")) {
                        md = map.addMarker(new MarkerOptions()
                                .position(new LatLng(buildings.get(i).getLat(), buildings.get(i).getLon()))
                                .title(buildings.get(i).getName())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.greencrc))); // changing ICON
                    } else {
                        md = map.addMarker(new MarkerOptions()
                                .position(new LatLng(buildings.get(i).getLat(), buildings.get(i).getLon()))
                                .title(buildings.get(i).getName())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.greenmarker))); // changing ICON
                    }
                } else if(buildings.get(i).getBusynessNow() >= 35 && buildings.get(i).getBusynessNow() < 70) {
                    if (buildings.get(i).getName().equals("Klaus")) {
                        md = map.addMarker(new MarkerOptions()
                                .position(new LatLng(buildings.get(i).getLat(), buildings.get(i).getLon()))
                                .title(buildings.get(i).getName())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.yellowklaus))); // changing ICON
                    } else if (buildings.get(i).getName().equals("Student Center")) {
                        md = map.addMarker(new MarkerOptions()
                                .position(new LatLng(buildings.get(i).getLat(), buildings.get(i).getLon()))
                                .title(buildings.get(i).getName())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.yellowsc))); // changing ICON
                    } else if (buildings.get(i).getName().equals("College of Computing")) {
                        md = map.addMarker(new MarkerOptions()
                                .position(new LatLng(buildings.get(i).getLat(), buildings.get(i).getLon()))
                                .title(buildings.get(i).getName())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.yellowcoc))); // changing ICON
                    } else if (buildings.get(i).getName().equals("Clough")) {
                        md = map.addMarker(new MarkerOptions()
                                .position(new LatLng(buildings.get(i).getLat(), buildings.get(i).getLon()))
                                .title(buildings.get(i).getName())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.yellowculc))); // changing ICON
                    } else if (buildings.get(i).getName().equals("Library")) {
                        md = map.addMarker(new MarkerOptions()
                                .position(new LatLng(buildings.get(i).getLat(), buildings.get(i).getLon()))
                                .title(buildings.get(i).getName())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.yellowlib))); // changing ICON
                    } else if (buildings.get(i).getName().equals("College of Architecture")) {
                        md = map.addMarker(new MarkerOptions()
                                .position(new LatLng(buildings.get(i).getLat(), buildings.get(i).getLon()))
                                .title(buildings.get(i).getName())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.yellowmarker))); // changing ICON
                    } else if (buildings.get(i).getName().equals("Campus Recreation Center")) {
                        md = map.addMarker(new MarkerOptions()
                                .position(new LatLng(buildings.get(i).getLat(), buildings.get(i).getLon()))
                                .title(buildings.get(i).getName())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.yellowcrc))); // changing ICON
                    } else {
                        md = map.addMarker(new MarkerOptions()
                                .position(new LatLng(buildings.get(i).getLat(), buildings.get(i).getLon()))
                                .title(buildings.get(i).getName())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.yellowmarker))); // changing ICON
                    }
                } else {
                    if (buildings.get(i).getName().equals("Klaus")) {
                        md = map.addMarker(new MarkerOptions()
                                .position(new LatLng(buildings.get(i).getLat(), buildings.get(i).getLon()))
                                .title(buildings.get(i).getName())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.redklaus))); // changing ICON
                    } else if (buildings.get(i).getName().equals("Student Center")) {
                        md = map.addMarker(new MarkerOptions()
                                .position(new LatLng(buildings.get(i).getLat(), buildings.get(i).getLon()))
                                .title(buildings.get(i).getName())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.redsc))); // changing ICON
                    } else if (buildings.get(i).getName().equals("College of Computing")) {
                        md = map.addMarker(new MarkerOptions()
                                .position(new LatLng(buildings.get(i).getLat(), buildings.get(i).getLon()))
                                .title(buildings.get(i).getName())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.redcoc))); // changing ICON
                    } else if (buildings.get(i).getName().equals("Clough")) {
                        md = map.addMarker(new MarkerOptions()
                                .position(new LatLng(buildings.get(i).getLat(), buildings.get(i).getLon()))
                                .title(buildings.get(i).getName())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.redculc))); // changing ICON
                    } else if (buildings.get(i).getName().equals("Library")) {
                        md = map.addMarker(new MarkerOptions()
                                .position(new LatLng(buildings.get(i).getLat(), buildings.get(i).getLon()))
                                .title(buildings.get(i).getName())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.redlib))); // changing ICON
                    } else if (buildings.get(i).getName().equals("College of Architecture")) {
                        md = map.addMarker(new MarkerOptions()
                                .position(new LatLng(buildings.get(i).getLat(), buildings.get(i).getLon()))
                                .title(buildings.get(i).getName())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.redmarker))); // changing ICON
                    } else if (buildings.get(i).getName().equals("Campus Recreation Center")) {
                        md = map.addMarker(new MarkerOptions()
                                .position(new LatLng(buildings.get(i).getLat(), buildings.get(i).getLon()))
                                .title(buildings.get(i).getName())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.redcrc))); // changing ICON
                    } else {
                        md = map.addMarker(new MarkerOptions()
                                .position(new LatLng(buildings.get(i).getLat(), buildings.get(i).getLon()))
                                .title(buildings.get(i).getName())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.redmarker))); // changing ICON
                    }
                }
                mdList.add(md);
            }
        }
        double newLat = (Collections.max(lat) + Collections.min(lat))/2;
        double newLon = (Collections.max(lon) + Collections.min(lon))/2;
        double maxLat = Math.abs(Collections.max(lat) - Collections.min(lat));
        double maxLon = Math.abs(Collections.max(lon) - Collections.min(lon));
        LatLng newPosition = new LatLng(newLat,newLon);
        float zoom;
        if (maxLat > maxLon) {
            zoom = (float) (dZoom + (1- (maxLat-0.001099)/0.006829)*2);
        } else {
            zoom = (float) (dZoom + (1 - (maxLon - 0.001099) / 0.006829) * 2);
        }
        cameraUpdate = CameraUpdateFactory.newLatLngZoom(newPosition, zoom);
        map.animateCamera(cameraUpdate);
    }

    //Update Recreation Area
    public void updateRecView() {
        study = false;
        food = false;
        rec = true;
        map.clear();
        ArrayList<Double> lat = new ArrayList<Double>();
        ArrayList<Double> lon = new ArrayList<Double>();
        mdList.clear();
        for (int i=0; i < buildings.size(); i++) {
            if (buildings.get(i).getRec()) {
                lat.add(buildings.get(i).getLat());
                lon.add(buildings.get(i).getLon());
                if (buildings.get(i).getBusynessNow() < 35) {
                    if (buildings.get(i).getName().equals("Klaus")) {
                        md = map.addMarker(new MarkerOptions()
                                .position(new LatLng(buildings.get(i).getLat(), buildings.get(i).getLon()))
                                .title(buildings.get(i).getName())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.greenklaus))); // changing ICON
                    } else if (buildings.get(i).getName().equals("Student Center")) {
                        md = map.addMarker(new MarkerOptions()
                                .position(new LatLng(buildings.get(i).getLat(), buildings.get(i).getLon()))
                                .title(buildings.get(i).getName())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.greensc))); // changing ICON
                    } else if (buildings.get(i).getName().equals("College of Computing")) {
                        md = map.addMarker(new MarkerOptions()
                                .position(new LatLng(buildings.get(i).getLat(), buildings.get(i).getLon()))
                                .title(buildings.get(i).getName())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.greencoc))); // changing ICON
                    } else if (buildings.get(i).getName().equals("Clough")) {
                        md = map.addMarker(new MarkerOptions()
                                .position(new LatLng(buildings.get(i).getLat(), buildings.get(i).getLon()))
                                .title(buildings.get(i).getName())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.greenculc))); // changing ICON
                    } else if (buildings.get(i).getName().equals("Library")) {
                        md = map.addMarker(new MarkerOptions()
                                .position(new LatLng(buildings.get(i).getLat(), buildings.get(i).getLon()))
                                .title(buildings.get(i).getName())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.greenlib))); // changing ICON
                    } else if (buildings.get(i).getName().equals("College of Architecture")) {
                        md = map.addMarker(new MarkerOptions()
                                .position(new LatLng(buildings.get(i).getLat(), buildings.get(i).getLon()))
                                .title(buildings.get(i).getName())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.greenmarker))); // changing ICON
                    } else if (buildings.get(i).getName().equals("Campus Recreation Center")) {
                        md = map.addMarker(new MarkerOptions()
                                .position(new LatLng(buildings.get(i).getLat(), buildings.get(i).getLon()))
                                .title(buildings.get(i).getName())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.greencrc))); // changing ICON
                    } else {
                        md = map.addMarker(new MarkerOptions()
                                .position(new LatLng(buildings.get(i).getLat(), buildings.get(i).getLon()))
                                .title(buildings.get(i).getName())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.greenmarker))); // changing ICON
                    }
                } else if(buildings.get(i).getBusynessNow() >= 35 && buildings.get(i).getBusynessNow() < 70) {
                    if (buildings.get(i).getName().equals("Klaus")) {
                        md = map.addMarker(new MarkerOptions()
                                .position(new LatLng(buildings.get(i).getLat(), buildings.get(i).getLon()))
                                .title(buildings.get(i).getName())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.yellowklaus))); // changing ICON
                    } else if (buildings.get(i).getName().equals("Student Center")) {
                        md = map.addMarker(new MarkerOptions()
                                .position(new LatLng(buildings.get(i).getLat(), buildings.get(i).getLon()))
                                .title(buildings.get(i).getName())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.yellowsc))); // changing ICON
                    } else if (buildings.get(i).getName().equals("College of Computing")) {
                        md = map.addMarker(new MarkerOptions()
                                .position(new LatLng(buildings.get(i).getLat(), buildings.get(i).getLon()))
                                .title(buildings.get(i).getName())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.yellowcoc))); // changing ICON
                    } else if (buildings.get(i).getName().equals("Clough")) {
                        md = map.addMarker(new MarkerOptions()
                                .position(new LatLng(buildings.get(i).getLat(), buildings.get(i).getLon()))
                                .title(buildings.get(i).getName())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.yellowculc))); // changing ICON
                    } else if (buildings.get(i).getName().equals("Library")) {
                        md = map.addMarker(new MarkerOptions()
                                .position(new LatLng(buildings.get(i).getLat(), buildings.get(i).getLon()))
                                .title(buildings.get(i).getName())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.yellowlib))); // changing ICON
                    } else if (buildings.get(i).getName().equals("College of Architecture")) {
                        md = map.addMarker(new MarkerOptions()
                                .position(new LatLng(buildings.get(i).getLat(), buildings.get(i).getLon()))
                                .title(buildings.get(i).getName())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.yellowmarker))); // changing ICON
                    } else if (buildings.get(i).getName().equals("Campus Recreation Center")) {
                        md = map.addMarker(new MarkerOptions()
                                .position(new LatLng(buildings.get(i).getLat(), buildings.get(i).getLon()))
                                .title(buildings.get(i).getName())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.yellowcrc))); // changing ICON
                    } else {
                        md = map.addMarker(new MarkerOptions()
                                .position(new LatLng(buildings.get(i).getLat(), buildings.get(i).getLon()))
                                .title(buildings.get(i).getName())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.yellowmarker))); // changing ICON
                    }
                } else {
                    if (buildings.get(i).getName().equals("Klaus")) {
                        md = map.addMarker(new MarkerOptions()
                                .position(new LatLng(buildings.get(i).getLat(), buildings.get(i).getLon()))
                                .title(buildings.get(i).getName())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.redklaus))); // changing ICON
                    } else if (buildings.get(i).getName().equals("Student Center")) {
                        md = map.addMarker(new MarkerOptions()
                                .position(new LatLng(buildings.get(i).getLat(), buildings.get(i).getLon()))
                                .title(buildings.get(i).getName())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.redsc))); // changing ICON
                    } else if (buildings.get(i).getName().equals("College of Computing")) {
                        md = map.addMarker(new MarkerOptions()
                                .position(new LatLng(buildings.get(i).getLat(), buildings.get(i).getLon()))
                                .title(buildings.get(i).getName())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.redcoc))); // changing ICON
                    } else if (buildings.get(i).getName().equals("Clough")) {
                        md = map.addMarker(new MarkerOptions()
                                .position(new LatLng(buildings.get(i).getLat(), buildings.get(i).getLon()))
                                .title(buildings.get(i).getName())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.redculc))); // changing ICON
                    } else if (buildings.get(i).getName().equals("Library")) {
                        md = map.addMarker(new MarkerOptions()
                                .position(new LatLng(buildings.get(i).getLat(), buildings.get(i).getLon()))
                                .title(buildings.get(i).getName())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.redlib))); // changing ICON
                    } else if (buildings.get(i).getName().equals("College of Architecture")) {
                        md = map.addMarker(new MarkerOptions()
                                .position(new LatLng(buildings.get(i).getLat(), buildings.get(i).getLon()))
                                .title(buildings.get(i).getName())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.redmarker))); // changing ICON
                    } else if (buildings.get(i).getName().equals("Campus Recreation Center")) {
                        md = map.addMarker(new MarkerOptions()
                                .position(new LatLng(buildings.get(i).getLat(), buildings.get(i).getLon()))
                                .title(buildings.get(i).getName())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.redcrc))); // changing ICON
                    } else {
                        md = map.addMarker(new MarkerOptions()
                                .position(new LatLng(buildings.get(i).getLat(), buildings.get(i).getLon()))
                                .title(buildings.get(i).getName())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.redmarker))); // changing ICON
                    }
                }
                mdList.add(md);
            }
        }

        double newLat = (Collections.max(lat) + Collections.min(lat))/2;
        double newLon = (Collections.max(lon) + Collections.min(lon))/2;
        double maxLat = Math.abs(Collections.max(lat) - Collections.min(lat));
        double maxLon = Math.abs(Collections.max(lon) - Collections.min(lon));
        LatLng newPosition = new LatLng(newLat,newLon);
        float zoom;
        if (maxLat > maxLon) {
            zoom = (float) (dZoom + (1- (maxLat-0.001099)/0.006829)*2);
        } else {
            zoom = (float) (dZoom + (1- (maxLon-0.001099)/0.006829)*2);
        }
        cameraUpdate = CameraUpdateFactory.newLatLngZoom(newPosition, zoom);
        map.animateCamera(cameraUpdate);
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
                buildings.clear();
                for(int i = 0 ; i < bldgs.length() ; i++){
                    String value = "0";
                    String name = bldgs.getJSONObject(i).getString("name");
                    String id = bldgs.getJSONObject(i).getString("id");
                    JSONObject geo = bldgs.getJSONObject(i).getJSONObject("geocode");
                    String lat = geo.getString("lat");
                    String lon = geo.getString("long");
                    Building b = new Building(id,name,Double.parseDouble(lat),Double.parseDouble(lon),0,false,false,false,false);
                    JSONArray types = bldgs.getJSONObject(i).getJSONArray("types");
                    String[] ts = types.toString().split("\\W");
                    for (String t:ts) {
                        if (t.equals("eat")) b.setFood(true);
                        if (t.equals("study")) b.setStudy(true);
                        if (t.equals("recreation")) b.setRec(true);
                    }
//                    Log.d("TYPE", ts[ts.length-1]);
//                    Log.d("GEO", lat + ", " + lon);
                    if (bldgs.getJSONObject(i).has("busyness")) {
                        JSONObject keys = bldgs.getJSONObject(i).getJSONObject("busyness");
                        value = keys.toString();


//                        bldgnames.put(id, name);

                        String[] tokens = value.split(":");
                        String percent = tokens[tokens.length - 1];
//                        Log.d("JSON", "key =" + id + "busyness" + percent);
                        int roundPercent = Math.round(Float.parseFloat(percent.substring(0, percent.length() - 1)));
//                        bldgbusyness.put(id, roundPercent);
                        b.setBusyneesNow(roundPercent);
                        Log.d("JSON", "key =" + id + ", busyness=" + roundPercent);
//                    } else {
//                        bldgnames.put(id, name);
//                        bldgbusyness.put(id, "0");
                    }
//                    bldglocs.put(id, geo.toString());
//                    Log.d("JSON", "key =" + id + "name" + name);
//                    Log.d("JSON", "key =" + id + "geo" + geo.toString());
                    buildings.add(b);

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

            getLocations(w.result);
//            System.out.println("result" + w.result);
            for (Building b: buildings) {
                Log.d("BLDG", b.getName()+ "  " + b.getBusynessNow());
                Log.d("Hey", b.getLat() + "  " +  b.getLon());
            }
            if (first) {
                studyButton.performClick();
                first = false;
            } else {
                if (timeS) {
                    if (study) {
                        study = false;
                        studyButton.performClick();
                    } else if (food) {
                        food = false;
                        foodButton.performClick();
                    } else if (rec) {
                        rec = false;
                        recButton.performClick();
                    }
                    timeS = false;
                } else if (dateS) {
                    if (study) {
                        study = false;
                        studyButton.performClick();
                    } else if (food) {
                        food = false;
                        foodButton.performClick();
                    } else if (rec) {
                        rec = false;
                        recButton.performClick();
                    }
                    dateS = false;
                }
            }
            check = false;
        }
    }
}
