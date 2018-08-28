package com.example.israel.myapplication.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.israel.myapplication.Model.CardResponse;
import com.example.israel.myapplication.Model.QReply;
import com.example.israel.myapplication.Presenter.Presenter;
import com.example.israel.myapplication.Presenter.PresenterImpl;
import com.example.israel.myapplication.R;

import java.net.MalformedURLException;
import java.util.ArrayList;

public class QRepliesAdapter extends RecyclerView.Adapter<QRepliesAdapter.MyViewHolder> {

    ArrayList<QReply> qReplies = new ArrayList<>();
    Context context;
    PresenterImpl presenter;

    public QRepliesAdapter(ArrayList<QReply> qReplies, Context context, PresenterImpl presenter) {
        this.qReplies = qReplies;
        this.context = context;
        this.presenter = presenter;
    }

    @Override
    public QRepliesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.botones, parent, false);
        return new QRepliesAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final QRepliesAdapter.MyViewHolder holder, int position) {
        holder.btn.setText(qReplies.get(position).getTitle_opc());
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    presenter.consultarws(holder.btn.getText().toString());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });

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