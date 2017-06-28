package com.example.textSearchEngine.service.impl;

import com.example.textSearchEngine.service.IFileReadServive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by suryansh on 22/6/17.
 */
@Service("fileReadService")
public class FileReadServiceImpl implements IFileReadServive {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Value("${files.dir}")
    String filesDir;

    /**
     * @param fileName
     * @param lineNumbers
     * @return List<String>
     * Retreiving given lines from file by parsing it sequentially
     */
    @Override
    public List<String> getLinesFromFile(String fileName, List<Long> lineNumbers) {
        LOG.info("Reading the file: {} to get relevant data on lineNumbers:{}", lineNumbers);
        List<Long> linesList = deepCopy(lineNumbers);
        List<String> relevantLines = new ArrayList<>();
        File filesDir = new File(this.filesDir);
        List<File> files = Arrays.asList(filesDir.listFiles());
        for (File file : files) {
            try {
                if (file.getName().equals(fileName)) {
                    try {
                        BufferedReader br = new BufferedReader(new FileReader(file.getAbsolutePath()));
                        String line;
                        Long i = 1l;
                        while ((line = br.readLine()) != null && linesList.size() > 0) {
                            if (lineNumbers.contains(i)) {
                                relevantLines.add(line);
                                linesList.remove(i);
                            }
                            i++;
                        }
                    } catch (Exception e) {
                        LOG.info("Error when reading file:{}", file.getName());
                    }
                }
            } catch (Exception e) {
                LOG.info("Error while reading file with name:{}", file.getName());
            }
        }
        return relevantLines;
    }

    /**
     * @param lineList
     * @return List
     * Returns a deepCopy of the list being sent as param
     */
    private List<Long> deepCopy(List<Long> lineList) {
        List<Long> tempFilesList = new ArrayList<>();
        if (lineList != null) {
            for (Long lineNumber : lineList) {
                tempFilesList.add(new Long(lineNumber));
            }
        }
        return tempFilesList;
    }
}
