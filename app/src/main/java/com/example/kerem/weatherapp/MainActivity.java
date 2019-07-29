package com.example.kerem.weatherapp;


import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class MainActivity extends AppCompatActivity implements LocationListener{

    LocationManager locationManager;

    static double lat, lng;

    TextView location;
    TextView weather;
    TextView pressure;
    TextView humidity;
    TextView wind;
    TextView details;

    SwipeRefreshLayout swipe;

    String show;


    int[] text = {R.id.text1,
            R.id.text2,
            R.id.text3,
            R.id.text4,
            R.id.text5};

    int[] image = {R.id.image1,
            R.id.image2,
            R.id.image3,
            R.id.image4,
            R.id.image5,};

    int[] detail = {R.id.detail1,
            R.id.detail2,
            R.id.detail3,
            R.id.detail4,
            R.id.detail5};

    int[] day = {R.id.day1,
            R.id.day2,
            R.id.day3,
            R.id.day4,
            R.id.day5};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        location = (TextView)findViewById(R.id.location);
        weather = (TextView) findViewById(R.id.weather);
        pressure = (TextView) findViewById(R.id.pressure);
        humidity = (TextView) findViewById(R.id.humidity);
        wind = (TextView) findViewById(R.id.wind);
        details = (TextView) findViewById(R.id.details);
        swipe = (SwipeRefreshLayout) findViewById(R.id.Swipe);


        getLocation();
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                taskLoadUp(show);
                swipe.setRefreshing(false);
            }
        });

        taskLoadUp(show);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

        }

    }



    public void taskLoadUp(String query) {
        if(com.example.kerem.weatherapp.Network.Internet(getApplicationContext())){
            Download task = new Download();
            task.execute(query);}
        else{
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }
    }



    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        lat = location.getLatitude();
        lng = location.getLongitude();

        new Download().execute(String.valueOf(lat),String.valueOf(lng));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }



    class Download extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... args) {

            String url1 = com.example.kerem.weatherapp.Network.getData("http://api.openweathermap.org/data/2.5/forecast?lat="+lat+"&lon="+lng+"&lang=tr&units=metric&appid=6a834403759e6591a33df3ebb9285b18");
            return url1;
        }



        @Override
        protected void onPostExecute(String url) {

            for(int i = 0; i < 5; i++) {
                TextView date = (TextView)findViewById(day[i]);
                SimpleDateFormat formatter = new SimpleDateFormat("dd MMM\n EEE ");
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DAY_OF_MONTH, i);
                Date today = cal.getTime();
                String d = formatter.format(today);
                date.setText(d);
            }


            try{

                JSONObject JSON = new JSONObject(url);
                if(JSON != null){
                    JSONObject list = JSON.getJSONArray("list").getJSONObject(0);
                    JSONObject main = list.getJSONObject("main");
                    JSONObject weatherdetails = list.getJSONArray("weather").getJSONObject(0);
                    JSONObject winddetails = list.getJSONObject("wind");
                    JSONObject cityname = JSON.getJSONObject("city");


                    for (int i = 0; i < 5; i++) {
                        JSONObject list5 = JSON.getJSONArray("list").getJSONObject(i*8);
                        JSONObject main5 = list5.getJSONObject("main");
                        JSONObject weatherdetails5 = list5.getJSONArray("weather").getJSONObject(0);


                        TextView txt = (TextView)findViewById(text[i]);
                        txt.setText(main5.getInt("temp") + "°");

                        ImageView img = (ImageView)findViewById(image[i]);
                        String icon1 = weatherdetails5.getString("icon");
                        String imageurl1 = "http://openweathermap.org/img/wn/" + icon1 + "@2x.png";
                        Picasso.get().load(imageurl1).into(img);

                        TextView dtl = (TextView)findViewById(detail[i]);
                        dtl.setText(weatherdetails5.getString("description"));

                    }
                    weather.setText(main.getInt("temp") + "°");
                    pressure.setText("Pressure\n" + main.getString("pressure"));
                    humidity.setText("Humidity\n" + main.getString("humidity"));
                    wind.setText("Wind Speed\n" + winddetails.getString("speed"));
                    details.setText(weatherdetails.getString("description"));

                    location.setText(cityname.getString("name"));



                    String icon = weatherdetails.getString("icon");
                    String imageurl = "http://openweathermap.org/img/wn/" + icon + "@2x.png";
                    Picasso.get().load(imageurl).into((ImageView)findViewById(R.id.ImgView));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }



}
