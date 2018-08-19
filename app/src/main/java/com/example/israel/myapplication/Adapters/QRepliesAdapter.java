package com.example.israel.myapplication.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.israel.myapplication.Model.CardResponse;
import com.example.israel.myapplication.Model.QReply;
import com.example.israel.myapplication.R;

import java.util.ArrayList;

public class QRepliesAdapter extends RecyclerView.Adapter<QRepliesAdapter.MyViewHolder> {

    ArrayList<QReply> qReplies = new ArrayList<>();

    public QRepliesAdapter(ArrayList<QReply> qReplies) {
        this.qReplies = qReplies;
    }

    @Override
    public QRepliesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.botones, parent, false);
        return new QRepliesAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(QRepliesAdapter.MyViewHolder holder, int position) {
        holder.btn.setText(qReplies.get(position).getTitle_opc());

    }

    @Override
    public int getItemCount() {
        return qReplies.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        Button btn;

        public MyViewHolder(View itemView) {
            super(itemView);
            btn = (Button) itemView.findViewById(R.id.btn_reply);
        }
    }
}