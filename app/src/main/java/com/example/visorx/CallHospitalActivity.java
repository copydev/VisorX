package com.example.visorx;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class CallHospitalActivity extends AppCompatActivity {

    ListView listView;
    public final static int REQUEST_PHONE_CALL = 1000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_hospital);
        listView = findViewById(R.id.callListView);
        //listView.setAdapter(new MyListAdapter(this,R.layout.hospital_number_list_item));
        final Hospital hospital = new Hospital();
        final ArrayList<String> list = new ArrayList<>();
        int pos=0;
        for(String i:hospital.getPhoneNum()){
            if(i != null){
                list.add(hospital.getName(pos));
            }
            pos++;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (ContextCompat.checkSelfPermission(CallHospitalActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(CallHospitalActivity.this, new String[]{Manifest.permission.CALL_PHONE},REQUEST_PHONE_CALL);
                }
                else
                {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + list.get(position)));
                    startActivity(intent);
                }


            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_left,R.anim.slide_right_out);
    }
}
