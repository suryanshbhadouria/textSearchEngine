package com.example.textSearchEngine.cache;

import com.example.textSearchEngine.index.InvertedIndex;
import com.example.textSearchEngine.trie.Trie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by suryansh on 21/6/17.
 */
@Component
public class FilesCache {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Value("${files.dir}")
    private String filesDir;

    private List<String> filesList;

    private List<String> addedFiles;

    private List<String> removedFiles;

    private boolean startup = true;

    @Autowired
    private Trie trie;

    @Autowired
    private InvertedIndex index;

    public void loadInvertedIndex(List<String> filesToBeAdded, List<String> filesToBeRemoved) {
        if (!startup) {
            LOG.info("Loading the invertedIndex with data post startup with  files to be added {} and files to be removed:{}", filesToBeAdded, filesToBeRemoved);
            File filesDir = new File(this.filesDir);
            List<File> files = Arrays.asList(filesDir.listFiles());
            for (File file : files) {
                if (filesToBeAdded.contains(file.getName())) {
                    readFileAndLoadContent(file);
                }
            }
        } else {
            //load data on startup
            LOG.info("Loading the trie with data on startup");
            File filesDir = new File(this.filesDir);
            List<File> files = Arrays.asList(filesDir.listFiles());
            for (File file : files) {
                try {
                    readFileAndLoadContent(file);
                } catch (Exception e) {
                    LOG.info("Error while reading file with name:{}", file.getName());
                }
            }



     /*   if (!startup) {
            LOG.info("Loading the trie with data post startup with  files to be added {} and files to be removed:{}", filesToBeAdded, filesToBeRemoved);
            File filesDir = new File(this.filesDir);
            List<File> files = Arrays.asList(filesDir.listFiles());
            for (File file : files) {
                try {
                    if (filesToBeAdded.contains(file.getName())) {
                        BufferedReader br = new BufferedReader(new FileReader(file.getAbsolutePath()))
                        String line;
                        Long i = 1l;
                        while ((line = br.readLine()) != null) {
                            String words[] = line.split(" ");
                            for (String word : words) {
                                trie.insert(word, file.getName(), i++);
                            }
                        }
                    }
                } catch (Exception e) {
                    LOG.info("Error while reading file with name:{}", file.getName());
                }
            }
        } else {
            //load data on startup
            LOG.info("Loading the trie with data on startup");
            File filesDir = new File(this.filesDir);
            List<File> files = Arrays.asList(filesDir.listFiles());
            for (File file : files) {
                try {
                    BufferedReader br = new BufferedReader(new FileReader(file.getAbsolutePath()))
                    String line;
                    Long i = 1l;
                    while ((line = br.readLine()) != null) {
                        String words[] = line.split(" ");
                        for (String word : words) {
                            trie.insert(word, file.getName(), i++);
                        }
                    }
                } catch (Exception e) {
                    LOG.info("Error while reading file with name:{}", file.getName());
                }
            }*/
            /*files.stream().map(file -> {
                FileInputStream fis = null;
                String data = new String();
                try {
                    fis = new FileInputStream(file);
                    LOG.info("Total file size to read (in bytes) : {}", fis.available());
                    int content;
                    while ((content = fis.read()) != -1) {
                        // convert to char and display it
                        data += (char) content;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (fis != null)
                            fis.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                return data;
            }).forEach(data -> {
                List<String> words = Arrays.asList(data.split(" "));
                words.forEach(word -> trie.insert(word));

            });*/


        }

    }

    public void readFileAndLoadContent(File file) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(file.getAbsolutePath()));
            String line;
            Long i = 1l;
            while ((line = br.readLine()) != null) {
                String words[] = line.split(" ");
                for (String word : words) {
                    index.addToken(word, file.getName(), i);
                }
                i++;
            }
        } catch (Exception e) {
            LOG.info("Error when reading file:{}", file.getName());
        }
    }

    public void reloadCache() {
        if (isReloadRequired() || startup) {
            if (startup) {
                LOG.info("Loading the trie for the first time");
                loadInvertedIndex(null, null);
            } else {
                //parse all the removed files and remove from trie
                loadInvertedIndex(addedFiles, removedFiles);
                //parse all the added files and add to trie
                startup = false;
            }
        } else {
            LOG.info("Not reloading cache as file structure has not changed");
        }
    }

    public boolean isReloadRequired() {
        boolean reloadCache = false;
        try {
            List<String> newFilesList = new ArrayList<>();
            File file = new File(filesDir);
            List<File> files = Arrays.asList(file.listFiles());
            files.forEach(f -> newFilesList.add(f.getName()));
            if (newFilesList.size() != filesList.size())
                return true;
            else {
                List<String> temporaryOldFileList = deepCopy(filesList);
                List<String> temporaryNewFileList = deepCopy(filesList);
                for (String fileName : newFilesList) {
                    if (temporaryOldFileList.contains(fileName)) {
                        temporaryOldFileList.remove(fileName);
                        temporaryNewFileList.remove(fileName);
                    }
                }
                if (temporaryOldFileList.size() == 0)
                    return false;
                else {
                    addedFiles = temporaryNewFileList;
                    removedFiles = temporaryOldFileList;
                    LOG.info("Added Files :{}", addedFiles);
                    LOG.info("Removed Files :{}", removedFiles);
                    reloadCache = true;
                }
            }
        } catch (Exception e) {
            LOG.error("Error while checking for changes in the file structure:{}", e);
        }
        return reloadCache;
    }

    private List<String> deepCopy(List<String> filesList) {
        List<String> tempFilesList = new ArrayList<>();
        for (String fileName : filesList) {
            tempFilesList.add(new String(fileName));
        }
        return tempFilesList;
    }
}
