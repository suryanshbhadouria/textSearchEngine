package com.example.textSearchEngine.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.annotation.Id;

import java.util.List;
import java.util.Map;

/**
 * Created by suryansh on 27/6/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DocumentNameToListOfLinesMap {
    @Id
    private String id;

    private Map<String, List<Long>> documentNameToLinesmap;

    public DocumentNameToListOfLinesMap() {
    }

    public DocumentNameToListOfLinesMap(Map<String, List<Long>> documentNameToLinesmap) {
        this.documentNameToLinesmap = documentNameToLinesmap;
    }

    public DocumentNameToListOfLinesMap(String id, Map<String, List<Long>> documentNameToLinesmap) {
        this.id = id;
        this.documentNameToLinesmap = documentNameToLinesmap;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Cacheable(value = "documentsFromMongo")
    public Map<String, List<Long>> getDocumentNameToLinesmap() {
        return documentNameToLinesmap;
    }

    public void setDocumentNameToLinesmap(Map<String, List<Long>> documentNameToLinesmap) {
        this.documentNameToLinesmap = documentNameToLinesmap;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DocumentNameToListOfLinesMap{");
        sb.append("id='").append(id).append('\'');
        sb.append(", documentNameToLinesmap=").append(documentNameToLinesmap);
        sb.append('}');
        return sb.toString();
    }
}
