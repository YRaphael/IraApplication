package com.example.iraapplication;


import com.example.iraapplication.contracts.ConverterContract;
import com.example.iraapplication.domain.SimpleDate;
import com.example.iraapplication.domain.ValutaItem;
import com.example.iraapplication.pojo.Record;
import com.example.iraapplication.pojo.ValCurs;
import com.example.iraapplication.utils.ConverterUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class CBRConvertModel implements ConverterContract.Model {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

    private final CBRAPI cbrapi;

    public CBRConvertModel() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.cbr.ru/scripts/")
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();
        cbrapi = retrofit.create(CBRAPI.class);
    }

    @Override
    public Observable<Double> getExchangeRate(ValutaItem valutaFrom, ValutaItem valutaTo, SimpleDate date) {
        String dateAsString = DATE_FORMAT.format(date.toDate());

        Call<ValCurs> valutaCallFrom = cbrapi.loadValCurs(dateAsString, dateAsString, valutaFrom.getId());
        Call<ValCurs> valutaCallTo = cbrapi.loadValCurs(dateAsString, dateAsString, valutaTo.getId());

        return Observable.fromCallable(() -> {
            Record from = getRecord(valutaFrom, valutaCallFrom);
            Record to = getRecord(valutaTo, valutaCallTo);
            return ConverterUtils.pureConvert(from, to);
        })
                         .subscribeOn(Schedulers.io())
                         .observeOn(AndroidSchedulers.mainThread());
    }

    private Record getRecord(ValutaItem valuta, Call<ValCurs> valCursCall) throws IOException {
        if (valuta == ValutaItem.RUB) {
            return Record.RUB_RECORD;
        }

        return valCursCall.execute()
                .body()
                .getRecord()
                .get(0);
    }
}
