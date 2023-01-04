package hr.algebra.java2.networking;

import hr.algebra.java2.model.ClientModel;
import hr.algebra.java2.model.Game;

import java.io.*;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    public static final String HOST = "localhost";
    public static final String GROUP = "230.0.0.1";
    public static final int PORT = 1989;
    public static List<ClientModel> connectedClientList;

    public static void main(String[] args) {
        acceptRequests();
    }

    private static void acceptRequests() {
        try (DatagramSocket serverSocket = new DatagramSocket(PORT)){ // ServerSocket
            System.err.println("Server listening on port: " + serverSocket.getLocalPort());

            connectedClientList = new ArrayList<>();

            while (true) {
                if (connectedClientList.size() < Game.MAX_PLAYERS){
//                    Socket clientSocket = serverSocket.accept();
                    InetAddress groupAdress = InetAddress.getByName(GROUP);

                    System.err.println("Client connected from port: " + clientSocket.getPort());
                    // outer try catch blocks cannot handle the anonymous implementations
                    //new Thread(() ->  processPrimitiveClient(clientSocket)).start();
//                    returnConnectionMessage(clientSocket);
                    new Thread(() ->  processSerializableClient(clientSocket)).start();
                    connectedClientList.add(ClientModel.createClientModelFromIpAndPort(
                            clientSocket.getPort(), clientSocket.getInetAddress().getHostAddress()));
                }else{
                    continue;
                }

            }
        }  catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void returnConnectionMessage(Socket clientSocket) {
        try (ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
             ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream())) {

            String message = ois.readUTF();
            System.out.println(message);

            oos.writeObject("Connection established");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void processPrimitiveClient(Socket clientSocket) {
        // we have to manually close dis and dos since clientSocket is not in try with resources
        // closing the streams closes the socket!
        try (DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
             DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream())) {

            String message = dis.readUTF();
            System.out.println("Server received: " + message);
            dos.writeInt(countVowels(message));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int countVowels(String message) {
        return message.toLowerCase().replaceAll("[^aeiou]", "").length();
    }

    private static void processSerializableClient(Socket clientSocket) {
        try (ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
             ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream())){

            String message = (String)ois.readObject();
            System.out.println(message);
            oos.writeObject("Connection established");
            oos.writeObject("Thread finished work");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
