package com.denkarz.taskerapp;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.denkarz.taskerapp.model.Project;
import com.denkarz.taskerapp.model.Todo;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.future.ResponseFuture;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class MainActivity extends AppCompatActivity {
    public ListView sectionedListView;
    public ArrayList<Project> projects;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        projects = new ArrayList<>();
        Ion.with(this)
                .load(getString(R.string.index_url))
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        if (result != null) {
                            for (final JsonElement projectJsonElement : result) {
                                projects.add(new Gson().fromJson(projectJsonElement, Project.class));
                                sectionedListView = findViewById(R.id.listView);
                                SectionedListAdapter adapter = new SectionedListAdapter(MainActivity.this);
                                for (int i=0; i<projects.size();i++) {
                                    adapter.addHeaderItem(projects.get(i).title,projects.get(i).id);
                                    ArrayList<Todo> tasks = projects.get(i).todos;
                                    for (int j = 0;j<tasks.size();j++){
                                     adapter.addItem(tasks.get(j));
                                    }
                                }
                                sectionedListView.setAdapter(adapter);
                            }
                        }
                    }
                });
        setContentView(R.layout.activity_main);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/OpenSans-Light.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickMethod();
            }
        });
    }


    private void clickMethod() {
        String[] category = new String[projects.size()];
        String[] id = new String[projects.size()];
        for (int i = 0; i < projects.size(); i++) {
            category[i] = projects.get(i).title;
            id[i]= String.valueOf(projects.get(i).id);
        }
        Bundle b = new Bundle();
        b.putStringArray("CAT", category);
        b.putStringArray("IDS",id);
        Intent i = new Intent(this, AddTaskActivity.class);
        i.putExtras(b);
        startActivityForResult(i, 1);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            Intent refresh = new Intent(this, MainActivity.class);
            startActivity(refresh);
            this.finish();
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
