package com.fisheradelakin.daysbefore;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by temidayo on 3/29/16.
 */
public class Day extends RealmObject {

    private Date date;
    private String occassion;
    private String color;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getOccassion() {
        return occassion;
    }

    public void setOccassion(String occassion) {
        this.occassion = occassion;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
