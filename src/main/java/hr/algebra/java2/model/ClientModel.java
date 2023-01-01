package hr.algebra.java2.model;

public class ClientModel {
    private int Port;
    private String IPAdress;


    private ClientModel(int port, String IPAdress) {
        Port = port;
        this.IPAdress = IPAdress;
    }

    public static ClientModel createClientModelFromIpAndPort(int port, String IPAddress) {
        return new ClientModel(port, IPAddress);
    }
}
