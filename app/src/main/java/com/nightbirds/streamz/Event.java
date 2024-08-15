package com.nightbirds.streamz;

public class Event {

    private String event_title;
    private String event_poster;
    private String event_url;

    public Event(){}
    public Event(String event_title,String event_poster,String event_url) {
        this.event_title = event_title;
        this.event_poster = event_poster;
        this.event_url = event_url;

    }

    public String getEvent_title() {
        return event_title;
    }

    public void setEvent_title(String event_title) {
        this.event_title = event_title;
    }

    public String getEvent_poster() {
        return event_poster;
    }

    public void setEvent_poster(String event_poster) {
        this.event_poster = event_poster;
    }

    public String getEvent_url() {
        return event_url;
    }

    public void setEvent_url(String event_url) {
        this.event_url = event_url;
    }
}
