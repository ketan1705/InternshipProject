package android.com.destek.demoproject1.ui;

import android.Manifest;
import android.com.destek.demoproject1.repository.DatabaseHelper;
import android.com.destek.demoproject1.R;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class SignInActivity extends AppCompatActivity {

    Button signInBtn;
    TextView dontHaveAccount;
    TextInputLayout signInEmail;
    TextInputLayout signInPassword;

    private DatabaseHelper databaseHelper;
    String cityName;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 101;
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        signInBtn = findViewById(R.id.signInBtn);
        signInEmail = findViewById(R.id.signInEmail);
        signInPassword = findViewById(R.id.signInPassword);
        dontHaveAccount = findViewById(R.id.dontHaveAccount);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(SignInActivity.this);

        getCurrentLocation();


        databaseHelper = new DatabaseHelper(SignInActivity.this);

        dontHaveAccount.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
            startActivity(intent);
        });


        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkUserPermission()) {
                    if (isLocationEnabled()) {
                        login();
                    } else {
                        alertDialogLocationEnabled();
                    }
                } else requestPermission();
            }
        });

    }

    private boolean checkUserPermission() {
        if (ContextCompat.checkSelfPermission(SignInActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;

    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(SignInActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("This app requires location permission")
                    .setTitle("Pemission Required")
                    .setCancelable(false)
                    .setPositiveButton("OK", (dialog, i) -> {
                        ActivityCompat.requestPermissions(SignInActivity.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                LOCATION_PERMISSION_REQUEST_CODE);
                        dialog.dismiss();
                    }).setNegativeButton("Cancel", (dialog, i) -> dialog.dismiss());
            builder.show();
        } else {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    // Get User Current Location
    private void getCurrentLocation() {
        if (checkUserPermission()) {
            if (isLocationEnabled()) {


                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location != null) {
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();
                            Log.d("Current Location", "Latitude: " + latitude + "Longitude: " + longitude);
                            getCityName(latitude, longitude);
                        } else {
                            Log.d("Current Location", "No Location");

                        }
                    }
                });


            } else {
                alertDialogLocationEnabled();
            }
        } else {
            requestPermission();
        }
    }

    private void alertDialogLocationEnabled() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("This app requires location.")
                .setTitle("Do you want to give permission?")
                .setCancelable(false)
                .setNegativeButton("Cancel", (dialog, id) -> dialog.dismiss())
                .setPositiveButton("Ok", (dialogInterface, i) -> {
                    startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    dialogInterface.dismiss();
                });
        builder.show();
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

    }

    //Get City Name
    private void getCityName(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {

            List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);

            if (addressList != null && !addressList.isEmpty()) {
                cityName = addressList.get(0).getLocality();
                Log.d("Check City Name", "City name: " + cityName);
            } else {
                Log.d("Check City Name", "City name: null");

            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
                Log.d("Check Permission", "Permission Granted");
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();

            }
        }
    }

    private void login() {

        if (isFormValid()) {

            boolean isAuthenticated = databaseHelper.authenticateUser(signInEmail.getEditText().getText().toString().trim(), signInPassword.getEditText().getText().toString().trim());
            if (isAuthenticated) {
                SharedPreferences sharedPref = getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("user_email", signInEmail.getEditText().getText().toString().trim()); // Replace with the actual email
                editor.apply();
                Toast.makeText(SignInActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                Log.d("CityName", "City Name: " + cityName);
                Log.d("Login Time", "Time : " + getLoginTime());

                intent.putExtra("cityName", cityName);
                intent.putExtra("currentTime", getLoginTime());
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(SignInActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();

            }
        }
    }

    private String getLoginTime() {
        LocalDateTime currentDateTime = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            currentDateTime = LocalDateTime.now();
        }
        DateTimeFormatter timeFormatter = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            timeFormatter = DateTimeFormatter.ofPattern("hh:mm:ss");
        }
        String currentTime = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            currentTime = currentDateTime.format(timeFormatter);
        }
        return currentTime;
    }


    private boolean isFormValid() {
        boolean isValid = true;

        if (TextUtils.isEmpty(signInEmail.getEditText().getText().toString().trim())) {
            signInEmail.setError("Please enter your email");
            isValid = false;
        } else {
            signInEmail.setError(null);
        }

        if (TextUtils.isEmpty(signInPassword.getEditText().getText().toString().trim())) {
            signInPassword.setError("Please enter your password");
            isValid = false;
        } else {
            signInPassword.setError(null);
        }

        return isValid;
    }


    @Override
    protected void onResume() {
        super.onResume();
        getCurrentLocation();
    }
}
