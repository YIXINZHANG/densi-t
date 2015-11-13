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
import android.widget.ImageButton;
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

import org.intracode.contactmanager.R;

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
    private Button allButton;
    private ImageButton foodButton, studyButton, recButton, favButton;
    public  ArrayList<Building> buildings = new ArrayList<Building>();


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

        allButton = (Button) fragment.findViewById(R.id.buttonAll);
        foodButton = (ImageButton) fragment.findViewById(R.id.buttonFood);
        studyButton = (ImageButton) fragment.findViewById(R.id.buttonStudy);
        recButton = (ImageButton) fragment.findViewById(R.id.buttonRec);
        favButton = (ImageButton) fragment.findViewById(R.id.buttonFavorite);

        allButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                foodButton.setImageResource(R.drawable.food);
                studyButton.setImageResource(R.drawable.study);
                recButton.setImageResource(R.drawable.recreation);
                favButton.setImageResource(R.drawable.favourite);
                updateAllView();

            }
        });
        foodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                foodButton.setImageResource(R.drawable.food_selected);

                studyButton.setImageResource(R.drawable.study);
                recButton.setImageResource(R.drawable.recreation);
                favButton.setImageResource(R.drawable.favourite);
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
                favButton.setImageResource(R.drawable.favourite);
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
                favButton.setImageResource(R.drawable.favourite);
                updateRecView();
            }
        });
        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                favButton.setImageResource(R.drawable.favourite_selected);

                foodButton.setImageResource(R.drawable.food);
                studyButton.setImageResource(R.drawable.study);
                recButton.setImageResource(R.drawable.recreation);
                updateFavView();

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
            if (buildings.get(i).getBusynessNow() < 35) {
                Marker md = map.addMarker(new MarkerOptions()
                        .position(positions.get(i))
                        .title(buildingNames.get(i))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.notbusy))); // changing ICON
            } else if(buildings.get(i).getBusynessNow() >= 35 && buildings.get(i).getBusynessNow() < 70) {
                Marker md = map.addMarker(new MarkerOptions()
                        .position(positions.get(i))
                        .title(buildingNames.get(i))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.lessbusy))); // changing ICON
            } else {
                Marker md = map.addMarker(new MarkerOptions()
                        .position(positions.get(i))
                        .title(buildingNames.get(i))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.busy))); // changing ICON
            }
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

            if (buildings.get(i).getBusynessNow() < 35) {
                Marker md = map.addMarker(new MarkerOptions()
                        .position(positions.get(i))
                        .title(buildingNames.get(i))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.notbusy))); // changing ICON
            } else if(buildings.get(i).getBusynessNow() >= 35 && buildings.get(i).getBusynessNow() < 70) {
                Marker md = map.addMarker(new MarkerOptions()
                        .position(positions.get(i))
                        .title(buildingNames.get(i))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.lessbusy))); // changing ICON
            } else {
                Marker md = map.addMarker(new MarkerOptions()
                        .position(positions.get(i))
                        .title(buildingNames.get(i))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.busy))); // changing ICON
            }
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
        for (int i=0; i < buildingNames.size(); i++) {
            if (i != 3) {
                lat.add(positions.get(i).latitude);
                lon.add(positions.get(i).longitude);
                if (buildings.get(i).getBusynessNow() < 35) {
                    Marker md = map.addMarker(new MarkerOptions()
                            .position(positions.get(i))
                            .title(buildingNames.get(i))
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.notbusy))); // changing ICON
                } else if(buildings.get(i).getBusynessNow() >= 35 && buildings.get(i).getBusynessNow() < 70) {
                    Marker md = map.addMarker(new MarkerOptions()
                            .position(positions.get(i))
                            .title(buildingNames.get(i))
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.lessbusy))); // changing ICON
                } else {
                    Marker md = map.addMarker(new MarkerOptions()
                            .position(positions.get(i))
                            .title(buildingNames.get(i))
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.busy))); // changing ICON
                }
            }
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
        for (int i=0; i < buildingNames.size(); i++) {
            if (i == 1 || i == 3 || i == 5) {
                lat.add(positions.get(i).latitude);
                lon.add(positions.get(i).longitude);
                if (buildings.get(i).getBusynessNow() < 35) {
                    Marker md = map.addMarker(new MarkerOptions()
                            .position(positions.get(i))
                            .title(buildingNames.get(i))
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.notbusy))); // changing ICON
                } else if(buildings.get(i).getBusynessNow() >= 35 && buildings.get(i).getBusynessNow() < 70) {
                    Marker md = map.addMarker(new MarkerOptions()
                            .position(positions.get(i))
                            .title(buildingNames.get(i))
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.lessbusy))); // changing ICON
                } else {
                    Marker md = map.addMarker(new MarkerOptions()
                            .position(positions.get(i))
                            .title(buildingNames.get(i))
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.busy))); // changing ICON
                }
            }
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
        for (int i=0; i < buildingNames.size(); i++) {
            if (i == 1 || i == 3) {
                lat.add(positions.get(i).latitude);
                lon.add(positions.get(i).longitude);
                if (buildings.get(i).getBusynessNow() < 35) {
                    Marker md = map.addMarker(new MarkerOptions()
                            .position(positions.get(i))
                            .title(buildingNames.get(i))
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.notbusy))); // changing ICON
                } else if(buildings.get(i).getBusynessNow() >= 35 && buildings.get(i).getBusynessNow() < 70) {
                    Marker md = map.addMarker(new MarkerOptions()
                            .position(positions.get(i))
                            .title(buildingNames.get(i))
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.lessbusy))); // changing ICON
                } else {
                    Marker md = map.addMarker(new MarkerOptions()
                            .position(positions.get(i))
                            .title(buildingNames.get(i))
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.busy))); // changing ICON
                }
            }
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

    //Update Recreation Area
    public void updateFavView() {
        map.clear();
        ArrayList<Double> lat = new ArrayList<Double>();
        ArrayList<Double> lon = new ArrayList<Double>();
        for (int i=0; i < buildingNames.size(); i++) {
            if (i == 0 || i == 1 || i == 3) {
                lat.add(positions.get(i).latitude);
                lon.add(positions.get(i).longitude);
                if (buildings.get(i).getBusynessNow() < 35) {
                    Marker md = map.addMarker(new MarkerOptions()
                            .position(positions.get(i))
                            .title(buildingNames.get(i))
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.notbusy))); // changing ICON
                } else if(buildings.get(i).getBusynessNow() >= 35 && buildings.get(i).getBusynessNow() < 70) {
                    Marker md = map.addMarker(new MarkerOptions()
                            .position(positions.get(i))
                            .title(buildingNames.get(i))
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.lessbusy))); // changing ICON
                } else {
                    Marker md = map.addMarker(new MarkerOptions()
                            .position(positions.get(i))
                            .title(buildingNames.get(i))
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.busy))); // changing ICON
                }
            }
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
