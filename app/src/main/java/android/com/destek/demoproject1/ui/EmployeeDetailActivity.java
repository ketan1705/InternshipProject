package android.com.destek.demoproject1.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.com.destek.demoproject1.model.EmployeeModel;
import android.com.destek.demoproject1.viewmodel.EmployeeViewModel;
import android.content.Context;
import android.os.Bundle;
import android.com.destek.demoproject1.R;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class EmployeeDetailActivity extends AppCompatActivity {
    EmployeeViewModel employeeViewModel;
    EditText editText;
    TextView singleEmpName,singleEmpSalary,singleEmpAge,textView;
    ImageButton search;
    LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_detail);

        editText = findViewById(R.id.editText);
        search = findViewById(R.id.search);
        singleEmpName = findViewById(R.id.singleEmpName);
        singleEmpSalary = findViewById(R.id.singleEmpSalary);
        singleEmpAge = findViewById(R.id.singleEmpAge);
        textView = findViewById(R.id.textView);
        linearLayout = findViewById(R.id.linearLayout);


        search.setOnClickListener(view -> {
            String empId = editText.getText().toString();

            Log.d("Check Id","Emp Id:"+editText.getText().toString());

            if (empId.isEmpty())
            {
                Log.d("Check Id","Emp Id:"+editText.getText().toString());

                Toast.makeText(EmployeeDetailActivity.this,"Enter your id",Toast.LENGTH_SHORT).show();

            }

            else {
                Toast.makeText(EmployeeDetailActivity.this,"Button Clicked",Toast.LENGTH_SHORT).show();
                search.setEnabled(false);
                int id = Integer.parseInt(empId);
                Log.d("LiveDataCheck", "employeeViewModel: " + (employeeViewModel != null));
                Log.d("LiveDataCheck", "getEmpDetaillistLiveData: " + (employeeViewModel != null ? employeeViewModel.getEmpDetaillistLiveData(id) : "N/A"));
                Log.d("LiveDataCheck", "getErrorLiveData: " + (employeeViewModel != null ? employeeViewModel.getErrorLiveData() : "N/A"));

                employeeViewModel = new ViewModelProvider(EmployeeDetailActivity.this).get(EmployeeViewModel.class);
                employeeViewModel.getEmpDetaillistLiveData(id).observe(EmployeeDetailActivity.this, new Observer<EmployeeModel>() {
                    @Override
                    public void onChanged(EmployeeModel employeeModel) {
                        if (employeeModel!=null)
                        {
                            String age = String.valueOf(employeeModel.getEmployee_age());
                            String name = employeeModel.getEmployee_name();
                            String salary = String.valueOf(employeeModel.getEmployee_salary());

                            singleEmpAge.setText(getString(R.string.age_label,age));
                            singleEmpName.setText(getString(R.string.name_label,name));
                            singleEmpSalary.setText(getString(R.string.salary_label,salary));
                            textView.setVisibility(View.GONE);
                            linearLayout.setVisibility(View.VISIBLE);
                        }
                    }
                });

                employeeViewModel.getErrorLiveData().observe(EmployeeDetailActivity.this, new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        if (s!=null){
                            final Context context = EmployeeDetailActivity.this;
                            search.setEnabled(true);
                            linearLayout.setVisibility(View.GONE);
                            textView.setVisibility(View.VISIBLE);
                            Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }
        });

    }
}