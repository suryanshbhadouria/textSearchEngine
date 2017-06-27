package com.example.textSearchEngine.mao.impl;

import com.example.textSearchEngine.entity.DocumentNameToListOfLinesMap;
import com.example.textSearchEngine.mao.IDocumentNameToListOfLinesMapMao;
import com.example.textSearchEngine.repository.DocumentNameToListOfLinesMapRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Created by suryansh on 27/6/17.
 */
@Repository("documentNameToListOfLinesMapDao")
public class DocumentNameToListOfLinesMapMaoImpl implements IDocumentNameToListOfLinesMapMao {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());
    @Autowired
    DocumentNameToListOfLinesMapRepository repository;

    @Override
    public DocumentNameToListOfLinesMap save(DocumentNameToListOfLinesMap documentNameToListOfLinesMap) {
        LOG.info("Saving DocumentNameToListOfLinesMap entity : {}", documentNameToListOfLinesMap);
        return repository.save(documentNameToListOfLinesMap);
    }

    @Override
    public DocumentNameToListOfLinesMap findById(String id) {
        LOG.info("Retreiving entity by id : {}", id);
        DocumentNameToListOfLinesMap byId = repository.findById(id);
        LOG.info("Entity found for id : {} is :{}", id, byId);
        return byId;
    }
}
