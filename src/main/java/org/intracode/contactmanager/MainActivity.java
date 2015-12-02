package org.intracode.contactmanager;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity implements ActionBar.TabListener, BuildingListFragment.UpdateListener, MapFragment.UpdateListener2{

    /**
     * Instance variable for ServerUtility.
     */

    private TabsAdapter mAdapter;
    private ActionBar actionBar;
    private ViewPager viewPager;
    /**
     * Instance variable for FragmentManager.
     */
    private FragmentManager fragmentManager;

    private String[] tabs = {"Building", "Map"};

    private String API = "http://densit-api.appspot.com/locations";

    public final static String EXTRA_MESSAGE = "API.MESSAGE";

    public static final String PREFS_NAME = "MyPrefsFile";

    private String favoriteList = "favorite";
    private boolean dummySetting;
    private int clickedPosition;
    private SharedPreferences settings;
    private GlobalVariables gv;

    @Override
    protected final void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ArrayList<String> bnames = new ArrayList<>();
        bnames.add("Clough");
        bnames.add("Student Center");
        bnames.add("Library");
        bnames.add("Campus Recreation Center");
        bnames.add("Klaus");
        bnames.add("College of Computing");
        bnames.add("College of Architecture");
        gv = (GlobalVariables) getApplication();

        //Adding preferences
        ////////////////////////////////////////////////////////////////////////
        settings = getSharedPreferences(PREFS_NAME, 0);

        for (String s:bnames) {

            System.out.println(settings.getBoolean(s, false) + " new " + s);
            if (settings.getBoolean(s, false)) {
                gv.addBuildingNames(s);
            }

        }
        try {
            System.out.println(settings.getString("Setting", null));
        } catch (Exception e) {
            System.out.println("pass");
        }

        gv.setSorting(settings.getString("Setting", null));
//        Toast.makeText(this, a, Toast.LENGTH_LONG).show();
        ////////////////////////////////////////////////////////////////////////

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#37ABE2")));
        getSupportActionBar().setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#37ABE2")));
        getSupportActionBar().setIcon(new ColorDrawable(Color.parseColor("#F4E245")));


        viewPager = (ViewPager) findViewById(R.id.container);
        actionBar = getSupportActionBar();
        mAdapter = new TabsAdapter(getSupportFragmentManager());

        viewPager.setAdapter(mAdapter);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" + "Densi-T" + "</font>")));

        for (String tab_name : tabs) {
            TextView tView = new TextView(getApplicationContext());
            tView.setText(tab_name);
            tView.setTextColor(Color.WHITE);
            actionBar.addTab(actionBar.newTab().setTabListener(this).setCustomView(tView));
        }

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                // TODO Auto-generated method stub
                actionBar.setSelectedNavigationItem(arg0);

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }
        });
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {

    }

    public void onArticleSelected(String name, boolean check) {
        // The user selected the headline of an article from the HeadlinesFragment
        // Do something here to display that article
//        BusynessFragment busynessFragment = (BusynessFragment) mAdapter.instantiateItem(viewPager, 1);
//        busynessFragment.reload(position, id, name);
//        MapFragment mapFragment = (MapFragment) mAdapter.instantiateItem(viewPager, 2);
//
//        dummySetting = true;
//        clickedPosition = position;
//        mapFragment.reload(position, id, name);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(name, check);
//        editor.putBoolean(favoriteList, dummySetting);
//        editor.putInt("clicked", clickedPosition);

        // Commit the edits!
        editor.commit();


    }

    public void onSettingSelected(String s) {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("Setting", s);
//        editor.putBoolean(favoriteList, dummySetting);
//        editor.putInt("clicked", clickedPosition);

        // Commit the edits!
        editor.commit();
    }

    public void onDayTimeSelected2(int day, int time) {
        // The user selected the headline of an article from the HeadlinesFragment
        // Do something here to display that article
//        BusynessFragment busynessFragment = (BusynessFragment) mAdapter.instantiateItem(viewPager, 1);
//        busynessFragment.reload(position, id, name);
//        MapFragment mapFragment = (MapFragment) mAdapter.instantiateItem(viewPager, 2);
//        mapFragment.reload(position, id, name);
        BuildingListFragment buidlinglistFragment = (BuildingListFragment) mAdapter.instantiateItem(viewPager, 0);
        buidlinglistFragment.reload(day, time);
    }

    public void onDayTimeSelected(int day, int time) {
        MapFragment mapFragment = (MapFragment) mAdapter.instantiateItem(viewPager, 1);
        mapFragment.reload(day, time);
    }

    @Override
    protected void onStop(){
        super.onStop();

        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(favoriteList, dummySetting);
        editor.putInt("clicked", clickedPosition);

        // Commit the edits!
        editor.commit();
    }

}
