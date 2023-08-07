package android.com.destek.demoproject1.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.com.destek.demoproject1.viewmodel.EmployeeViewModel;
import android.com.destek.demoproject1.model.EmployeeUserModel;
import android.os.Bundle;
import android.com.destek.demoproject1.R;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

public class AddUserActivity extends AppCompatActivity {
    TextInputLayout addName;
    TextInputLayout addAge;
    TextInputLayout addSalary;
    Button addDataBtn;
    EmployeeViewModel employeeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        addName = findViewById(R.id.addName);
        addAge = findViewById(R.id.addAge);
        addSalary = findViewById(R.id.addSalary);
        addDataBtn = findViewById(R.id.addDataBtn);

        addDataBtn.setOnClickListener(view -> {
            String name = addName.getEditText().getText().toString();
            String age =  addAge.getEditText().getText().toString();
            String salary = addSalary.getEditText().getText().toString();

            if (TextUtils.isEmpty(name)&&TextUtils.isEmpty(age)&&TextUtils.isEmpty(salary))
            {
                Toast.makeText(AddUserActivity.this,"Fill All Fields",Toast.LENGTH_SHORT).show();
            }
            else {
                addDataBtn.setEnabled(false);
                Toast.makeText(AddUserActivity.this,"Button Clicked",Toast.LENGTH_SHORT).show();
                EmployeeUserModel employeeUserModel = new EmployeeUserModel(name,salary,age);
                employeeViewModel = new ViewModelProvider(AddUserActivity.this).get(EmployeeViewModel.class);
                employeeViewModel.createEmployeee(employeeUserModel).observe(AddUserActivity.this, employeeUserModel1 -> {
                    if (employeeUserModel1 !=null)
                    {
                        Toast.makeText(AddUserActivity.this,"User Created",Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                });
                employeeViewModel.getErrorLiveData().observe(AddUserActivity.this, s -> {
                    addDataBtn.setEnabled(true);
                    Toast.makeText(AddUserActivity.this, "User Not Created", Toast.LENGTH_SHORT).show();
                });


            }


        });

    }
}