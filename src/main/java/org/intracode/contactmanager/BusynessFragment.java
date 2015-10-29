package org.intracode.contactmanager;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
    private TextView busyness;
    private TextView hour;
    private Random rand;
    private ListView listView;
    private ArrayAdapter<String> adapter;

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
        hours.add("00:00-02:00am");
        hours.add("02:00-04:00am");
        hours.add("04:00-06:00am");
        hours.add("06:00-08:00am");
        hours.add("08:00-10:00am");
        hours.add("10:00-12:00am");
        hours.add("12:00-02:00pm");
        hours.add("02:00-04:00pm");
        hours.add("04:00-06:00pm");
        hours.add("06:00-08:00pm");
        hours.add("08:00-10:00pm");
        hours.add("10:00-12:00pm");

        c = Calendar.getInstance();
//        dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
//        hour = c.get(Calendar.HOUR_OF_DAY);
//        minute = c.get(Calendar.MINUTE);
        displayListView();

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

            return view;
        }
    }
}
