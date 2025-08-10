package com.example.todolist;

import java.io.Serializable;

public class Task implements Serializable {
    private String title;
    private String dueDate;
    private String time;         // ✅ Added
    private boolean completed;

    // Constructor
    public Task(String title, String dueDate) {
        this.title = title;
        this.dueDate = dueDate;
        this.time = "";         // ✅ Initialize time as empty
        this.completed = false;
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getTime() {                  // ✅ Getter
        return time;
    }

    public boolean isCompleted() {
        return completed;
    }

    // Setters
    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public void setTime(String time) {         // ✅ Setter
        this.time = time;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
