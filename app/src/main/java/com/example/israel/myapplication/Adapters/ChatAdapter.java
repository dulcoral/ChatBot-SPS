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
import java.util.List;


public class ChatAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<Object> items;
    private final int MESSAGESEND = 0;
    private final int SIMPLEMESSAGE = 1;
    private final int CARROUSEL = 2;
    private final int BTN = 3;

    public ChatAdapter(Context context, List<Object> items) {
        this.context = context;
        if (items == null) {
            this.items = new ArrayList<Object>();
        } else {
            this.items = items;
        }
    }

    public void add(Object message) {
        items.add(message);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        RecyclerView.ViewHolder holder;
        switch (viewType) {
            case SIMPLEMESSAGE:
                view = inflater.inflate(R.layout.left_chat_bubble, parent, false);
                return new ChatBubbleViewHolder(view);
            case MESSAGESEND:
                view = inflater.inflate(R.layout.right_chat_bubble, parent, false);
                return new ChatBubbleViewHolder(view);
            case CARROUSEL:
                view = inflater.inflate(R.layout.carrusel_layout, parent, false);
                return new CarrouselViewHolder(view);
            case BTN:
                view = inflater.inflate(R.layout.quick_replies, parent, false);
                return new QRepliesViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case SIMPLEMESSAGE:
                ChatBubble message = (ChatBubble) items.get(position);
                ((ChatBubbleViewHolder) holder).bind(message);
                break;
            case CARROUSEL:
                ArrayList<CardResponse> cardResponses = (ArrayList<CardResponse>) items.get(position);
                ((CarrouselViewHolder) holder).bind(cardResponses);
                break;
            case MESSAGESEND:
                ChatBubble messageR = (ChatBubble) items.get(position);
                ((ChatBubbleViewHolder) holder).bind(messageR);
                break;
            case BTN:
                ArrayList<QReply> qReplies = (ArrayList<QReply>) items.get(position);
                ((QRepliesViewHolder) holder).bind(qReplies);
                break;
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position) instanceof ChatBubble) {
            ChatBubble chatBubble = (ChatBubble) items.get(position);
            if (chatBubble.myMessage())
                return SIMPLEMESSAGE;
            else
                return MESSAGESEND;
        }
        if (items.get(position) instanceof ArrayList) {
            if (((ArrayList<?>) items.get(position)).get(0) instanceof CardResponse) {
                return CARROUSEL;
            } else
                return BTN;
        }
        return -1;
    }

    public class CarrouselViewHolder extends RecyclerView.ViewHolder {

        RecyclerView recyclerView;

        CarrouselViewHolder(View itemView) {
            super(itemView);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.inner_recyclerView);
        }

        public void bind(ArrayList<CardResponse> cardResponses) {
            CarrouselAdapter adapter = new CarrouselAdapter(cardResponses);
            recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            recyclerView.setAdapter(adapter);
        }

    }

    public class ChatBubbleViewHolder extends RecyclerView.ViewHolder {
        private TextView msg;

        ChatBubbleViewHolder(View itemView) {
            super(itemView);
            msg = (TextView) itemView.findViewById(R.id.txt_msg);
        }

        public void bind(ChatBubble message) {
            msg.setText(message.getContent());

        }
    }

    public class QRepliesViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;

        QRepliesViewHolder(View itemView) {
            super(itemView);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.inner_recyclerView);
        }

        public void bind(ArrayList<QReply> qReplies) {
            QRepliesAdapter adapter = new QRepliesAdapter(qReplies);
            recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            recyclerView.setAdapter(adapter);
        }
    }

}
