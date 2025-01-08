package com.nightbirds.streamz;

public class NotificationItem {
    private String id;
    private String title;
    private String message;

    public NotificationItem(String id, String title, String message) {
        this.id = id;
        this.title = title;
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }
}
