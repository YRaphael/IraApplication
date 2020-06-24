package com.example.iraapplication.pojo;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;


@Root(name = "Valuta", strict = false)
public class Valuta {
    @Attribute(name = "name")
    private String name;

    @ElementList(name = "Item", inline = true)
    private List<Item> item;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Item> getItem() {
        return item;
    }

    public void setItem(List<Item> item) {
        this.item = item;
    }

    @Override
    public String toString() {
        return "Valuta{" +
                "name='" + name + '\'' +
                ", item=" + item +
                '}';
    }
}
