package android.com.destek.demoproject1;

import android.com.destek.demoproject1.model.EmployeeUserModel;

public class EmployeeCreateResponse {
    private String status;
    private EmployeeUserModel data;

    public String getStatus() {
        return status;
    }

    public EmployeeUserModel getData() {
        return data;
    }
}
