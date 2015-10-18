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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private int dayOfWeek;
    private int hour;
    private int minute;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,

                             Bundle savedInstanceState) {

        // inflat and return the layout

        fragment = inflater.inflate(R.layout.fragment_map, container, false);

        mapView = (MapView) fragment.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        c = Calendar.getInstance();
        dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);

        parentActivity = getActivity();

        final LocationManager lm = (LocationManager) parentActivity.getSystemService( Context.LOCATION_SERVICE );

        Criteria c = new Criteria();
        String provider = lm.getBestProvider(c, false);
        cLocation = lm.getLastKnownLocation(provider);
        if (cLocation != null) {
            pLong = cLocation.getLongitude();
            pLat = cLocation.getLatitude();
        }
        LatLng currentLocation = new LatLng(pLat, pLong);

        MapsInitializer.initialize(parentActivity.getApplicationContext());
        map = mapView.getMap();
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.setMyLocationEnabled(true);
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentLocation, 15.0f);
        map.animateCamera(cameraUpdate);

        return fragment;

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


}
