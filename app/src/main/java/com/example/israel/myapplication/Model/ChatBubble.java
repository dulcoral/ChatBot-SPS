package com.example.israel.myapplication.Model;

public class ChatBubble {

    private String content;
    private boolean myMessage;

    public ChatBubble(String content, boolean myMessage) {
        this.content = content;
        this.myMessage = myMessage;
    }

public ChatBubble(){}
    public String getContent() {
        return content;
    }

    public boolean myMessage() {
        return myMessage;
    }

    public void setContent(String message){
    this.content = message;
    }

    public void setMyMessage(boolean myMessage){
    this.myMessage = myMessage;
    }
}