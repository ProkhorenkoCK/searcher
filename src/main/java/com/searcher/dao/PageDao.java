package com.searcher.dao;

import com.searcher.entity.Page;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ConcurrentHashMap;

@Repository
public class PageDao {

    private ConcurrentHashMap<String ,Page> pages = new ConcurrentHashMap<>(); // temp storage for the indexed pages

    public ConcurrentHashMap<String ,Page> getPages() {
        return pages;
    }

    public void setPages(ConcurrentHashMap<String ,Page> pages) {
        this.pages = pages;
    }
}
