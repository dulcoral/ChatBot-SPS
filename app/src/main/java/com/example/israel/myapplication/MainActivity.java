package com.example.israel.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.israel.myapplication.Adapters.ChatAdapter;
import com.example.israel.myapplication.Presenter.Presenter;
import com.example.israel.myapplication.Presenter.PresenterImpl;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements Presenter {
    private EditText inputET;
    ChatAdapter adapter;
    RecyclerView recyclerView;
    PresenterImpl presenter;
    private ImageButton sendBtn;
    private ArrayList<Object> objects = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_View);
        presenter = new PresenterImpl(this, this);
        inputET = findViewById(R.id.et_msg_content);
        sendBtn = findViewById(R.id.btn_chat_send);
        initComponets();
    }

    private void initComponets() {
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String messageText = inputET.getText().toString();
                if (messageText.equals("") || messageText.isEmpty()) {
                    return;
                }
                inputET.setText(""); // clear message field
                // disable send button until retrieve success from API
                sendBtn.setClickable(false);
                presenter.send(messageText);
            }
        });
        adapter = new ChatAdapter(this, fromChat());
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter);
    }

    public List<Object> fromChat() {
        List<Object> mMessageList = new ArrayList<>();
        return mMessageList;
    }

    public void updateChatResponse(Object response) {
        adapter.add(response);
        sendBtn.setClickable(true);
    }
}