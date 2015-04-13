package com.example.alien.myapplication1.map;

import android.content.Intent;
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
import com.example.alien.myapplication1.tracks.RecordRoute;


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
        drawer();
        mapa();

        Log.i("testing", "onCreate (Map)");
        this.runOnUiThread(new Runnable() {
            public void run() {
                if (rr == null)
                    rr = new RecordRoute(getApplicationContext());
            }
        });

        extra();
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
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getBaseContext(),
                R.layout.element_menu ,
                getResources().getStringArray(R.array.options)
        );

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view,
                                    int position,
                                    long id) {

                switch(position){
                    case 0:
                        if(!rr.isRecording()) {
                            rr.startRecording();
                            Toast.makeText(getApplicationContext(), "rejestruję trasę", Toast.LENGTH_LONG).show();
                        }
                        else{
                            rr.stopRecording();
                            Toast.makeText(getApplicationContext(), "koniec rejestrowania trasy", Toast.LENGTH_LONG).show();
                        }
                        break;
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

        mDrawerList.setAdapter(adapter);
        mDrawerList.setSelector(android.R.color.holo_blue_dark);

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





}
