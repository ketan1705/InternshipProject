package android.com.destek.demoproject1.ui;

import android.com.destek.demoproject1.repository.DatabaseHelper;
import android.com.destek.demoproject1.R;
import android.com.destek.demoproject1.model.TaskModel;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TaskActivity extends AppCompatActivity {

    EditText addTaskTxt;
    Button saveTaskBtn;
    DatabaseHelper databaseHelper;
    String currentDate = null;
    String currentTime = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        addTaskTxt = findViewById(R.id.addTaskTxt);
        saveTaskBtn = findViewById(R.id.saveTaskBtn);
        databaseHelper = new DatabaseHelper(this);

        SharedPreferences sharedPref = getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
        String emailTxt = sharedPref.getString("user_email","");


        saveTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                LocalDateTime currentDateTime = null;

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    currentDateTime = LocalDateTime.now();
                }

                DateTimeFormatter dateFormatter = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                }
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    currentDate = currentDateTime.format(dateFormatter);
                }

                DateTimeFormatter timeFormatter = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
                }
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    currentTime = currentDateTime.format(timeFormatter);
                }

                String taskTxt = addTaskTxt.getText().toString();
                if (!TextUtils.isEmpty(taskTxt)) {
                    saveUserTask(taskTxt, currentDate, currentTime,emailTxt);
                    Toast.makeText(TaskActivity.this,"Task Added",Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }

            }
        });


    }

    private void saveUserTask(String taskTxt, String currentDate, String currentTime, String emailTxt) {
        TaskModel taskModel = new TaskModel();
        taskModel.setEmail(emailTxt);
        taskModel.setTaskTxt(taskTxt);
        taskModel.setDateTxt(currentDate);
        taskModel.setTimeTxt(currentTime);
         databaseHelper.addTask(taskModel);
    }

}