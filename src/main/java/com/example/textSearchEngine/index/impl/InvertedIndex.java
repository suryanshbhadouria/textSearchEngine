package com.example.textSearchEngine.index.impl;

import com.example.textSearchEngine.dto.response.SearchResponse;
import com.example.textSearchEngine.index.IInvertedIndex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by suryansh on 22/6/17.
 */
@Component(value = "InvertedIndex")
public class InvertedIndex implements IInvertedIndex {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private Map<String, Map<String, List<Long>>> tokenToDocumentNameToLineNumberMap;

    @PostConstruct
    public void init() {
        tokenToDocumentNameToLineNumberMap = new HashMap<>();
    }

    @Override
    public Map<String, Map<String, List<Long>>> getTokenToDocumentNameToLineNumberMap() {
        return tokenToDocumentNameToLineNumberMap;
    }

    @Override
    public void setTokenToDocumentNameToLineNumberMap(Map<String, Map<String, List<Long>>> tokenToDocumentNameToLineNumberMap) {
        this.tokenToDocumentNameToLineNumberMap = tokenToDocumentNameToLineNumberMap;
    }

    @Override
    public void addToken(String token, String documentName, Long lineNumber) {
        if (tokenToDocumentNameToLineNumberMap != null) {
            if (!tokenToDocumentNameToLineNumberMap.containsKey(token)) {
                tokenToDocumentNameToLineNumberMap.put(token, new HashMap<>());
            }
            Map<String, List<Long>> documentToPageNumberMap = tokenToDocumentNameToLineNumberMap.get(token);
            if (documentToPageNumberMap != null) {
                if (documentToPageNumberMap.containsKey(documentName)) {
                    List<Long> pages = documentToPageNumberMap.get(documentName);
                    pages.add(lineNumber);
                } else {
                    List<Long> pages = new ArrayList<Long>();
                    pages.add(lineNumber);
                    documentToPageNumberMap.put(documentName, pages);
                }
            } else {
                LOG.error("Should not come here");
            }
        }
    }

    @Override
    public boolean removeToken(String token, String documentName) {
        boolean removed = false;
        if (tokenToDocumentNameToLineNumberMap != null && !tokenToDocumentNameToLineNumberMap.isEmpty()) {
            if (tokenToDocumentNameToLineNumberMap.containsKey(token)) {
                Map<String, List<Long>> documentToPagesMap = tokenToDocumentNameToLineNumberMap.get(token);
                if (documentToPagesMap != null && !documentToPagesMap.isEmpty()) {
                    if (documentToPagesMap.containsKey(documentName)) {
                        documentToPagesMap.remove(documentName);
                        removed = true;
                    } else {
                        LOG.info("documentNameToLineNumberMap does not contain the key :{}", documentName);
                        return removed;
                    }
                }
            }
            LOG.info("tokenToDocumentNameToLineNumberMap does not contain the key :{}", token);
            return removed;
        }
        LOG.info("tokenToDocumentNameToLineNumberMap is empty :{}");
        return removed;
    }
}
