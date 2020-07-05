package com.example.iraapplication.utils;

import com.example.iraapplication.pojo.Record;

public class ConverterUtils {
    public static double pureConvert(Record from, Record to) {
        int fromNom = Integer.parseInt(from.getNominal());
        double fromValue = Double.parseDouble(from.getValue().replace(",", "."));

        int toNom = Integer.parseInt(to.getNominal());
        double toValue = Double.parseDouble(to.getValue().replace(",", "."));

        return (toNom * fromValue) / (fromNom * toValue);
    }
}
