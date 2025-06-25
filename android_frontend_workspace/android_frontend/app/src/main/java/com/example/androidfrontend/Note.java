package com.example.androidfrontend;

import java.io.Serializable;
import java.util.UUID;

// PUBLIC_INTERFACE
public class Note implements Serializable {
    private String id;
    private String title;
    private String content;
    private long timestamp;

    // PUBLIC_INTERFACE
    public Note(String title, String content) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.content = content;
        this.timestamp = System.currentTimeMillis();
    }

    // PUBLIC_INTERFACE
    public Note(String id, String title, String content, long timestamp) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.timestamp = timestamp;
    }

    // PUBLIC_INTERFACE
    public String getId() { return id; }
    // PUBLIC_INTERFACE
    public String getTitle() { return title; }
    // PUBLIC_INTERFACE
    public String getContent() { return content; }
    // PUBLIC_INTERFACE
    public long getTimestamp() { return timestamp; }

    // PUBLIC_INTERFACE
    public void setTitle(String title) { this.title = title; }
    // PUBLIC_INTERFACE
    public void setContent(String content) { this.content = content; }
    // PUBLIC_INTERFACE
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
