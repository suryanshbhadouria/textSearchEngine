package com.example.textSearchEngine.service;

import com.example.textSearchEngine.dto.request.SearchRequest;
import com.example.textSearchEngine.error.ValidationError;

/**
 * Created by suryansh on 22/6/17.
 */
public interface IRequestValidationService {
    ValidationError validateInputRequest(SearchRequest request) throws Exception;
}
