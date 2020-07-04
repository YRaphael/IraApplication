package com.example.iraapplication.contracts;

import com.example.iraapplication.domain.HistoryItem;

import java.util.List;

public interface HistoryContract {
    interface HistoryRepository {
        List<HistoryItem> getHistory();
        void addHistoryItem(HistoryItem item);
    }

    interface View {
        void showHistory(List<HistoryItem> history);
    }
}
