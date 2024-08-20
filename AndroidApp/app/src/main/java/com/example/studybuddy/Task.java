package com.example.studybuddy;

// Task Class
// Depicts task object structure
public class Task {
    private String name;
    private boolean isCompleted;

    public Task(String name) {
        this.name = name;
        this.isCompleted = false;
    }

    public Task(String name, boolean completed) {
        this.name = name;
        this.isCompleted = completed;
    }

    public String getName() {
        return name;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}

