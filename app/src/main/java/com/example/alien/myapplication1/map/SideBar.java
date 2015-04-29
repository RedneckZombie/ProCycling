package com.example.alien.myapplication1.map;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.alien.myapplication1.R;
import com.example.alien.myapplication1.account.LogInActivity;
import com.example.alien.myapplication1.account.RegistrationActivity;
import com.example.alien.myapplication1.tracks.GetTracks;
import com.example.alien.myapplication1.tracks.RecordRoute;
import com.example.alien.myapplication1.tracks.TrackDetails;
import com.example.alien.myapplication1.tracks.TrackList;
import com.example.alien.myapplication1.tracks.TrackSummary;

import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;


public class SideBar extends ActionBarActivity {


    DrawerLayout mDrawerLayout;
    ListView mDrawerList;
    ActionBarDrawerToggle mDrawerToggle;
    String username;
    private static RecordRoute rr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.side_bar_activity);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        extra();
        drawer();
        mapa();
        rejestrujTrase();

    }

    public void rejestrujTrase()
    {
        this.runOnUiThread(new Runnable() {
            public void run() {
                if (rr == null)
                {
                    rr = new RecordRoute(getApplicationContext());
                    rr.createListener();
                }
            }
        });
    }

    public void extra()
    {
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        getSupportActionBar().setTitle("Witaj w ProCycling");
    }
    public void mapa()
    {
        Fragment fr = new Map();
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.content_frame, fr).commit();
    }
    public void drawer()
    {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.drawer_list);
        mDrawerToggle = new ActionBarDrawerToggle( this,
                mDrawerLayout,
                R.mipmap.ic_drawer,
                R.string.drawer_open,
                R.string.drawer_close){

            /** Called when drawer is closed */
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(R.string.menu);
                invalidateOptionsMenu();
            }

            /** Called when a drawer is opened */
            public void onDrawerOpened(View drawerView) {
                if(username==null) {
                    getSupportActionBar().setTitle(R.string.guest);
                }else{
                    getSupportActionBar().setTitle(username);
                }
                invalidateOptionsMenu();///
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        int usedArray;
        if(username==null)
        {
            usedArray = R.array.guest;
        }
        else{
            usedArray = R.array.options;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getBaseContext(),
                R.layout.element_menu ,
                getResources().getStringArray(usedArray)
        );

        mDrawerList.setAdapter(adapter);
        mDrawerList.setSelector(android.R.color.holo_blue_dark);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view,
                                    int position,
                                    long id) {

                if(username!=null) {//dla uzytkownika
                    switch (position) {
                        case 0:
                            if (!rr.isRecording()) {
                                rr.startRecording();
                                Toast.makeText(getApplicationContext(), R.string.rejestruj_trase, Toast.LENGTH_LONG).show();
                                aktualizujAdapter(0);
                            } else {
                                rr.stopRecording();
                                Toast.makeText(getApplicationContext(), R.string.zakoncz_trase, Toast.LENGTH_LONG).show();
                                aktualizujAdapter(1);
                                podsumowanie();

                            }
                            mDrawerLayout.closeDrawer(mDrawerList);
                            break;
                        case 1:
                            Fragment fr = new TrackList();
                            Bundle b = new Bundle();
                            b.putString("username", username);
                            b.putBoolean("isConnected", isConnected(getApplicationContext()));
                            fr.setArguments(b);
                            FragmentManager fm = getSupportFragmentManager();
                            fm.beginTransaction().replace(R.id.content_frame, fr).commit();
                            break;
                        case 2:
                            Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
                            startActivity(intent);
                            finish();
                            break;
                    }
                }
                else{//dla gościa
                    switch(position)
                    {
                        case 0:
                            if (!rr.isRecording()) {
                                rr.startRecording();
                                Toast.makeText(getApplicationContext(), R.string.rejestruj_trase, Toast.LENGTH_LONG).show();
                                aktualizujAdapter(0);
                            } else {
                                rr.stopRecording();
                                Toast.makeText(getApplicationContext(), R.string.zakoncz_trase, Toast.LENGTH_LONG).show();
                                aktualizujAdapter(1);
                                podsumowanie();

                            }
                            mDrawerLayout.closeDrawer(mDrawerList);
                            break;
                        case 1:
                            Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
                            startActivity(intent);
                            finish();
                            break;
                    }
                }
                /*
                // Getting an array of rivers
                String[] rivers = getResources().getStringArray(R.array.rivers);

               // Creating a fragment object
                Map rFragment = new Map();


                FragmentManager fragmentManager = getFragmentManager();

                // Creating a fragment transaction
                FragmentTransaction ft = fragmentManager.beginTransaction();

                // Adding a fragment to the fragment transaction
                ft.replace(R.id.content_frame, rFragment);

                // Committing the transaction
                ft.commit();

                // Closing the drawer
                mDrawerLayout.closeDrawer(mDrawerList);*/
            }
        });



    }

    public void podsumowanie()
    {
        JSONObject ob = rr.getJSON();
        if(ob != null)
            try {
                if (ob.getJSONArray("points").length() > 0) {
                    Fragment fr = new TrackSummary();
                    Bundle b = new Bundle();
                    b.putString("json", ob.toString());
                    b.putBoolean("isSaved", false);
                    fr.setArguments(b);
                    FragmentManager fm = getSupportFragmentManager();//
                    fm.beginTransaction().replace(R.id.content_frame, fr).commit();
                }
            }catch(Exception e){}
    }


    public void aktualizujAdapter(int n)
    {
        String[] array;
        if(username!=null) {
            if (n == 0) {
                array = getResources().getStringArray(R.array.options2);
            } else {
                array = getResources().getStringArray(R.array.options);
            }
        }else{
            if (n == 0) {
                array = getResources().getStringArray(R.array.guest2);
            } else {
                array = getResources().getStringArray(R.array.guest);
            }
        }
        ArrayAdapter<String> a = new ArrayAdapter<>(
                getBaseContext(),
                R.layout.element_menu,
                array
        );
        mDrawerList.setAdapter(a);
    }




    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);

       // menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        menu.findItem(R.id.action_settings).setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return true;
    }


    public boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            try {
                URL url = new URL("http://www.google.com/");
                HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                urlc.setRequestProperty("User-Agent", "test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1000); // mTimeout is in seconds
                urlc.connect();
                if (urlc.getResponseCode() == 200) {
                    return true;
                } else {
                    return false;
                }
            } catch (IOException e) {
                return false;
            }
        }
        return false;
    }



}
