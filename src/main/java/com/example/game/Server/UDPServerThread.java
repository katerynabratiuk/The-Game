package com.example.game.Server;

import com.example.game.DataStructures.Actor;
import com.example.game.DataStructures.ClientInput;
import com.example.game.DataStructures.Coordinates;
import com.example.game.DataStructures.GameState;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap; // can we use this?

public class UDPServerThread extends Thread {
    protected DatagramSocket socket = null;

    private final int BUFFER_SIZE = 1024;
    private final int PORT = this.getPort();

    private final Set<InetSocketAddress> clients = ConcurrentHashMap.newKeySet();
    private final Map<String, Actor> actors = new ConcurrentHashMap<>();

    public void run() {
        connectSocket();
        while (true) {
            try {
                DatagramPacket packet = new DatagramPacket(new byte[BUFFER_SIZE], BUFFER_SIZE);
                socket.receive(packet);
                ClientInput input = process(packet);
                updateActor(input);
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
        GameState gameState = new GameState(new ArrayList<>(actors.values()));

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(gameState);
        byte[] dataToSend = baos.toByteArray();

        for (InetSocketAddress client : clients) {
            DatagramPacket response = new DatagramPacket(dataToSend, dataToSend.length, client.getAddress(), client.getPort());
            socket.send(response);
        }
    }


    private ClientInput process(DatagramPacket packet) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bais = new ByteArrayInputStream(packet.getData());
        ObjectInputStream ois = new ObjectInputStream(bais);
        ClientInput input = (ClientInput) ois.readObject();
        System.out.printf("Received input: %d\n", input.keyInput());
        return input;
    }

    private void trackClient(DatagramPacket packet) {
        InetSocketAddress senderAddress = new InetSocketAddress(packet.getAddress(), packet.getPort());
        clients.add(senderAddress);
    }

    private void updateActor(ClientInput input) {
        Actor actor = actors.get(input.uuid());

        if (actor == null) {
            Coordinates startCoords = new Coordinates(0, 0);
            actor = new Actor(input.uuid(), startCoords);
            actors.put(input.uuid(), actor);
        }

        PositionUpdater.handleInput(input.keyInput(), actor);
        System.out.printf("Updated Actor %s. Set coordinates: x:%s y:%s\n", actor.getClientId(), actor.getCoordinates().x(), actor.getCoordinates().y());
    }

    private int getPort() {
        Map<String, String> dotenv = EnvLoader.loadEnv(".env");
        return Integer.parseInt(dotenv.get("UPD_SERVER_PORT"));
    }
}
