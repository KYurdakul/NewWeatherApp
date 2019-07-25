package com.example.kerem.weatherapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.kerem.weatherapp.MainActivity;
import com.example.kerem.weatherapp.R;

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
        double lattext= Double.parseDouble(latitude.getText().toString());

        EditText longitude =(EditText)findViewById(R.id.lon);
        double lontext= Double.parseDouble(longitude.getText().toString());

        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra(TEXT,lattext);
        intent.putExtra(TEXT1,lontext);
        startActivity(intent);
    }
}
