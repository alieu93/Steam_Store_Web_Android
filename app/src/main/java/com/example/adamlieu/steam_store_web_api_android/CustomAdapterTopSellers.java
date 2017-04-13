package com.example.adamlieu.steam_store_web_api_android;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by Adam Lieu on 3/17/2017.
 */

public class CustomAdapterTopSellers extends RecyclerView.Adapter<CustomAdapterTopSellers.MyViewHolder> {
    public WebView webview;
    private ArrayList<TopSellerGames> dataset;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewDate;
        TextView textViewPlatform;
        private Context context;


        public MyViewHolder(final View itemView){
            super(itemView);
            context = itemView.getContext();
            this.textViewName = (TextView) itemView.findViewById(R.id.textName);
            this.textViewDate = (TextView) itemView.findViewById(R.id.textDate);
            this.textViewPlatform = (TextView) itemView.findViewById(R.id.textPlatform);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    int position = getAdapterPosition();
                    //Log.v("URL: ", dataset.get(position).getStoreURL());
                    Intent intent = new Intent(itemView.getContext(), WebViewController.class);
                    intent.putExtra("URL", dataset.get(position).getStoreURL());
                    context.startActivity(intent);
                }
            });
        }
    }

    public CustomAdapterTopSellers(ArrayList<TopSellerGames> data){
        this.dataset = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_layout, parent, false);
        view.setOnClickListener(NewReleasesFragment.myOnClickListener);
        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition){
        TextView textViewName = holder.textViewName;
        TextView textViewDate = holder.textViewDate;
        TextView textViewPlatform = holder.textViewPlatform;

        textViewName.setText(dataset.get(listPosition).getTitleName());
        textViewDate.setText(dataset.get(listPosition).getReleaseDate());
        textViewPlatform.setText(dataset.get(listPosition).getPlatform());

    }

    @Override
    public int getItemCount(){
        return dataset.size();
    }
}