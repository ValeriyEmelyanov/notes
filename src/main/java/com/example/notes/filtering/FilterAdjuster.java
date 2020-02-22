package com.example.notes.filtering;

public class FilterAdjuster {
    private String searchText;
    private DoneFilterOption done;

    public FilterAdjuster() {
        this.searchText = "";
        this.done = DoneFilterOption.ALL;
    }

    public FilterAdjuster(String searchText, DoneFilterOption done) {
        this.searchText = searchText;
        this.done = done;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public DoneFilterOption getDone() {
        return done;
    }

    public void setDone(DoneFilterOption done) {
        this.done = done;
    }

    public boolean isAll() {
        return (searchText == null || searchText.isEmpty()) &&
                (done == null || done == DoneFilterOption.ALL);
    }

}
