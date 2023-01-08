package hr.algebra.java2.thread;

import hr.algebra.java2.dal.GameState;
import hr.algebra.java2.hunt.StartMenuController;
import hr.algebra.java2.message.Message;
import hr.algebra.java2.model.ClientModel;
import hr.algebra.java2.model.Game;
import hr.algebra.java2.networking.Server;
import javafx.application.Platform;
import jndi.helper.ConfigEnum;
import jndi.helper.JndiHelper;

import javax.naming.NamingException;
import java.io.*;
import java.net.*;

public class ClientThread implements Runnable {
    private int port;
    private GameState gameState;
    private StartMenuController startMenuController;

    public ClientThread(StartMenuController startMenuController) {
        this.startMenuController = startMenuController;
    }


//    public ClientThread(GameState gameState){
//        this.gameState = gameState;
//    }

    @Override
    public void run() {
        System.out.println("Client Server thread started.");
        String groupString;
        String hostString;
        try {
            port = Integer.parseInt(JndiHelper.getConfigurationParameter(ConfigEnum.PORT));
            groupString = JndiHelper.getConfigurationParameter(ConfigEnum.GROUP);
            hostString = JndiHelper.getConfigurationParameter(ConfigEnum.HOST);
        } catch (NamingException | IOException e) {
            throw new RuntimeException(e);
        }

        try (MulticastSocket clientSocket = new MulticastSocket(port)) {

            InetAddress group = InetAddress.getByName(groupString);
            InetSocketAddress groupAddress = new InetSocketAddress(group, port);
            NetworkInterface networkInterface = NetworkInterface.getByInetAddress(InetAddress.getByName(hostString));

            if (!Server.isServerFull()) {
                System.err.println("Client joining group");
                clientSocket.joinGroup(groupAddress, networkInterface);
            }
            ClientModel client = new ClientModel();
            //sending client
            sendPakcet(clientSocket, groupAddress, client, port);

//            DatagramPacket clientPacket = new DatagramPacket(client.getBytes(), client.length(), group, Server.PORT);
//            clientSocket.send(clientPacket);

            while (true) {
//                clientSocket.setSoTimeout(100);

                byte[] buffer = new byte[6400];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                clientSocket.receive(packet);

                //  input (recieve) packet
                try (ByteArrayInputStream bis = new ByteArrayInputStream(packet.getData());
                     ObjectInputStream ois = new ObjectInputStream(bis)) {
                    Object obj = ois.readObject();
                    if (obj instanceof GameState){
                        gameState = (GameState) obj;
                        System.err.println("Client got gameState");
                        //startMenuController.loadGameState();
                        Platform.runLater(() -> {
                            try {
                                startMenuController.loadGameState();
                            } catch (IOException | ClassNotFoundException e) {
                                System.out.println("Error with refresh menu screen");
                                throw new RuntimeException(e);
                            }
                        });
                    }else if(obj instanceof Boolean){
                        if (((Boolean)obj).booleanValue()){
                            System.err.println("Client got Boolean");
                            startMenuController.refreshChat();
                        }
                    }
                    //startMenuController.loadOnlineGameState(deserializedGameState);
                    //Platform.runLater(() -> startMenuController.loadOnlineGameState(gameState));

                }
                if (StartMenuController.sendStatus == Game.SEND_BOOLEAN) {
                    //ideja salji packet da se svi moraju refreshat
                    sendPakcet(clientSocket, groupAddress, Boolean.TRUE, port);
                }
            }
//            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Connection failed");
            e.printStackTrace();
        }
    }

    private static void sendPakcet(MulticastSocket clientSocket, InetSocketAddress groupAddress, Object objectForSend, int port) {
        try(ByteArrayOutputStream bos = new ByteArrayOutputStream(6400);
            ObjectOutputStream oos = new ObjectOutputStream(bos)){
            oos.writeObject(objectForSend);
            byte[] byteBuffer = bos.toByteArray();

            DatagramPacket Packet = new DatagramPacket(byteBuffer, byteBuffer.length, groupAddress.getAddress(), port);
            clientSocket.send(Packet);
            System.err.println("Client sent " + objectForSend.toString());
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
