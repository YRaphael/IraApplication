package com.example.iraapplication.contracts;

import com.example.iraapplication.domain.SimpleDate;
import com.example.iraapplication.domain.ValutaItem;

import io.reactivex.rxjava3.core.Observable;

public interface ConverterContract {

    interface View {
        void setFromValutaItems(ValutaItem[] items, int selected);
        void setToValutaItems(ValutaItem[] items, int selected);
        void showDateSelector(DateSelectListener listener, SimpleDate selected);

        void showDate(SimpleDate date);

        void showResult(String result);

        String getEnteredAmount();

        void showNetworkError();
        void showCommonError();
    }

    interface Presenter {
        void onAttachView();
        void onSelectDateClick();
        void onConvertClick();
        void onFromValutaSelected(ValutaItem selected);
        void onToValutaSelected(ValutaItem selected);
        void onDetachView();
    }

    interface Model {
        Observable<Double> getExchangeRate(ValutaItem valutaFrom, ValutaItem valutaTo, SimpleDate date);
    }

    interface DateSelectListener {
        void onSelect(SimpleDate date);
    }

}
