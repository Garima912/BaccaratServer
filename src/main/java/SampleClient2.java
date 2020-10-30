package main.java;

import main.java.model.Packet;

import java.io.*;
import java.net.Socket;

public class SampleClient2 {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1", 9090);
        ObjectOutputStream out = new ObjectOutputStream((socket.getOutputStream()));

        System.out.println("get remote: "+socket.getLocalSocketAddress().toString());

        Packet packet = new Packet(socket.getLocalSocketAddress().toString(), socket.getPort(), "Johnny");
        out.writeObject(packet);
        new Thread()
        {
            public void run() {
                try {
                    ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                    Packet packet = (Packet)in.readObject();
                    System.out.println("[CLient1: ]"+ packet.getName());
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
