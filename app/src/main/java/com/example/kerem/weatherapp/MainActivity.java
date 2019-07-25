package com.example.kerem.weatherapp;


import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {
    TextView city;
    TextView weather;
    TextView pressure;
    TextView humidity;
    TextView wind;
    TextView details;
    ImageView image;
    String show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        city = (TextView)findViewById(R.id.city);
        weather = (TextView)findViewById(R.id.weather);
        pressure = (TextView)findViewById(R.id.pressure);
        humidity = (TextView)findViewById(R.id.humidity);
        wind = (TextView)findViewById(R.id.wind);
        details = (TextView)findViewById(R.id.details);
        image = (ImageView)findViewById(R.id.ImgView);

        taskLoadUp(show);

    }


    public void taskLoadUp(String query) {
        Download task = new Download();
        task.execute(query);
    }

    class Download extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String url = Network.getData("http://api.openweathermap.org/data/2.5/weather?q=Istanbul&units=metric&appid=" +
                    "6a834403759e6591a33df3ebb9285b18");
            return url;
        }

        @Override
        protected void onPostExecute(String url) {

            try{

                JSONObject JSON = new JSONObject(url);
                if(JSON != null){
                    JSONObject main = JSON.getJSONObject("main");
                    JSONObject winddetails = JSON.getJSONObject("wind");
                    JSONObject weatherdetails = JSON.getJSONArray("weather").getJSONObject(0);


                    city.setText(JSON.getString("name"));
                    weather.setText(main.getInt("temp") + "°");
                    pressure.setText("Pressure\n"+main.getString("pressure"));
                    humidity.setText("Humidity\n"+main.getString("humidity"));
                    wind.setText("Wind Speed\n"+winddetails.getString("speed"));
                    details.setText(weatherdetails.getString("description"));
                    String icon = weatherdetails.getString("icon");


                    String imageurl = "http://openweathermap.org/img/wn/"+icon+"@2x.png";

                    Picasso.get().load(imageurl).into(image);


                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


}