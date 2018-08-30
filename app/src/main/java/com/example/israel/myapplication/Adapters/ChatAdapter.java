package com.example.israel.myapplication.Adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.TextView;

import com.example.israel.myapplication.Model.ChatBubble;
import com.example.israel.myapplication.Model.CardResponse;
import com.example.israel.myapplication.Model.QReply;
import com.example.israel.myapplication.Model.VideoResponse;
import com.example.israel.myapplication.Presenter.PresenterImpl;
import com.example.israel.myapplication.R;

import java.util.ArrayList;
import java.util.List;


public class ChatAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<Object> items;
    private PresenterImpl presenter;
    private final int MESSAGESEND = 0;
    private final int SIMPLEMESSAGE = 1;
    private final int CARROUSEL = 2;
    private final int BTN = 3;
    private final int VIDEO = 4;

    public ChatAdapter(Context context, List<Object> items, PresenterImpl presenter) {
        this.context = context;
        this.presenter = presenter;
        if (items == null) {
            this.items = new ArrayList<>();
        } else {
            this.items = items;
        }
    }

    public void add(Object message) {
        items.add(message);
        notifyDataSetChanged();
    }

    public void remove() {
        items.remove(getItemCount() - 1);
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
            case VIDEO:
                view = inflater.inflate(R.layout.video_view, parent, false);
                return new VideoViewHolder(view);
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
            case VIDEO:
                VideoResponse video = (VideoResponse) items.get(position);
                ((VideoViewHolder) holder).bind(video);
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
        if (items.get(position) instanceof VideoResponse){
            VideoResponse videoResponse = (VideoResponse) items.get(position);
            return VIDEO;
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
            CarrouselAdapter adapter = new CarrouselAdapter(cardResponses, context, presenter);
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
            QRepliesAdapter adapter = new QRepliesAdapter(qReplies, context, presenter);
            recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            recyclerView.setAdapter(adapter);
        }
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder {
        WebView videoWeb;

        VideoViewHolder(View itemView) {
            super(itemView);
            videoWeb = (WebView) itemView.findViewById(R.id.videoWebView);
        }

        public void bind(VideoResponse videoResponse) {
            videoWeb.getSettings().setJavaScriptEnabled(true);
            videoWeb.setWebChromeClient(new WebChromeClient() {
            });
            videoWeb.loadData(videoResponse.getVideoUrl(), "text/html", "utf-8");
            //videoWeb.loadUrl(videoResponse.getVideoUrl());
        }
    }

}
