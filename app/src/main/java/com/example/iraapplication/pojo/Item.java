package com.example.iraapplication.pojo;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "Item", strict = false)
public class Item {

//    public static final String RUB_ITEM_PARENT_CODE = "fixIssueConnectedWithAbsentOfRubInCBRF";
//    public static final Item RUB_ITEM = new Item() {
//        {
//            setParentCode(RUB_ITEM_PARENT_CODE);
//            setName("Российский рубль");
//            setEngName("");
//        }
//    };

    @Attribute(name = "ID")
    private String id;

    @Element(name = "Name")
    private String name;

    @Element(name = "EngName")
    private String engName;

    @Element(name = "Nominal")
    private String nominal;

    @Element(name = "ParentCode")
    private String parentCode;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEngName() {
        return engName;
    }

    public void setEngName(String engName) {
        this.engName = engName;
    }

    public String getNominal() {
        return nominal;
    }

    public void setNominal(String nominal) {
        nominal = nominal;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    @Override
    public String toString() {
        return name + (!(engName.equals("")) ? "(" + (engName) + ")" : "");
    }
}
