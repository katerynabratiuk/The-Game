package org.server.network;

import org.lib.PacketProcessing.receive.Decoder;
import org.lib.PacketProcessing.receive.Decryptor;
import org.lib.PacketProcessing.receive.PacketReceiverThread;
import org.lib.PacketProcessing.send.Encoder;
import org.lib.PacketProcessing.send.Encryptor;
import org.lib.PacketProcessing.send.PacketSenderThread;
import org.server.gameLogic.PlayerController;

import java.net.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap; // can we use this?

import static org.lib.Environment.EnvLoader.ENV_VARS;

public class UDPServerThread extends Thread {
    public DatagramSocket socket;
    private final int PORT = Integer.parseInt(ENV_VARS.get("UDP_SERVER_PORT"));
    private final String SERVER_ADDRESS = ENV_VARS.get("UDP_SERVER_HOST");
    private final PacketReceiverThread  receivingThread;
    private final PacketSenderThread sendingThread;

    public UDPServerThread() throws SocketException {
        this.socket = new DatagramSocket(PORT);
        Set<SocketAddress> clients = ConcurrentHashMap.newKeySet();
        this.sendingThread = new PacketSenderThread(socket, new Encoder(), new Encryptor(), clients);
        this.receivingThread = new PacketReceiverThread(socket, new PlayerController(), new Decoder(), new Decryptor(), clients);
    }

    public void run() {
        try {
            connectSocket();
            startProcessingThreads();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        socket.close();
    }

    private void startProcessingThreads() {
        sendingThread.start();
        receivingThread.start();
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

//    private void broadcast() throws IOException {
//        GameState gameState = new GameState(new ArrayList<>(actors.values()));
//
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        ObjectOutputStream oos = new ObjectOutputStream(baos);
//        oos.writeObject(gameState);
//        byte[] dataToSend = baos.toByteArray();
//
//        for (InetSocketAddress client : clients) {
//            DatagramPacket response = new DatagramPacket(dataToSend, dataToSend.length, client.getAddress(), client.getPort());
//            socket.send(response);
//        }
//    }
//
//
//    private ClientInput process(DatagramPacket packet) throws IOException, ClassNotFoundException {
//        ByteArrayInputStream bais = new ByteArrayInputStream(packet.getData());
//        ObjectInputStream ois = new ObjectInputStream(bais);
//        ClientInput input = (ClientInput) ois.readObject();
//        System.out.printf("Received input: %d\n", input.keyInput());
//        return input;
//    }
//
//    private void trackClient(DatagramPacket packet) {
//        InetSocketAddress senderAddress = new InetSocketAddress(packet.getAddress(), packet.getPort());
//        clients.add(senderAddress);
//    }
//
//    private void updateActor(ClientInput input) {
//        Actor actor = actors.get(input.uuid());
//
//        if (actor == null) {
//            Coordinates startCoords = new Coordinates(0, 0);
//            actor = new Actor(input.uuid(), startCoords);
//            actors.put(input.uuid(), actor);
//        }
//
//        PositionUpdater.handleInput(input.keyInput(), actor);
//        System.out.printf("Updated Actor %s. Set coordinates: x:%s y:%s\n", actor.getClientId(), actor.getCoordinates().x(), actor.getCoordinates().y());
//    }
}
