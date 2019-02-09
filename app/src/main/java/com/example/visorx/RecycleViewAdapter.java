package com.example.visorx;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.myViewHolder>{

    private Context context;
    private Hospital hospital;
    private int lastPos = -1;

    RecycleViewAdapter(Context context){
        this.context = context;
        hospital = new Hospital();
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.hospital_list_item,parent,false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder myViewHolder, final int pos) {

        myViewHolder.address.setText(hospital.getAddress(pos));
        myViewHolder.name.setText(hospital.getName(pos));

        if(myViewHolder.getAdapterPosition() > lastPos){
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_right);
            myViewHolder.cardView.startAnimation(animation);
            lastPos = myViewHolder.getAdapterPosition();
        }

        myViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,HospitalMapsActivity.class);
                intent.putExtra("POS",pos);
                context.startActivity(intent);
                Activity activity = (Activity) context;
                activity.overridePendingTransition(R.anim.slide_right,R.anim.slide_left_out);
            }
        });

    }

    @Override
    public int getItemCount() {
        return hospital.getHospitalName().size();
    }


    class myViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView address;
        CardView cardView;

        myViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.listItemCV);
            name = itemView.findViewById(R.id.hospitalName);
            address = itemView.findViewById(R.id.hospitalAddress);
        }
    }

}
