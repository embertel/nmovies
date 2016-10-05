package com.emilybertelson.newtonmovies.services;

import com.emilybertelson.newtonmovies.entities.MoviesCollection;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OMDbClient {

    // Returns deserialized response from OMDb API, given a title to search for and page number.
    // Note that the query searches only for movies, not TV series or episodes.
    public MoviesCollection searchForMovies(String title, int page) {

        // make the HTTP request to OMDb
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(
                "http://www.omdbapi.com/?s=" + title + "&type=movie&r=json&page=" + page,
                String.class);

        // deserialize JSON result to a MoviesCollection object
        Gson g = new GsonBuilder().create();
        MoviesCollection moviesCollection = g.fromJson(response, MoviesCollection.class);

        return moviesCollection;
    }
}
