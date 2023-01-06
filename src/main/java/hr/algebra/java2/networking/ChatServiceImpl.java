package hr.algebra.java2.networking;

import hr.algebra.java2.message.Message;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class ChatServiceImpl implements ChatServiceInterface {

    List<Message> chatMessageList;

    public ChatServiceImpl() {
        chatMessageList = new ArrayList<>();
    }


    @Override
    public void sendMessage(Message newMessage) throws RemoteException {
        chatMessageList.add(newMessage);

    }

    @Override
    public List<Message> getChatHistory() {
        return chatMessageList;
    }
}
