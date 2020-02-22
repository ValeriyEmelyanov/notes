package com.example.notes.filtering;

public enum DoneFilterOption {
    ALL("All"),
    PLAN("Plan"),
    DONE("Done");

    private String description;

    DoneFilterOption(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
