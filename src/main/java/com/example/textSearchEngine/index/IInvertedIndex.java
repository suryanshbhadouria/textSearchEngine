package com.example.textSearchEngine.index;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by suryansh on 22/6/17.
 */
public interface IInvertedIndex {
    public Map<String, Map<String, List<Long>>> getTokenToDocumentNameToLineNumberMap();

    public void setTokenToDocumentNameToLineNumberMap(Map<String, Map<String, List<Long>>> tokenToDocumentNameToLineNumberMap);

    public void addToken(String token, String documentName, Long lineNumber);

    public boolean removeToken(String token, String documentName);
}
