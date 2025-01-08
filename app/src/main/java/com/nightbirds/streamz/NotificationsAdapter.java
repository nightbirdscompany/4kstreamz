package com.nightbirds.streamz;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder> {
    private List<NotificationItem> notifications;

    public NotificationsAdapter(List<NotificationItem> notifications) {
        this.notifications = notifications;
        sortChannelsAlphabetically();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NotificationItem notification = notifications.get(position);
        holder.title.setText(notification.getTitle());
        holder.message.setText(notification.getMessage());
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    /**
     * Sort the banglaChannels list alphabetically by channel name.
     */
    private void sortChannelsAlphabetically() {
        Collections.sort(notifications, new Comparator<NotificationItem>() {
            @Override
            public int compare(NotificationItem noti2, NotificationItem noti1) {
                return noti2.getId().compareToIgnoreCase(noti1.getId());
            }
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView message;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.notificationTitle);
            message = itemView.findViewById(R.id.notificationMessage);
        }
    }
}
