package org.intracode.contactmanager;

/**
 * Created by pbz18_000 on 10/19/2015.
 */
import android.app.Application;
import android.provider.Settings;

import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.List;


public class GlobalVariables extends Application {

    private ArrayList<String> buildingNames = new ArrayList<String>();
    private ArrayList<Marker> markers = new ArrayList<Marker>();
    private int selected;

    public GlobalVariables() {
        buildingNames.clear();
        buildingNames.add("Culc");
        buildingNames.add("Student Center");
        buildingNames.add("Library");
        buildingNames.add("CRC");
        buildingNames.add("Klaus");
        buildingNames.add("CoC");
        selected  = 0;
    }

    public void setSelected(int i) {
        System.out.println("set" + i);
        this.selected = i;
    }

    public int getSelected() {
        return this.selected;
    }
//
//    public Integer getPercent(Integer i) {
//        return percentages.get(i);
//    }
//
//    public void insertPercent(Integer i) {
//        percentages.add(i);
//    }
//
//    public void clear() {
//        percentages.clear();
//    }
//
//    public void add(Marker m) {
//        markers.add(m);
//    }
//    public void upDate(Integer i, Integer value) {
//        markers.get(i).setSnippet(Integer.toString(value) + "%");
//    }
//
//    public ArrayList<Integer> getAll() {
//        return percentages;
//    }

}