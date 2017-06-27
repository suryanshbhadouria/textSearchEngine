package com.example.textSearchEngine.mao;

import com.example.textSearchEngine.entity.DocumentNameToListOfLinesMap;

/**
 * Created by suryansh on 27/6/17.
 */
public interface IDocumentNameToListOfLinesMapMao {

    public DocumentNameToListOfLinesMap save(DocumentNameToListOfLinesMap documentNameToListOfLinesMap);

    public DocumentNameToListOfLinesMap findById(String id);

}
