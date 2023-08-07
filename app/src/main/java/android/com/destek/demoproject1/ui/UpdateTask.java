package android.com.destek.demoproject1.ui;

import android.com.destek.demoproject1.repository.DatabaseHelper;
import android.com.destek.demoproject1.R;
import android.com.destek.demoproject1.adapter.TaskAdapter;
import android.com.destek.demoproject1.model.TaskModel;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class UpdateTask extends AppCompatActivity {

    FloatingActionButton floatingBtn;
    TextView textView;
    RecyclerView recyclerView;
    TaskAdapter taskAdapter;
    List<TaskModel> taskList;

    DatabaseHelper databaseHelper;
    String email = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_task);

        floatingBtn = findViewById(R.id.floatingBtn);
        textView = findViewById(R.id.textView);
        floatingBtn.setOnClickListener(view -> startActivity(new Intent(UpdateTask.this, TaskActivity.class)));
        databaseHelper = new DatabaseHelper(this);

        SharedPreferences sharedPref = getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
        email = sharedPref.getString("user_email", "");


        recyclerView = findViewById(R.id.recyclerView);
        taskList = new ArrayList<>();
        fetchData();

        taskAdapter = new TaskAdapter(taskList);
        recyclerView.setAdapter(taskAdapter);

    }

    private void fetchData() {

            taskList.clear();
            List<TaskModel> tasks  = databaseHelper.getAllTask(email);
            if (!tasks.isEmpty()){
                taskList.addAll(tasks);
                recyclerView.setVisibility(View.VISIBLE);
                textView.setVisibility(View.GONE);
            }
            else {
                recyclerView.setVisibility(View.GONE);
                textView.setVisibility(View.VISIBLE);
            }


    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchData();
        taskAdapter.notifyDataSetChanged();
    }
}