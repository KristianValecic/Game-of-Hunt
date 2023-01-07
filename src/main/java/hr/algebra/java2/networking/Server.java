package hr.algebra.java2.networking;

import hr.algebra.java2.dal.GameState;
import hr.algebra.java2.model.ClientModel;
import hr.algebra.java2.model.Game;

import java.io.*;
import java.net.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class Server {
    public static final String HOST = "localhost";
    public static final String GROUP = "230.0.0.1";
    public static final int PORT = 1989;
    private static final int RANDOM_PORT_HINT = 0;

    public static List<ClientModel> connectedClientList;
    private static boolean isServerFull;
    private static GameState gameState;
    private static int testCounter = 0;

    public static void main(String[] args) {
        acceptRequests();
    }

    private static void acceptRequests() {
        gameState = new GameState();
        gameState.setMatchAllCount(66);
        gameState.setTrapCount(55);
        connectedClientList = new ArrayList<>();
        isServerFull = false;

        try (MulticastSocket serverSocket = new MulticastSocket(PORT)) { // ServerSocket
            System.err.println("Server listening on port: " + serverSocket.getLocalPort());

            //Server joining gruop
            InetAddress group = InetAddress.getByName(GROUP);
            InetSocketAddress groupAddress = new InetSocketAddress(group, PORT);
            NetworkInterface networkInterface = NetworkInterface.getByInetAddress(InetAddress.getByName(HOST));

            serverSocket.joinGroup(groupAddress, networkInterface);
            System.err.println("Server joining group");

            //RMI for chat
            try {
                Registry registry = LocateRegistry.createRegistry(PORT);
                ChatServiceInterface chatService = new ChatServiceImpl();
                ChatServiceInterface skeleton = (ChatServiceInterface) UnicastRemoteObject.exportObject(chatService, RANDOM_PORT_HINT);
                registry.rebind(ChatServiceInterface.REMOTE_OBJECT_NAME, skeleton);
                System.err.println("Object registered in RMI registry");
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            while (true) {
//                if (connectedClientList.size() < Game.MAX_PLAYERS){
//                  Socket clientSocket = serverSocket.accept();
                //new Thread(() ->{

                //recieves at least first connection message.
                recievePacket(serverSocket);

                sendPacket(serverSocket, gameState);
                //dictates how often packets are sent
                Thread.sleep(500);
                //} ).start();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void recievePacket(DatagramSocket serverSocket) throws IOException {
        byte[] buffer = new byte[6400];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        serverSocket.receive(packet);

        try (ByteArrayInputStream bis = new ByteArrayInputStream(packet.getData());
             ObjectInputStream ois = new ObjectInputStream(bis)) {
            Object obj = ois.readObject();
            //System.out.println(gameState);
            //startMenuController.loadOnlineGameState(deserializedGameState);
            //Platform.runLater(() -> startMenuController.loadOnlineGameState(gameState));

            if (obj instanceof GameState) {
                gameState = (GameState) obj;//ois.readObject()
                System.out.println(gameState.getMatchAllCount() + " " + gameState.getTrapCount());
//                gameState.setTrapCount(testCounter++);
                System.err.println("Server got gameState");

            } else if (obj instanceof ClientModel && !isServerFull) {
                ClientModel client = (ClientModel) obj;
                System.out.println(obj);
                connectedClientList.add(client);
                if (connectedClientList.size() >= Game.MAX_PLAYERS){
                    isServerFull = true;
                }
                System.err.println("Client Connected");
            } else if (obj instanceof Boolean) {
                Boolean bool = (Boolean) obj;
                sendPacket(serverSocket, bool);
            }

            System.out.println("Server got client message.");
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static void sendPacket(DatagramSocket serverSocket, Object ObjectForSend) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream(6400);
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
//            String message = "test message";
            oos.writeObject(ObjectForSend);
            //byte[] buffer = message.getBytes();
            byte[] buffer = bos.toByteArray();
            InetAddress groupAddress = InetAddress.getByName(GROUP);

            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, groupAddress, PORT);
            System.out.println("Server sending " + ObjectForSend.toString());
            serverSocket.send(packet);
        } catch (IOException e) {
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


    private static void processSerializableClient(Socket clientSocket) {
        try (ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
             ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream())) {

            String message = (String) ois.readObject();
            System.out.println(message);
            oos.writeObject("Connection established");
            oos.writeObject("Thread finished work");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static boolean isServerFull() {
        return isServerFull;
    }
}
