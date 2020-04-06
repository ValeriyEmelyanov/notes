package com.example.notes.filtering;

/**
 * Перечисление возможных вариантов фильтрации заметок по признаку выполнения (done).
 */
public enum DoneFilterOption {
    ALL("All"),
    PLAN("Plan"),
    DONE("Done");

    /**
     * Описание значения.
     */
    private String description;

    DoneFilterOption(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
