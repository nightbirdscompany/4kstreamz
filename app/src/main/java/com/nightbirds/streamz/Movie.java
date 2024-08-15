package com.nightbirds.streamz;

public class Movie {

    private String movie_title;
    private String movie_poster;
    private String movie_url;
    private String movie_actor;

    public Movie(){}
    public Movie(String movie_title,String movie_poster,String movie_url,String movie_actor){
        this.movie_title = movie_title;
        this.movie_poster = movie_poster;
        this.movie_url = movie_url;
        this.movie_actor = movie_actor;

    }

    public String getMovie_title() {
        return movie_title;
    }

    public void setMovie_title(String movie_title) {
        this.movie_title = movie_title;
    }

    public String getMovie_poster() {
        return movie_poster;
    }

    public void setMovie_poster(String movie_poster) {
        this.movie_poster = movie_poster;
    }

    public String getMovie_url() {
        return movie_url;
    }

    public void setMovie_url(String movie_url) {
        this.movie_url = movie_url;
    }

    public String getMovie_actor(){ return movie_actor;}

    public void setMovie_actor(String movie_actor){this.movie_actor = movie_actor;}
}
