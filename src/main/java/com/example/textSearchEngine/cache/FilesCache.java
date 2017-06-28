package com.example.textSearchEngine.cache;

import com.example.textSearchEngine.index.impl.InvertedIndex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by suryansh on 21/6/17.
 */
@Component
public class FilesCache {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Value("${files.dir}")
    private String filesDir;

    @Value("${log.pattern}")
    private String logPattern;

    /**
     * Defines the pattern used for tekenisation
     * It is defined in application.properties
     */
    @Value("${use.pattern.based.tokenisation}")
    private Boolean usePatternBasedTokenisation;

    private List<String> filesList;

    private List<String> addedFiles;

    private List<String> removedFiles;

    private boolean startup = true;

    @Autowired
    private InvertedIndex index;

    /**
     * @param filesToBeAdded
     * @param filesToBeRemoved Responsible for updating the cache by removing the words present in filesToBeRemoved and adding the words present in filesToBeRemoved from the inverted Index
     */
    public void loadInvertedIndex(List<String> filesToBeAdded, List<String> filesToBeRemoved) {
        if (!startup) {
            LOG.info("Loading the invertedIndex with data post startup with  files to be added {} and files to be removed:{}", filesToBeAdded, filesToBeRemoved);
            File filesDir = new File(this.filesDir);
            List<File> files = Arrays.asList(filesDir.listFiles());
            //updating files to be added
            for (File file : files) {
                if (filesToBeAdded.contains(file.getName())) {
                    readFileAndLoadContent(file);
                    filesList.add(file.getName());
                }
                if (filesToBeRemoved.contains(file.getName())) {
                    removeFileContentFromIndex(file.getName());
                    filesList.remove(file.getName());
                }
            }
        } else {
            //load data on startup
            LOG.info("Loading the invertedIndex with data on startup");
            File filesDir = new File(this.filesDir);
            List<File> files = Arrays.asList(filesDir.listFiles());
            for (File file : files) {
                try {
                    readFileAndLoadContent(file);
                    filesList.add(file.getName());
                } catch (Exception e) {
                    LOG.info("Error while reading file with name:{}", file.getName());
                }
            }
        }

    }

    /**
     * @param fileName Responsible for updating the cache by removing the words present in file with name fileName
     */
    private void removeFileContentFromIndex(String fileName) {
        Map<String, Map<String, List<Long>>> tokenToDocumentNameToLineNumberMap = index.getTokenToDocumentNameToLineNumberMap();
        for (Map.Entry<String, Map<String, List<Long>>> entry : tokenToDocumentNameToLineNumberMap.entrySet()) {
            Map<String, List<Long>> documentNameToPagesMap = entry.getValue();
            if (documentNameToPagesMap != null && !documentNameToPagesMap.isEmpty()) {
                if (documentNameToPagesMap.containsKey(fileName))
                    documentNameToPagesMap.remove(fileName);
            }
        }
    }


    /**
     * @param file Responsible for updating the cache by adding the words present in file
     */
    public void readFileAndLoadContent(File file) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(file.getAbsolutePath()));
            String line;
            Long i = 1l;
            String logP = new String(logPattern);
            String orderOfSplit = logP.replaceAll("[a-zA-Z]", "");
            orderOfSplit += " ";//adding one to process the last token
            while ((line = br.readLine()) != null) {
                String words[] = null;
                if (usePatternBasedTokenisation) {
                    for (int j = 0; j < orderOfSplit.length(); j++) {
                        String[] splitPattern = logP.split(String.valueOf(orderOfSplit.charAt(j)), 2);
                        String[] splitLine = null;
                        switch (splitPattern[0]) {
                            case "date":
                            case "level":
                                splitLine = line.split(String.valueOf(orderOfSplit.charAt(j)), 2);
                                words = splitLine[0].split(" ");//split to get just the first token
                                line = splitLine[1];
                                logP = splitPattern[1];
                                break;
                            case "time":
                                String timeString = line.substring(0, 8);
                                words = timeString.split(" ");//split to get just the first token
                                line = line.substring(9);
                                logP = splitPattern[1];
                                break;
                            case "class":
                                splitLine = line.split(String.valueOf(orderOfSplit.charAt(j)), 2);
                                words = splitLine[0].split("\\.");
                                line = splitLine[1];
                                logP = splitPattern[1];
                                break;
                            case "message":
                                words = line.replaceAll("[^a-zA-Z0-9_]", " ").split(" ");
                        }
                        for (String word : words) {
                            if (word.equals("\\s+") || word.length() == 0)
                                continue;
                            index.addToken(word, file.getName(), i);
                        }
                    }
                }
                i++;
            }
        } catch (Exception e) {
            LOG.info("Error when reading file:{};error:{}", file.getName(), e);
        }
    }

    /**
     * Responsible for reloading the invertedIndex
     */
    public void reloadCache() {
        if (startup) {
            LOG.info("Loading the invertedIndex for the first time");
            loadInvertedIndex(null, null);
            startup = false;
        } else {
            //parse all the removed files and remove from invertedIndex
            loadInvertedIndex(addedFiles, removedFiles);
            //parse all the added files and add to invertedIndex
        }

    }

    public void printCache() {
        index.printCache();
    }

    /**
     * Checks if reloading of the invertedIndex is required based on if the underlying file structure has changed
     *
     * @return boolean
     */
    public boolean isReloadRequired() {
        boolean reloadCache = false;
        try {
            if (startup) {
                if (filesList == null)
                    filesList = new ArrayList<>();
                return true;
            }
            List<String> newFilesList = new ArrayList<>();
            File file = new File(filesDir);
            List<File> files = Arrays.asList(file.listFiles());
            files.forEach(f -> newFilesList.add(f.getName()));
            List<String> temporaryOldFileList = deepCopy(filesList);
            List<String> temporaryNewFileList = deepCopy(newFilesList);
            for (String fileName : newFilesList) {
                if (temporaryOldFileList.contains(fileName)) {
                    temporaryOldFileList.remove(fileName);
                    temporaryNewFileList.remove(fileName);
                }
            }
            if (temporaryOldFileList.size() == 0 && temporaryNewFileList.size() == 0)
                return false;
            else {
                addedFiles = temporaryNewFileList;
                removedFiles = temporaryOldFileList;
                LOG.info("Added Files :{}", addedFiles);
                LOG.info("Removed Files :{}", removedFiles);
                reloadCache = true;
            }

        } catch (Exception e) {
            LOG.error("Error while checking for changes in the file structure:{}", e);
        }
        return reloadCache;
    }

    /**
     * @param filesList
     * @return List
     * Returns a deepCopy of the list being sent as param
     */

    private List<String> deepCopy(List<String> filesList) {
        List<String> tempFilesList = new ArrayList<>();
        if (filesList != null) {
            for (String fileName : filesList) {
                tempFilesList.add(new String(fileName));
            }
        }
        return tempFilesList;
    }
}