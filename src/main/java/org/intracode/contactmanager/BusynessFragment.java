package org.intracode.contactmanager;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
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
//    private int dayOfWeek;
//    private int hour;
//    private int minute;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        hours.add("00:00 -  02:00am");
        hours.add("02:00 -  04:00am");
        hours.add("04:00 -  06:00am");
        hours.add("06:00 -  08:00am");
        hours.add("08:00 -  10:00am");
        hours.add("10:00 -  12:00am");
        hours.add("12:00 -  02:00pm");
        hours.add("02:00 -  04:00pm");
        hours.add("04:00 -  06:00pm");
        hours.add("06:00 -  08:00pm");
        hours.add("08:00 -  10:00pm");
        hours.add("10:00 -  12:00pm");
        buildingNames.clear();
        buildingNames.add("Culc");
        buildingNames.add("Student Center");
        buildingNames.add("Library");
        buildingNames.add("CRC");
        buildingNames.add("Klaus");
        buildingNames.add("CoC");
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

    public void reload(int p) {
//        spinner.setSelection(position);
//        adapter.notifyDataSetChanged();
        this.position = p;
        spinner.setSelection(position);
        floorSpinner.setAdapter(null);
        adapter.notifyDataSetChanged();
        if (position == 0) {
            floorAdapters = ArrayAdapter.createFromResource(getActivity(), R.array.culc_array, android.R.layout.simple_spinner_item);
        } else if (position == 1) {
            floorAdapters = ArrayAdapter.createFromResource(getActivity(), R.array.sc_array, android.R.layout.simple_spinner_item);
        } else if (position == 2) {
            floorAdapters = ArrayAdapter.createFromResource(getActivity(), R.array.library_array, android.R.layout.simple_spinner_item);
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

            hour = (TextView) view.findViewById(R.id.hours);
            hour.setText(buildingName);
            busyness = (TextView) view.findViewById(R.id.busynessHours);
            rand = new Random();
            int percent = rand.nextInt(41) + 40;
            busyness.setText(Integer.toString(percent) + "%");
            progress1 = (RoundCornerProgressBar) view.findViewById(R.id.progress2);
            progress1.setProgressColor(Color.parseColor("#6960ec"));
            progress1.setBackgroundColor(Color.parseColor("#e5e4e2"));
            progress1.setMax(100);
            progress1.setProgress(percent);

            return view;
        }
    }
}
