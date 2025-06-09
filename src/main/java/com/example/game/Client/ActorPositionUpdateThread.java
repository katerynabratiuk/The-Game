package com.example.game.Client;

import com.example.game.DataStructures.Actor;
import com.example.game.DataStructures.ConcurrentQueue;
import com.example.game.DataStructures.Coordinates;
import com.example.game.Server.EnvLoader;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Map;
import java.util.UUID;


public class ActorPositionUpdateThread extends Thread {
    private volatile boolean running = true;
    private final String clientId = UUID.randomUUID().toString();
    private final ConcurrentQueue<Coordinates> coordinateQueue;
    private DatagramSocket socket;
    private final int PORT = this.getPort();

    public ActorPositionUpdateThread() {
        this.coordinateQueue = new ConcurrentQueue<>();
        try {
            this.socket = new DatagramSocket();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create UDP socket", e);
        }
        setDaemon(true);
    }

    public void updateCoordinates(Coordinates newCoordinates) {
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
                update();
                // Thread.sleep(50);
            } catch (InterruptedException e) {
                this.interrupt();
                break;
            }
        }

        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
        System.out.println("Actor Coordinates Update Thread stopped");
    }

    private void update() throws InterruptedException {
        Coordinates coords = coordinateQueue.get();
        Actor actor = new Actor(clientId, coords);
        sendCoordinates(actor);
    }

    private void sendCoordinates(Actor actor) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(actor);
            oos.flush();

            byte[] data = baos.toByteArray();
            DatagramPacket packet = new DatagramPacket(
                    data, data.length, java.net.InetAddress.getByName(null), PORT);

            socket.send(packet);
            oos.close();
            baos.close();
        } catch (Exception e) {
            if (running) {
                System.err.println("Error sending coordinates: " + e.getMessage());
            }
        }
    }

    public DatagramSocket getSocket() {
        return socket;
    }

    private int getPort() {
        Map<String, String> dotenv = EnvLoader.loadEnv(".env");
        return Integer.parseInt(dotenv.get("UPD_SERVER_PORT"));
    }

}