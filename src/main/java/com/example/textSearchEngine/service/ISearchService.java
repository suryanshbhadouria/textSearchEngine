package com.example.textSearchEngine.service;

import com.example.textSearchEngine.dto.response.SearchResponse;

/**
 * Created by suryansh on 22/6/17.
 */
public interface ISearchService {
    SearchResponse searchToken(String token);
}
