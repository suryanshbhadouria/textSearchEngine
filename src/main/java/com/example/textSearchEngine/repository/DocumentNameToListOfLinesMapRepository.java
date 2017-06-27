package com.example.textSearchEngine.repository;

import com.example.textSearchEngine.entity.DocumentNameToListOfLinesMap;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by suryansh on 27/6/17.
 */
public interface DocumentNameToListOfLinesMapRepository extends MongoRepository<DocumentNameToListOfLinesMap, String> {

    public DocumentNameToListOfLinesMap findById(String id);

}
