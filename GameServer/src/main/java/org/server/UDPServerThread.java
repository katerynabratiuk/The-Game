package org.server;

import org.lib.DataStructures.Actor;
import org.lib.DataStructures.ClientInput;
import org.lib.DataStructures.Coordinates;
import org.lib.DataStructures.GameState;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap; // can we use this?

import static org.lib.Environment.EnvLoader.ENV_VARS;

public class UDPServerThread extends Thread {
    protected DatagramSocket socket = null;
    private final int BUFFER_SIZE = 1024;
    private final int PORT = Integer.parseInt(ENV_VARS.get("UDP_SERVER_PORT"));
    private final String SERVER_ADDRESS = ENV_VARS.get("UDP_SERVER_HOST");
    private final Set<InetSocketAddress> clients = ConcurrentHashMap.newKeySet();
    private final Map<String, Actor> actors = new ConcurrentHashMap<>();

    public void run() {
        try {
            connectSocket();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
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

    private void connectSocket() throws UnknownHostException {
        try {
            InetAddress serverAddress = InetAddress.getByName(SERVER_ADDRESS);
            socket = new DatagramSocket(PORT, serverAddress);

        } catch (SocketException | UnknownHostException e) {
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
}
