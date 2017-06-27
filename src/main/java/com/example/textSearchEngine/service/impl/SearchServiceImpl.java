package com.example.textSearchEngine.service.impl;

import com.example.textSearchEngine.dto.response.SearchResponse;
import com.example.textSearchEngine.dto.response.codes.ResponseCodes;
import com.example.textSearchEngine.entity.DocumentNameToListOfLinesMap;
import com.example.textSearchEngine.index.IInvertedIndex;
import com.example.textSearchEngine.mao.IDocumentNameToListOfLinesMapMao;
import com.example.textSearchEngine.service.IFileReadServive;
import com.example.textSearchEngine.service.ISearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by suryansh on 22/6/17.
 */
@Service("searchService")
public class SearchServiceImpl implements ISearchService {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Value("${use.mongodb}")
    private boolean useMongo;

    @Autowired
    IInvertedIndex index;

    @Autowired
    IFileReadServive fileReadServive;

    @Autowired
    IDocumentNameToListOfLinesMapMao documentNameToListOfLinesMapMao;

    @Override
    public SearchResponse searchToken(String token) {
        SearchResponse response = null;
        if (useMongo) {
            Map<String, String> tokenToDocumentEntityMap = index.getTokenToDocumentEntityMap();
            if (tokenToDocumentEntityMap != null) {
                if (!tokenToDocumentEntityMap.containsKey(token)) {
                    LOG.info("Token : {} not found", token);
                    return tokenNotFoundResponse(token);
                }
                String id = tokenToDocumentEntityMap.get(token);
                DocumentNameToListOfLinesMap byId = documentNameToListOfLinesMapMao.findById(id);
                Map<String, List<Long>> documentToListOfLineNumbersMap = byId.getDocumentNameToLinesmap();
                if (documentToListOfLineNumbersMap != null && documentToListOfLineNumbersMap.size() > 0) {
                    return tokenFoundResponse(documentToListOfLineNumbersMap, token);
                }
            }
        } else {
            Map<String, Map<String, List<Long>>> tokenToDocumentNameToLineNumberMap = index.getTokenToDocumentNameToLineNumberMap();
            if (tokenToDocumentNameToLineNumberMap != null) {
                if (!tokenToDocumentNameToLineNumberMap.containsKey(token)) {
                    LOG.info("Token : {} not found", token);
                    return tokenNotFoundResponse(token);
                }
                Map<String, List<Long>> documentToPageNumberMap = tokenToDocumentNameToLineNumberMap.get(token);
                if (documentToPageNumberMap != null) {
                    return tokenFoundResponse(documentToPageNumberMap, token);
                }
            }
        }
        return response;
    }

    private SearchResponse tokenNotFoundResponse(String token) {
        SearchResponse response = new SearchResponse();
        response.setSuccessful(true);
        response.setDocumentToListOfLinesMap(null);
        response.setMessage("Token " + token + " not found");
        response.setCode(ResponseCodes.SUCCESSFUL.getCode());
        return response;
    }

    private SearchResponse tokenFoundResponse(Map<String, List<Long>> documentToListOfLineNumbersMap, String token) {
        SearchResponse response = new SearchResponse();
        Map<String, List<String>> documentToLinesMap = new HashMap<>();
        for (Map.Entry<String, List<Long>> entry : documentToListOfLineNumbersMap.entrySet()) {
            List<String> linesFromFile = fileReadServive.getLinesFromFile(entry.getKey(), entry.getValue());
            if (linesFromFile != null && linesFromFile.size() > 0) {
                documentToLinesMap.put(entry.getKey(), linesFromFile);
            }
        }
        response = new SearchResponse();
        response.setSuccessful(true);
        response.setMessage("Token " + token + " found");
        response.setCode(ResponseCodes.SUCCESSFUL.getCode());
        response.setDocumentToListOfLinesMap(documentToLinesMap);
        return response;
    }
}
