package com.example.textSearchEngine.trie;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by suryansh on 21/6/17.
 */
@Component
public class Trie {
    private TrieNode root;

    /* Constructor */
    public Trie() {
        root = new TrieNode(' ');
    }

    /* Function to insert word */
    public void insert(String word, String fileName, Long lineNumber) {
        if (search(word) == true)
            return;
        TrieNode current = root;
        for (char ch : word.toCharArray()) {
            TrieNode child = current.subNode(ch);
            if (child != null)
                current = child;
            else {
                current.getChildList().add(new TrieNode(ch));
                current = current.subNode(ch);
            }
            current.setCount(current.getCount() + 1);
        }
        current.setEnd(true);
        Map<String, List<Long>> fileNameToLineNumberMap = current.getFileNameToLineNumberMap();
        if (fileNameToLineNumberMap.isEmpty()) {
            fileNameToLineNumberMap = new HashMap<>();
            fileNameToLineNumberMap.put(fileName, new ArrayList<>());
        }
        List<Long> pages = fileNameToLineNumberMap.get(fileName);
        pages.add(lineNumber);
    }

    /* Function to search for word */
    public boolean search(String word) {
        TrieNode current = root;
        for (char ch : word.toCharArray()) {
            if (current.subNode(ch) == null)
                return false;
            else
                current = current.subNode(ch);
        }
        if (current.isEnd() == true)
            return true;
        return false;
    }

    /* Function to remove a word */
    public void remove(String word, String fileName, String lineNumber) {
        if (search(word) == false) {
            System.out.println(word + " does not exist in trie\n");
            return;
        }
        TrieNode current = root;
        for (char ch : word.toCharArray()) {
            TrieNode child = current.subNode(ch);
            if (child.getCount() == 1) {
                current.getChildList().remove(child);
                return;
            } else {
                child.setCount(child.getCount() - 1);
                current = child;
            }
        }
        current.setEnd(false);
        Map<String, List<Long>> fileNameToLineNumberMap = current.getFileNameToLineNumberMap();
        if (!fileNameToLineNumberMap.isEmpty()) {
            List<Long> pages = fileNameToLineNumberMap.get(fileName);
            pages.remove(lineNumber);
        }
    }
}