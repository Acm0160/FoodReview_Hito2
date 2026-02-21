package com.example.foodreview;

import android.view.*;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.*;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MessageViewHolder> {

    private List<ChatMessage> messages = new ArrayList<>();

    private static final int TYPE_MINE = 1;
    private static final int TYPE_OTHER = 2;

    public void addMessage(ChatMessage message) {
        messages.add(message);
        notifyItemInserted(messages.size() - 1);
    }

    @Override
    public int getItemViewType(int position) {
        return messages.get(position).isMine() ? TYPE_MINE : TYPE_OTHER;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        int layout = (viewType == TYPE_MINE)
                ? R.layout.activity_mis_mensajes
                : R.layout.activity_mensaje_soporte;

        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        holder.txtMessage.setText(messages.get(position).getMessage());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView txtMessage;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            txtMessage = itemView.findViewById(R.id.txtMessage);
        }
    }
}