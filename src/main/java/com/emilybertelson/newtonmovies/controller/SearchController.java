package com.emilybertelson.newtonmovies.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SearchController {

    @RequestMapping("/search")
    public ModelAndView showMessage(
            /*@RequestParam(value = "name", required = false, defaultValue = "World") String name*/) {

        // make the HTTP request to OMdb
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(
                "http://www.omdbapi.com/?s=newton&type=movie&r=json",
                String.class);

        ModelAndView mv = new ModelAndView("search");
        mv.addObject("json", response);
        return mv;
    }
}