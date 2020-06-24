package com.example.iraapplication.pojo;


import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;


@Root(name = "Record", strict = false)
public class Record {

    public static final Record RUB_RECORD = new Record() {
        {
            setId("1");
            setValue("1");
            setNominal("1");
        }
    };

    @Attribute(name = "Date")
    private String date;

    @Attribute(name = "Id")
    private String id;

    @Element(name = "Nominal")
    private String nominal;

    @Element(name = "Value")
    private String value;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNominal() {
        return nominal;
    }

    public void setNominal(String nominal) {
        this.nominal = nominal;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Record{" +
                "date='" + date + '\'' +
                ", id='" + id + '\'' +
                ", nominal='" + nominal + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
