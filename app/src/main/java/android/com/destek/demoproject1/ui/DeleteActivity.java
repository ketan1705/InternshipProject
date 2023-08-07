package android.com.destek.demoproject1.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.com.destek.demoproject1.DeleteResponse;
import android.com.destek.demoproject1.viewmodel.EmployeeViewModel;
import android.os.Bundle;
import android.com.destek.demoproject1.R;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class DeleteActivity extends AppCompatActivity {
    EmployeeViewModel employeeViewModel;
    EditText idEditText;
    ImageButton idSearch;
    TextView msgTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);
        idEditText = findViewById(R.id.idEditText);
        idSearch = findViewById(R.id.idSearch);
        msgTextView = findViewById(R.id.msgTextView);

        idSearch.setOnClickListener(view -> {
            String empId = idEditText.getText().toString();

            Log.d("Check Id", "Emp Id:" + idEditText.getText().toString());

            if (empId.isEmpty()) {
                Log.d("Check Id", "Emp Id:" + idEditText.getText().toString());

                Toast.makeText(DeleteActivity.this, "Enter your id", Toast.LENGTH_SHORT).show();

            } else {
                int id = Integer.parseInt(empId);
                employeeViewModel = new ViewModelProvider(this).get(EmployeeViewModel.class);
                employeeViewModel.deleteEmployeeLiveData(id).observe(this, new Observer<DeleteResponse>() {
                    @Override
                    public void onChanged(DeleteResponse deleteResponse) {
                        if (deleteResponse!=null)
                        {
                            msgTextView.setText(deleteResponse.getMessage());
                            msgTextView.setVisibility(View.VISIBLE);
                            Log.d("Check Deleted Data","data deleted");
                        }
                    }
                });
            }

        });
    }
}