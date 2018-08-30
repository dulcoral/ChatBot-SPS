package com.example.israel.myapplication.Presenter;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;

import com.example.israel.myapplication.Model.CardResponse;
import com.example.israel.myapplication.Model.ChatBubble;
import com.example.israel.myapplication.Model.QReply;
import com.example.israel.myapplication.Model.VideoResponse;
import com.example.israel.myapplication.R;

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

    public void sendMessage(String input) {
        simpleResponse(input, false);
        simpleResponse("...", true);
        if (isOnline(context))
            sendRequest(input);
        else {
            delegate.deleteWait();
            simpleResponse(context.getResources().getString(R.string.conexion), true);
        }
    }

    public void sendRequest(String input) {
        try {
            consultarws(input);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void consultarws(String comentario) throws MalformedURLException {
        String request = context.getResources().getString(R.string.request);
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
        callBack(connection);

    }

    private void callBack(HttpURLConnection connection) {
        try {
            if (connection.getResponseCode() == 200) {
                delegate.deleteWait();
                InputStream inputStream = new BufferedInputStream(connection.getInputStream());
                BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
                String respuesta = r.readLine();
                try {
                    JSONArray jsonRes = new JSONArray(respuesta);
                    JSONObject objJson = new JSONObject(jsonRes.get(0).toString());
                    JSONObject queryResult = new JSONObject(objJson.getString("queryResult"));
                    JSONArray fulfillmentMessages = new JSONArray(queryResult.optJSONArray("fulfillmentMessages").toString());
                    for (int i = 0; i < fulfillmentMessages.length(); i++) {
                        JSONObject fulfillmentMessages1 = new JSONObject(fulfillmentMessages.get(i).toString());
                        //SimpleResponses
                        if (fulfillmentMessages1.has("simpleResponses")) {
                            JSONObject simpleResponses = fulfillmentMessages1.getJSONObject("simpleResponses");
                            JSONArray simpleResponses1 = new JSONArray(simpleResponses.optJSONArray("simpleResponses").toString());
                            JSONObject textToSpeech = new JSONObject(simpleResponses1.get(0).toString());
                            simpleResponse(textToSpeech.getString("textToSpeech"), true);
                        }
                        //QuickResponses
                        else if (fulfillmentMessages1.has("listSelect")) {
                            JSONObject btnResponse = fulfillmentMessages1.getJSONObject("listSelect");
                            JSONArray items = new JSONArray(btnResponse.optJSONArray("items").toString());
                            simpleResponse(btnResponse.getString("title"), true);
                            ArrayList<Object> responses = new ArrayList<>();
                            for (int j = 0; j < items.length(); j++) {
                                JSONObject item = new JSONObject(items.get(j).toString());
                                responses.add(new QReply(item.getString("title")));
                            }
                            delegate.updateChatResponse(responses);
                        } else if (fulfillmentMessages1.has("suggestions")) {
                            JSONObject suggestions = fulfillmentMessages1.getJSONObject("suggestions");
                            JSONArray suggestionsArray = suggestions.getJSONArray("suggestions");
                            ArrayList<Object> responses = new ArrayList<>();
                            for (int k = 0; k < suggestionsArray.length(); k++) {
                                JSONObject suggestion = new JSONObject(suggestionsArray.get(k).toString());
                                responses.add(new QReply(suggestion.getString("title")));
                                //simpleResponse(suggestion.getString("title"),true);
                            }
                            delegate.updateChatResponse(responses);
                        }
                        //CarrouselResponses
                        else if (fulfillmentMessages1.has("carouselSelect")) {
                            JSONObject btnResponse = fulfillmentMessages1.getJSONObject("carouselSelect");
                            JSONArray items = new JSONArray(btnResponse.optJSONArray("items").toString());
                            ArrayList<Object> responses = new ArrayList<>();
                            for (int j = 0; j < items.length(); j++) {
                                JSONObject item = new JSONObject(items.get(j).toString());
                                responses.add(new CardResponse(item.getString("title"), item.getJSONObject("image").getString("imageUri"), item.getString("description"), "seleccionar"));
                            }
                            delegate.updateChatResponse(responses);
                        }
                        //videoResponse
                        else if (fulfillmentMessages1.has("linkOutSuggestion")) {
                            JSONObject linkResponse = fulfillmentMessages1.getJSONObject("linkOutSuggestion");
                            String respons = linkResponse.getString("destinationName");
                            String link = linkResponse.getString("uri");
                            String videoLink = "<iframe width=\"100%\" height=\"80%\" src=" + "\"" + link + "\"" + " frameborder=\"0\" allowfullscreen></iframe>";
                            simpleResponse(respons, true);
                            VideoResponse videoResponse = new VideoResponse();
                            videoResponse.setVideoUrl("<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/Mx-XKLL6iY0\" frameborder=\"0\" allow=\"autoplay; encrypted-media\" allowfullscreen></iframe>");
                            delegate.updateChatResponse(videoResponse);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void simpleResponse(String message, boolean myMessage) {
        ChatBubble chatMessage = new ChatBubble();
        chatMessage.setContent(message);
        chatMessage.setMyMessage(myMessage);
        delegate.updateChatResponse(chatMessage);
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }
}
