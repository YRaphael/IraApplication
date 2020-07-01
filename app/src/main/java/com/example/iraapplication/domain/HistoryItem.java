package com.example.iraapplication.domain;

import java.util.UUID;

public class HistoryItem {

    private String id;
    private String time;
    private String from;
    private String to;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public HistoryItem( String time, String from, String to) {
        this.id = UUID.randomUUID().toString();
        this.time = time;
        this.from = from;
        this.to = to;
    }
    public HistoryItem(String id, String time, String from, String to) {
        this.id = id;
        this.time = time;
        this.from = from;
        this.to = to;
    }
}
