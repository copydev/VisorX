package com.example.visorx;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.visorx.Hospital;

import java.util.ArrayList;
import java.util.List;


public class MyListAdapter extends ArrayAdapter<String> {

    
    private Hospital hospital;
    ArrayList<String> list;
    private Context context;
    private int resource;

    public MyListAdapter(Context context, int resource) {
        super(context, resource);
        hospital = new Hospital();
        this.context = context;
        this.resource = resource;
        list = hospital.getHospitalName();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        convertView = layoutInflater.inflate(resource,parent,false);

        TextView hospName = convertView.findViewById(R.id.callHospName);

        hospName.setText(list.get(position));

        return convertView;
    }
}
