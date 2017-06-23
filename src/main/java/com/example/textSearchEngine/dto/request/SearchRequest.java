package com.example.textSearchEngine.dto.request;

/**
 * Created by suryansh on 22/6/17.
 */
public class SearchRequest {
    private String searchQuery;

    public SearchRequest() {
    }

    public SearchRequest(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SearchRequest that = (SearchRequest) o;

        return searchQuery != null ? searchQuery.equals(that.searchQuery) : that.searchQuery == null;

    }

    @Override
    public int hashCode() {
        return searchQuery != null ? searchQuery.hashCode() : 0;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SearchRequest{");
        sb.append("searchQuery='").append(searchQuery).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
