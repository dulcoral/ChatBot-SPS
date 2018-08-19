package com.example.israel.myapplication.Adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.israel.myapplication.Model.ChatBubble;
import com.example.israel.myapplication.Model.CardResponse;
import com.example.israel.myapplication.Model.QReply;
import com.example.israel.myapplication.R;

import java.util.ArrayList;

import static com.example.israel.myapplication.MainActivity.getChatDialog;
import static com.example.israel.myapplication.MainActivity.getHorizontalData;
import static com.example.israel.myapplication.MainActivity.getQReplies;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<Object> items;
    private final int SIMPLEMESSAGE = 1;
    private final int CARROUSEL = 2;
    private Boolean myMessage;
    private final int BTN = 3;

    public ChatAdapter(Context context, ArrayList<Object> items, Boolean myMessage) {
        this.context = context;
        this.items = items;
        this.myMessage = myMessage;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        RecyclerView.ViewHolder holder;
        switch (viewType) {
            case SIMPLEMESSAGE:
                if (myMessage) {
                    view = inflater.inflate(R.layout.left_chat_bubble, parent, false);
                } else {
                    view = inflater.inflate(R.layout.right_chat_bubble, parent, false);
                }
                holder = new ChatBubbleViewHolder(view);
                break;

            case CARROUSEL:
                view = inflater.inflate(R.layout.carrusel_layout, parent, false);
                holder = new HorizontalViewHolder(view);
                break;
            case BTN:
                view = inflater.inflate(R.layout.quick_replies, parent, false);
                holder = new QRepliesViewHolder(view);
                break;
            default:
                if (myMessage) {
                    view = inflater.inflate(R.layout.left_chat_bubble, parent, false);
                } else {
                    view = inflater.inflate(R.layout.right_chat_bubble, parent, false);
                }
                holder = new ChatBubbleViewHolder(view);
                break;
        }


        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == CARROUSEL)
            carrouselView((HorizontalViewHolder) holder);
        else if (holder.getItemViewType() == BTN)
            qRepliesView((QRepliesViewHolder) holder);
        else if(holder.getItemViewType() == SIMPLEMESSAGE)
            chatBView((ChatBubbleViewHolder) holder);

    }
    private void chatBView(ChatBubbleViewHolder holder) {
        ChatBubble chatB = getChatDialog();
        holder.msg.setText(chatB.getContent());
    }
    private void qRepliesView(QRepliesViewHolder holder) {
        QRepliesAdapter adapter = new QRepliesAdapter(getQReplies());
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        holder.recyclerView.setAdapter(adapter);
    }

    private void carrouselView(HorizontalViewHolder holder) {
        CarrouselAdapter adapter = new CarrouselAdapter(getHorizontalData());
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        holder.recyclerView.setAdapter(adapter);
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position) instanceof ChatBubble)
            return SIMPLEMESSAGE;
        if (items.get(position) instanceof QReply)
            return BTN;
        if (items.get(position) instanceof CardResponse)
            return CARROUSEL;
        return -1;
    }

    public class HorizontalViewHolder extends RecyclerView.ViewHolder {

        RecyclerView recyclerView;

        HorizontalViewHolder(View itemView) {
            super(itemView);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.inner_recyclerView);
        }
    }

    public class ChatBubbleViewHolder extends RecyclerView.ViewHolder {
        private TextView msg;

        ChatBubbleViewHolder(View itemView) {
            super(itemView);
            msg = (TextView) itemView.findViewById(R.id.txt_msg);
        }
    }

    public class QRepliesViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;

        QRepliesViewHolder(View itemView) {
            super(itemView);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.inner_recyclerView);
        }
    }

}
