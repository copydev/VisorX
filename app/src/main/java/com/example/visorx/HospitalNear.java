package com.example.visorx;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class HospitalNear extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_near);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_left,R.anim.slide_right_out);
    }
}
