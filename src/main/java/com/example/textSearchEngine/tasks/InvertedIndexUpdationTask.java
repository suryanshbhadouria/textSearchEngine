package com.example.textSearchEngine.tasks;

/**
 * Created by suryansh on 22/6/17.
 */

import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.textSearchEngine.cache.FilesCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class InvertedIndexUpdationTask {

    private static final Logger LOG = LoggerFactory.getLogger(InvertedIndexUpdationTask.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Autowired
    FilesCache filesCache;

    @Scheduled(fixedDelay = 10000)
    public void reportCurrentTime() {
        Long startTime = System.currentTimeMillis();
        if (filesCache.isReloadRequired()) {
            filesCache.reloadCache();
            LOG.info("Time taken to reloadCache:{}", System.currentTimeMillis() - startTime);
        } else {
            LOG.info("Not reloading cache as file structure has not changed");
        }
    }
}