package com.example.iraapplication.repos;

import com.example.iraapplication.domain.HistoryItem;

import java.util.List;

public interface IRepo {
    List<HistoryItem> getHistory();
    void addHistoryItem(HistoryItem item);
}
