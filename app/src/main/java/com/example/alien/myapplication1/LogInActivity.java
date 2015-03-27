package com.example.alien.myapplication1;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class LogInActivity extends Activity {

    private EditText login_mail;
    private EditText login_password;
    private CheckBox checkbox_remember;
    private Button button_login;
    private Button button_guest;
    private Button button_register;
    SharedPreferences preferences;

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
    }

    public void restoreData()
    {
        login_mail.setText(preferences.getString("mail", ""));
        login_password.setText(preferences.getString("pass", ""));
        checkbox_remember.setChecked(preferences.getBoolean("rem", false));
    }
    public void addListeners()
    {
        button_guest.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                startActivity(intent);
            }
        });
        button_register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(), RegistrationActivity.class);
                startActivity(intent);
            }
        });
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor pref = preferences.edit();

                String login = login_mail.getText().toString();
                String password = login_password.getText().toString();

                if(checkbox_remember.isChecked())
                {
                    pref.putString("mail", login);
                    pref.putString("pass", password);
                    pref.putBoolean("rem", checkbox_remember.isChecked());
                }
                else{
                    pref.putString("mail", "");
                    pref.putString("pass", "");
                    pref.putBoolean("rem", false);
                }
                pref.commit();

                new LogIn(getApplicationContext(), 1).execute(login, password);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

}
