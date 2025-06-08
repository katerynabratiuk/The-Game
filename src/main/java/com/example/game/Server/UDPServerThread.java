package com.example.game.Server;

import com.example.game.DataStructures.ConcurrentQueue;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UDPServerThread extends Thread {
    protected DatagramSocket socket = null;

    public UDPServerThread() throws IOException {
    }

    public void run() {
        try {
            socket = new DatagramSocket(5678);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        byte[] buffer = new byte[1024];

        System.out.println("Server listening on port 5678...");

        while (true) {
            try {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                ByteArrayInputStream bais = new ByteArrayInputStream(packet.getData());
                ObjectInputStream ois = new ObjectInputStream(bais);
                ConcurrentQueue.Coordinates coords = (ConcurrentQueue.Coordinates) ois.readObject();

                System.out.printf("Received coordinates: x=%d, y=%d%n", coords.x(), coords.y());
            } catch (IOException | ClassNotFoundException e) {
                break;
            }
        }
        socket.close();
    }


}
