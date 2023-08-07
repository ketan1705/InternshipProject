package android.com.destek.demoproject1.ui;

import android.com.destek.demoproject1.R;
import android.com.destek.demoproject1.model.EmployeeUserModel;
import android.com.destek.demoproject1.viewmodel.EmployeeViewModel;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class EmployeeUpdateActivity extends AppCompatActivity {
    EmployeeViewModel employeeViewModel;
    TextInputLayout updateId;
    TextInputLayout updateName;
    TextInputLayout updateAge;
    TextInputLayout updateSalary;
    Button updateBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_update);

        updateId = findViewById(R.id.updateId);
        updateName = findViewById(R.id.updateName);
        updateAge = findViewById(R.id.updateAge);
        updateSalary = findViewById(R.id.updateSalary);
        updateBtn = findViewById(R.id.updateBtn);


        updateBtn.setOnClickListener(view -> {
            String id = Objects.requireNonNull(updateId.getEditText()).getText().toString();
            String name = Objects.requireNonNull(updateName.getEditText()).getText().toString();
            String age = Objects.requireNonNull(updateAge.getEditText()).getText().toString();
            String salary = Objects.requireNonNull(updateSalary.getEditText()).getText().toString();

            Log.d("Check Id","id:"+id);

            if (TextUtils.isEmpty(id) && TextUtils.isEmpty(name) && TextUtils.isEmpty(age)&&TextUtils.isEmpty(salary))
            {
                Toast.makeText(EmployeeUpdateActivity.this,"Button Clicked1",Toast.LENGTH_SHORT).show();

            }
            else {
                Toast.makeText(EmployeeUpdateActivity.this,"Button Clicked",Toast.LENGTH_SHORT).show();
                int empId = Integer.parseInt(id);
                employeeViewModel = new ViewModelProvider(EmployeeUpdateActivity.this).get(EmployeeViewModel.class);
                EmployeeUserModel employeeUserModel = new EmployeeUserModel(name,salary,age);

                employeeViewModel.empUpdateDetailLiveData(empId, employeeUserModel).observe(EmployeeUpdateActivity.this, employeeUserModel1 -> {
                    if (employeeUserModel1 !=null)
                    {
                        Toast.makeText(EmployeeUpdateActivity.this,"Data updated",Toast.LENGTH_SHORT).show();
                        onBackPressed();
                        Log.d("Update Data","name"+ employeeUserModel1.getName());
                    }else {
                        Toast.makeText(EmployeeUpdateActivity.this,"Data Not Updated",Toast.LENGTH_SHORT).show();

                        Log.d("Update Data","Data not updated");
                    }
                });


            }
        });

    }
}