package com.nightbirds.streamz;

public class Movie {
    private String title;
    private String year;
    private String story;
    private String posterUrl;

    private String movieUrl;
    private String movieCategory;

    public Movie(String title, String year, String story, String posterUrl, String movieUrl, String movieCategory) {
        this.title = title;
        this.year = year;
        this.story = story;
        this.posterUrl = posterUrl;
        this.movieUrl = movieUrl;
        this.movieCategory = movieCategory;
    }

    public String getTitle() {
        return title;
    }

    public String getYear() {
        return year;
    }

    public String getStory() {
        return story;
    }

    public String getPosterUrl() {
        return posterUrl;
    }
    public String getMovieUrl(){
        return movieUrl;
    }
    public  String getMovieCategory(){return movieCategory;}
}
