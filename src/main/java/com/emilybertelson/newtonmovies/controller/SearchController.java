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

    private MoviesCollection searchForMovies(String title, int page) {
        // make the HTTP request to OMdb
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(
                "http://www.omdbapi.com/?s=" + title + "&type=movie&r=json&page=" + page,
                String.class);

        // deserialize JSON result to a MoviesCollection object
        Gson g = new GsonBuilder().create();
        MoviesCollection moviesCollection = g.fromJson(response, MoviesCollection.class);

        return moviesCollection;
    }

    // not using below for now because getting ALL movies for certain queries may result in a very long
    // results page, and a lot of separate queries without the user's consent.
    private List<Movie> getAllMoviesContaining(String title) {
        MoviesCollection moviesCollection = searchForMovies(title, 1);
        List<Movie> moviesList = new ArrayList<Movie>(moviesCollection.getMovies());
        //Movie[] moviesArray = moviesCollection.getMovies();

        if(moviesCollection.getTotalResults() > 10) {
                // each page contains up to 10 results. The line below divides the total number of
                // results by 10 and rounds up.
                int totalPages = (int)Math.ceil((double)moviesCollection.getTotalResults() / 10);

                for(int i = 2; i <= totalPages; i++) {
                    moviesCollection = searchForMovies(title, i);
                    moviesList.addAll(moviesCollection.getMovies());
                }
        }

        return moviesList;
    }

    @RequestMapping("/search")
    public ModelAndView showResult(
            @RequestParam(value = "title", required = false, defaultValue = "Newton") String title,
            @RequestParam(value = "page", required = false, defaultValue = "1") String page) {

        int pageNumber = Integer.parseInt(page);
        // round up negative/zero page number to 1
        if(pageNumber < 1) {
            pageNumber = 1;
        }
        // TODO can we handle too-large page numbers here?

        MoviesCollection moviesCollection = searchForMovies(title, pageNumber);

        // pass data to the model and view
        ModelAndView mv = new ModelAndView("search");
        mv.addObject("title", title);
        mv.addObject("movies", moviesCollection.getMovies());
        mv.addObject("totalResults", moviesCollection.getTotalResults());
        mv.addObject("page", pageNumber);
        return mv;
    }
}