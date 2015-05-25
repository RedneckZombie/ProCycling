package com.example.alien.myapplication1.account;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.example.alien.myapplication1.OnASyncTaskCompleted;
import com.example.alien.myapplication1.map.SideBar;
import com.example.alien.myapplication1.R;

public class LogInActivity extends Activity implements OnASyncTaskCompleted {
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
    }

    public void restoreData() {
        login_mail.setText(preferences.getString("mail", ""));
        login_password.setText(preferences.getString("pass", ""));

        checkbox_remember.setChecked(preferences.getBoolean("rem", false));

        String username = preferences.getString("username", "");
        String userID = preferences.getString("userID", "");

        isLogged = getIntent().getBooleanExtra("isLogged", true);

        if (!isLogged) {
            checkbox_remember.setChecked(false);
        }

        if (checkbox_remember.isChecked() && !username.equals("") && isLogged &&!userID.equals("")) {
            Intent intent = new Intent(getApplicationContext(), SideBar.class);
            intent.putExtra("username", username);
            intent.putExtra("userID", userID);
            startActivity(intent);
            finish();
        }
    }

    public void addListeners() {
        checkbox_remember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    SharedPreferences.Editor pref = preferences.edit();
                    pref.putString("mail", "");
                    pref.putString("pass", "");
                    pref.putBoolean("rem", false);
                    pref.apply();
                }
            }
        });

        button_guest.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v)
                {
                    Intent intent = new Intent(getApplicationContext(), SideBar.class);
                    startActivity(intent);
                    finish();
            }
        });

        button_register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(), RegistrationActivity.class);
                startActivity(intent);
                finish();
            }
        });

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor pref = preferences.edit();

                String login = login_mail.getText().toString();
                String password = login_password.getText().toString();

                if (checkbox_remember.isChecked()) {
                    pref.putString("mail", login);
                    pref.putString("pass", password);
                    pref.putBoolean("rem", checkbox_remember.isChecked());
                }
                else {
                    pref.putString("mail", "");
                    pref.putString("pass", "");
                    pref.putBoolean("rem", false);
                }

                pref.apply();

                LogIn loginTask = new LogIn(getApplicationContext(), callback);
                loginTask.execute(login, password);
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
}