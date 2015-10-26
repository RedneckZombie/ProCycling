package com.example.alien.myapplication1.map;

import android.app.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.alien.myapplication1.NetConnection.CheckingConnection;

import com.example.alien.myapplication1.NetConnection.OnASyncTaskCompleted;
import com.example.alien.myapplication1.Options.OptionsActivity;
import com.example.alien.myapplication1.R;
import com.example.alien.myapplication1.account.LogInActivity;
import com.example.alien.myapplication1.rankings.ViewPagerFragment;
import com.example.alien.myapplication1.tracks.AllStatsFragment;
import com.example.alien.myapplication1.tracks.GetAllStats;
import com.example.alien.myapplication1.tracks.RecordRoute;
import com.example.alien.myapplication1.tracks.SaveTrack;
import com.example.alien.myapplication1.tracks.StatisticsCalculator;
import com.example.alien.myapplication1.tracks.Stats;
import com.example.alien.myapplication1.tracks.TrackFilesManager;
import com.example.alien.myapplication1.tracks.TrackList;
import com.example.alien.myapplication1.tracks.TrackSummary;

import org.joda.time.Period;
import org.json.JSONException;
import org.json.JSONObject;


public class SideBar extends ActionBarActivity implements OnASyncTaskCompleted {


    DrawerLayout mDrawerLayout;
    ListView mDrawerList;
    ActionBarDrawerToggle mDrawerToggle;
    String username;
    String userID;
    boolean isMarkersOn;
    boolean isSynthOn;
    boolean isRecognOn;
    private static RecordRoute rr;
    Map fr;
    SharedPreferences preferences;
    private Stats stats;
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.side_bar_activity);
        fr = new Map();
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        extra();
        drawer();
        mapa();
        rejestrujTrase();
        fr.ustawDaneOpcji(isMarkersOn);
    }
    public void rejestrujTrase()
    {
        this.runOnUiThread(new Runnable() {
            public void run() {
                if (rr == null) {
                    rr = new RecordRoute(getApplicationContext());
                    rr.createListener();
                }
            }
        });
    }

    public void extra()
    {
        preferences = getSharedPreferences("PREFS", Activity.MODE_WORLD_READABLE);
        Intent intent = getIntent();
        SharedPreferences.Editor pref = preferences.edit();
        username = intent.getStringExtra("username");
        userID = intent.getStringExtra("userID");
        isMarkersOn = intent.getBooleanExtra("isMarkerOn", false);
        isRecognOn = intent.getBooleanExtra("isRecognOn", true);
        isSynthOn = intent.getBooleanExtra("isSynthOn", true);
        getSupportActionBar().setTitle("Witaj w ProCycling");
        pref.putString("username",username);
        pref.putString("userID", userID);
        pref.commit();
    }
    public void mapa()
    {
        Bundle bundle = new Bundle();
        bundle.putString("userID", userID);
        fr.setArguments(bundle);
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

                if (username != null) {//dla uzytkownika
                    switch (position) {
                        case 0:    //rejestracja trasy
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
                        case 1:      //moje trasy
                            CheckingConnection cc = new CheckingConnection(getApplicationContext());
                            cc.execute();
                            Fragment tl = new TrackList();
                            Bundle b = new Bundle();
                            b.putString("username", username);
                            b.putString("userID", userID);
                            while (!cc.isFinished()) {
                                try {
                                    Thread.sleep(100);
                                } catch (Exception e) {
                                }
                            }
                            if (cc.isConnected()) {
                                new TrackFilesManager(getApplicationContext(), userID);
                            }
                            b.putBoolean("isConnected", cc.isConnected());

                            tl.setArguments(b);
                            FragmentManager fm = getSupportFragmentManager();
                            fm.beginTransaction().replace(R.id.content_frame, tl).commit();

                            mDrawerLayout.closeDrawer(mDrawerList);
                            mDrawerList.setItemChecked(-1, true);
                            break;
                        case 2:    //Mapa
                            if (!fr.isVisible()) {
                                mapa();
                            }
                            mDrawerLayout.closeDrawer(mDrawerList);
                            mDrawerList.setItemChecked(-1, true);
                            break;
                        case 3:  //Statystyki
                            statystyki();
                            mDrawerLayout.closeDrawer(mDrawerList);
                            break;
                        case 4:       //rankingi
                            rankingi();
                            mDrawerLayout.closeDrawer(mDrawerList);
                            break;
                        case 5:   //wyloguj
                            Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
                            intent.putExtra("isLogged", false);
                            startActivity(intent);
                            finish();
                            break;
                        case 6:     // wyjdź
                            finish();
                            break;
                    }
                } else {//dla gościa
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
                            if (!fr.isVisible()) {
                                mapa();
                            }
                            mDrawerLayout.closeDrawer(mDrawerList);
                            mDrawerList.setItemChecked(-1, true);
                            break;
                        case 2:
                            Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
                            intent.putExtra("isLogged", false);
                            startActivity(intent);
                            finish();
                            break;
                        case 3:
                            finish();
                            break;
                    }
                }
            }
        });
    }

    public void podsumowanie()
    {
        JSONObject ob = rr.getJSON();
        if(ob != null)
            try {
                if (ob.getJSONArray("points").length() > 0) {
                    CheckingConnection cc = new CheckingConnection(getApplicationContext());
                    cc.execute();
                    Fragment fr = new TrackSummary();
                    Bundle b = new Bundle();
                    b.putString("json", ob.toString());
                    fr.setArguments(b);
                    FragmentManager fm = getSupportFragmentManager();//
                    fm.beginTransaction().replace(R.id.content_frame, fr).commit();
                    while(!cc.isFinished()){
                        try{
                            Thread.sleep(100);
                        }catch(Exception e){}
                    }
                    if(cc.isConnected())
                        zapisz(ob);
                }
            }catch(Exception e){}
    }

    public void statystyki()
    {
        CheckingConnection cc = new CheckingConnection(getApplicationContext());
        cc.execute();
        while(!cc.isFinished()){
            try{
                Thread.sleep(100);
            }catch(Exception e){}
        }
        if(cc.isConnected())
        {
            new GetAllStats(getApplicationContext(),this).execute(userID);
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Nie mozna pobrac statystyk z bazy", Toast.LENGTH_LONG).show();
        }
    }

    public void rankingi()
    {
        CheckingConnection cc = new CheckingConnection(getApplicationContext());
        cc.execute();
        while(!cc.isFinished()){
            try{
                Thread.sleep(100);
            }catch(Exception e){}
        }
        if(cc.isConnected())
        {
            Fragment fr = new ViewPagerFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fr).commit();
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Nie mozna pobrac rankingow", Toast.LENGTH_LONG).show();
        }
    }

    public void zapisz(JSONObject jsonObj)
    {
        try {
            StatisticsCalculator calc = new StatisticsCalculator(jsonObj);
            String trackName = jsonObj.getString("finish");
            Period trTime = calc.getTravelTime();
            new SaveTrack(getApplicationContext()).execute(userID, trackName, jsonObj.toString(),
                    String.valueOf(calc.getDistance()), String.format("%02d:%02d:%02d", trTime.getHours(), trTime.getMinutes(), trTime.getSeconds()),
                    String.valueOf(calc.getAverageSpeed()));
            //new TrackFilesManager(getApplicationContext(), userID);
            Toast.makeText(getApplicationContext(), "Zapisano w bazie", Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void updateAdapter()
    {
       ArrayAdapter aa = (ArrayAdapter) mDrawerList.getAdapter();

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
        int id = item.getItemId();

        if (id == R.id.options)
        {
            Intent intent = new Intent(getApplicationContext(), OptionsActivity.class);
            intent.putExtra("isMarkersOn", isMarkersOn);
            intent.putExtra("isSynthOn", isSynthOn);
            intent.putExtra("isRecognOn", isRecognOn);
            startActivity(intent);
        }
        /*
        if (id == R.id.action_markers_off) {
            fr.offMarkers();
            return true;
        }
        if (id == R.id.action_markers_on) {
            fr.onMarkers();
            return true;
        }*/
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
       // menu.findItem(R.id.action_markers_off).setVisible(true);
       // menu.findItem(R.id.action_markers_on).setVisible(true);
        menu.findItem(R.id.options).setVisible(true);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return true;
    }

    @Override
    public void onASyncTaskCompleted(Object... value) {
        if(((String)value[1]).equals("1")) {
            stats = (Stats) value[0];
            Fragment fr = new AllStatsFragment();
            Bundle b = new Bundle();
            FragmentManager fm = getSupportFragmentManager();
            b.putString("time", stats.getTime());
            b.putInt("dist", stats.getDistance());
            b.putDouble("avg", stats.getAverage());
            b.putString("username", username);
            b.putString("userID", userID);
            fr.setArguments(b);
            fm.beginTransaction().replace(R.id.content_frame, fr).commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Naciśnij ponownie, aby wyjść z aplikacji", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
