package com.example.alien.myapplication1.account;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alien.myapplication1.R;
import com.example.alien.myapplication1.Speech.MicroListener;
import com.example.alien.myapplication1.Speech.SpeechInterface;

import java.util.Calendar;


public class RegistrationActivity extends ActionBarActivity implements MicroListener{

    private EditText etEmail, etPassword, etPasswordConfirm, etUserName, etCity;
    private RadioButton female, male;
    private Button saveSignUp, clear;
    private Button datePickerButton;
    protected TextView date;

    private int year;
    private int month;
    private int day;
    private boolean dateChanged;
    private SpeechInterface speechInterface;

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
        speechInterface = new SpeechInterface(this, getClass().getSimpleName(),this);
    }

    private void register()
    {
        if (etPassword.getText().toString().equals(etPasswordConfirm.getText().toString())) {
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();
            String user_name = etUserName.getText().toString();
            String city = etCity.getText().toString();

            String sex = female.isChecked() ? "K" : "M";

            if (!isEmailCorrect(email)) {
                Toast.makeText(getApplicationContext(), "Email nieprawidłowy!", Toast.LENGTH_SHORT).show();
            }
            else if (!isPasswordCorrect(password)) {
                Toast.makeText(getApplicationContext(), "Hasło nie spełnia wymagań!\nDozwolone litery i cyfry",
                        Toast.LENGTH_SHORT).show();
            }
            else if (!isUserNameCorrect(user_name)) {
                Toast.makeText(getApplicationContext(), "Nazwa użytkownika nie spełnia wymagań!\nDozwolone litery i cyfry",
                        Toast.LENGTH_SHORT).show();
            }
            else if (!isCityCorrect(city)) {
                Toast.makeText(getApplicationContext(), "Nazwa miasta nie spełnia wymagań!\nDozwolone litery.",
                        Toast.LENGTH_SHORT).show();
            }
            else if (dateChanged) {
                String date = year + "-" + month + "-" + day;
                new Registration(getApplicationContext()).execute(email, password, user_name, date, sex, city);
            }
            else {
                new Registration(getApplicationContext()).execute(email, password, user_name, "", sex, city);
            }
        }
        else{
            Toast.makeText(getApplicationContext(), "Hasło i potwierdzenie hasła nie są takie same !", Toast.LENGTH_SHORT).show();
            etPassword.setText("");
            etPasswordConfirm.setText("");
        }
    }
    public void listener() {
        saveSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear();
            }
        });

        datePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //noinspection deprecation
                showDialog(1);
            }
        });
    }
    private void clear()
    {
        etEmail.setText("");
        etPassword.setText("");
        etPasswordConfirm.setText("");
        etUserName.setText("");
        setCurrentDateOnView();
        etCity.setText("");
        female.setChecked(false);
        male.setChecked(false);
    }

    public boolean isEmailCorrect(String email) {
        if (!email.contains("@")) {
            return false;
        }
        if (!email.contains(".")) {
            return false;
        }
        if (email.charAt(0) == '@' || email.charAt(0) == '.' || email.charAt(email.length()-1) == '@'
                || email.charAt(email.length()-1) == '.') {
            return false;
        }
        if (email.lastIndexOf(".") < email.indexOf("@")) {
            return false;
        }

        for (int i = 0 ; i < email.length() ; i++) {
            if ((Character.getNumericValue(email.charAt(i)) == 46 || Character.getNumericValue(email.charAt(i)) >= 48 &&
                    Character.getNumericValue(email.charAt(i)) <= 57 || Character.getNumericValue(email.charAt(i)) >= 64 &&
                    Character.getNumericValue(email.charAt(i)) <= 90 || Character.getNumericValue(email.charAt(i)) >= 97 &&
                    Character.getNumericValue(email.charAt(i)) <= 122)) {
                return false;
            }
        }

        return true;
    }

    public boolean isPasswordCorrect(String password) {
        if (password.length() > 16 || password.length() < 6) {
            return false;
        }
        for (int i = 0 ; i < password.length() ; i++) {
            if (Character.getNumericValue(password.charAt(i)) >= 48 && Character.getNumericValue(password.charAt(i)) <= 57 ||
                    Character.getNumericValue(password.charAt(i)) >= 65 && Character.getNumericValue(password.charAt(i)) <= 90 ||
                    Character.getNumericValue(password.charAt(i)) >= 97 && Character.getNumericValue(password.charAt(i)) <= 122) {
                return false;
            }
        }

        return true;
    }

    public boolean isUserNameCorrect(String name) {
        if (name.length() > 16 || name.length() < 6) {
            return false;
        }
        for (int i = 0; i < name.length(); i++) {
            if (Character.getNumericValue(name.charAt(i)) >= 48 && Character.getNumericValue(name.charAt(i)) <= 57 ||
                    Character.getNumericValue(name.charAt(i)) >= 65 && Character.getNumericValue(name.charAt(i)) <= 90 ||
                    Character.getNumericValue(name.charAt(i)) >= 97 && Character.getNumericValue(name.charAt(i)) <= 122) {
                return false;
            }
        }

        return true;
    }

    public boolean isCityCorrect(String city) {
        if (city.length() > 30) {
            return false;
        }

        for (int i = 0; i < city.length(); i++) {
            if (Character.getNumericValue(city.charAt(i)) >= 48 && Character.getNumericValue(city.charAt(i)) <= 57 ||
                    Character.getNumericValue(city.charAt(i)) >= 65 && Character.getNumericValue(city.charAt(i)) <= 90 ||
                    Character.getNumericValue(city.charAt(i)) >= 97 && Character.getNumericValue(city.charAt(i)) <= 122) {
                return false;
            }
        }

        return true;
    }

    public void setCurrentDateOnView() {
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH) + 1;
        day = c.get(Calendar.DAY_OF_MONTH);

        datePickerButton.setText(day + "-" + month + "-" + year);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected Dialog onCreateDialog(int id) {
       return new DatePickerDialog(this, pickerListener, year, month, day);
    }

    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
            if(selectedDay != day || selectedMonth != month || selectedYear != year) {
                dateChanged = true;
            }

            year  = selectedYear;
            month = selectedMonth;
            day   = selectedDay;

            datePickerButton.setText(new StringBuilder()
                    .append(day).append("-").append(month + 1).append("-")
                    .append(year).append(" "));
        }
    };

    @Override
    public void microCommandRun(int result) {
        speechInterface.tell(result+"");
        switch(result){
            case 0:
                finish();
                break;
            case 1:
                register();
                break;
            case 2:
                clear();
                break;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_charts, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.listenMicro)
        {
            speechInterface.listenCommand();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        speechInterface.destroy();
        super.onDestroy();
    }

}