package com.example.iraapplication.presenters;

import com.example.iraapplication.CBRAPI;
import com.example.iraapplication.contracts.ConverterContract;
import com.example.iraapplication.pojo.Record;
import com.example.iraapplication.pojo.ValCurs;

import java.io.IOException;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class ConverterPresenter {
    private Retrofit retrofit;

    public ConverterPresenter() {
        this.retrofit = new Retrofit.Builder()
                .baseUrl("http://www.cbr.ru/scripts/")
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();
    }

    public void convert(ConverterContract converterContract, Runnable callback) {
        CBRAPI cbrapi = retrofit.create(CBRAPI.class);
        String date = converterContract.getDate();

        String idFrom = converterContract.getIdFrom();
        Call<ValCurs> valutaCallFrom = cbrapi.loadValCurs(date, date, idFrom);
        String idTo = converterContract.getIdTo();
        Call<ValCurs> valutaCallTo = cbrapi.loadValCurs(date, date, idTo);

        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@io.reactivex.rxjava3.annotations.NonNull ObservableEmitter<String> emitter) throws Throwable {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Record from;
                            Record to;
                            if (idFrom.equals(Record.RUB_RECORD.getId())) {
                                from = Record.RUB_RECORD;
                            } else {
                                Response<ValCurs> response = valutaCallFrom.execute();
                                from = response.body().getRecord().get(0);
                            }

                            if (idTo.equals(Record.RUB_RECORD.getId())) {
                                to = Record.RUB_RECORD;
                            } else {
                                Response<ValCurs> response = valutaCallTo.execute();
                                to = response.body().getRecord().get(0);
                            }
                            double result = pureConvert(from, to);
                            emitter.onNext(String.valueOf(result));

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {

                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        double d = converterContract.getAmount() * Double.parseDouble(s);
                        converterContract.setResult(String.valueOf(d));
                        callback.run();
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }

                });

        converterContract.setResult("");
    }

    public static double pureConvert(Record from, Record to) {

        int fromNom = Integer.parseInt(from.getNominal());
        double fromValue = Double.parseDouble(from.getValue().replace(",", "."));

        int toNom = Integer.parseInt(to.getNominal());
        double toValue = Double.parseDouble(to.getValue().replace(",", "."));

        return (toNom * fromValue) / (fromNom * toValue);
    }

}
