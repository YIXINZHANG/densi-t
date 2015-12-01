package org.intracode.contactmanager;

/**
 * Created by pbz18_000 on 11/12/2015.
 */
public class Building {

    private String id;
    private String name;
    private double lon;
    private double lat;
    private int busynessNow;
//    private int[] busynessArray;
    private boolean favorite;
    private boolean food;
    private boolean study;
    private boolean rec;

    public Building (String id, String name, double lat, double lon, int busynessNow,
                     boolean favorite, boolean food, boolean study, boolean rec) {
        this.id = id;
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.busynessNow = busynessNow;
//        this.busynessArray = busynessArray;
        this.favorite = favorite;
        this.food = food;
        this.study = study;
        this.rec = rec;
    }

    public String getName() {
        return this.name;
    }

    public String getId() { return this.id; }

    public double getLon() {
        return this.lon;
    }

    public double getLat() {
        return this.lat;
    }

    public int getBusynessNow() {
        return this.busynessNow;
    }

//    public int[] getBusynessArray() {
//        return this.busynessArray;
//    }

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

//    public void setBusynessArray(int[] busynessArray) {
//        this.busynessArray = busynessArray;
//    }

    public void setFavorite() {
        this.favorite = !this.favorite;
    }

    public void setId(String id) { this.id = id; }
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
//{1, 2, 3, 4, 5, 6, 7, 8, 9 , 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24}
//        {21,18,10,7,5,5,10,29,38,58,73,85,87,83,71,53,46,42,62,65,53,51,42,36}
//        {18,10,4,2,3,4,9,24,34,49,73,87,93,81,67,42,37,39,44,31,26,27,23,20}
//        {32,25,11,8,6,5,4,22,29,32,42,59,62,78,75,61,73,55,78,72,64,58,47,39}
//        {0,0,0,0,25,34,30,21,29,35,33,28,39,35,49,52,67,61,72,74,13,2,0,0}
//        {19,17,11,6,4,5,8,28,37,56,68,71,70,68,65,59,42,38,40,43,47,36,31,20}
//        {24,19,15,9,4,5,5,25,42,51,62,87,82,71,65,61,44,41,43,49,39,37,31,29}
//
//        String name, double lon, double lat, int busynessNow, int[] busynessArray, boolean favorite, boolean food, boolean study, boolean rec
//        Buildng newBuilding = new Building("Culc", 33.774599, -84.396372, 39, {21,18,10,7,5,5,10,29,38,58,73,85,87,83,71,53,46,42,62,65,53,51,42,36}, true, false, true, false);
//        Buildng newBuilding = new Building("Student Center", 33.774028, -84.398818, 35, {18,10,4,2,3,4,9,24,34,49,73,87,93,81,67,42,37,39,44,31,26,27,23,20}, true, true, true, true);
//        Buildng newBuilding = new Building("Library", 33.774327, -84.395825, 31, {32,25,11,8,6,5,4,22,29,32,42,59,62,78,75,61,73,55,78,72,64,58,47,39}, false, false, true, false);
//        Buildng newBuilding = new Building("CRC", 33.77562, -84.403753, 46, {0,0,0,0,25,34,30,21,29,35,33,28,39,35,49,52,67,61,72,74,13,2,0,0}, true, true, false, true);
//        Buildng newBuilding = new Building("Klaus", 33.777212, -84.396281, 33, {19,17,11,6,4,5,8,28,37,56,68,71,70,68,65,59,42,38,40,43,47,36,31,20}, false, false, true, false);
//        Buildng newBuilding = new Building("CoC", 33.777386, -84.396281, 52, {24,19,15,9,4,5,5,25,42,51,62,87,82,71,65,61,44,41,43,49,39,37,31,29}, false, true, true, false);
