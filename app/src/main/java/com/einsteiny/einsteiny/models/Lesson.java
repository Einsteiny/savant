package com.einsteiny.einsteiny.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class Lesson implements Serializable {
    @SerializedName("description")
    String description;

    @SerializedName("title")
    String title;

    @SerializedName("image_url")
    String imageUrl;

    @SerializedName("video_url")
    String videoUrl;


    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getTitle() {
        return title;
    }

}
