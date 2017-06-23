package com.example.textSearchEngine.service;

import java.util.List;

/**
 * Created by suryansh on 22/6/17.
 */
public interface IFileReadServive {
    List<String> getLinesFromFile(String fileName, List<Long> lineNumbers);
}
