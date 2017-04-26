package com.example.yash.intellih_alpha1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Admin on 13-04-2017.
 */

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private ArrayList<DataModel> dataSet;
    private final Context context;


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView roomName;
        ImageView imageViewIcon;
        CardView cardView;

        public MyViewHolder(final View itemView) {
            super(itemView);
            this.roomName = (TextView) itemView.findViewById(R.id.room_name);
            this.imageViewIcon = (ImageView) itemView.findViewById(R.id.room_icon);
            this.cardView = (CardView) itemView.findViewById(R.id.card_view);
        }
    }


    @Override
    public CustomAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardlayout, parent, false);

        view.setOnClickListener(MainActivity.myOnClickListener);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;

    }

    public CustomAdapter(Context context, ArrayList<DataModel> data) {
        this.dataSet = data;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(CustomAdapter.MyViewHolder holder, final int position) {
        TextView textViewName = holder.roomName;
        ImageView imageView = holder.imageViewIcon;
        CardView cardView = holder.cardView;

        textViewName.setText(dataSet.get(position).getRoom());
        imageView.setImageResource(dataSet.get(position).getImage());

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (dataSet.get(position).getRoom()) {
                    case "Drawing Room": {
                        v.getContext().startActivity(new Intent(context.getApplicationContext(), DrawingRoomActivity.class));
                        break;
                    }

                    default:
                        v.getContext().startActivity(new Intent(context.getApplicationContext(), DrawingRoomActivity.class));
                        break;
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
