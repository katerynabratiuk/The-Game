package org.client;

import org.lib.DataStructures.ConcurrentQueue;
import org.lib.DataStructures.ClientInput;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

import static org.lib.Environment.EnvLoader.ENV_VARS;


public class InputsSenderThread extends Thread {
    private final String CLIENT_UUID = UUID.randomUUID().toString();
    private final int PORT = Integer.parseInt(ENV_VARS.get("UDP_SERVER_PORT"));
    private final InetAddress SERVER_ADDRESS = InetAddress.getByName(ENV_VARS.get("UDP_SERVER_HOST"));

    private final ConcurrentQueue<ClientInput> inputsQueue;
    private volatile boolean running = true;
    private final DatagramSocket socket;


    public InputsSenderThread() throws UnknownHostException {
        try {
            this.inputsQueue = new ConcurrentQueue<>();
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

        if (socket != null && !socket.isClosed()) socket.close();
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
            DatagramPacket packet = new DatagramPacket(data, data.length, SERVER_ADDRESS, PORT);

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

}