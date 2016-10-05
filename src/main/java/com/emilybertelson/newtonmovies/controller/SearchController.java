package com.emilybertelson.newtonmovies.controller;

import com.emilybertelson.newtonmovies.entities.MoviesCollection;
import com.emilybertelson.newtonmovies.services.OMDbClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class SearchController {

    private OMDbClient client;

    SearchController() {
        client = new OMDbClient();
    }

    // TODO: learn proper dependency injection...
    // I think there's something that can be done with @Autowired but I don't understand it yet.
    SearchController(OMDbClient client) {
        this.client = client;
    }

    @RequestMapping("/search")
    public ModelAndView showResult(
            @RequestParam(value = "title", required = false, defaultValue = "Newton") String title,
            @RequestParam(value = "page", required = false, defaultValue = "1") String page) {

        // change page number to 1 if it's not something reasonable
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

        // make the request to OMDb
        MoviesCollection moviesCollection = client.searchForMovies(title, pageNumber);

        // set page number to 0 if we didn't get any results
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