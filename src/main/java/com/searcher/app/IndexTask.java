package com.searcher.app;

import com.searcher.component.Indexer;

import java.util.Set;

public class IndexTask implements Runnable {

    private String url;
    private int depth;
    private Set<String> indexedLink;
    private Indexer indexer;

    public IndexTask(Indexer indexer, String url, int depth, Set<String> indexedLink) {
        this.url = url;
        this.depth = depth;
        this.indexer = indexer;
        this.indexedLink = indexedLink;
    }

    @Override
    public void run() {
        indexer.recursiveIndex(url, depth, indexedLink);
    }
}
