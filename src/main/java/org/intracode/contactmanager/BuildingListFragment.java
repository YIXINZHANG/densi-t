package org.intracode.contactmanager;

import android.app.ListFragment;
import android.content.Context;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class BuildingListFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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

        //Array list of countries
        List<String> urlList = new ArrayList<String>();
        urlList.add("http://www.google.com");
        urlList.add("http://mail.google.com");
        urlList.add("http://maps.google.com");

        //create an ArrayAdaptar from the String Array
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.listview_item, urlList);
        ListView listView = (ListView) getView().findViewById(R.id.listview);
        listView.setAdapter(dataAdapter);
        listView.setTextFilterEnabled(true);

    }
}
