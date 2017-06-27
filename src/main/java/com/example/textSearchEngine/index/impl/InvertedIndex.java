package com.example.textSearchEngine.index.impl;

import com.example.textSearchEngine.entity.DocumentNameToListOfLinesMap;
import com.example.textSearchEngine.index.IInvertedIndex;
import com.example.textSearchEngine.mao.IDocumentNameToListOfLinesMapMao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by suryansh on 22/6/17.
 */
@Component(value = "invertedIndex")
public class InvertedIndex implements IInvertedIndex {

    @Value("${use.mongodb}")
    private boolean useMongo;

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private Map<String, Map<String, List<Long>>> tokenToDocumentNameToLineNumberMap;

    private Map<String, String> tokenToDocumentEntityMap;

    @Autowired
    private IDocumentNameToListOfLinesMapMao documentNameToListOfLinesMapMao;

    @PostConstruct

    public void init() {
        tokenToDocumentNameToLineNumberMap = new HashMap<>();
        tokenToDocumentEntityMap = new HashMap<>();
    }

    @Override
    @Cacheable("documentsInMemory")
    public Map<String, Map<String, List<Long>>> getTokenToDocumentNameToLineNumberMap() {
        return tokenToDocumentNameToLineNumberMap;
    }

    @Override
    public void setTokenToDocumentNameToLineNumberMap(Map<String, Map<String, List<Long>>> tokenToDocumentNameToLineNumberMap) {
        this.tokenToDocumentNameToLineNumberMap = tokenToDocumentNameToLineNumberMap;
    }

    @Override
    @Cacheable("documentsFromMongo")
    public Map<String, String> getTokenToDocumentEntityMap() {
        return tokenToDocumentEntityMap;
    }

    @Override
    public void setTokenToDocumentEntityMap(Map<String, String> tokenToDocumentEntityMap) {
        this.tokenToDocumentEntityMap = tokenToDocumentEntityMap;
    }

    @Override
    public void addToken(String token, String documentName, Long lineNumber) {
        if (useMongo) {
            if (tokenToDocumentEntityMap != null) {
                if (!tokenToDocumentEntityMap.containsKey(token)) {
                    tokenToDocumentEntityMap.put(token, null);
                }
                String documentId = tokenToDocumentEntityMap.get(token);
                if (documentId == null) {
                    Map<String, List<Long>> documentNameToLinesmap = new HashMap<>();
                    List<Long> pages = new ArrayList<Long>();
                    pages.add(lineNumber);
                    documentNameToLinesmap.put(documentName, pages);
                    DocumentNameToListOfLinesMap byId = new DocumentNameToListOfLinesMap();
                    byId.setDocumentNameToLinesmap(documentNameToLinesmap);
                    documentNameToListOfLinesMapMao.save(byId);
                    tokenToDocumentEntityMap.put(token, byId.getId());
                } else {
                    DocumentNameToListOfLinesMap byId = documentNameToListOfLinesMapMao.findById(documentId);
                    Map<String, List<Long>> documentNameToLinesmap = byId.getDocumentNameToLinesmap();
                    if (documentNameToLinesmap.containsKey(documentName)) {
                        List<Long> pages = documentNameToLinesmap.get(documentName);
                        pages.add(lineNumber);
                    } else {
                        List<Long> pages = new ArrayList<Long>();
                        pages.add(lineNumber);
                        documentNameToLinesmap.put(documentName, pages);
                    }
                    byId.setDocumentNameToLinesmap(documentNameToLinesmap);
                    documentNameToListOfLinesMapMao.save(byId);
                    tokenToDocumentEntityMap.put(token, byId.getId());
                }
            }
        } else {
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
    }

    @Override
    public void removeFileContentFromIndex(String fileName) {
        if (useMongo) {
            for (Map.Entry<String, String> entry : tokenToDocumentEntityMap.entrySet()) {
                String id = entry.getValue();
                if (id != null) {
                    DocumentNameToListOfLinesMap byId = documentNameToListOfLinesMapMao.findById(id);
                    Map<String, List<Long>> documentNameToLinesmap = byId.getDocumentNameToLinesmap();
                    if (documentNameToLinesmap != null && !documentNameToLinesmap.isEmpty()) {
                        if (documentNameToLinesmap.containsKey(fileName)) {
                            documentNameToLinesmap.remove(fileName);
                        }
                    }
                    documentNameToListOfLinesMapMao.save(byId);
                }
            }
        } else {
            for (Map.Entry<String, Map<String, List<Long>>> entry : tokenToDocumentNameToLineNumberMap.entrySet()) {
                Map<String, List<Long>> documentNameToPagesMap = entry.getValue();
                if (documentNameToPagesMap != null && !documentNameToPagesMap.isEmpty()) {
                    if (documentNameToPagesMap.containsKey(fileName))
                        documentNameToPagesMap.remove(fileName);
                }
            }
        }
    }

    @Override
    public void printCache() {
        if (useMongo) {
            for (Map.Entry<String, String> entry : tokenToDocumentEntityMap.entrySet()) {
                LOG.info("token is: {} ; mongo doc id is: {}", entry.getKey(), entry.getValue());
            }
        } else {
            for (Map.Entry<String, Map<String, List<Long>>> entry : tokenToDocumentNameToLineNumberMap.entrySet()) {
                LOG.info("token is:{}", entry.getKey());
                for (Map.Entry<String, List<Long>> entry1 : entry.getValue().entrySet()) {
                    LOG.info("Document is:{} and lines are:{}", entry1.getKey(), entry1.getValue());
                }
            }
        }
    }

}
