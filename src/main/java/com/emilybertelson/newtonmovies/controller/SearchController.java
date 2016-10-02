package com.emilybertelson.newtonmovies.controller;

import com.emilybertelson.newtonmovies.Movie;
import com.emilybertelson.newtonmovies.MoviesCollection;
import com.google.gson.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;


@Controller
public class SearchController {

    // Returns deserialized response from OMDb API, given a title to search for and page number.
    // Note that the query searches only for movies, not TV series or episodes.
    private MoviesCollection searchForMovies(String title, int page) {
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

    @RequestMapping("/search")
    public ModelAndView showResult(
            @RequestParam(value = "title", required = false, defaultValue = "Newton") String title,
            @RequestParam(value = "page", required = false, defaultValue = "1") String page) {

        int pageNumber;
        try {
            pageNumber = Integer.parseInt(page);
        } catch(java.lang.NumberFormatException ex) {
            // if the page number is not an int (e.g. "2.5" or "blah"), just redirect to page 1
            pageNumber = 1;
        }
        // round up negative/zero page number to 1
        if(pageNumber < 1) {
            pageNumber = 1;
        }

        MoviesCollection moviesCollection = searchForMovies(title, pageNumber);

        if(moviesCollection.getTotalResults() == 0) {
            pageNumber = 0;
        }

        // pass data to the model and view
        ModelAndView mv = new ModelAndView("search");
        mv.addObject("title", title);
        mv.addObject("movies", moviesCollection.getMovies());
        mv.addObject("totalResults", moviesCollection.getTotalResults());
        mv.addObject("page", pageNumber);
        return mv;
    }
}