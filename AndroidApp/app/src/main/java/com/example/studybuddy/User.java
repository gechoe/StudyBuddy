package com.example.studybuddy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

// User class for user object structure
public class User {
    private String username;
    private String password;
    private List<String> studyBuddies;
    private List<Task> tasks;
    private int points;

    public User(String username, String password, List<String> buddies, List<Task> tasks, int points) {
        this.username = username;
        this.password = password;
        this.studyBuddies = buddies;
        this.tasks = tasks;
        this.points = points;
    }

    // Make a public User(JSON Object) to populate the user field with the JSON Object
    // Constructor that takes a JSON object and constructs a User object
    public User(JSONObject jsonObject) throws JSONException {
        System.out.println(jsonObject);
        try {
            // Extract values from JSON object
            String username = jsonObject.getString("username");
            String password = jsonObject.getString("password");

            // Check if "buddies" is present and not null, otherwise create an empty list
            List<String> buddies;
            if (jsonObject.has("buddies") && !jsonObject.isNull("buddies")) {
                buddies = jsonArrayToList(jsonObject.getJSONArray("buddies"));
            } else {
                buddies = new ArrayList<>();
            }

            // Check if "tasks" is present and not null, otherwise create an empty list
            List<Task> tasks;
            if (jsonObject.has("tasks") && !jsonObject.isNull("tasks")) {
                tasks = jsonArrayToTaskList(jsonObject.getJSONArray("tasks"));
            } else {
                tasks = new ArrayList<>();
            }

            // Check if "points" is present and not null, otherwise assign 0
            int points;
            if (jsonObject.has("points") && !jsonObject.isNull("points")) {
                points = jsonObject.getInt("points");
            } else {
                points = 0;
            }

            // Create User object
            this.username = username;
            this.password = password;
            this.studyBuddies = buddies;
            this.tasks = tasks;
            this.points = points;
        } catch (JSONException e) {
            // Handle JSON parsing exception
            e.printStackTrace();
            throw e;  // re-throw the exception to notify caller
        }
    }

    // Helper method to convert JSONArray to List<String>
    private List<String> jsonArrayToList(JSONArray jsonArray) throws JSONException {
        try {
            List<String> list = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                list.add(jsonArray.getString(i));
            }
            return list;
        } catch (JSONException e) {
            // Handle JSON parsing exception
            e.printStackTrace();
            throw e;  // re-throw the exception to notify caller
        }
    }

    // Helper method to convert JSONArray to List<Task>
    private List<Task> jsonArrayToTaskList(JSONArray jsonArray) throws JSONException {
        try {
            List<Task> list = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject taskObj = jsonArray.getJSONObject(i);
                String taskName = taskObj.getString("task");
                boolean isComplete = taskObj.getBoolean("complete");
                Task task = new Task(taskName, isComplete);
                list.add(task);
            }
            return list;
        } catch (JSONException e) {
            // Handle JSON parsing exception
            e.printStackTrace();
            throw e;  // re-throw the exception to notify caller
        }
    }

    // Getters
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getPoints() {
        return points;
    }

    public List<String> getStudyBuddies() {
        return studyBuddies;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    // Setters
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPoints(int points_total) {
        this.points = points_total;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    // Increments points earned
    public void incrementPoints(int pointsToAdd) {
        this.points += pointsToAdd;
    }

    public void addStudyBuddy(String buddyType) {
        studyBuddies.add(buddyType);
    }

    public void removeStudyBuddy(String buddyType) {
        studyBuddies.remove(buddyType);
    }
}
