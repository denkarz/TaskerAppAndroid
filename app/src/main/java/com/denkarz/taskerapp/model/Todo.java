package com.denkarz.taskerapp.model;

public class Todo {
    public int id;
    public String text;
    public boolean is_completed;

    public Todo() {
        this.id = 0;
        this.text = "";
        this.is_completed = false;
    }
}
