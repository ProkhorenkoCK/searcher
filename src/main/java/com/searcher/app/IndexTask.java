package com.searcher.app;

import com.searcher.entity.Page;

import java.util.Set;

public class IndexTask implements Runnable {

    private String url;
    private int depth;
    private Set<Page> pages;
    private Set<String> indexedLink;
    private Indexer indexer;

    public IndexTask(Indexer indexer, String url, int depth, Set<Page> pageSet, Set<String> indexedLink) {
        this.url = url;
        this.depth = depth;
        this.pages = pageSet;
        this.indexer = indexer;
        this.indexedLink = indexedLink;
    }

    @Override
    public void run() {
        indexer.recursiveIndex(url, depth, pages, indexedLink);
    }
}
