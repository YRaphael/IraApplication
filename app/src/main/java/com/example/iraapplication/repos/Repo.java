package com.example.iraapplication.repos;

import android.content.Context;

import com.example.iraapplication.domain.HistoryItem;
import com.example.iraapplication.helpers.DBHelper;

import java.util.List;

public class Repo implements IRepo {

    private DBHelper dbHelper;

    public Repo(Context ctx) {
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
