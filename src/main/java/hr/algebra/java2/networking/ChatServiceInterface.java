package hr.algebra.java2.networking;

import hr.algebra.java2.message.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ChatServiceInterface extends Remote {

    String REMOTE_OBJECT_NAME = "hr.algebra.rmi.service";

    void sendMessage(Message newMessage) throws RemoteException;

    List<Message> getChatHistory() throws RemoteException; // lista message-a

}
