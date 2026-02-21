package com.example.foodreview;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class SocketClient {

    private final String host;
    private final int port;
    private final ChatListener listener;

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    private Thread readThread;
    private final ExecutorService writeExecutor = Executors.newSingleThreadExecutor();
    private volatile boolean running = false;

    public SocketClient(String host, int port, ChatListener listener) {
        this.host = host;
        this.port = port;
        this.listener = listener;
    }

    public void connect() {
        new Thread(() -> {
            try {
                socket = new Socket();
                socket.connect(new InetSocketAddress(host, port), 5000);

                in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));

                running = true;
                if (listener != null) listener.onConnected();

                startReading();
            } catch (Exception e) {
                if (listener != null) listener.onError(e);
                close();
            }
        }, "Socket-Connect").start();
    }

    private void startReading() {
        readThread = new Thread(() -> {
            try {
                while (running && isConnected()) {
                    String msg = in.readUTF(); // texto plano
                    if (listener != null) listener.onMessageReceived(msg);
                }
            } catch (EOFException eof) {
                // servidor cerró
                if (listener != null) listener.onDisconnected();
            } catch (Exception e) {
                if (running && listener != null) listener.onError(e);
            } finally {
                close();
            }
        }, "Socket-Read");
        readThread.start();
    }

    public void sendMessage(String text) {
        if (text == null || text.trim().isEmpty()) return;

        writeExecutor.execute(() -> {
            try {
                if (!isConnected()) throw new IOException("No conectado");
                out.writeUTF(text);
                out.flush();
            } catch (Exception e) {
                if (listener != null) listener.onError(e);
                close();
            }
        });
    }

    public boolean isConnected() {
        return socket != null && socket.isConnected() && !socket.isClosed();
    }

    public synchronized void close() {
        running = false;

        try { if (in != null) in.close(); } catch (Exception ignored) {}
        try { if (out != null) out.close(); } catch (Exception ignored) {}
        try { if (socket != null) socket.close(); } catch (Exception ignored) {}

        in = null;
        out = null;
        socket = null;

        if (listener != null) listener.onDisconnected();
    }

    public void shutdown() {
        close();
        writeExecutor.shutdownNow();
    }
}