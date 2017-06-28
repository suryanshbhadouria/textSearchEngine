package com.example.textSearchEngine.service.impl;

import com.example.textSearchEngine.dto.response.SearchResponse;
import com.example.textSearchEngine.index.IInvertedIndex;
import com.example.textSearchEngine.service.IFileReadServive;
import com.example.textSearchEngine.service.ISearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by suryansh on 22/6/17.
 */
@Service("searchService")
public class SearchServiceImpl implements ISearchService {
    @Autowired
    IInvertedIndex index;

    @Autowired
    IFileReadServive fileReadServive;

    /**
     * @param token
     * @return SearchResponse
     * Searches for the token in the invertedIndex
     */

    @Override
    public SearchResponse searchToken(String token) {
        SearchResponse response = null;
        Map<String, Map<String, List<Long>>> tokenToDocumentNameToLineNumberMap = index.getTokenToDocumentNameToLineNumberMap();
        if (tokenToDocumentNameToLineNumberMap != null) {
            if (!tokenToDocumentNameToLineNumberMap.containsKey(token)) {
                response = new SearchResponse();
                response.setSuccessful(true);
                response.setDocumentToListOfLinesMap(null);
                response.setMessage("Token " + token + " not found");
                response.setCode(200);
                return response;
            }
            Map<String, List<Long>> documentToPageNumberMap = tokenToDocumentNameToLineNumberMap.get(token);
            if (documentToPageNumberMap != null) {
                Map<String, List<String>> documentToListOfLinesMap = new HashMap<>();
                for (Map.Entry<String, List<Long>> entry : documentToPageNumberMap.entrySet()) {
                    List<String> linesFromFile = fileReadServive.getLinesFromFile(entry.getKey(), entry.getValue());
                    if (linesFromFile != null && linesFromFile.size() > 0) {
                        documentToListOfLinesMap.put(entry.getKey(), linesFromFile);
                    }
                }
                response = new SearchResponse();
                response.setSuccessful(true);
                response.setMessage("Token " + token + " found");
                response.setCode(200);
                response.setDocumentToListOfLinesMap(documentToListOfLinesMap);
            }
        }
        return response;
    }

}
