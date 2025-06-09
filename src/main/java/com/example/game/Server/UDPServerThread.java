package com.example.game.Server;

import com.example.game.DataStructures.Actor;
import com.example.game.DataStructures.GameState;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap; // can we use this?
import java.util.concurrent.CopyOnWriteArrayList;

public class UDPServerThread extends Thread {
    protected DatagramSocket socket = null;

    private final int BUFFER_SIZE = 1024;
    private final int PORT = this.getPort();
    private byte[] BUFFER = new byte[BUFFER_SIZE];

    private final Set<InetSocketAddress> clients = ConcurrentHashMap.newKeySet();
    private final List<Actor> actors = new CopyOnWriteArrayList<>();

    public void run() {
        connectSocket();
        while (true) {
            try {
                DatagramPacket packet = new DatagramPacket(BUFFER, BUFFER.length);
                socket.receive(packet);

                Actor update = process(packet);
                updateActorList(update);
                trackClient(packet);
                broadcast();
            } catch (IOException | ClassNotFoundException e) {
                break;
            }
        }
        socket.close();
    }

    private void connectSocket() {
        try {
            socket = new DatagramSocket(PORT);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Server listening on port " + PORT + "...");
    }

    private void broadcast() throws IOException {
        GameState gameState = new GameState(new ArrayList<>(actors));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(gameState);
        byte[] dataToSend = baos.toByteArray();  // reuse original data

        for (InetSocketAddress client : clients) {
            DatagramPacket response = new DatagramPacket(dataToSend, dataToSend.length,
                    client.getAddress(), client.getPort());
            socket.send(response);
            System.out.println("Updates sent to " + clients.size() + " clients");
        }
    }


    private Actor process(DatagramPacket packet) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bais = new ByteArrayInputStream(packet.getData());
        ObjectInputStream ois = new ObjectInputStream(bais);
        Actor actor = (Actor) ois.readObject();
        System.out.printf("Received coordinates: x=%d, y=%d%n", actor.getCoordinates().x(), actor.getCoordinates().y());
        return actor;
    }

    private void trackClient(DatagramPacket packet) {
        InetSocketAddress senderAddress = new InetSocketAddress(packet.getAddress(), packet.getPort());
        clients.add(senderAddress);
    }

    private void updateActorList(Actor newActor) {
        for (int i = 0; i < actors.size(); i++) {
            if (actors.get(i).getClientId().equals(newActor.getClientId())) {
                actors.set(i, newActor);
                return;
            }
        }
        actors.add(newActor);
    }

    private int getPort() {
        Map<String, String> dotenv = EnvLoader.loadEnv(".env");
        return Integer.parseInt(dotenv.get("UPD_SERVER_PORT"));
    }
}
