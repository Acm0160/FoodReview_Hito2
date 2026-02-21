package com.example.foodreview;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ChatActivity extends AppCompatActivity {

    private TextView txtStatus;
    private EditText edtMsg;
    private Button btnSend, btnReconnect;

    private RecyclerView rvChat;
    private ChatAdapter adapter;

    private SocketClient client;

    //IP de nuestro ordenador
    private static final String SERVER_IP = "192.168.1.110";
    private static final int SERVER_PORT = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        txtStatus = findViewById(R.id.txtStatus);
        edtMsg = findViewById(R.id.edtMsg);
        btnSend = findViewById(R.id.btnSend);
        btnReconnect = findViewById(R.id.btnReconnect);
        rvChat = findViewById(R.id.rvChat);

        // Configurar RecyclerView
        adapter = new ChatAdapter();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setStackFromEnd(true); // mensajes abajo
        rvChat.setLayoutManager(manager);
        rvChat.setAdapter(adapter);

        // Crear cliente socket
        client = new SocketClient(SERVER_IP, SERVER_PORT, new ChatListener() {

            @Override
            public void onConnected() {
                runOnUiThread(() ->
                        txtStatus.setText("Conectado")
                );
            }

            @Override
            public void onDisconnected() {
                runOnUiThread(() ->
                        txtStatus.setText("Desconectado")
                );
            }

            @Override
            public void onMessageReceived(String msg) {
                runOnUiThread(() -> {
                    adapter.addMessage(new ChatMessage(msg, false));
                    rvChat.scrollToPosition(adapter.getItemCount() - 1);
                });
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(() ->
                        txtStatus.setText("Error: " + e.getMessage())
                );
            }
        });

        // Botón enviar
        btnSend.setOnClickListener(v -> {
            String text = edtMsg.getText().toString().trim();
            if (!text.isEmpty()) {
                client.sendMessage(text);
                adapter.addMessage(new ChatMessage(text, true));
                rvChat.scrollToPosition(adapter.getItemCount() - 1);
                edtMsg.setText("");
            }
        });

        // Botón reconectar
        btnReconnect.setOnClickListener(v -> {
            if (!client.isConnected()) {
                client.connect();
            }
        });

        // Conectar automáticamente al abrir
        client.connect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (client != null) {
            client.shutdown();
        }
    }
}