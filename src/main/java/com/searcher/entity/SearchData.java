package com.searcher.entity;

public class SearchData {
    private String url;
    private String title;
    private float score;

    public SearchData(String url, String title, float score) {
        this.url = url;
        this.title = title;
        this.score = score;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "SearchData{" +
                "url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", score=" + score +
                '}';
    }
}
