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
import com.example.iraapplication.contracts.ConverterContract;
import com.example.iraapplication.contracts.HistoryContract;
import com.example.iraapplication.domain.HistoryItem;
import com.example.iraapplication.domain.ValutaItem;
import com.example.iraapplication.pojo.Record;
import com.example.iraapplication.pojo.ValCurs;
import com.example.iraapplication.presenters.ConverterPresenter;
import com.example.iraapplication.presenters.HistoryPresenter;
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

public class MainActivity extends AppCompatActivity implements ConverterContract, HistoryContract {

    private Spinner from;
    private Spinner to;

    private Button convert;
    private RecyclerView recyclerView;

    private Button currDay;
    private TextView resultView;

    private ConverterPresenter presenter;
    private HistoryPresenter historyPresenter;




    private int day;
    private int month;
    private int year;



    {
        Calendar calendar = Calendar.getInstance();

        day = calendar.get(Calendar.DATE);
        month = calendar.get(Calendar.MONTH) + 1;
        year = calendar.get(Calendar.YEAR);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainActivity act = this;


        presenter = new ConverterPresenter();
        historyPresenter = new HistoryPresenter(this);


        resultView = findViewById(R.id.result);
        currDay = findViewById(R.id.currentDay);
        from = findViewById(R.id.from);
        to = findViewById(R.id.to);
        convert = findViewById(R.id.convert);
        recyclerView = findViewById(R.id.recycleView);

        initSpinners();
        setDate(getDate());
        initHistoryRV();

        convert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.convert(act, new Runnable() {
                    @Override
                    public void run() {
                        historyPresenter.addHistoryItem(act);
                        updateHistory();
                    }
                });
            }
        });

        currDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new MainActivity.DatePickerFragment(year, month - 1, day, act);
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

    }

    private void initHistoryRV() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        updateHistory();
    }

    private void initSpinners() {
        ArrayAdapter<ValutaItem> adapterFrom = new ArrayAdapter<ValutaItem>(this, android.R.layout.simple_list_item_1, getValutaItems());
        ArrayAdapter<ValutaItem> adapterTo = new ArrayAdapter<ValutaItem>(this, android.R.layout.simple_list_item_1, getValutaItems());

        from.setAdapter(adapterFrom);
        to.setAdapter(adapterTo);
    }

    private ValutaItem[] getValutaItems() {
        return new ValutaItem[]{ValutaItem.RUB, ValutaItem.EUR, ValutaItem.US, ValutaItem.IEN};
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
            activity.setDate(activity.getDate());
        }


    }


    //    Контракт истории
    @Override
    public void updateHistory() {
        recyclerView.setAdapter(historyPresenter.getHistory());
    }

    //    Контракт обменика
    @Override
    public String getIdFrom() {
        return ((ValutaItem) from.getSelectedItem()).getId();
    }

    @Override
    public String getIdTo() {
        return ((ValutaItem) to.getSelectedItem()).getId();
    }

    @Override
    public void setResult(String result) {
        resultView.setText(result);
    }

    @Override
    public void setDate(String date) {
        TextView txt = findViewById(R.id.currDate);
        txt.setText(getDate());
    }

    @Override
    public String getDate() {
        return ((day < 10) ? "0" + String.valueOf(day) : String.valueOf(day))
                + "/"
                + (((month) < 10) ? "0" + String.valueOf(month) : String.valueOf(month))
                + "/" +
                String.valueOf(year);
    }

    @Override
    public int getAmount() {
        EditText editText = findViewById(R.id.amount);
        String text = editText.getText().toString();
        return text.equals("") ? 1 : Integer.parseInt(text);
    }

    @Override
    public String getFromName() {
        return ((ValutaItem) from.getSelectedItem()).name();
    }

    @Override
    public String getToName() {
        return ((ValutaItem) to.getSelectedItem()).name();
    }

    @Override
    public String getResult() {
        return resultView.getText().toString();
    }
}