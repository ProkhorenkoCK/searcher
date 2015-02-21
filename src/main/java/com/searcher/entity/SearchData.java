package com.searcher.entity;

public class SearchData {
    private String url;
    private String title;
    private String text;
    private float score;

    public SearchData(String url, String title, float score, String text) {
        this.url = url;
        this.title = title;
        this.score = score;
        this.text = text;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
