package com.example.textSearchEngine.service.impl;

import com.example.textSearchEngine.dto.request.SearchRequest;
import com.example.textSearchEngine.dto.response.codes.ResponseCodes;
import com.example.textSearchEngine.error.ValidationError;
import com.example.textSearchEngine.service.IRequestValidationService;
import org.springframework.stereotype.Service;

/**
 * Created by suryansh on 22/6/17.
 */
@Service("requestValidationService")
public class RequestValidationService implements IRequestValidationService {

    @Override
    public ValidationError validateInputRequest(SearchRequest request) throws Exception {
        ValidationError error = null;
        if (request == null) {
            error = new ValidationError();
            error.setCode(ResponseCodes.BAD_REQUEST.getCode());
            error.setMessage(ResponseCodes.BAD_REQUEST.getMessage());
            return error;
        }
        String searchQuery = request.getSearchQuery();
        if (searchQuery == null || (searchQuery.length() == 0)) {
            error = new ValidationError();
            error.setCode(ResponseCodes.BAD_REQUEST.getCode());
            error.setMessage(ResponseCodes.BAD_REQUEST.getMessage());
        }
        return error;
    }

}
