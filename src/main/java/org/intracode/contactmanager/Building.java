package org.intracode.contactmanager;

/**
 * Created by pbz18_000 on 11/12/2015.
 */
public class Building {

    private String name;
    private double lon;
    private double lat;
    private int busynessNow;
    private int[] busynessArray;
    private boolean favorite;
    private boolean food;
    private boolean study;
    private boolean rec;

    public Building (String name, double lat, double lon, int busynessNow, int[] busynessArray,
                     boolean favorite, boolean food, boolean study, boolean rec) {
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.busynessNow = busynessNow;
        this.busynessArray = busynessArray;
        this.favorite = favorite;
        this.food = food;
        this.study = study;
        this.rec = rec;
    }

    public String getName() {
        return this.name;
    }

    public double getLon() {
        return this.lon;
    }

    public double getLat() {
        return this.lat;
    }

    public int getBusynessNow() {
        return this.busynessNow;
    }

    public int[] getBusynessArray() {
        return this.busynessArray;
    }

    public boolean getFavorite() {
        return this.favorite;
    }

    //Categories
    public boolean getFood() {
        return this.food;
    }

    public boolean getStudy() {
        return this.study;
    }

    public boolean getRec() {
        return this.rec;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setBusyneesNow(int busynessNow) {
        this.busynessNow = busynessNow;
    }

    public void setBusynessArray(int[] busynessArray) {
        this.busynessArray = busynessArray;
    }

    public void setFavorite() {
        this.favorite = !this.favorite;
    }


    //Categories
    public void setFood(boolean food) {
        this.food = food;
    }

    public void setStudy(boolean study) {
        this.study = study;
    }

    public void setRec(boolean rec) {
        this.rec = rec;

    }
}
//{43, 39, }
//        String name, double lon, double lat, int busynessNow, int[] busynessArray, boolean favorite, boolean food, boolean study, boolean rec
//        Buildng newBuilding = new Building("Culc", 33.774599, -84.396372, 39, , true, false, true, false);
//        Buildng newBuilding = new Building("Student Center", 33.774028, -84.398818, 35, , true, true, true, true);
//        Buildng newBuilding = new Building("Library", 33.774327, -84.395825, 31, , false, false, true, false);
//        Buildng newBuilding = new Building("CRC", 33.77562, -84.403753, 46, , true, true, false, true);
//        Buildng newBuilding = new Building("Klaus", 33.777212, -84.396281, 33, , false, false, true, false);
//        Buildng newBuilding = new Building("CoC", 33.777386, -84.396281, 52, , false, true, true, false);
