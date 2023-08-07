package android.com.destek.demoproject1.adapter;

import android.com.destek.demoproject1.R;
import android.com.destek.demoproject1.model.EmployeeModel;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder> {

    List<EmployeeModel> employeeModelList;

    public EmployeeAdapter(List<EmployeeModel> employeeModelList) {
        this.employeeModelList = employeeModelList;
    }


    @NonNull
    @Override
    public EmployeeAdapter.EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.employee_recycler_view,parent,false);
        return new EmployeeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeAdapter.EmployeeViewHolder holder, int position) {
        EmployeeModel employeeModel = employeeModelList.get(position);
        holder.empName.setText(employeeModel.getEmployee_name());
        holder.empSalary.setText(String.valueOf(employeeModel.getEmployee_salary()));
        holder.empAge.setText(String.valueOf(employeeModel.getEmployee_age()));
    }

    @Override
    public int getItemCount() {
        return employeeModelList.size();
    }

    public static class EmployeeViewHolder extends RecyclerView.ViewHolder {

        TextView empName;
        TextView empSalary;
        TextView empAge;

        public EmployeeViewHolder(@NonNull View itemView) {
            super(itemView);

            empAge = itemView.findViewById(R.id.empAge);
            empName= itemView.findViewById(R.id.empName);
            empSalary = itemView.findViewById(R.id.empSalary);

        }
    }
}
