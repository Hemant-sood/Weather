package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androdocs.httprequest.HttpRequest;

import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {
    private EditText city ;
    private Button find;
    private TextView temp;
    private String API = "f553e411d4cc589807ac64e4714da846", cityName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        city = findViewById(R.id.editText);
        find = findViewById(R.id.button);
        temp = findViewById(R.id.textView);
        String res = HttpRequest.excuteGet("http://api.openweathermap.org/data/2.5/weather?q=delhi,in&APPID="+API);
        temp.setText(res);
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 cityName = city.getText().toString();


                if( ! cityName.equals("")){
                    findTemp();
                }

            }
        });
    }

    class WeatherInfo extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(MainActivity.this, "Started...",Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... String) {
            String result = HttpRequest.excuteGet("http://api.openweathermap.org/data/2.5/weather?q=" +cityName+"&APPID="+API);
            return  result;
        }

        @Override
        protected void onPostExecute(String res){
            try{
                JSONObject json  = new JSONObject(res);
                JSONObject main = json.getJSONObject("main");
                double temperatur = Double.parseDouble( main.get("temp").toString() ) - 273.15;
                temp.setText(String.valueOf(temperatur));
            }catch (Exception e){
                Log.d("Exception",e.getMessage());
            }
        }
    }
    private void findTemp() {
        new WeatherInfo().execute();
    }
}
