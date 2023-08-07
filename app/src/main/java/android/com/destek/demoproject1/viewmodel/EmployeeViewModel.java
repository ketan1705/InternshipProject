package android.com.destek.demoproject1.viewmodel;

import android.com.destek.demoproject1.DeleteResponse;
import android.com.destek.demoproject1.repository.EmployeeRepository;
import android.com.destek.demoproject1.model.EmployeeModel;
import android.com.destek.demoproject1.model.EmployeeUserModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class EmployeeViewModel extends ViewModel {
    private EmployeeRepository employeeRepository;
    private LiveData<List<EmployeeModel>> allEmplistLiveData;
    private LiveData<String> errorLiveData;

    public EmployeeViewModel() {
        employeeRepository = new EmployeeRepository();
        allEmplistLiveData = employeeRepository.getAllEmployee();
    }

    public LiveData<List<EmployeeModel>> getEmployeeListLiveData() {
        return allEmplistLiveData;
    }

    public LiveData<EmployeeModel> getEmpDetaillistLiveData(int id) {
        return employeeRepository.getEmployeeDetail(id);
    }

    public LiveData<EmployeeUserModel> createEmployeee(EmployeeUserModel employeeUserModel) {
        return employeeRepository.createEmployee(employeeUserModel);
    }


    public LiveData<EmployeeUserModel> empUpdateDetailLiveData(int id, EmployeeUserModel userModel) {
        return employeeRepository.updateEmployeeDetail(id, userModel);
    }

    public LiveData<DeleteResponse> deleteEmployeeLiveData(int id) {
        return employeeRepository.deleteEmployee(id);
    }
    public LiveData<String> getErrorLiveData() {
        return employeeRepository.getErrorLiveData();
    }
}
