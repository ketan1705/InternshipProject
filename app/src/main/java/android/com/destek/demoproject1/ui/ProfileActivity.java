package android.com.destek.demoproject1.ui;

import android.com.destek.demoproject1.repository.DatabaseHelper;
import android.com.destek.demoproject1.R;
import android.com.destek.demoproject1.model.UserModel;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textview.MaterialTextView;

public class ProfileActivity extends AppCompatActivity {

    MaterialTextView nameTextView,emailTextView,mobileTextView,genderTextView,dobTextView;
    DatabaseHelper databaseHelper;
    ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        nameTextView = findViewById(R.id.nameTextView);
        emailTextView = findViewById(R.id.emailTextView);
        genderTextView = findViewById(R.id.genderTextView);
        mobileTextView = findViewById(R.id.mobileTextView);
        dobTextView = findViewById(R.id.dobTextView);
        backBtn = findViewById(R.id.backBtn);


        databaseHelper = new DatabaseHelper(this);

        SharedPreferences sharedPref = getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
        String email = sharedPref.getString("user_email",""); // Replace with the default value

        UserModel userModel = databaseHelper.getUserByEmail(email);

        String userName = getString(R.string.name_label, userModel.getUserName());
        String userEmail = getString(R.string.email_label, userModel.getUserEmail());
        String userDob = getString(R.string.dob_label, userModel.getUserDob());
        String userGender = getString(R.string.gender_label, userModel.getUserGender());
        String userMobileNo = getString(R.string.mobile_label, userModel.getUserMobileNo());

        nameTextView.setText(userName);
        genderTextView.setText(userGender);
        emailTextView.setText(userEmail);
        dobTextView.setText(userDob);
        mobileTextView.setText(userMobileNo);


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }
}



