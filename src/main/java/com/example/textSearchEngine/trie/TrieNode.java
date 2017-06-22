package com.example.textSearchEngine.trie;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by suryansh on 21/6/17.
 */
class TrieNode {
    char content;
    boolean isEnd;
    int count;
    LinkedList<TrieNode> childList;
    Map<String, List<Long>> fileNameToLineNumberMap;

    /* Constructor */
    public TrieNode(char c) {
        childList = new LinkedList<TrieNode>();
        isEnd = false;
        content = c;
        count = 0;
//        fileNameToLineNumberMap = new HashMap<>();
    }

    public TrieNode subNode(char c) {
        if (childList != null)
            for (TrieNode eachChild : childList)
                if (eachChild.content == c)
                    return eachChild;
        return null;
    }

    public char getContent() {
        return content;
    }

    public void setContent(char content) {
        this.content = content;
    }

    public boolean isEnd() {
        return isEnd;
    }

    public void setEnd(boolean end) {
        isEnd = end;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public LinkedList<TrieNode> getChildList() {
        return childList;
    }

    public void setChildList(LinkedList<TrieNode> childList) {
        this.childList = childList;
    }

    public Map<String, List<Long>> getFileNameToLineNumberMap() {
        return fileNameToLineNumberMap;
    }

    public void setFileNameToLineNumberMap(Map<String, List<Long>> fileNameToLineNumberMap) {
        this.fileNameToLineNumberMap = fileNameToLineNumberMap;
    }
}