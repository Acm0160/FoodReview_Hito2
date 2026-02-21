package com.example.foodreview;

import android.content.Context;
import android.net.*;

public class NetworkMonitor {

    public interface Listener {
        void onOnline();
        void onOffline();
    }

    private final ConnectivityManager cm;
    private ConnectivityManager.NetworkCallback callback;

    public NetworkMonitor(Context ctx) {
        cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public void start(Listener listener) {
        NetworkRequest request = new NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build();

        callback = new ConnectivityManager.NetworkCallback() {
            @Override public void onAvailable(Network network) { listener.onOnline(); }
            @Override public void onLost(Network network) { listener.onOffline(); }
        };

        cm.registerNetworkCallback(request, callback);
    }

    public void stop() {
        if (callback != null) cm.unregisterNetworkCallback(callback);
        callback = null;
    }
}