package org.intracode.contactmanager;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.MatrixCursor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

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
    private Calendar c;
    private ActionBar actionBar;
    private Button allButton, foodButton, studyButton, recButton;


    private ArrayList<String> buildingNames = new ArrayList<String>();
    private ArrayList<LatLng> positions = new ArrayList<LatLng>();
    private ArrayList<Marker> mapMarker = new ArrayList<Marker>();
    UpdateListener2 mCallback2;

    public interface UpdateListener2 {
        public void onArticleSelected2(int position, String id, String name);
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

        mapView = (MapView) fragment.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

//        c = Calendar.getInstance();
//        dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
//        hour = c.get(Calendar.HOUR_OF_DAY);
//        minute = c.get(Calendar.MINUTE);

        parentActivity = getActivity();
        buildingNames.add("Culc");
        positions.add(new LatLng(33.774599, -84.396372));

        buildingNames.add("Student Center");
        positions.add(new LatLng(33.774028, -84.398818));

        buildingNames.add("Library");
        positions.add(new LatLng(33.774327, -84.395825));

        buildingNames.add("CRC");
        positions.add(new LatLng(33.77562, -84.403753));

        buildingNames.add("Klaus");
        positions.add(new LatLng(33.777212,-84.396281));

        buildingNames.add("CoC");
        positions.add(new LatLng(33.777386,-84.39738));

        allButton = (Button) fragment.findViewById(R.id.buttonAll);
        foodButton = (Button) fragment.findViewById(R.id.buttonFood);
        studyButton = (Button) fragment.findViewById(R.id.buttonStudy);
        recButton = (Button) fragment.findViewById(R.id.buttonRec);

        allButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateAllView();

            }
        });
        foodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateFoodView();

            }
        });
        studyButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                updateStudyView();
            }
        });
        recButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                updateRecView();
            }
        });

        MapsInitializer.initialize(parentActivity.getApplicationContext());
        map = mapView.getMap();
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.setMyLocationEnabled(true);
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        ArrayList<Double> lat = new ArrayList<Double>();
        ArrayList<Double> lon = new ArrayList<Double>();
        for (int i=0; i < positions.size(); i++) {
            lat.add(positions.get(i).latitude);
            lon.add(positions.get(i).longitude);
        }
        double newLat = (Collections.max(lat) + Collections.min(lat))/2;
        double newLon = (Collections.max(lon) + Collections.min(lon))/2;
        LatLng newPosition = new LatLng(newLat,newLon);
        cameraUpdate = CameraUpdateFactory.newLatLngZoom(newPosition, 15.5f);
        map.animateCamera(cameraUpdate);

        for (int i=0; i < positions.size(); i++) {
//            map.addMarker(new MarkerOptions().position(positions.get(i))
//                            .title(buildingNames.get(i)));
            ////
            ///
            Marker md = map.addMarker(new MarkerOptions()
                    .position(positions.get(i))
                    .title(buildingNames.get(i))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.busyness))); // changing ICON
        }
        map.setOnInfoWindowClickListener(new WindowHandler());
        return fragment;

    }

    public void reload(int p, String id, String name) {
        LatLng position = positions.get(p);
        cameraUpdate = CameraUpdateFactory.newLatLngZoom(position, 15.0f);
        map.animateCamera(cameraUpdate);
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

    private class WindowHandler implements GoogleMap.OnInfoWindowClickListener {

        @Override
        public void onInfoWindowClick(Marker marker) {
            // TODO Auto-generated method stub
            String buidlingName = marker.getTitle();
            int number = buildingNames.indexOf(buidlingName);
            System.out.println(buidlingName + number);
            ///////////////////////////////////////////////////////////////////////////
            String name = "Something"; // please add the name of building
            String id = "ID"; // add the ID
            ///////////////////////////////////////////////////////////////////////////
            mCallback2.onArticleSelected2(number, id, name);
            actionBar = ((ActionBarActivity)getActivity()).getSupportActionBar();
            actionBar.setSelectedNavigationItem(1);
        }
    }


    public void updateAllView() {
        map.clear();
        ArrayList<Double> lat = new ArrayList<Double>();
        ArrayList<Double> lon = new ArrayList<Double>();

        for (int i=0; i < positions.size(); i++) {
            lat.add(positions.get(i).latitude);
            lon.add(positions.get(i).longitude);

            map.addMarker(new MarkerOptions()
                    .position(positions.get(i))
                    .title(buildingNames.get(i))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.busyness)));
        }
        double newLat = (Collections.max(lat) + Collections.min(lat))/2;
        double newLon = (Collections.max(lon) + Collections.min(lon))/2;
        double maxLat = Math.abs(Collections.max(lat) - Collections.min(lat));
        double maxLon = Math.abs(Collections.max(lon) - Collections.min(lon));
        LatLng newPosition = new LatLng(newLat,newLon);
        float zoom;
        if (maxLat > maxLon) {
            zoom = (float) (15.5 + (1- (maxLat-0.001099)/0.006829)*2);
        } else {
            zoom = (float) (15.5 + (1- (maxLon-0.001099)/0.006829)*2);
        }
        cameraUpdate = CameraUpdateFactory.newLatLngZoom(newPosition, zoom);
        map.animateCamera(cameraUpdate);
    }


    //Update study area
    public void updateStudyView() {
        map.clear();
        ArrayList<Double> lat = new ArrayList<Double>();
        ArrayList<Double> lon = new ArrayList<Double>();
        for (int i=0; i < 2; i++) {
            lat.add(positions.get(i).latitude);
            lon.add(positions.get(i).longitude);
            map.addMarker(new MarkerOptions()
                    .position(positions.get(i))
                    .title(buildingNames.get(i))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.busyness)));
        }
        double newLat = (Collections.max(lat) + Collections.min(lat))/2;
        double newLon = (Collections.max(lon) + Collections.min(lon))/2;
        double maxLat = Math.abs(Collections.max(lat) - Collections.min(lat));
        double maxLon = Math.abs(Collections.max(lon) - Collections.min(lon));
        LatLng newPosition = new LatLng(newLat,newLon);
        float zoom;
        if (maxLat > maxLon) {
            System.out.println("Max " + maxLat);
            zoom = (float) (15.5 + (1- (maxLat-0.001099)/0.006829)*2);
        } else {
            zoom = (float) (15.5 + (1- (maxLon-0.001099)/0.006829)*2);
        }
        cameraUpdate = CameraUpdateFactory.newLatLngZoom(newPosition, zoom);
        map.animateCamera(cameraUpdate);
    }

    //Update food Area
    public void updateFoodView() {
        map.clear();
        ArrayList<Double> lat = new ArrayList<Double>();
        ArrayList<Double> lon = new ArrayList<Double>();
        for (int i=2; i < 4; i++) {
            lat.add(positions.get(i).latitude);
            lon.add(positions.get(i).longitude);
            map.addMarker(new MarkerOptions()
                    .position(positions.get(i))
                    .title(buildingNames.get(i))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.busyness)));
        }
        double newLat = (Collections.max(lat) + Collections.min(lat))/2;
        double newLon = (Collections.max(lon) + Collections.min(lon))/2;
        double maxLat = Math.abs(Collections.max(lat) - Collections.min(lat));
        double maxLon = Math.abs(Collections.max(lon) - Collections.min(lon));
        LatLng newPosition = new LatLng(newLat,newLon);
        float zoom;
        if (maxLat > maxLon) {
            zoom = (float) (15.5 + (1- (maxLat-0.001099)/0.006829)*2);
        } else {
            zoom = (float) (15.5 + (1 - (maxLon - 0.001099) / 0.006829) * 2);
        }
        cameraUpdate = CameraUpdateFactory.newLatLngZoom(newPosition, zoom);
        map.animateCamera(cameraUpdate);
    }

    //Update Recreation Area
    public void updateRecView() {
        map.clear();
        ArrayList<Double> lat = new ArrayList<Double>();
        ArrayList<Double> lon = new ArrayList<Double>();
        for (int i=4; i < 6; i++) {
            lat.add(positions.get(i).latitude);
            lon.add(positions.get(i).longitude);
            map.addMarker(new MarkerOptions()
                    .position(positions.get(i))
                    .title(buildingNames.get(i))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.busyness)));
        }
        double newLat = (Collections.max(lat) + Collections.min(lat))/2;
        double newLon = (Collections.max(lon) + Collections.min(lon))/2;
        double maxLat = Math.abs(Collections.max(lat) - Collections.min(lat));
        double maxLon = Math.abs(Collections.max(lon) - Collections.min(lon));
        LatLng newPosition = new LatLng(newLat,newLon);
        float zoom;
        if (maxLat > maxLon) {
            zoom = (float) (15.5 + (1- (maxLat-0.001099)/0.006829)*2);
        } else {
            zoom = (float) (15.5 + (1- (maxLon-0.001099)/0.006829)*2);
        }
        cameraUpdate = CameraUpdateFactory.newLatLngZoom(newPosition, zoom);
        map.animateCamera(cameraUpdate);
    }
}
