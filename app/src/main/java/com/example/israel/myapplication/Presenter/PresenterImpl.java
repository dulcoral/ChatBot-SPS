package com.example.israel.myapplication.Presenter;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;;
import com.example.israel.myapplication.Model.CardResponse;
import com.example.israel.myapplication.Model.ChatBubble;
import com.example.israel.myapplication.Model.QReply;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

public class PresenterImpl {
    Context context;
    Presenter.Chat delegate;

    public PresenterImpl(Context context, Presenter.Chat delegate) {
        this.context = context;
        this.delegate = delegate;
    }

    public void send(String input) {
        ChatBubble sendMessage = new ChatBubble();
        sendMessage.setContent(input);
        sendMessage.setMyMessage(false);
        delegate.updateChatResponse(sendMessage);
        ArrayList<Object> responses = new ArrayList<>();
        if (input.toLowerCase().equals("hola")) {
            responses.add(new CardResponse("dasd", "Charlie Chaplin", "Sir Charles Spencer was an English comic actor,....", "2010/2/1"));
            responses.add(new CardResponse("bye", "Mr.Bean", "Mr. Bean is a British sitcom created by Rowan Atkinson and Richard Curtis, and starring Atkinson as the title character.", "2010/2/1"));
            responses.add(new CardResponse("hola", "Jim Carrey", "James Eugene Carrey is a Canadian-American actor, comedian, impressionist, screenwriter...", "2010/2/1"));
            delegate.updateChatResponse(responses);

        } else if (input.toLowerCase().equals("bye")) {
            responses.add(new QReply("Charlie Chaplin"));
            responses.add(new QReply("Mr.Bean"));
            responses.add(new QReply("Jim Carrey"));
            delegate.updateChatResponse(responses);

        } else {
            ChatBubble chatMessage = new ChatBubble();
            chatMessage.setContent("hola");
            chatMessage.setMyMessage(true);
            delegate.updateChatResponse(chatMessage);

        }
    }

    public void onSucess() {
        if (delegate != null) {

        }
    }

    public void onError(String error) {
        if (delegate != null) {

        }
    }

    public void consultarws(String comentario) throws MalformedURLException {
        //String otherParametersUrServiceNeed =  "";
        String request = "http://chatbotsp.herokuapp.com/api/message";
        URL url = new URL(request);

        //agrear comentario propio

        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setInstanceFollowRedirects(false);
        try {
            connection.setRequestMethod("POST");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        connection.setRequestProperty("Content-Type", "application/json");
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        DataOutputStream wr = null;
        try {
            wr = new DataOutputStream(connection.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //wr.writeBytes(otherParametersUrServiceNeed);

        JSONObject jsonParam = new JSONObject();
        try {
            jsonParam.put("message", comentario);
            jsonParam.put("sessionId", "123");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        try {
            wr.writeBytes(jsonParam.toString());
            wr.flush();
            wr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            if (connection.getResponseCode() == 200) {
                InputStream inputStream = new BufferedInputStream(connection.getInputStream());
                BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder total = new StringBuilder();
                String resuesta = r.readLine();
                //Log.i("r.readLine=  ",resuesta.replace(",null",""));

                try {
                    JSONArray jsonRes = new JSONArray(resuesta);
                    JSONObject objJson = new JSONObject(jsonRes.get(0).toString());
                    Log.i("jsonRes.get(0)=  ", objJson.toString());
                    JSONObject queryResult = new JSONObject(objJson.getString("queryResult"));
                    Log.i("queryResult=  ", queryResult.toString());
                    /*
                    JSONArray fulfillmentMessages = new JSONArray(queryResult.optJSONArray("fulfillmentMessages").toString());

                    JSONObject fulfillmentMessages1 = new JSONObject(fulfillmentMessages.get(0).toString());
                    JSONObject fulfillmentMessages2 = new JSONObject(fulfillmentMessages.get(1).toString());
                    JSONObject simpleResponses1= fulfillmentMessages1.getJSONObject("simpleResponses");
                    Log.i("simpleResponses1=  ",simpleResponses1.toString());
                    JSONObject suggestions1= fulfillmentMessages2.getJSONObject("suggestions");
                    Log.i("suggestions1=  ",suggestions1.toString());

                    JSONArray simpleResponses2 = new JSONArray(simpleResponses1.optJSONArray("simpleResponses").toString());
                    Log.i("simpleResponses2=  ",simpleResponses2.get(0).toString());
                    JSONArray suggestions2 = new JSONArray(suggestions1.optJSONArray("suggestions").toString());
                    for (int i=0; i<suggestions2.length(); i++){
                        JSONObject boton = new JSONObject(suggestions2.get(i).toString());
                        Log.i("titulos boton   ",boton.getString("title"));
                    }
                    JSONObject textToSpeech = new JSONObject(simpleResponses2.get(0).toString());
                    Log.i("textToSpeech=  ",textToSpeech.getString("textToSpeech"));
                    */



                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i("error parse json ", e.toString());
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
