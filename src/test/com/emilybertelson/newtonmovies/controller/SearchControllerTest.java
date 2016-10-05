package com.emilybertelson.newtonmovies.controller;

import com.emilybertelson.newtonmovies.entities.Movie;
import com.emilybertelson.newtonmovies.entities.MoviesCollection;
import com.emilybertelson.newtonmovies.services.OMDbClient;
import junit.framework.TestCase;
import junitparams.JUnitParamsRunner;
import org.junit.BeforeClass;
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

    private static SearchController searchController;

    private String noResults = "{\"Response\":\"False\",\"Error\":\"Movie not found!\"}";

    @BeforeClass
    public static void initializeController()
    {
        searchController = new SearchController();
    }

    // utility method to normalize capitalization of input titles
    private String capitalize(String input) {
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }

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

        searchController = new SearchController(mockClient);
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

        searchController = new SearchController(mockClient);
        ModelAndView mv = searchController.showResult("Newton", "80");

        assertTrue(mv.hasView());
        assertEquals("search", mv.getViewName());

        Map<String, Object> map = mv.getModel();
        assertEquals("Newton", map.get("title"));
        assertEquals(0, map.get("totalResults"));
        assertEquals(0, map.get("page"));
        assertEquals(null, map.get("movies"));
    }

//    // Test default case and some variations that should work fine
//    @Test
//    @Parameters({
//            "Newton, 1",
//            "newton, 1",
//            "NEWtOn, 1",
//            "Newton, 2",
//            "Newt, 1",
//            "hello, 4"
//    })
//    public void testSearchController_HappyCases(String title, String page) {
//
//        ModelAndView mv = searchController.showResult(title, page);
//        assertTrue(mv.hasView());
//        assertEquals("search", mv.getViewName());
//
//        Map<String, Object> map = mv.getModel();
//        assertEquals(title, map.get("title"));
//        assertEquals(Integer.parseInt(page), map.get("page"));
//        int totalResults = (Integer) map.get("totalResults");
//        assertTrue(totalResults > 0);
//
//        // examine results
//        List<Movie> movies = (List<Movie>) map.get("movies");
//
//        if(totalResults >= 10) {
//            assertEquals(
//                    "Results per page expected to be 10, but got " + movies.size(),
//                    10, movies.size());
//        }
//        else {
//            assertEquals(
//                    "Expected to get " + totalResults + " results, but got " + movies.size(),
//                    totalResults, movies.size());
//        }
//
//        for (Movie movie : movies) {
//            assertTrue(
//                    "Movie title " + movie.getTitle() + " does not contain " + title,
//                    movie.getTitle().contains(capitalize(title)));
//        }
//    }
//
//    // Confirm that page numbers that are less than 0 or non-integers get redirected to page 1
//    @Test
//    @Parameters({"Newton, 0",
//                "Newton, -1",
//                "Newton, -0.2",
//                "Newton, 4.6",
//                "Newton, this_is_not_a page_number!"})
//    public void testSearchController_MalformedPageNumber(String title, String page) {
//
//        ModelAndView mv = searchController.showResult(title, page);
//        assertTrue(mv.hasView());
//        assertEquals("search", mv.getViewName());
//
//        Map<String, Object> map = mv.getModel();
//        assertEquals(title, map.get("title"));
//        assertEquals(1, map.get("page"));
//    }
//
//    // Cases where OMDb returns zero results: search term doesn't exist in any movie,
//    // or page number is beyond the number of pages for the results
//    // (Note that the OMDb API's response is identical for titles that return 0 results
//    // and titles that do return results, but the page is out of range. For the time being,
//    // we aren't distinguishing between the two, though we could try an extra API call to
//    // page 1 if we get back no results...)
//    @Test
//    @Parameters({"there's no way there's a movie with this in the title, 1",
//                "weird strings !#x/:%@$#$@, 45%*$3",
//                "Newton, 85"
//    })
//    public void testSearchController_NoResult(String title, String page) {
//
//        ModelAndView mv = searchController.showResult(title, page);
//        assertTrue(mv.hasView());
//        assertEquals("search", mv.getViewName());
//
//        Map<String, Object> map = mv.getModel();
//        assertEquals(title, map.get("title"));
//        assertEquals(0, map.get("totalResults"));
//        assertEquals(0, map.get("page"));
//        assertEquals(null, map.get("movies"));
//    }
}