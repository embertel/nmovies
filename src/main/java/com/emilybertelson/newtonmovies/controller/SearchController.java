package com.emilybertelson.newtonmovies.controller;

import com.google.gson.*;
import com.google.gson.annotations.SerializedName;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.lang.reflect.Type;


class Movie {
    @SerializedName("Title")
    private String title;
    @SerializedName("Year")
    private String year;
    @SerializedName("imdbID")
    private String imdbID;

    public Movie() { }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getImdbID() {
        return imdbID;
    }

    public void setImdbID(String imdbID) {
        this.imdbID = imdbID;
    }
}

class MoviesCollection {
    @SerializedName("Search")
    public Movie[] movies;

    public MoviesCollection() { }
}

@Controller
public class SearchController {

    @RequestMapping("/search")
    public ModelAndView showResult(
            @RequestParam(value = "title", required = false, defaultValue = "Newton") String title) {

        // make the HTTP request to OMdb
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(
                "http://www.omdbapi.com/?s=" + title + "&type=movie&r=json",
                String.class);

        Gson g = new GsonBuilder().create();

        MoviesCollection moviesCollection = g.fromJson(response, MoviesCollection.class);

        String tableRows = "";
        for(int i = 0; i < moviesCollection.movies.length; i++)
        {
            tableRows += "<tr>";
            tableRows += "<td>" + moviesCollection.movies[i].getTitle() + "</td>";
            tableRows += "<td>" + moviesCollection.movies[i].getYear() + "</td>";
            tableRows += "<td><a href=\"http://imdb.com/title/" + moviesCollection.movies[i].getImdbID() + "/\">IMDb</a></td>";
            tableRows += "</tr>";
        }

        ModelAndView mv = new ModelAndView("search");
        mv.addObject("title", title);
        mv.addObject("movies", moviesCollection.movies);
        mv.addObject("tableRows", tableRows);
        return mv;
    }
}