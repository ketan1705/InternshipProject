package android.com.destek.demoproject1.ui;

import android.com.destek.demoproject1.adapter.EmployeeAdapter;
import android.com.destek.demoproject1.viewmodel.EmployeeViewModel;
import android.com.destek.demoproject1.R;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class MainActivity extends AppCompatActivity {

    TextView loginTime, cityName;
    Toolbar toolbar;
    String cName;
    String lTime;
    EmployeeAdapter employeeAdapter;
//    List<EmployeeModel> employeeModelList;
    RecyclerView recyclerView;
    EmployeeViewModel employeeViewModel;

    TextView addActionText,updateActionText,detailActionText,deleteActionText;

//    FloatingActionButton floatingActionButton;


    FloatingActionButton mAddFab, mUpdateFab,empDetail,empDelete;


    ExtendedFloatingActionButton extendedFloatingActionButton;


    Boolean isAllFabsVisible;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityName = findViewById(R.id.cityName);
        loginTime = findViewById(R.id.loginTime);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        cName = getIntent().getStringExtra("cityName");
        lTime = getIntent().getStringExtra("currentTime");
        Log.d("MainAcitivity", "City Name: " + cName);
        Log.d("MainAcitivity", "Current Time: " + lTime);

        cityName.setText(cName);
        loginTime.setText(lTime);

        recyclerView = findViewById(R.id.recyclerView);
        extendedFloatingActionButton = findViewById(R.id.floating);
        mUpdateFab = findViewById(R.id.update_fab);
        mAddFab = findViewById(R.id.add_fab);
        empDetail = findViewById(R.id.empDetail);
        empDelete = findViewById(R.id.empDelete);
        addActionText = findViewById(R.id.add_action_text);
        updateActionText = findViewById(R.id.update_action_text);
        deleteActionText = findViewById(R.id.delete_action_text);
        detailActionText = findViewById(R.id.detail_action_text);



        mUpdateFab.setVisibility(View.GONE);
        empDetail.setVisibility(View.GONE);
        mAddFab.setVisibility(View.GONE);
        empDelete.setVisibility(View.GONE);
        addActionText.setVisibility(View.GONE);
        updateActionText.setVisibility(View.GONE);
        deleteActionText.setVisibility(View.GONE);
        detailActionText.setVisibility(View.GONE);

        isAllFabsVisible = false;

        extendedFloatingActionButton.shrink();

        extendedFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isAllFabsVisible)
                {
                    mAddFab.show();
                    mUpdateFab.show();
                    empDetail.show();
                    empDelete.show();
                    addActionText.setVisibility(View.VISIBLE);
                    updateActionText.setVisibility(View.VISIBLE);
                    deleteActionText.setVisibility(View.VISIBLE);
                    detailActionText.setVisibility(View.VISIBLE);


                    extendedFloatingActionButton.extend();
                    isAllFabsVisible = true;
                }else {
                    mAddFab.hide();
                    mUpdateFab.hide();
                    empDetail.hide();
                    empDelete.hide();
                    addActionText.setVisibility(View.GONE);
                    updateActionText.setVisibility(View.GONE);
                    deleteActionText.setVisibility(View.GONE);
                    detailActionText.setVisibility(View.GONE);


                    extendedFloatingActionButton.shrink();
                    isAllFabsVisible = false;
                }
            }
        });

        empDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, EmployeeDetailActivity.class));
            }
        });
        mAddFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddUserActivity.class));

            }
        });
        mUpdateFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, EmployeeUpdateActivity.class));

            }
        });
        empDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, DeleteActivity.class));

            }
        });

//        employeeAdapter = new EmployeeAdapter(employeeModelList);
//        employeeModelList = new ArrayList<>();

        employeeViewModel  = new ViewModelProvider(this).get(EmployeeViewModel.class);

        employeeViewModel.getEmployeeListLiveData().observe(this, employeeModels -> {

            if (employeeModels!=null)
            {
//                employeeAdapter.employeeViewModel = employeeModels;
                employeeAdapter = new EmployeeAdapter(employeeModels);
                recyclerView.setAdapter(employeeAdapter);

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_item, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("Check Item Id", "Item ");

        int itemId = item.getItemId();
        Log.d("Check Item Id", "Item Id: " + itemId);
        if (itemId == R.id.action_profile) {
            Log.d("Check Item Id", "Clicked");

            // Handle profile menu item click
            Log.d("Check intent", "start");
            Toast.makeText(this, "Click", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(MainActivity.this, ProfileActivity.class));
            Log.d("Check intent", "sucess");
            return true;
        } else if (itemId == R.id.action_task_update) {
            Log.d("Check Item Id", "Clicked");

            // Handle task update menu item click
            startActivity(new Intent(MainActivity.this, UpdateTask.class));

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}