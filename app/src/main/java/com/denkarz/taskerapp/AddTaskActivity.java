package com.denkarz.taskerapp;

import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

public class AddTaskActivity extends AppCompatActivity {
    String project_id="-1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        String[] array=getIntent().getExtras().getStringArray("CAT");
        final String[] _id =getIntent().getExtras().getStringArray("IDS");
        ListView categoryListView = findViewById(R.id.category_lv);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.category_item);
        adapter.addAll(array);
        categoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setSelected(true);
                project_id = _id[position];

            }
        });
        categoryListView.setAdapter(adapter);
        Toolbar toolbar2 = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.done_btn_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.done:
                EditText textField = (EditText) findViewById(R.id.task_name);
                String text = textField.getText().toString();
                if (!text.equals("") && !project_id.equals("-1")) {
                    JsonObject obj = new JsonObject();
                    obj.addProperty("project_id", project_id);
                    obj.addProperty("text", text);
                    JsonObject params = new JsonObject();
                    params.add("todo", obj);
//                    String url = "http://192.168.1.68:3000/custom_controller/create";
//                    String url = getString(R.string.create_url)+"?project_id="+project_id+"&text="+text;
                    Ion.with(getApplicationContext())
                            .load("POST", getString(R.string.create_url))
                            .setJsonObjectBody(params)
                            .asString()
                            .setCallback(new FutureCallback<String>() {
                                @Override
                                public void onCompleted(Exception e, String result) {

                                }
                            });
                    setResult(RESULT_OK, null);
                }
                finish();
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
