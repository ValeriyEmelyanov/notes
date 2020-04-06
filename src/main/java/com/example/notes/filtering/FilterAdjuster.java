package com.example.notes.filtering;

/**
 * Настройки фильтра заметок.
 */
public class FilterAdjuster {
    /**
     * Значение для контекстного поиска по тексту заметки.
     */
    private String searchText;
    /**
     * Значение для поиска по признаку выполнения заметки (задания).
     */
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

    /**
     * Определяет выводим все записи (без фильтров) или нет.
     *
     * @return истина, если нет никаких фильтров
     */
    public boolean isAll() {
        return (searchText == null || searchText.isEmpty()) &&
                (done == null || done == DoneFilterOption.ALL);
    }

}
