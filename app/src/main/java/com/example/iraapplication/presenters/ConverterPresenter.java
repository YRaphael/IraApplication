package com.example.iraapplication.presenters;

import com.example.iraapplication.CBRConvertModel;
import com.example.iraapplication.contracts.ConverterContract;
import com.example.iraapplication.contracts.HistoryContract;
import com.example.iraapplication.domain.HistoryItem;
import com.example.iraapplication.domain.SimpleDate;
import com.example.iraapplication.domain.ValutaItem;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import io.reactivex.rxjava3.disposables.Disposable;

public class ConverterPresenter implements ConverterContract.Presenter {
    private static final ValutaItem[] VALUTA_ITEMS = ValutaItem.values();

    private final ConverterContract.View converterView;
    private final HistoryContract.View historyView;
    private final HistoryContract.HistoryRepository historyRepository;

    private ConverterContract.Model model = new CBRConvertModel();

    private SimpleDate date;
    private ValutaItem fromValuta;
    private ValutaItem toValuta;

    private Disposable convertDisposable;

    private final ConverterContract.DateSelectListener dateSelectListener = new ConverterContract.DateSelectListener() {
        @Override
        public void onSelect(SimpleDate date) {
            converterView.showDate(date);
            ConverterPresenter.this.date = date;
        }
    };

    public ConverterPresenter(
            ConverterContract.View converterView,
            HistoryContract.View  historyView,
            HistoryContract.HistoryRepository historyRepository) {
        this.converterView = converterView;
        this.historyView = historyView;
        this.historyRepository = historyRepository;

        Calendar calendar = Calendar.getInstance();
        date = new SimpleDate(
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.YEAR)
        );

        fromValuta = ValutaItem.US;
        toValuta = ValutaItem.RUB;
    }

    @Override
    public void onAttachView() {
        converterView.showDate(date);
        converterView.setFromValutaItems(VALUTA_ITEMS, getIndex(fromValuta));
        converterView.setToValutaItems(VALUTA_ITEMS, getIndex(toValuta));

        historyView.showHistory(historyRepository.getHistory());
    }

    @Override
    public void onSelectDateClick() {
        converterView.showDateSelector(dateSelectListener, date);
    }

    @Override
    public void onConvertClick() {
        cancelDisposable();

        ValutaItem fromLocal = fromValuta;
        ValutaItem toLocal = toValuta;
        SimpleDate dateLocal = date;

        convertDisposable = model.getExchangeRate(
                fromLocal,
                toLocal,
                dateLocal)
        .subscribe(rate -> {
            double amount;
            try {
                amount = Double.parseDouble(converterView.getEnteredAmount());
            } catch (Throwable t) {
                amount = 0d;
            }
            double result = rate * amount;

            historyRepository.addHistoryItem(createHistoryItem(fromLocal, toLocal, amount, result, dateLocal));

            converterView.showResult(String.format(Locale.getDefault(), "%.2f", result));

            historyView.showHistory(historyRepository.getHistory());
        }, t -> {
            if (t instanceof IOException) {
                converterView.showNetworkError();
            } else {
                converterView.showCommonError();
            }
        });
    }

    @Override
    public void onFromValutaSelected(ValutaItem selected) {
        ConverterPresenter.this.fromValuta = selected;
    }

    @Override
    public void onToValutaSelected(ValutaItem selected) {
        ConverterPresenter.this.toValuta = selected;
    }

    @Override
    public void onDetachView() {
        cancelDisposable();
    }

    private void cancelDisposable() {
        if (convertDisposable != null) {
            convertDisposable.dispose();
        }
        convertDisposable = null;
    }

    private int getIndex(ValutaItem item) {
        for (int i = 0; i < VALUTA_ITEMS.length; i++) {
            if (VALUTA_ITEMS[i] == item) {
                return i;
            }
        }
        return 0;
    }

    private HistoryItem createHistoryItem(
            ValutaItem from,
            ValutaItem to,
            double amount,
            double result,
            SimpleDate date) {
        String time = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                .format(date.toDate());
        return new HistoryItem(
                time,
                String.format(Locale.getDefault(), "%.2f %s", amount, from.name()),
                String.format(Locale.getDefault(), "%.2f %s", result, to.name()));
    }
}
