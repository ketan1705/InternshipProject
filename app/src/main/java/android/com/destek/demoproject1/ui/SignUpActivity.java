package android.com.destek.demoproject1.ui;


import android.app.DatePickerDialog;
import android.com.destek.demoproject1.repository.DatabaseHelper;
import android.com.destek.demoproject1.R;
import android.com.destek.demoproject1.model.UserModel;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

public class SignUpActivity extends AppCompatActivity {

    private TextInputLayout signUpName;
    private TextInputLayout signUpPassword;
    private TextInputLayout signUpEmail;
    private TextInputLayout signUpMobileNo;
    private TextInputLayout signUpDob;
    private Button signUpBtn;
    private TextView alreadyHaveAccount;
    private TextView signUpGenderTxtView;
    private RadioGroup genderRadioGroup;
    private String name, password, email, gender, dob, mobileNo;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TextInputEditText signUpDobTxt,signUpMobileNoTxt;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        signUpName = findViewById(R.id.signUpName);
        signUpEmail = findViewById(R.id.signUpEmail);
        signUpDob = findViewById(R.id.signUpDOB);
        signUpPassword = findViewById(R.id.signUpPassword);
        signUpMobileNo = findViewById(R.id.signUpMobileNo);
        signUpBtn = findViewById(R.id.signUpBtn);
        genderRadioGroup = findViewById(R.id.genderRadioGroup);
        alreadyHaveAccount = findViewById(R.id.alreadyHaveAccount);
        signUpGenderTxtView = findViewById(R.id.signUpGenderTxtView);
        signUpDobTxt = findViewById(R.id.signUpDobTxt);
        signUpMobileNoTxt = findViewById(R.id.signUpMobileNoTxt);

        databaseHelper = new DatabaseHelper(this);


        mDateSetListener = (datePicker, year, month, day) -> {
            month = month + 1;
            Log.d("Check Date", "dd:" + day + "mm: " + month + "yy: " + year);
            String date = day + "/"+ month + "/" + year;
            signUpDobTxt.setText(date);

        };

        signUpMobileNoTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 10) {
                    String newText = editable.toString().substring(0, 10);
                    signUpMobileNoTxt.setText(newText);
                    signUpMobileNoTxt.setSelection(newText.length());
                }

            }
        });



        signUpBtnClick();




    }



    private void signUpBtnClick() {
        signUpBtn.setOnClickListener(view -> {
            name = signUpName.getEditText().getText().toString();
            password = signUpPassword.getEditText().getText().toString().trim();

            dob = signUpDob.getEditText().getText().toString().trim();
            gender = getSelectedGender(genderRadioGroup);
            email = signUpEmail.getEditText().getText().toString().trim();
            mobileNo = signUpMobileNo.getEditText().getText().toString().trim();

            if (isFormValid()) {
                long result = saveUserData(name, password, email, dob, gender, mobileNo);
                if (result != -1) {
                    Toast.makeText(SignUpActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
                    finish();
                } else {

                    Toast.makeText(SignUpActivity.this, "Registration Failed!", Toast.LENGTH_SHORT).show();

                }

            }
        });
    }

    private long saveUserData(String name, String password, String email, String dob, String gender, String mobileNo) {
        UserModel userModel = new UserModel();
        userModel.setUserName(name);
        userModel.setUserPassword(password);
        userModel.setUserDob(dob);
        userModel.setUserEmail(email);
        userModel.setUserGender(gender);
        userModel.setUserMobileNo(mobileNo);
        return databaseHelper.insertUser(userModel);
    }

    private String getSelectedGender(RadioGroup genderRadioGroup) {
        int selectedId = genderRadioGroup.getCheckedRadioButtonId();

        if (selectedId == R.id.maleRadioButton) {
            return "Male";

        } else if (selectedId == R.id.femaleRadioButton) {
            return "Female";
        } else if (selectedId == R.id.otherRadioButton) {
            return "Other";

        } else {
            return null;
        }

    }

    private boolean isFormValid() {
        boolean isValid = true;
        String regex = "^[6-9]\\d{9}$";

        if (TextUtils.isEmpty(name)) {
            signUpName.setError("Enter your name");
            isValid = false;
        } else {

            signUpName.setError(null);
        }


        if (TextUtils.isEmpty(password)) {
            signUpPassword.setError("Enter Your Password");
            isValid = false;
        } else {
            signUpPassword.setError(null);
        }
        if (TextUtils.isEmpty(email)) {
            signUpEmail.setError("Enter your email");
            isValid = false;
        } else {
            signUpEmail.setError(null);
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            signUpEmail.setError("Enter valid email");
            isValid = false;
        } else {
            signUpEmail.setError(null);
        }

        if (databaseHelper.isEmailExists(email)) {
            signUpEmail.setError("Email already exists");
            isValid = false;
        } else {
            signUpEmail.setError(null);
        }
        if (TextUtils.isEmpty(dob)) {
            signUpDob.setError("Enter your date of birth");
            isValid = false;
        } else {

            signUpDob.setError(null);
        }
        if (TextUtils.isEmpty(mobileNo)) {

            signUpMobileNo.setError("Enter your mobileNo");
            isValid = false;
        } else {

            signUpMobileNo.setError(null);
        }
        if (!Patterns.PHONE.matcher(mobileNo).matches() || !isValidMobileNumber(mobileNo)) {

            signUpMobileNo.setError("Enter your mobileNo");
            isValid = false;
        } else {

            signUpMobileNo.setError(null);
        }

        if (gender != null) {
            signUpGenderTxtView.setError(null);
        } else {
            signUpGenderTxtView.setError("Please select gender");
            isValid = false;
        }


        return isValid;
    }

    public boolean isValidMobileNumber(String mobileNumber) {
        // Regular expression for a 10-digit number starting with 6 to 9
        String regex = "^[6-9]\\d{9}$";

        // Check if the mobile number matches the regex pattern
        return mobileNumber.matches(regex);
    }

    public void signUpDobClick(View view) {


        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                SignUpActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener, year, month, day
        );
        datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        datePickerDialog.show();
    }
}