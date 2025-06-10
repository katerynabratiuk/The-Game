package com.example.game.Client;

import com.example.game.DataStructures.ConcurrentQueue;
import com.example.game.DataStructures.ClientInput;
import com.example.game.Server.EnvLoader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.UUID;


public class InputsUpdateThread extends Thread {
    private final String CLIENT_UUID = UUID.randomUUID().toString();
    private final int PORT = this.getPort();

    private final ConcurrentQueue<ClientInput> inputsQueue;
    private volatile boolean running = true;
    private final DatagramSocket socket;
    private final InetAddress CLIENT_ADDRESS = java.net.InetAddress.getByName(null);

    public InputsUpdateThread() throws UnknownHostException {
        this.inputsQueue = new ConcurrentQueue<>();
        try {
            this.socket = new DatagramSocket();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create UDP socket", e);
        }
        setDaemon(true);
    }

    public void updateInputsQueue(int keyCode) {
        ClientInput input = new ClientInput(CLIENT_UUID, keyCode);
        inputsQueue.put(input);
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
        System.out.println("Actor Inputs Update Thread started");

        while (running) {
            send();
        }

        if (socket != null && !socket.isClosed()) {
            socket.close();
        }

        System.out.println("Actor Inputs Update Thread stopped");
    }

    private void send() {
        try {
            ClientInput input = inputsQueue.get();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(input);
            oos.flush();

            byte[] data = baos.toByteArray();
            DatagramPacket packet = new DatagramPacket(data, data.length, CLIENT_ADDRESS, PORT);

            socket.send(packet);
            oos.close();
            baos.close();
        } catch (InterruptedException | IOException e) {
            if (running) {
                System.err.println("Error sending input: " + e.getMessage());
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