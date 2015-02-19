package com.searcher.entity;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Page {
    private String title;
    private String url;
    private Element body;
    private String allText;
    private String heightScoreText;

    public Page(Document document) {
        this.title = document.title();
        this.url = document.location();
        this.body = document.body();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Element getBody() {
        return body;
    }

    public void setBody(Element body) {
        this.body = body;
    }

    public String getAllText() {
        return allText;
    }

    public void setAllText(String allText) {
        this.allText = allText;
    }

    public String getHeightScoreText() {
        return heightScoreText;
    }

    public void setHeightScoreText(String heightScoreText) {
        this.heightScoreText = heightScoreText;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Page page = (Page) o;

        return url.equals(page.url);
    }

    @Override
    public int hashCode() {
        return url != null ? url.hashCode() : 0;
    }
}
