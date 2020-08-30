package com.jarifjak.digitalsecuritysolution.model;

public class DialogExtra {

    private String title;
    private String description;

    public DialogExtra() {
    }

    public DialogExtra(String title, String description) {

        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
