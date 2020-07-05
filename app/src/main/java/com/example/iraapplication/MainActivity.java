package com.example.iraapplication;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iraapplication.adapters.HistoryAdapter;
import com.example.iraapplication.contracts.ConverterContract;
import com.example.iraapplication.contracts.HistoryContract;
import com.example.iraapplication.domain.HistoryItem;
import com.example.iraapplication.domain.SimpleDate;
import com.example.iraapplication.domain.ValutaItem;
import com.example.iraapplication.presenters.ConverterPresenter;
import com.example.iraapplication.repos.SqliteHistoryRepository;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import io.reactivex.rxjava3.plugins.RxJavaPlugins;

public class MainActivity extends AppCompatActivity
        implements ConverterContract.View, HistoryContract.View {

    private final ConverterContract.Presenter convertPresenter = new ConverterPresenter(
            this, this, new SqliteHistoryRepository(this));

    private TextView tvDate;
    private EditText etAmount;
    private TextView tvResult;

    private Spinner fromValutaSelector;
    private Spinner toValutaSelector;

    private HistoryAdapter historyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        RxJavaPlugins.setErrorHandler(throwable -> {});

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvDate = findViewById(R.id.tv_date);
        etAmount = findViewById(R.id.amount);
        tvResult = findViewById(R.id.result);

        Button selectDateBtn = findViewById(R.id.btn_select_date);
        selectDateBtn.setOnClickListener(view -> convertPresenter.onSelectDateClick());

        fromValutaSelector = findViewById(R.id.from);
        fromValutaSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ValutaItem valutaFrom = (ValutaItem) fromValutaSelector.getAdapter().getItem(i);
                convertPresenter.onFromValutaSelected(valutaFrom);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        toValutaSelector = findViewById(R.id.to);
        toValutaSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ValutaItem valutaTo = (ValutaItem) toValutaSelector.getAdapter().getItem(i);
                convertPresenter.onToValutaSelected(valutaTo);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        Button convertBtn = findViewById(R.id.convert);
        convertBtn.setOnClickListener(view -> convertPresenter.onConvertClick());

        RecyclerView recyclerView = findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        historyAdapter = new HistoryAdapter();
        recyclerView.setAdapter(historyAdapter);

        convertPresenter.onAttachView();
    }

    @Override
    protected void onDestroy() {
        convertPresenter.onDetachView();
        super.onDestroy();
    }

    // >>> Контракт конвертации

    @Override
    public void setFromValutaItems(ValutaItem[] items, int selected) {
        ArrayAdapter<ValutaItem> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                items);
        fromValutaSelector.setAdapter(adapter);
        fromValutaSelector.setSelection(selected);
    }

    @Override
    public void setToValutaItems(ValutaItem[] items, int selected) {
        ArrayAdapter<ValutaItem> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                items);
        toValutaSelector.setAdapter(adapter);
        toValutaSelector.setSelection(selected);
    }

    @Override
    public void showDateSelector(ConverterContract.DateSelectListener listener, SimpleDate selected) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (datePicker, y, m, d) -> listener.onSelect(new SimpleDate(d, m + 1, y)),
                selected.year,
                selected.month - 1,
                selected.day);

        datePickerDialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());

        datePickerDialog.show();
    }

    @Override
    public void showDate(SimpleDate date) {
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        tvDate.setText(format.format(date.toDate()));
    }

    @Override
    public void showResult(String result) {
        tvResult.setText(result);
    }

    @Override
    public String getEnteredAmount() {
        return etAmount.getText().toString();
    }

    @Override
    public void showNetworkError() {
        Toast.makeText(this, R.string.error_network, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showCommonError() {
        Toast.makeText(this, R.string.error_common, Toast.LENGTH_SHORT).show();
    }

    // <<< Контракт конвертации

    // >>> Контракт истории

    @Override
    public void showHistory(List<HistoryItem> history) {
        historyAdapter.setItems(history);
    }

    // <<< Контракт истории
}