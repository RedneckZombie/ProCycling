package com.example.alien.myapplication1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

public class SigninActivity  extends AsyncTask<String,Void,String>{

    private TextView statusField,roleField;
    private Context context;
    private int byGetOrPost = 0;
    //flag 0 means get and 1 means post.(By default it is get.)
    public SigninActivity(Context context,int flag) {
        this.context = context;
        byGetOrPost = flag;
    }

    protected void onPreExecute(){

    }
    @Override
    protected String doInBackground(String... arg0) {
        if(byGetOrPost == 0){ //means by Get Method
            try{
                String email = (String)arg0[0];
                String password = (String)arg0[1];
                String user_name = (String)arg0[2];
                //String link = "http://85.17.73.180/android_connect/get_accounts.php";
                String link = "http://rommam.cba.pl/registration.php?email=zibi@gmail.com&password=ania&user_name=zbychu";
                URL url = new URL(link);
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                request.setURI(new URI(link));
                HttpResponse response = client.execute(request);
                BufferedReader in = new BufferedReader
                        (new InputStreamReader(response.getEntity().getContent()));

                StringBuffer sb = new StringBuffer("");
                String line="";
                while ((line = in.readLine()) != null) {
                    sb.append(line);

                }
                in.close();
                return sb.toString();
            }catch(IOException e){
                return new String("IOException: " + e.getMessage());
            }catch(URISyntaxException e){
                return new String("URIException: " + e.getMessage());
            }
        }
        else{
            try{
                String email = (String)arg0[0];
                String password = (String)arg0[1];
                String user_name = (String)arg0[2];
                String dateOfBirth = (String)arg0[3];
                String sex = (String)arg0[4];
                String city = (String)arg0[5];
                //String link="http://85.17.73.180/android_connect/get_accounts.php";
                String link = "http://rommam.cba.pl/registration.php";
                String data  = "email"
                        + "=" + email;
                data += "&" + "password"
                        + "=" + password;
                data += "&" + "user_name"
                        + "=" + user_name;
                data += "&" + "dateOfBirth"
                        + "=" + dateOfBirth;
                data += "&" + "sex"
                        + "=" + sex;
                data += "&" + "city"
                        + "=" + city;
                URL url = new URL(link);
                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter
                        (conn.getOutputStream());
                wr.write( data );
                wr.flush();

                InputStreamReader isr = new InputStreamReader(conn.getInputStream());
                BufferedReader reader = new BufferedReader(isr);
                /*
                StringBuilder sb = new StringBuilder();
                String line = null;
                // Read Server Response
                while((line = reader.readLine()) != null)
                {
                    sb.append(line);
                    break;
                }*/
                return "ds";//sb.toString();
            }catch(UnsupportedEncodingException e){
                return new String("UEEException: " + e.getMessage());
            }
            catch(MalformedURLException e){
                return new String("MUException: " + e.getMessage());
            }catch(IOException e){
                return new String("IOException: " + e.getStackTrace().toString());
            }
        }
    }
    @Override
    protected void onPostExecute(String result){
        System.out.println("OK! -> " + result);
        //this.statusField.setText("Login Successful");
        //this.roleField.setText(result);
    }
}
