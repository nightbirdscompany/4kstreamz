package com.nightbirds.streamz;

public class BanglaChannel {
    private String channel_name;
    private String channel_logo;
    private String channel_url;

    public BanglaChannel(){}

    public BanglaChannel(String channel_name,String channel_logo,String channel_url){
        this.channel_name = channel_name;
        this.channel_logo = channel_logo;
        this.channel_url = channel_url;

    }

    public String getChannel_name() {
        return channel_name;
    }

    public void setChannel_name(String channel_name) {
        this.channel_name = channel_name;
    }

    public String getChannel_logo() {
        return channel_logo;
    }

    public void setChannel_logo(String channel_logo) {
        this.channel_logo = channel_logo;
    }

    public String getChannel_url() {
        return channel_url;
    }

    public void setChannel_url(String channel_url) {
        this.channel_url = channel_url;
    }
}
