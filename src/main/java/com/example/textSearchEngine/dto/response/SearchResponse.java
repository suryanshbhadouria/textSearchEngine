package com.example.textSearchEngine.dto.response;

import com.example.textSearchEngine.error.ValidationError;

import java.util.List;
import java.util.Map;

/**
 * Created by suryansh on 22/6/17.
 */
public class SearchResponse {
    private ValidationError validationError;
    private Map<String, List<String>> documentToListOfLinesMap;
    private boolean successful;
    private Integer code;
    private String message;

    public SearchResponse() {
    }

    public SearchResponse(ValidationError validationError, Map<String, List<String>> documentToListOfLinesMap, boolean successful, Integer code, String message) {
        this.validationError = validationError;
        this.documentToListOfLinesMap = documentToListOfLinesMap;
        this.successful = successful;
        this.code = code;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public ValidationError getValidationError() {
        return validationError;
    }

    public void setValidationError(ValidationError validationError) {
        this.validationError = validationError;
    }

    public Map<String, List<String>> getDocumentToListOfLinesMap() {
        return documentToListOfLinesMap;
    }

    public void setDocumentToListOfLinesMap(Map<String, List<String>> documentToListOfLinesMap) {
        this.documentToListOfLinesMap = documentToListOfLinesMap;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SearchResponse response = (SearchResponse) o;

        if (successful != response.successful) return false;
        if (validationError != null ? !validationError.equals(response.validationError) : response.validationError != null)
            return false;
        if (documentToListOfLinesMap != null ? !documentToListOfLinesMap.equals(response.documentToListOfLinesMap) : response.documentToListOfLinesMap != null)
            return false;
        if (code != null ? !code.equals(response.code) : response.code != null) return false;
        return message != null ? message.equals(response.message) : response.message == null;

    }

    @Override
    public int hashCode() {
        int result = validationError != null ? validationError.hashCode() : 0;
        result = 31 * result + (documentToListOfLinesMap != null ? documentToListOfLinesMap.hashCode() : 0);
        result = 31 * result + (successful ? 1 : 0);
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (message != null ? message.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SearchResponse{");
        sb.append("validationError=").append(validationError);
        sb.append(", documentToListOfLinesMap=").append(documentToListOfLinesMap);
        sb.append(", successful=").append(successful);
        sb.append(", code=").append(code);
        sb.append(", message='").append(message).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
