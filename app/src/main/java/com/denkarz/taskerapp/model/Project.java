package com.denkarz.taskerapp.model;

import java.util.ArrayList;

public class Project {
    public int id;
    public String title;
    public ArrayList<Todo> todos;

    public Project() {
        this.id=0;
        this.title="";
        todos = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "title: " + title;
    }
}
