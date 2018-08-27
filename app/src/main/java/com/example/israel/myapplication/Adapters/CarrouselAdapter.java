package com.example.israel.myapplication.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.israel.myapplication.Model.CardResponse;
import com.example.israel.myapplication.R;

import java.util.ArrayList;

public class CarrouselAdapter extends RecyclerView.Adapter<CarrouselAdapter.MyViewHolder> {

    ArrayList<CardResponse> carrousels = new ArrayList<>();
    Context context;

    public CarrouselAdapter(ArrayList<CardResponse> carrousels, Context context) {
        if (carrousels == null) {
            this.carrousels = new ArrayList<CardResponse>();
        } else {
            this.carrousels = carrousels;
        }
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.description.setText(carrousels.get(position).getDescription());
        holder.title.setText(carrousels.get(position).getTitle());
        holder.btn.setText(carrousels.get(position).getText_btn());

        Glide.with(context)
                .load(carrousels.get(position).getUrl_img())
                .into(holder.image);

    }

    @Override
    public int getItemCount() {
        return carrousels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, description;
        Button btn;
        ImageView image;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title_Card);
            description = (TextView) itemView.findViewById(R.id.text_Card);
            image = (ImageView) itemView.findViewById(R.id.img_Card);
            btn = (Button) itemView.findViewById(R.id.btn_Card);
        }
    }
}
