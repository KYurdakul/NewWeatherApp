package com.example.kerem.weatherapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class ChangeLocation extends AppCompatActivity {
    public static final String TEXT ="com.example.kerem.sample.TEXT";
    public static final String TEXT1 ="com.example.kerem.sample.TEXT1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_location);


        Button button = (Button)findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainActivity();
            }
        });
    }

    public void openMainActivity(){
        EditText latitude =(EditText)findViewById(R.id.lat);
        EditText longitude =(EditText)findViewById(R.id.lon);

        double lattext = 0;
        double lontext=0;

        lattext = Double.parseDouble(latitude.getText().toString());
        lontext = Double.parseDouble(longitude.getText().toString());

        if((lattext<-90 || lattext>90)||(lontext<-180 || lontext>180)){
            Toast.makeText(getApplicationContext(),"Wrong Coordinates",Toast.LENGTH_LONG).show();
            }

        else {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(TEXT, lattext);
            intent.putExtra(TEXT1, lontext);
            startActivity(intent);
        }
    }
}
