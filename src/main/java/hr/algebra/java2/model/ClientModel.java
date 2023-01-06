package hr.algebra.java2.model;

import java.io.Serializable;

public class ClientModel implements Serializable {
    private int Port;
    private String IPAdress;

    public ClientModel() {
    }

    private ClientModel(int port, String IPAdress) {
        Port = port;
        this.IPAdress = IPAdress;
    }

    public static ClientModel createClientModelFromIpAndPort(int port, String IPAddress) {
        return new ClientModel(port, IPAddress);
    }
}
