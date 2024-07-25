package com.example.b07demosummer2024;

import java.io.Serializable;

public class Item implements Serializable {

    private String id;
    private String name;
    private String timePeriod;
    private String category;
    private String description;
    private Media media;

    public Item() {}

    public Item(String id, String name, String timePeriod, String category, String description, Media media) {
        this.id = id;
        this.name = name;
        this.timePeriod = timePeriod;
        this.category = category;
        this.description = description;
        this.media = media;
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getTimePeriod() { return timePeriod; }
    public void setTimePeriod(String timePeriod) { this.timePeriod = timePeriod; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Media getMedia() { return media; }
    public void setMedia(Media media) { this.media = media; }

    @Override
    public String toString(){
        return getId() + " " + getName();
    }
}