package android.com.destek.demoproject1.repository;

import android.com.destek.demoproject1.DeleteResponse;
import android.com.destek.demoproject1.EmployeeCreateResponse;
import android.com.destek.demoproject1.EmployeeListResponse;
import android.com.destek.demoproject1.EmployeeResponse;
import android.com.destek.demoproject1.model.EmployeeModel;
import android.com.destek.demoproject1.model.EmployeeUserModel;
import android.com.destek.demoproject1.repository.ApiInterface;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.io.IOException;
import java.util.List;

import okhttp3.RequestBody;
import okio.Buffer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EmployeeRepository {
    private final ApiInterface apiInterface;
    private MutableLiveData<String> errorLiveData;

    public EmployeeRepository(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://dummy.restapiexample.com/api/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiInterface = retrofit.create(ApiInterface.class);
        errorLiveData = new MutableLiveData<>();


    }
    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public LiveData<List<EmployeeModel>> getAllEmployee(){
//        String employeeId = "1";
        MutableLiveData<List<EmployeeModel>> allEmployeemutableLiveData = new MutableLiveData<>();

        Call<EmployeeListResponse> employeeListCall = apiInterface.getAllEmployee();

        employeeListCall.enqueue(new Callback<EmployeeListResponse>() {
            @Override
            public void onResponse(Call<EmployeeListResponse> call, Response<EmployeeListResponse> response) {
                if (response.isSuccessful()){
                    EmployeeListResponse employeeResponse = response.body();
                    if (employeeResponse!=null)
                    {
                        allEmployeemutableLiveData.setValue(employeeResponse.getData());
                    }

                    Log.d("Check LiveData","response success");

                }else {
                    errorLiveData.setValue("Something Went Wrong");
                    try {
                        String errorResponse = response.errorBody().string();
                        int errorCode = response.code();

                        Log.d("Check LiveData", "Response failed. Error Code: " + errorCode + " Error Body: " + errorResponse);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<EmployeeListResponse> call, Throwable t) {
                errorLiveData.setValue("Something Went Wrong");
                Log.d("Check LiveData","response failed" + t);

            }
        });

        return allEmployeemutableLiveData;
    }


    public LiveData<EmployeeModel> getEmployeeDetail(int id){

        MutableLiveData<EmployeeModel> detailEmployeemutableLiveData = new MutableLiveData<>();
        Call<EmployeeResponse> empDetailCall = apiInterface.getEmployeeDetail(id);

        Log.d("EmpDetailCall", "URL: " + empDetailCall.request().url().toString());
        empDetailCall.enqueue(new Callback<EmployeeResponse>() {
            @Override
            public void onResponse(Call<EmployeeResponse> call, Response<EmployeeResponse> response) {
                if (response.isSuccessful())
                {
                    EmployeeResponse employeeResponse = response.body();
                    if (employeeResponse != null) {
                        detailEmployeemutableLiveData.setValue(employeeResponse.getData());
                            Log.d("Employee Detail","data fetched");


                    }
                    Log.d("Employee Detail","data not fetched");
                }
                else {
                    errorLiveData.setValue("Something Went Wrong");

                }
            }

            @Override
            public void onFailure(Call<EmployeeResponse> call, Throwable t) {
                errorLiveData.setValue("Something Went Wrong");


            }
        });

        return detailEmployeemutableLiveData;
    }
    public LiveData<EmployeeUserModel> createEmployee(EmployeeUserModel userModel){
        MutableLiveData<EmployeeUserModel> createEmpmutableLiveData = new MutableLiveData<>();
        Call<EmployeeCreateResponse> employeeCreateResponseCall = apiInterface.createUser(userModel);
        Log.d("EmpDetailCall", "URL: " + employeeCreateResponseCall.request().url().toString());

        RequestBody requestBody = employeeCreateResponseCall.request().body();
        if (requestBody != null) {
            Buffer buffer = new Buffer();
            try {
                requestBody.writeTo(buffer);
                String requestBodyString = buffer.readUtf8();
                Log.d("EmpDetailCall", "Request Body: " + requestBodyString);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        employeeCreateResponseCall.enqueue(new Callback<EmployeeCreateResponse>() {
            @Override
            public void onResponse(Call<EmployeeCreateResponse> call, Response<EmployeeCreateResponse> response) {
                if (response.isSuccessful())
                {
                    EmployeeCreateResponse employeeResponse = response.body();
                    if (employeeResponse != null) {
                        createEmpmutableLiveData.setValue(employeeResponse.getData());

                    }
                    else {
                        Log.d("Check User Create","User Not created");
                    }
                }
                else {
                    errorLiveData.setValue("Something Went Wrong");

                }

            }

            @Override
            public void onFailure(Call<EmployeeCreateResponse> call, Throwable t) {
                errorLiveData.setValue("Something Went Wrong");

                Log.d("Check User Create","User Not created1");

            }
        });

        return createEmpmutableLiveData;
    }

    public LiveData<EmployeeUserModel> updateEmployeeDetail(int id, EmployeeUserModel userModel){
        MutableLiveData<EmployeeUserModel> updateDetailEmpmutableLiveData = new MutableLiveData<>();
        Call<EmployeeCreateResponse> updateDetailCall = apiInterface.updateEmployeeDetail(id,userModel);
        RequestBody requestBody = updateDetailCall.request().body();
        if (requestBody != null) {
            Buffer buffer = new Buffer();
            try {
                requestBody.writeTo(buffer);
                String requestBodyString = buffer.readUtf8();
                Log.d("EmpDetailCall", "Request Body: " + requestBodyString);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        updateDetailCall.enqueue(new Callback<EmployeeCreateResponse>() {
            @Override
            public void onResponse(Call<EmployeeCreateResponse> call, Response<EmployeeCreateResponse> response) {
                if (response.isSuccessful())
                {
                    EmployeeCreateResponse employeeResponse = response.body();
                    if (employeeResponse != null) {
                        updateDetailEmpmutableLiveData.setValue(employeeResponse.getData());

                    }
                }
                else {
                    errorLiveData.setValue("Something Went Wrong");

                }
            }

            @Override
            public void onFailure(Call<EmployeeCreateResponse> call, Throwable t) {
                errorLiveData.setValue("Something Went Wrong");

            }
        });

        return updateDetailEmpmutableLiveData;
    }

    public LiveData<DeleteResponse> deleteEmployee (int id)
    {
        MutableLiveData<DeleteResponse> deleteMutableLiveData = new MutableLiveData<>();

        Call<DeleteResponse> call = apiInterface.deleteEmployee(id);
        RequestBody requestBody = call.request().body();

        call.enqueue(new Callback<DeleteResponse>() {
            @Override
            public void onResponse(Call<DeleteResponse> call, Response<DeleteResponse> response) {
                if (response.isSuccessful())
                {
                    DeleteResponse deleteResponse = response.body();
                    Log.d("Check Delete Data","Deleted" + deleteResponse.getStatus());

                    if (deleteResponse.getStatus().equals("success"))
                    {
                        deleteMutableLiveData.setValue(response.body());
                        Log.d("Check Delete Data","Deleted" + deleteResponse.getMessage());

                    }
                }
                else {

                    Log.d("Check Delete Data","not Deleted");
                }
            }

            @Override
            public void onFailure(Call<DeleteResponse> call, Throwable t) {

            }
        });

        return deleteMutableLiveData;
    }

}
