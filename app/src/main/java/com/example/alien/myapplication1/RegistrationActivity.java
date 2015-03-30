package com.example.alien.myapplication1;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alien.myapplication1.account.LogIn;
import com.example.alien.myapplication1.account.Registration;

import java.util.Calendar;


public class RegistrationActivity extends Activity {

    private EditText etEmail, etPassword, etPasswordConfirm, etUserName, etCity;
    private RadioButton female, male;
    private Button saveSignUp, clear;
    private Button datePickerButton;
    private TextView date;

    private int year;
    private int month;
    private int day;
    private boolean dateChanged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etPasswordConfirm = (EditText) findViewById(R.id.etPasswordConfirm);
        etUserName = (EditText) findViewById(R.id.etUserName);
        etCity = (EditText) findViewById(R.id.etCity);

        female = (RadioButton) findViewById(R.id.female);
        male = (RadioButton) findViewById(R.id.male);

        saveSignUp = (Button) findViewById(R.id.saveSignUp);
        clear = (Button) findViewById(R.id.clear);

        datePickerButton = (Button) findViewById(R.id.datePickerButton);
        date = (TextView) findViewById(R.id.date);
        dateChanged = false;
        setCurrentDateOnView();

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
                    String city = etCity.getText().toString();

                    String sex = female.isChecked() ? "K" : "M";

                    if(!isEmailCorrect(email))
                        Toast.makeText(getApplicationContext(),"Email nieprawidłowy!", Toast.LENGTH_SHORT).show();
                    else if(!isPasswordCorrect(password))
                        Toast.makeText(getApplicationContext(),"Hasło nie spełnia wymagań!\nDozwolone litery i cyfry", Toast.LENGTH_SHORT).show();
                    else if(!isUserNameCorrect(user_name))
                        Toast.makeText(getApplicationContext(),"Nazwa użytkownika nie spełnia wymagań!\nDozwolone litery i cyfry", Toast.LENGTH_SHORT).show();
                    else if(!isCityCorrect(city))
                        Toast.makeText(getApplicationContext(),"Nazwa miasta nie spełnia wymagań!\nDozwolone litery.", Toast.LENGTH_SHORT).show();
                    else if(dateChanged) {
                        String date = day+"-"+month+"-"+year;
                        new Registration(getApplicationContext(), 1).execute(email, password, user_name, date, sex, city);
                    }
                    else
                        new LogIn(getApplicationContext(),1).execute(email, password, user_name, "", sex, city);

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
                setCurrentDateOnView();
                etCity.setText("");
                female.setChecked(false);
                male.setChecked(false);

            }
        });

        datePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(1);
            }
        });
    }

    public boolean isEmailCorrect(String email)
    {
        if(!email.contains("@"))
            return false;
        if(!email.contains("."))
            return false;
        if(email.charAt(0) == '@' || email.charAt(0) == '.' || email.charAt(email.length()-1) == '@' || email.charAt(email.length()-1) == '.')
            return false;
        if(email.lastIndexOf(".") < email.indexOf("@"))
            return false;

        for(int i = 0 ; i < email.length() ; i++) {
            if ((Character.getNumericValue(email.charAt(i)) == 46 || Character.getNumericValue(email.charAt(i)) >= 48 && Character.getNumericValue(email.charAt(i)) <= 57 || Character.getNumericValue(email.charAt(i)) >= 64 && Character.getNumericValue(email.charAt(i)) <= 90 || Character.getNumericValue(email.charAt(i)) >= 97 && Character.getNumericValue(email.charAt(i)) <= 122)) {
                {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isPasswordCorrect(String password)
    {
        if(password.length()>16||password.length()<6)
            return false;
        for(int i = 0 ; i < password.length() ; i++)
        {
            if( Character.getNumericValue(password.charAt(i)) >= 48 && Character.getNumericValue(password.charAt(i)) <= 57 ||  Character.getNumericValue(password.charAt(i)) >= 65 && Character.getNumericValue(password.charAt(i)) <= 90 ||  Character.getNumericValue(password.charAt(i)) >= 97 && Character.getNumericValue(password.charAt(i)) <= 122)
            {
                return false;
            }
        }
        return true;
    }
    public boolean isUserNameCorrect(String name)
    {
        if(name.length()>16||name.length()<6)
            return false;
        for(int i = 0 ; i < name.length() ; i++)
        {
            if( Character.getNumericValue(name.charAt(i)) >= 48 && Character.getNumericValue(name.charAt(i)) <= 57 ||  Character.getNumericValue(name.charAt(i)) >= 65 && Character.getNumericValue(name.charAt(i)) <= 90 ||  Character.getNumericValue(name.charAt(i)) >= 97 && Character.getNumericValue(name.charAt(i)) <= 122)
            {
                return false;
            }
        }
        return true;
    }
    public boolean isCityCorrect(String city)
    {
        if(city.length()>30)
            return false;
        for(int i = 0 ; i < city.length() ; i++)
        {
            if( Character.getNumericValue(city.charAt(i)) >= 48 && Character.getNumericValue(city.charAt(i)) <= 57 ||  Character.getNumericValue(city.charAt(i)) >= 65 && Character.getNumericValue(city.charAt(i)) <= 90 ||  Character.getNumericValue(city.charAt(i)) >= 97 && Character.getNumericValue(city.charAt(i)) <= 122)
            {
                return false;
            }
        }
        return true;
    }

    public boolean isSexCorrect(String sex)
    {
        if(sex.equals(""))
            return true;
        if(sex.length() == 1 && (sex.charAt(0) == 'K' || sex.charAt(0) == 'M'))
            return true;
        else
            return false;
    }

    public boolean isDateCorrect(String date)
    {
        if(date.equals(""))
            return true;
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

    public void setCurrentDateOnView()
    {
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH)+1;
        day = c.get(Calendar.DAY_OF_MONTH);


        // set current date into datepicker
        //datePicker.init(year, month, day, null);
        datePickerButton.setText(day+"-"+month+"-"+year);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
       return new DatePickerDialog(this, pickerListener, year, month,day);
    }

    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {

            if(selectedDay != day || selectedMonth != month || selectedYear != year)
                dateChanged = true;

            year  = selectedYear;
            month = selectedMonth;
            day   = selectedDay;

            datePickerButton.setText(new StringBuilder()
                    // Month is 0 based, just add 1
                    .append(day).append("-").append(month + 1).append("-")
                    .append(year).append(" "));
        }
    };
}
