package com.example.memomia;
import android.net.Uri;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JournalEntry {

    //userId is either email of username
    private String userId;
    private String entryId;
    private int mood;
    private String title;
    private String text;
    private int year;
    private int month;
    private int day;
    private String dateTime;
    private Map<String, String> images;
    //private Map<String, String> tags;
    private String tags;

    private int imgIndex = 0;
    //private static int tagIndex = 0;
    private static final String DEFAULT_TITLE = "Things Happened Today";

    public JournalEntry() {
        this.userId = "";
        this.dateTime = "";
        this.year = 0;
        this.month = 0;
        this.day = 0;
        this.title = DEFAULT_TITLE;
        this.tags = "";
        this.images = new HashMap<>();
        this.entryId = "";
    }

    public JournalEntry(String userId, String dateTime, int year, int month, int day) {
        this.userId = userId;
        this.dateTime = dateTime;
        this.year = year;
        this.month = month;
        this.day = day;
        this.title = DEFAULT_TITLE;
        //this.tags = new HashMap<>();
        this.tags = "";
        this.images = new HashMap<>();
        this.entryId = String.format("memomia-entry-%d-%04d-%02d-%02d", this.userId.hashCode(), this.year, this.month, this.day);
    }

    //Getters
    public String getDateTime() { return this.dateTime; }
    public String getText() { return this.text; }
    public String getTitle() { return this.title; }
    public String getTags() { return this.tags; }
    public int getMood() { return this.mood; }
    public String getUserId() { return this.userId; }
    public String getEntryId() {return this.entryId; }
    public Map<String, String> getImages() { return this.images; }
    public int getDay() { return day; }
    public int getMonth() { return month; }
    public int getYear() { return year; }
    public int getImgIndex() { return this.imgIndex; }

    //Setters
    public void setText(String text) {
        this.text = text;
    }
    //If added, return true, else return false
    public boolean addTag(String tag) {
        if (this.tags.contains(tag)) {
            return false;
        }
        tags += tag;
        tags += " ";
        return true;
    }
    public boolean deleteTag(String tag) {
        if (!this.tags.contains(tag)) {
            return false;
        }
        tags = tags.replace(tag + " ", "");
        return true;
    }
    public void setMood(int mood) { this.mood = mood; }
    public void setTitle(String title) { this.title = title;}
    public void setDateTime(String dateTime) { this.dateTime = dateTime; }

    public boolean addImage(Uri uri) {
        String url = uri.toString();
        if (this.images.containsValue(url)) {
            return false;
        }
        images.put(Integer.toString(imgIndex++) + "_key", url);
        return true;
    }
    public boolean deleteImage(Uri uri) {
        String url = uri.toString();
        if (!this.images.containsValue(url)) {
            return false;
        }
        images.remove(url);
        return true;
    }

    public boolean hasImage() {
        if (this.images.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public void setMonth(int month) { this.month = month; }
    public void setYear(int year) { this.year = year; }
    public void setDay(int day) { this.day = day; }
    public void setUserId(String userId) { this.userId = userId; }
    public void setEntryId(String entryId) { this.entryId = entryId; }
    public void setTags(String tags) { this.tags = tags; }
    public void setImages(Map<String, String> imgs) { this.images = imgs; }
    public void setImgIndex(int i) { this.imgIndex = i; }
}
