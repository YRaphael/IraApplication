package com.example.iraapplication.repos;

import android.content.Context;

import com.example.iraapplication.contracts.HistoryContract;
import com.example.iraapplication.domain.HistoryItem;
import com.example.iraapplication.helpers.DBHelper;

import java.util.List;

public class SqliteHistoryRepository implements HistoryContract.HistoryRepository {

    private DBHelper dbHelper;

    public SqliteHistoryRepository(Context ctx) {
        dbHelper = new DBHelper(ctx);
    }


    @Override
    public List<HistoryItem> getHistory() {
        return dbHelper.getItems();
    }

    @Override
    public void addHistoryItem(HistoryItem item) {
        dbHelper.addItem(item);
    }
}
