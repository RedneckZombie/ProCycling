package com.example.alien.myapplication1;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
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

                    String sex = female.isChecked() ? "K" : "M";

                    if(!isEmailCorrect(email))
                        Toast.makeText(getApplicationContext(),"Email nieprawidłowy!", Toast.LENGTH_SHORT).show();
                    else if(!isPasswordUserNameCityCorrect(password))
                        Toast.makeText(getApplicationContext(),"Hasło nie spełnia wymagań!\nDozwolone litery i cyfry", Toast.LENGTH_SHORT).show();
                    else if(!isPasswordUserNameCityCorrect(user_name))
                        Toast.makeText(getApplicationContext(),"Nazwa użytkownika nie spełnia wymagań!\nDozwolone litery i cyfry", Toast.LENGTH_SHORT).show();
                    else if(!isDateCorrect(dateOfBirth))
                        Toast.makeText(getApplicationContext(),"Nieprawidłowy format daty", Toast.LENGTH_SHORT).show();
                    else if(!isPasswordUserNameCityCorrect(city))
                        Toast.makeText(getApplicationContext(),"Nazwa miasta nie spełnia wymagań!\nDozwolone litery i cyfry", Toast.LENGTH_SHORT).show();
                    else
                        new SigninActivity(getApplicationContext(),1).execute(email, password, user_name, dateOfBirth, sex, city);

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

    public boolean isEmailCorrect(String email)
    {
        boolean correct = true;

        if(!email.contains("@"))
            correct = false;
        if(!email.contains("."))
            correct = false;

        if(email.charAt(0) == '@' || email.charAt(0) == '.' || email.charAt(email.length()-1) == '@' || email.charAt(email.length()-1) == '.')
            correct = false;

        if(email.lastIndexOf(".") < email.indexOf("@"))
            correct = false;

        for(int i = 0 ; i < email.length() ; i++)
        {
            //if( Character.getNumericValue(email.charAt(i)) < 64 && Character.getNumericValue(email.charAt(i)) != 46 || Character.getNumericValue(email.charAt(i)) > 122)
            //    correct = false;

            if( Character.getNumericValue(email.charAt(i)) == 46 || Character.getNumericValue(email.charAt(i)) >= 48 && Character.getNumericValue(email.charAt(i)) <= 57 ||  Character.getNumericValue(email.charAt(i)) >= 64 && Character.getNumericValue(email.charAt(i)) <= 90 ||  Character.getNumericValue(email.charAt(i)) >= 97 && Character.getNumericValue(email.charAt(i)) <= 122)
            {}
            else
                correct = false;
        }

        return correct;
    }

    public boolean isPasswordUserNameCityCorrect(String password)
    {
        boolean correct = true;

        for(int i = 0 ; i < password.length() ; i++)
        {
            if( Character.getNumericValue(password.charAt(i)) >= 48 && Character.getNumericValue(password.charAt(i)) <= 57 ||  Character.getNumericValue(password.charAt(i)) >= 65 && Character.getNumericValue(password.charAt(i)) <= 90 ||  Character.getNumericValue(password.charAt(i)) >= 97 && Character.getNumericValue(password.charAt(i)) <= 122)
            {}
            else
                correct = false;
        }
        return correct;
    }

    public boolean isSexCorrect(String sex)
    {
        if(sex.length() == 1 && (sex.charAt(0) == 'K' || sex.charAt(0) == 'M'))
            return true;
        else
            return false;
    }

    public boolean isDateCorrect(String date)
    {
        if(date.length() == 10 && date.charAt(2) == '-' && date.charAt(5) == '-' && isNumber(date.charAt(0)) && isNumber(date.charAt(1)) && isNumber(date.charAt(3)) && isNumber(date.charAt(4)) && isNumber(date.charAt(6)) && isNumber(date.charAt(7)) && isNumber(date.charAt(8)) && isNumber(date.charAt(9)))
            return true;
        else
            return false;
    }

    public boolean isNumber(char number)
    {
        if(Character.getNumericValue(number) >= 48 && Character.getNumericValue(number) <= 57)
            return true;
        else
            return false;
    }
}
