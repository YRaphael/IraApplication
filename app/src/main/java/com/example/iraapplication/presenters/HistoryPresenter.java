package com.example.iraapplication.presenters;

import android.content.Context;

import com.example.iraapplication.adapters.Adapter;
import com.example.iraapplication.contracts.ConverterContract;
import com.example.iraapplication.domain.HistoryItem;
import com.example.iraapplication.repos.IRepo;
import com.example.iraapplication.repos.Repo;

import java.util.Calendar;

public class HistoryPresenter {
    IRepo repo;

    public HistoryPresenter(Context ctx) {
        this.repo = new Repo(ctx);
    }

    public void addHistoryItem(ConverterContract contract) {
        Calendar calendar = Calendar.getInstance();
        repo.addHistoryItem(new HistoryItem(
                calendar.getTime().toString(),
                contract.getAmount() + " " + contract.getFromName(),
                contract.getResult() + " " + contract.getToName()
        ));

    }

    public Adapter getHistory() {
        return new Adapter(repo.getHistory());
    }
}
