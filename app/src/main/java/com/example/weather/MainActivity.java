package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androdocs.httprequest.HttpRequest;

import org.json.JSONObject;

import java.text.DecimalFormat;




public class MainActivity extends AppCompatActivity {
    private EditText city ;
    private TextView temp, humidity, wind, pressure;
    private Button find;
    private String API = "f553e411d4cc589807ac64e4714da846", cityName;
    private LinearLayout linearLayout ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        linearLayout = findViewById(R.id.display);
        linearLayout.setVisibility(View.INVISIBLE);
        city = findViewById(R.id.editText);
        find = findViewById(R.id.button);
        temp = findViewById(R.id.temp);
        humidity = findViewById(R.id.humidity);
        wind = findViewById(R.id.wind);
        pressure = findViewById(R.id.pressure);


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
                JSONObject windSpeed = json.getJSONObject("wind");

                float t = Float.parseFloat( main.get("temp").toString() ) - 273.15f;

                temp.setText( new DecimalFormat("##.##").format(t) +"°C");
                humidity.setText(main.get("humidity").toString() +"% humidity");
                pressure.setText(main.get("pressure").toString()+" hpa");
                wind.setText(windSpeed.get("speed").toString()+" m/s");
                linearLayout.setVisibility(View.VISIBLE);

            }catch (Exception e){
                Log.d("Exception",e.getMessage());
            }
        }
    }
    private void findTemp() {
        new WeatherInfo().execute();
    }
}
