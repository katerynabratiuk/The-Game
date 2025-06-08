package com.example.game.Client;

import com.example.game.DataStructures.ConcurrentQueue;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;


public class ActorPositionUpdateThread extends Thread {
    private volatile boolean running = true;
    private final ConcurrentQueue<ConcurrentQueue.Coordinates> coordinateQueue;
    private DatagramSocket socket;

    public ActorPositionUpdateThread() {
        this.coordinateQueue = new ConcurrentQueue<>();
        try {
            this.socket = new DatagramSocket();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create UDP socket", e);
        }
        setDaemon(true);
    }

    public void updateCoordinates(ConcurrentQueue.Coordinates newCoordinates) {
        coordinateQueue.put(newCoordinates);
    }

    public void shutdown() {
        running = false;
        interrupt();
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
    }

    @Override
    public void run() {
        System.out.println("Actor Coordinates Update Thread started");

        while (running && !Thread.currentThread().isInterrupted()) {
            try {
                ConcurrentQueue.Coordinates coords = coordinateQueue.get();
                sendCoordinates(coords);
                Thread.sleep(50);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
        System.out.println("Actor Coordinates Update Thread stopped");
    }

    private void sendCoordinates(ConcurrentQueue.Coordinates coords) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(coords);
            oos.flush();

            byte[] data = baos.toByteArray();
            DatagramPacket packet = new DatagramPacket(
                    data, data.length, java.net.InetAddress.getByName(null), 5678
            );

            socket.send(packet);
            oos.close();
            baos.close();

        } catch (Exception e) {
            if (running) {
                System.err.println("Error sending coordinates: " + e.getMessage());
            }
        }
    }
}