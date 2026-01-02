package com.example.ragdemo.model;

/**
 * Simple Document model for RAG demo
 */
public class Document {

    private String id;
    private String text;

    public Document() {}

    public Document(String id, String text) {
        this.id = id;
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Document{id='" + id + "', text='" + text + "'}";
    }
}
