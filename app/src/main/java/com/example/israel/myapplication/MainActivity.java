package com.example.israel.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.israel.myapplication.Adapters.ChatAdapter;
import com.example.israel.myapplication.Model.CardResponse;
import com.example.israel.myapplication.Model.ChatBubble;
import com.example.israel.myapplication.Model.QReply;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private ArrayList<Object> objects = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_View);
        ChatAdapter adapter = new ChatAdapter(this, getObject(), false);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    private ArrayList<Object> getObject() {
        objects.add(getChatDialog());
        objects.add(getQReplies().get(0));
        objects.add(getHorizontalData().get(0));
        return objects;
    }

    public static ChatBubble getChatDialog() {
       ChatBubble singleVerticals = new ChatBubble("Jim Carrey", false);
        return singleVerticals;
    }

    public static ArrayList<QReply> getQReplies() {
        ArrayList<QReply> singleVerticals = new ArrayList<>();
        singleVerticals.add(new QReply("Charlie Chaplin"));
        singleVerticals.add(new QReply("Mr.Bean"));
        singleVerticals.add(new QReply("Jim Carrey"));
        return singleVerticals;
    }

    public static ArrayList<CardResponse> getHorizontalData() {
        ArrayList<CardResponse> singleHorizontals = new ArrayList<>();
        singleHorizontals.add(new CardResponse("dasd", "Charlie Chaplin", "Sir Charles Spencer \"Charlie\" Chaplin, KBE was an English comic actor,....", "2010/2/1"));
        singleHorizontals.add(new CardResponse("bye", "Mr.Bean", "Mr. Bean is a British sitcom created by Rowan Atkinson and Richard Curtis, and starring Atkinson as the title character.", "2010/2/1"));
        singleHorizontals.add(new CardResponse("hola", "Jim Carrey", "James Eugene \"Jim\" Carrey is a Canadian-American actor, comedian, impressionist, screenwriter...", "2010/2/1"));
        return singleHorizontals;
    }
}