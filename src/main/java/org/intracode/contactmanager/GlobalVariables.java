package org.intracode.contactmanager;

/**
 * Created by pbz18_000 on 10/19/2015.
 */
import android.app.Application;
import android.provider.Settings;
import android.util.Log;

import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GlobalVariables extends Application {

    private  Map<String, String> bldgnames = new HashMap<String, String>();
    private  Map<String, String> bldglocs = new HashMap<String, String>();
    private  Map<String, String> bldgbusyness = new HashMap<String, String>();
    private  Map<String, String> busynessTemp = new HashMap<String, String>();
    private ArrayList<String> buildingNames = new ArrayList<String>();

    public GlobalVariables() {

    }
    public void addBldgames (Map<String, String> bldgames) {
        this.bldgnames.putAll(bldgames);
        System.out.println(this.bldgnames);
    }

    public void addBldglocs (Map<String, String> bldglocs) {
        this.bldglocs.putAll(bldglocs);
    }

    public void addBldgbusyness (Map<String, String> bldgbusyness) {
        this.bldgbusyness.putAll(bldgbusyness);
    }

    public void addBusynessTemp (Map<String, String> bldgbusyness) {
        this.bldgbusyness.putAll(bldgbusyness);
    }

    public void addBuildingNames (ArrayList<String> buildingNames) {
        this.buildingNames = new ArrayList<String>(buildingNames);
    }

    public Map<String, String> getBldgnames () {
        return this.bldgnames;
    }

    public Map<String, String> getBldgbusyness() {
        return this.bldgbusyness;
    }

    public Map<String, String> getBusynessTemp() {
        return this.busynessTemp;
    }

    public Map<String, String> getBldglocs() {
        return this.bldglocs;
    }

    public ArrayList<String> getBuildingNames() {
        return this.buildingNames;
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