package hr.algebra.java2.hunt;

import hr.algebra.java2.networking.Server;
import hr.algebra.java2.thread.ClientThread;
import hr.algebra.java2.utilities.SceneUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StartMenuAplication extends Application {
    private static Stage mainStage;

    @Override
    public void start(Stage stage) throws IOException {
       /* try (MulticastSocket clientSocket = new MulticastSocket(Server.PORT)){ //Socket clientSocket = new Socket(Server.HOST, Server.PORT)
            //System.err.println("Client is connecting to " + clientSocket.getInetAddress() + ":" +clientSocket.getPort());

            InetAddress group = InetAddress.getByName(Server.GROUP);
            InetSocketAddress groupAddress = new InetSocketAddress(group, Server.PORT);
            NetworkInterface networkInterface = NetworkInterface.getByInetAddress(InetAddress.getByName(Server.HOST));
            System.err.println("Client joining group");

            clientSocket.joinGroup(groupAddress, networkInterface);

            while(true){
                byte[] buffer = new byte[64];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                clientSocket.receive(packet);
                //String message = new String(packet.getData(), 0, packet.getLength());
                System.err.println("Client got message");
            }

//            ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
//            ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());

//            if (ois.available() > 0){
//                oos.writeObject("Client " + " connected to server");

//                String rtrnMess = (String)ois.readObject();
//                System.out.println(rtrnMess);
//                rtrnMess = (String)ois.readObject();
//                System.out.println(rtrnMess);
//            }
        } catch (IOException /*| ClassNotFoundException e) {
            System.err.println("Connection failed");
//            e.printStackTrace();
        }*/
//        System.err.println("Connected successfully");


        this.mainStage = stage;
        SceneUtils.createScene(stage, "startMenu.fxml", "Game of Hunt");
    }

    public static Stage getMainStage() {
        return mainStage;
    }

    public static void main(String[] args) {
        launch();
    }
}