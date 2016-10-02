package com.emilybertelson.newtonmovies;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MoviesCollection {
    @SerializedName("Search")
    private List<Movie> movies;
    @SerializedName("totalResults")
    private int totalResults;

    public MoviesCollection() { }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }
}
