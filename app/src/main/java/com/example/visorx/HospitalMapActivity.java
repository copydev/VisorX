package com.example.visorx;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class HospitalMapActivity extends AppCompatActivity {

    int pos = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_map);

        Bundle extras = getIntent().getExtras();

        if(extras != null){
            pos = extras.getInt("POS");
        }


    }
}
