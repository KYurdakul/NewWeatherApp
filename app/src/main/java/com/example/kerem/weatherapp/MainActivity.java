package com.example.kerem.weatherapp;


import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class MainActivity extends AppCompatActivity {
    TextView city;
    TextView weather;
    TextView pressure;
    TextView humidity;
    TextView wind;
    TextView details;

    SwipeRefreshLayout swipe;


    String show;


    int[] text ={R.id.text1,
            R.id.text2,
            R.id.text3,
            R.id.text4,
            R.id.text5};

    int[] image ={R.id.image1,
            R.id.image2,
            R.id.image3,
            R.id.image4,
            R.id.image5,};

    int[] detail ={R.id.detail1,
            R.id.detail2,
            R.id.detail3,
            R.id.detail4,
            R.id.detail5};

    int[] day ={R.id.day1,
            R.id.day2,
            R.id.day3,
            R.id.day4,
            R.id.day5};




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
        swipe = (SwipeRefreshLayout)findViewById(R.id.Swipe);


        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                taskLoadUp(show);
                swipe.setRefreshing(false);
            }
        });



        taskLoadUp(show);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.item1:
                Intent intent = new Intent(this,ChangeLocation.class);
                this.startActivity(intent);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    public void taskLoadUp(String query) {
        if(com.example.kerem.weatherapp.Network.Internet(getApplicationContext())){
            Download task = new Download();
            task.execute(query);}
        else{
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }
    }




    class Download extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... args) {
            Intent intent = getIntent();
            double lat = intent.getDoubleExtra(ChangeLocation.TEXT,41.0138);
            double lon = intent.getDoubleExtra(ChangeLocation.TEXT1,28.9497);
            String url1 = com.example.kerem.weatherapp.Network.getData("http://api.openweathermap.org/data/2.5/forecast?lat="+lat+"&lon="+lon+"&lang=tr&units=metric&appid=6a834403759e6591a33df3ebb9285b18");
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
                    JSONObject city1 = JSON.getJSONObject("city");

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
                    city.setText(city1.getString("name"));


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