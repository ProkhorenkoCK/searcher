package com.searcher.app;

import com.searcher.entity.Page;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class IndexTask extends Thread {

    private String url;
    private int depth;
    private ConcurrentHashMap<String, Page> pages;
    private Set<String> indexedLink;
    private IndexCrawler crawler;

    public IndexTask(IndexCrawler crawler, String url, int depth, ConcurrentHashMap<String, Page> pageSet, Set<String> indexedLink) throws InterruptedException {
        this.url = url;
        this.depth = depth;
        this.pages = pageSet;
        this.crawler = crawler;
        this.indexedLink = indexedLink;
    }

    @Override
    public void run() {
        try {
            crawler.recursiveIndex(url, depth, pages, indexedLink);
        } catch (InterruptedException e) {
            System.out.println("Error: " + e);
        }
    }
}
