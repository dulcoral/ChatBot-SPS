package com.example.israel.myapplication.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.israel.myapplication.Adapters.ChatAdapter;
import com.example.israel.myapplication.Presenter.Presenter;
import com.example.israel.myapplication.Presenter.PresenterImpl;
import com.example.israel.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements Presenter.Chat {
    private EditText inputET;
    ChatAdapter adapter;
    RecyclerView recyclerView;
    Toolbar toolbar;
    PresenterImpl presenter;
    private ImageButton sendBtn;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setElevation(4);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
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
                try {
                    presenter.send(messageText);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });
        adapter = new ChatAdapter(this, fromChat(),presenter);
        linearLayoutManager = new LinearLayoutManager(this);
       // linearLayoutManager.setStackFromEnd(true);
       // recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount()-1);
        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
            }
        }, 300);
        recyclerView.setLayoutManager(linearLayoutManager);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                //Realizamos el cierre de sesion en firebase y volvemos al login
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}