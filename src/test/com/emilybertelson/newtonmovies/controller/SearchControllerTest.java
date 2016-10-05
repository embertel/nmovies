package com.emilybertelson.newtonmovies.controller;

import com.emilybertelson.newtonmovies.entities.Movie;
import com.emilybertelson.newtonmovies.entities.MoviesCollection;
import com.emilybertelson.newtonmovies.services.OMDbClient;
import junit.framework.TestCase;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(JUnitParamsRunner.class)
public class SearchControllerTest extends TestCase{

    // utility method to normalize capitalization of input titles
    private String capitalize(String input) {
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }

    // utility method to generate a list of dummy movies for the mocked OMDBClientService to return
    private List<Movie> generateMovieList(int count) {
        List<Movie> movieList = new ArrayList<Movie>();
        for(int i = 0; i < count; i++) {
            movieList.add(new Movie());
        }
        return movieList;
    }

    @Test
    public void testSearchController_Default() {

        MoviesCollection moviesCollection = new MoviesCollection();
        moviesCollection.setTotalResults(44);
        List<Movie> movieList = generateMovieList(10);
        moviesCollection.setMovies(movieList);

        OMDbClient mockClient = mock(OMDbClient.class);
        when(mockClient.searchForMovies("Newton", 1))
                .thenReturn(moviesCollection);

        SearchController searchController = new SearchController(mockClient);
        ModelAndView mv = searchController.showResult("Newton", "1");

        assertTrue(mv.hasView());
        assertEquals("search", mv.getViewName());

        Map<String, Object> map = mv.getModel();
        assertEquals("Newton", map.get("title"));
        assertEquals(44, map.get("totalResults"));
        assertEquals(1, map.get("page"));
        assertEquals(movieList, map.get("movies"));
    }

    @Test
    public void testSearchController_NoResults() {

        MoviesCollection moviesCollection = new MoviesCollection();
        moviesCollection.setTotalResults(0);

        OMDbClient mockClient = mock(OMDbClient.class);
        when(mockClient.searchForMovies("Newton", 80))
                .thenReturn(moviesCollection);

        SearchController searchController = new SearchController(mockClient);
        ModelAndView mv = searchController.showResult("Newton", "80");

        assertTrue(mv.hasView());
        assertEquals("search", mv.getViewName());

        Map<String, Object> map = mv.getModel();
        assertEquals("Newton", map.get("title"));
        assertEquals(0, map.get("totalResults"));
        assertEquals(0, map.get("page"));
        assertEquals(null, map.get("movies"));
    }

    // Verify that page numbers less than zero or non-integer get converted to 1.
    @Test
    @Parameters({
            "-1",
            "0",
            "3.14",
            "insert page number here"
    })
    public void testSearchController_BadPageNumber(String page) {

        MoviesCollection moviesCollection = new MoviesCollection();
        moviesCollection.setTotalResults(44);
        List<Movie> movieList = generateMovieList(10);
        moviesCollection.setMovies(movieList);

        OMDbClient mockClient = mock(OMDbClient.class);
        when(mockClient.searchForMovies("Newton", 1))
                .thenReturn(moviesCollection);

        SearchController searchController = new SearchController(mockClient);
        ModelAndView mv = searchController.showResult("Newton", page);

        assertTrue(mv.hasView());
        assertEquals("search", mv.getViewName());

        Map<String, Object> map = mv.getModel();
        assertEquals("Newton", map.get("title"));
        assertEquals(44, map.get("totalResults"));
        assertEquals(1, map.get("page"));
        assertEquals(movieList, map.get("movies"));
    }
}