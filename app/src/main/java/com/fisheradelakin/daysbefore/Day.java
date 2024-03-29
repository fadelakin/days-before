package com.fisheradelakin.daysbefore;

import java.io.Serializable;
import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.Required;

/**
 * Created by temidayo on 3/29/16.
 */
public class Day extends RealmObject implements Serializable {

    @Required
    private Date date;

    @Required
    private String occasion;

    @Required
    private String timestamp;

    private int color;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getOccasion() {
        return occasion;
    }

    public void setOccasion(String occasion) {
        this.occasion = occasion;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
