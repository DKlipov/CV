package com.test.spring.model;

public class SearchParams {

    private boolean isFullSearch=false;
    public SearchParams(){
        quickSearch="";
        category="";
        text="";
        title="";
    }

    public void setFullSearch(boolean fullSearch) {
        isFullSearch = fullSearch;
    }

    public boolean isFullSearch() {
        return isFullSearch;
    }

    public static SearchParams fullSearch(){
        SearchParams sp=new SearchParams();
        sp.isFullSearch=true;
        return sp;
    }

    private String quickSearch, category, title, text;

    public String getQuickSearch() {
        return quickSearch;
    }

    public void setQuickSearch(String quickSearch) {
        this.quickSearch = quickSearch;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
