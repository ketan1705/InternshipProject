package android.com.destek.demoproject1.repository;

import android.com.destek.demoproject1.DeleteResponse;
import android.com.destek.demoproject1.EmployeeCreateResponse;
import android.com.destek.demoproject1.EmployeeListResponse;
import android.com.destek.demoproject1.EmployeeResponse;
import android.com.destek.demoproject1.model.EmployeeUserModel;
import android.com.destek.demoproject1.model.UserModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiInterface {
    @GET("employees")
    Call<EmployeeListResponse> getAllEmployee();

    @GET("employee/{id}")
    Call<EmployeeResponse> getEmployeeDetail(@Path("id") int empId);

    @POST("create")
    Call<EmployeeCreateResponse> createUser(@Body EmployeeUserModel employeeUserModel);

    @PUT("update/{id}")
    Call<EmployeeCreateResponse> updateEmployeeDetail(@Path("id") int id, @Body EmployeeUserModel employeeUserModel);

    @DELETE("delete/{id}")
    Call<DeleteResponse> deleteEmployee(@Path("id") int id);
}
