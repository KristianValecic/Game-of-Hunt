package hr.algebra.java2.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Server extends Thread{
    public static final int CLIENT_PORT = 4446;
    public static final String HOST = "localhost";
    public static final String GROUP = "230.0.0.1";
    @Override
    public void run() {

        try(DatagramSocket serverSocket = new DatagramSocket()) {
            System.err.println("Server listening on port: " + serverSocket.getLocalPort());
            while (true) {

                String message = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                byte[] buffer = message.getBytes();
                InetAddress groupAddress = InetAddress.getByName(GROUP);
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, groupAddress, CLIENT_PORT);
                serverSocket.send(packet);

                Thread.sleep(6000);
            }
        } catch (SocketException | InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
