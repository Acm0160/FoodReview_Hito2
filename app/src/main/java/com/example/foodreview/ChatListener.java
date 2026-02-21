package com.example.foodreview;

public interface ChatListener {
    void onConnected();
    void onDisconnected();
    void onMessageReceived(String msg);
    void onError(Exception e);
}
