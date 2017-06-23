package com.example.textSearchEngine.controller;

import com.example.textSearchEngine.dto.request.SearchRequest;
import com.example.textSearchEngine.dto.response.SearchResponse;
import com.example.textSearchEngine.dto.response.codes.ResponseCodes;
import com.example.textSearchEngine.error.ValidationError;
import com.example.textSearchEngine.service.IRequestValidationService;
import com.example.textSearchEngine.service.ISearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.gson.Gson;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by suryansh on 22/6/17.
 */

@RestController("SearchApiController")
public class SearchApiController {

    @Autowired
    IRequestValidationService requestValidationService;

    @Autowired
    ISearchService searchService;

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());


    @RequestMapping(value = "/findwordinfile", method = RequestMethod.GET)
    @Produces(MediaType.APPLICATION_JSON)
    public SearchResponse searchToken(@RequestParam(value = "word", required = true) String word) throws ParseException {
        LOG.info("Request recieved to get fetch all occurences of : {} ", word);
        SearchResponse response = new SearchResponse();
        try {
            if (word == null || word.length() == 0) {
                ValidationError error = new ValidationError();
                error.setCode(ResponseCodes.BAD_REQUEST.getCode());
                error.setMessage(ResponseCodes.BAD_REQUEST.getMessage());
                response.setValidationError(error);
                return response;
            }
            response = searchService.searchToken(word);
        } catch (Exception e) {
            LOG.error("Error while GET request for api-findwordinfile: {}", e);
        }
        return response;
    }

    /**
     * @param request
     * @return
     * @throws ParseException
     */

    @RequestMapping("/findwordinfile")
    @Produces(MediaType.APPLICATION_JSON)
    public SearchResponse searchTokenPost(@RequestBody SearchRequest request) throws ParseException {
        LOG.info("Request recieved to get fetch all occurences for request: {} ", request);
        SearchResponse response = new SearchResponse();
        try {
            ValidationError error = requestValidationService.validateInputRequest(request);
            if (error != null) {
                response.setValidationError(error);
                return response;
            }
            String searchQuery = request.getSearchQuery();
            response = searchService.searchToken(searchQuery);
        } catch (Exception e) {
            LOG.error("Error while POST request for api-findwordinfile: {}", e);
        }
        return response;
    }


}
