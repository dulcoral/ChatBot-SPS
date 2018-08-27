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

    public void send(String input) throws MalformedURLException {
        ChatBubble sendMessage = new ChatBubble();
        sendMessage.setContent(input);
        sendMessage.setMyMessage(false);
        delegate.updateChatResponse(sendMessage);
        ArrayList<Object> responses = new ArrayList<>();
        if (input.toLowerCase().equals("d")) {
            responses.add(new CardResponse("dasd", "Charlie Chaplin", "Sir Charles Spencer was an English comic actor,....", "2010/2/1"));
            responses.add(new CardResponse("bye", "Mr.Bean", "Mr. Bean is a British sitcom created by Rowan Atkinson and Richard Curtis, and starring Atkinson as the title character.", "2010/2/1"));
            responses.add(new CardResponse("hola", "Jim Carrey", "James Eugene Carrey is a Canadian-American actor, comedian, impressionist, screenwriter...", "2010/2/1"));
            delegate.updateChatResponse(responses);

        }
        consultarws(input);

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
        String request = "http://chatbotsp.herokuapp.com/api/message";
        URL url = new URL(request);
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
                String respuesta = r.readLine();
                try {
                    JSONArray jsonRes = new JSONArray(respuesta);
                    JSONObject objJson = new JSONObject(jsonRes.get(0).toString());
                    JSONObject queryResult = new JSONObject(objJson.getString("queryResult"));
                    JSONArray fulfillmentMessages = new JSONArray(queryResult.optJSONArray("fulfillmentMessages").toString());
                    JSONObject fulfillmentMessages1 = new JSONObject(fulfillmentMessages.get(0).toString());
                    if (fulfillmentMessages1.has("simpleResponses")) {
                        JSONObject simpleResponses = fulfillmentMessages1.getJSONObject("simpleResponses");
                        JSONArray simpleResponses1 = new JSONArray(simpleResponses.optJSONArray("simpleResponses").toString());
                        JSONObject textToSpeech = new JSONObject(simpleResponses1.get(0).toString());
                        ChatBubble chatMessage = new ChatBubble();
                        chatMessage.setContent(textToSpeech.getString("textToSpeech"));
                        chatMessage.setMyMessage(true);
                        delegate.updateChatResponse(chatMessage);
                    } else if (fulfillmentMessages1.has("listSelect")) {
                        JSONObject btnResponse = fulfillmentMessages1.getJSONObject("listSelect");
                        JSONArray items = new JSONArray(btnResponse.optJSONArray("items").toString());
                        ChatBubble chatMessage = new ChatBubble();
                        chatMessage.setContent(btnResponse.getString("title"));
                        chatMessage.setMyMessage(true);
                        delegate.updateChatResponse(chatMessage);
                        ArrayList<Object> responses = new ArrayList<>();
                        for (int i = 0; i < items.length(); i++) {
                            JSONObject item = new JSONObject(items.get(i).toString());
                            responses.add(new QReply(item.getString("title")));
                        }
                        delegate.updateChatResponse(responses);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
