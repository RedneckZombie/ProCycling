package com.example.alien.myapplication1.account;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;


import com.example.alien.myapplication1.NetConnection.OnASyncTaskCompleted;
import com.example.alien.myapplication1.R;
import com.example.alien.myapplication1.Speech.MicroListener;
import com.example.alien.myapplication1.Speech.SpeechInterface;
import com.example.alien.myapplication1.map.SideBarActivity;

public class LogInActivity extends ActionBarActivity implements OnASyncTaskCompleted, MicroListener {
    private EditText login_mail;
    private EditText login_password;

    private CheckBox checkbox_remember;

    private Button button_login;
    private Button button_guest;
    private Button button_register;

    SharedPreferences preferences;

    boolean isLogged = true;
    protected String result;
    private OnASyncTaskCompleted callback;
    private SpeechInterface speechInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login_mail = (EditText) findViewById(R.id.email);
        login_password = (EditText) findViewById(R.id.password);

        checkbox_remember = (CheckBox) findViewById(R.id.checkBox_remember);

        button_login = (Button) findViewById(R.id.Login);
        button_guest = (Button) findViewById(R.id.guest);
        button_register = (Button) findViewById(R.id.registration_button);

        preferences = getSharedPreferences("PREFS", Activity.MODE_PRIVATE);

        addListeners();
        restoreData();

        callback = this;
        speechInterface = new SpeechInterface(this, getClass().getSimpleName(), this);
    }

    public void restoreData() {
        login_mail.setText(preferences.getString("mail", ""));
        login_password.setText(preferences.getString("pass", ""));

        checkbox_remember.setChecked(preferences.getBoolean("rem", false));

        String username = preferences.getString("username", "");
        String userID = preferences.getString("userID", "");

        isLogged = getIntent().getBooleanExtra("isLogged", true);
        boolean markers = false;
        boolean synth = true;
        boolean recogn = true;
        try {
            markers = preferences.getBoolean("enableMarkers", false);
            synth = preferences.getBoolean("enableSynth", true);
            recogn = preferences.getBoolean("enableRecogn", true);
        }catch(Exception e){System.out.println("test");}

        if (!isLogged) {
            checkbox_remember.setChecked(false);
        }

        if (checkbox_remember.isChecked() && !username.equals("") && isLogged &&!userID.equals("")) {
            Intent intent = new Intent(getApplicationContext(), SideBarActivity.class);
            intent.putExtra("username", username);
            intent.putExtra("userID", userID);
            intent.putExtra("isMarkerOn", markers);
            intent.putExtra("isSynthOn", synth);
            intent.putExtra("isRecognOn", recogn);
            startActivity(intent);
            finish();
        }
    }

    public void guestLogIn()
    {
        Intent intent = new Intent(getApplicationContext(), SideBarActivity.class);
        startActivity(intent);
        finish();
    }
    public void register()
    {
        Intent intent = new Intent(getApplicationContext(), RegistrationActivity.class);
        startActivity(intent);
        finish();
    }
    public void logIn()
    {
        SharedPreferences.Editor pref = preferences.edit();

        String login = login_mail.getText().toString();
        String password = login_password.getText().toString();

        if (checkbox_remember.isChecked()) {
            pref.putString("mail", login);
            pref.putString("pass", password);
            pref.putBoolean("rem", checkbox_remember.isChecked());
        } else {
            pref.putString("mail", "");
            pref.putString("pass", "");
            pref.putBoolean("rem", false);
        }

        pref.apply();


        LogIn loginTask = new LogIn(getApplicationContext(), callback);
        loginTask.execute(login, password);
    }
    public void addListeners() {
        checkbox_remember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                saveCheckbox(isChecked);
            }
        });

        button_guest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guestLogIn();
            }
        });

        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logIn();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onASyncTaskCompleted(Object... value) {
        result = (String) value[0];
    }

    public void saveCheckbox(boolean isChecked)
    {
        checkbox_remember.setChecked(isChecked);
        if (!isChecked) {
            SharedPreferences.Editor pref = preferences.edit();
            pref.putString("mail", "");
            pref.putString("pass", "");
            pref.putBoolean("rem", false);
            pref.apply();
        }

    }
    @Override
    public void microCommandRun(int result) {
        speechInterface.tell(result+"");
        switch (result){
            case 0:
                saveCheckbox(!checkbox_remember.isChecked());
                break;
            case 1:
                logIn();
                break;
            case 2:
                guestLogIn();
                break;
            case 3:
                register();
                break;
            case 4:
                finish();
                break;
            case 5:
                showInfoDialog();
                break;
        }
    }

    @Override
    public void showInfoDialog() {
        speechInterface.showInfoDialog();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.listenMicro)
        {
            speechInterface.listenCommand();
        }
        else if(id==R.id.avaible_comands)
        {
            showInfoDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        speechInterface.destroy();
        super.onDestroy();
    }
}