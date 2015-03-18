package com.example.alien.myapplication1;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;


public class RegistrationActivity extends ActionBarActivity {

    private EditText etEmail, etPassword, etPasswordConfirm, etUserName, etDateOfBirth, etCity;
    private RadioButton female, male;
    private Button saveSignUp, clear;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etPasswordConfirm = (EditText) findViewById(R.id.etPasswordConfirm);
        etUserName = (EditText) findViewById(R.id.etUserName);
        etDateOfBirth = (EditText) findViewById(R.id.etDateOfBirth);
        etCity = (EditText) findViewById(R.id.etCity);

        female = (RadioButton) findViewById(R.id.female);
        male = (RadioButton) findViewById(R.id.male);

        saveSignUp = (Button) findViewById(R.id.saveSignUp);
        clear = (Button) findViewById(R.id.clear);

        listener();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_registration, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void listener()
    {
        saveSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etPassword.getText().toString().equals(etPasswordConfirm.getText().toString()))
                {
                    String email = etEmail.getText().toString();
                    String password = etPassword.getText().toString();
                    String user_name = etUserName.getText().toString();
                    String dateOfBirth = etDateOfBirth.getText().toString();
                    String city = etCity.getText().toString();

                    new SigninActivity(getApplicationContext(),1).execute(email, password, user_name);

                }
                else{
                    Toast.makeText(getApplicationContext(),"Hasło i potwierdzenie hasła nie są takie same !", Toast.LENGTH_SHORT).show();
                    //WYWALIĆ POWIADOMINEIE
                    etPassword.setText("");
                    etPasswordConfirm.setText("");
                }
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etEmail.setText("");
                etPassword.setText("");
                etPasswordConfirm.setText("");
                etUserName.setText("");
                etDateOfBirth.setText("");
                etCity.setText("");
            }
        });
    }
}
