package android.com.destek.demoproject1;

import android.com.destek.demoproject1.model.EmployeeModel;

import java.util.List;

public class EmployeeListResponse {
    public List<EmployeeModel> getData() {
        return data;
    }

    public void setData(List<EmployeeModel> data) {
        this.data = data;
    }

    private List<EmployeeModel> data;
}
