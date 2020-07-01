package com.example.iraapplication;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iraapplication.adapters.Adapter;
import com.example.iraapplication.domain.HistoryItem;
import com.example.iraapplication.domain.ValutaItem;
import com.example.iraapplication.pojo.Record;
import com.example.iraapplication.pojo.ValCurs;
import com.example.iraapplication.repos.IRepo;
import com.example.iraapplication.repos.Repo;

import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class MainActivity extends AppCompatActivity {

    Button convert;
    IRepo repo;
    RecyclerView recyclerView;

    private int day;
    private int month;
    private int year;


    private int fromNom;
    private double fromValue;
    private int toNom;
    private double toValue;

    {
        Calendar calendar = Calendar.getInstance();

        day = calendar.get(Calendar.DATE);
        month = calendar.get(Calendar.MONTH) + 1;
        year = calendar.get(Calendar.YEAR);
    }

    TextView resultView;

    Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        repo = new Repo(this);


        RecyclerView recyclerView = findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        updateHistory(recyclerView);


        resultView = findViewById(R.id.result);

        Button currDay = findViewById(R.id.currentDay);
        setCurrentDateInTextView();
        retrofit = new Retrofit.Builder()
                .baseUrl("http://www.cbr.ru/scripts/")
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();

        MainActivity act = this;
        currDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new MainActivity.DatePickerFragment(year, month - 1, day, act);
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        Spinner from = findViewById(R.id.from);
        Spinner to = findViewById(R.id.to);

        ArrayAdapter<ValutaItem> adapterFrom = new ArrayAdapter<ValutaItem>(this, android.R.layout.simple_list_item_1, new ValutaItem[]{ValutaItem.RUB, ValutaItem.EUR, ValutaItem.US, ValutaItem.IEN});
        ArrayAdapter<ValutaItem> adapterTo = new ArrayAdapter<ValutaItem>(this, android.R.layout.simple_list_item_1, new ValutaItem[]{ValutaItem.RUB, ValutaItem.EUR, ValutaItem.US, ValutaItem.IEN});

        from.setAdapter(adapterFrom);
        to.setAdapter(adapterTo);

        convert = findViewById(R.id.convert);
        convert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                convert(((ValutaItem) from.getSelectedItem()).getId(), ((ValutaItem) to.getSelectedItem()).getId(),
                        new Runnable() {
                            @Override
                            public void run() {
                                Calendar calendar = Calendar.getInstance();

                                repo.addHistoryItem(new HistoryItem(
                                        calendar.getTime().toString(),
                                        String.valueOf(getAmount()) + ((ValutaItem) from.getSelectedItem()).name(),
                                        resultView.getText().toString() + ((ValutaItem) to.getSelectedItem()).name()
                                ));
                                updateHistory(recyclerView);
                            }
                        });
            }
        });
    }

    private void updateHistory(RecyclerView recyclerView) {
        recyclerView.setAdapter(new Adapter(repo.getHistory()));
    }

    private void convert(String idFrom, String idTo, Runnable callback) {
        CBRAPI cbrapi = retrofit.create(CBRAPI.class);
        Call<ValCurs> valutaCallFrom = cbrapi.loadValCurs(getDate(), getDate(), idFrom);
        Call<ValCurs> valutaCallTo = cbrapi.loadValCurs(getDate(), getDate(), idTo);

        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@io.reactivex.rxjava3.annotations.NonNull ObservableEmitter<String> emitter) throws Throwable {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (idFrom == Record.RUB_RECORD.getId()) {
                                fromNom = Integer.valueOf(Record.RUB_RECORD.getNominal());
                                fromValue = Double.valueOf(Record.RUB_RECORD.getValue());
                            } else {
                                Response<ValCurs> response = valutaCallFrom.execute();
                                fromNom = Integer.valueOf(response.body().getRecord().get(0).getNominal());
                                fromValue = Double.valueOf(response.body().getRecord().get(0).getValue().replace(",", "."));
                            }

                            if (idTo == Record.RUB_RECORD.getId()) {
                                toNom = Integer.valueOf(Record.RUB_RECORD.getNominal());
                                toValue = Double.valueOf(Record.RUB_RECORD.getValue());
                            } else {
                                Response<ValCurs> response2 = valutaCallTo.execute();
                                toNom = Integer.valueOf(response2.body().getRecord().get(0).getNominal());
                                toValue = Double.valueOf(response2.body().getRecord().get(0).getValue().replace(",", "."));
                            }
                            double result = (toNom * fromValue) / (fromNom * toValue);
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
                        double d = getAmount() * Double.parseDouble(s);
                        resultView.setText(String.valueOf(d));
                        callback.run();
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }

                });

    }

    public void setCurrentDate(int i, int i2, int i3) {
        day = i;
        month = i2;
        year = i3;
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        private int year;
        private int month;
        private int day;
        private MainActivity activity;

        public DatePickerFragment(int year, int month, int day, MainActivity activity) {
            this.year = year;
            this.month = month;
            this.day = day;
            this.activity = activity;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            return new DatePickerDialog(getContext(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            System.out.println(i);
            System.out.println(i1);
            System.out.println(i2);
            activity.setCurrentDate(i2, i1 + 1, i);
            activity.setCurrentDateInTextView();
        }
    }

    public String getDate() {
        return ((day < 10) ? "0" + String.valueOf(day) : String.valueOf(day))
                + "/"
                + (((month) < 10) ? "0" + String.valueOf(month) : String.valueOf(month))
                + "/" +
                String.valueOf(year);
    }

    public void setCurrentDateInTextView() {
        TextView txt = findViewById(R.id.currDate);
        txt.setText(getDate());
    }

    public int getAmount() {
        EditText editText = findViewById(R.id.amount);
        String text = editText.getText().toString();
        return text.equals("") ? 1 : Integer.parseInt(text);
    }
}