package com.example.israel.myapplication.Presenter;

import android.content.Context;;
import com.example.israel.myapplication.Model.CardResponse;
import com.example.israel.myapplication.Model.ChatBubble;
import com.example.israel.myapplication.Model.QReply;

import java.util.ArrayList;

public class PresenterImpl {
    Context context;
    Presenter delegate;

    public PresenterImpl(Context context, Presenter delegate) {
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

}
