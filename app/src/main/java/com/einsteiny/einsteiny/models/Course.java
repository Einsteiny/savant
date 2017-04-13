package com.einsteiny.einsteiny.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Course implements Serializable {

    @SerializedName("id")
    String id;

    @SerializedName("title")
    String title;

    @SerializedName("description")
    String description;

    @SerializedName("photo_url")
    String photoUrl;

    @SerializedName("lessons")
    List<Lesson> lessons;

    public String getPhotoUrl() {
        if (photoUrl != null && !photoUrl.isEmpty())
            return photoUrl;

        return lessons.get(0).getImageUrl();
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

//    public Course(JSONObject jsonObject) throws JSONException {
//        id = jsonObject.getString("id");
//        title = jsonObject.getString("title");
//        description = jsonObject.getString("description");
//        photoUrl = jsonObject.optString("photo_url");
//        lessons = Lesson.fromJsonArray(jsonObject.getJSONArray("lessons"));
//    }

//    public static ArrayList<Course> fromJSONArray(JSONArray array) {
//        ArrayList<Course> courses = new ArrayList<>();
//
//        for (int i = 0; i < array.length(); i++) {
//            try {
//                courses.add(new Course(array.getJSONObject(i)));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//
//        return courses;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Course course = (Course) o;

        return id != null ? id.equals(course.id) : course.id == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }


}