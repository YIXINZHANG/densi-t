package org.intracode.contactmanager;

import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

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

    @Override
    protected final void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //Adding preferences
        ////////////////////////////////////////////////////////////////////////
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        dummySetting = settings.getBoolean(favoriteList, false);
        clickedPosition = settings.getInt("clicked", 0);
        String a = dummySetting + " " + clickedPosition;
//        Toast.makeText(this, a, Toast.LENGTH_LONG).show();
        ////////////////////////////////////////////////////////////////////////



        viewPager = (ViewPager) findViewById(R.id.container);
        actionBar = getSupportActionBar();
        mAdapter = new TabsAdapter(getSupportFragmentManager());

        viewPager.setAdapter(mAdapter);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        for (String tab_name : tabs) {
            actionBar.addTab(actionBar.newTab().setText(tab_name).setTabListener(this));
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

    public void onArticleSelected(int position, String id, String name) {
        // The user selected the headline of an article from the HeadlinesFragment
        // Do something here to display that article
        BusynessFragment busynessFragment = (BusynessFragment) mAdapter.instantiateItem(viewPager, 1);
        busynessFragment.reload(position, id, name);
        MapFragment mapFragment = (MapFragment) mAdapter.instantiateItem(viewPager, 2);

        dummySetting = true;
        clickedPosition = position;
//        mapFragment.reload(position, id, name);


    }

    public void onArticleSelected2(int position, String id, String name) {
        // The user selected the headline of an article from the HeadlinesFragment
        // Do something here to display that article
        BusynessFragment busynessFragment = (BusynessFragment) mAdapter.instantiateItem(viewPager, 1);
        busynessFragment.reload(position, id, name);
        MapFragment mapFragment = (MapFragment) mAdapter.instantiateItem(viewPager, 2);
        mapFragment.reload(position, id, name);

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
