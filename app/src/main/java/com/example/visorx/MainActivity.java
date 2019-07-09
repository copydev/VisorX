package com.example.visorx;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.animation.Animation;

public class MainActivity extends AppCompatActivity {

    CardView helpCV,hospNearCV,xrayCV,callCV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        helpCV = findViewById(R.id.cardView2);
        hospNearCV = findViewById(R.id.cardView1);
        xrayCV = findViewById(R.id.xrayCardView);
        callCV = findViewById(R.id.cardView3);




        setOnClickListeners();

    }

    private void setOnClickListeners() {

        hospNearCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,HospitalNear.class));
                overridePendingTransition(R.anim.slide_right,R.anim.slide_left_out);
            }
        });

        helpCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, HelpActivity.class));
                overridePendingTransition(R.anim.slide_right,R.anim.slide_left_out);
            }
        });

        xrayCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, XrayActivity.class));
                overridePendingTransition(R.anim.slide_right,R.anim.slide_left_out);
            }
        });

        callCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CallHospitalActivity.class));
                overridePendingTransition(R.anim.slide_right,R.anim.slide_left_out);
            }
        });

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_left,R.anim.slide_right_out);
    }
}
