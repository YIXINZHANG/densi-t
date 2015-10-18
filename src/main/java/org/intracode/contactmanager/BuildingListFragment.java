package org.intracode.contactmanager;

import android.app.ListFragment;
import android.content.Context;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BuildingListFragment extends Fragment {

    private List<String> urlList = new ArrayList<String>();
    private ArrayList<String> buildingNames = new ArrayList<String>();
    private ArrayList<LatLng> positions = new ArrayList<LatLng>();
    private TextView busyness;
    private TextView name;
    private Random rand;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        buildingNames.add("Culc");
        buildingNames.add("Student Center");
        buildingNames.add("Library");
        buildingNames.add("CRC");
        buildingNames.add("Klaus");
        buildingNames.add("CoC");

        Spinner spinner = (Spinner) getActivity().findViewById(R.id.time_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapters = ArrayAdapter.createFromResource(getActivity(), R.array.time_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapters.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapters);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                //Update the time period
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });
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

        //create an ArrayAdaptar from the String Array
        adapter = new ContactListAdapter(getActivity(), R.layout.listview_item, buildingNames);
//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.listview_item, urlList);
        listView = (ListView) getView().findViewById(R.id.listview);
        listView.setAdapter(adapter);
        listView.setTextFilterEnabled(true);

    }

    private class ContactListAdapter extends ArrayAdapter<String> {
        public ContactListAdapter(Context c, int r, List<String> o) {
            super(c, r, o);
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            if (view == null)
                view = getActivity().getLayoutInflater().inflate(R.layout.listview_item, parent, false);

            String buildingName = buildingNames.get(position);

            name = (TextView) view.findViewById(R.id.buildingName);
            name.setText(buildingName);
            busyness = (TextView) view.findViewById(R.id.busyness);
            rand = new Random();
            int percent = rand.nextInt(101);
            busyness.setText(Integer.toString(percent) + "%");

            return view;
        }
    }
}
