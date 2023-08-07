package android.com.destek.demoproject1.adapter;

import android.com.destek.demoproject1.R;
import android.com.destek.demoproject1.model.TaskModel;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
    List<TaskModel> arrayList;

    public TaskAdapter(List<TaskModel> arrayList) {
        this.arrayList = arrayList;
    }


    @NonNull
    @Override
    public TaskAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Log.d("TaskAdapter", "Arraylist size: " + arrayList.size());
        Log.d("TaskAdapter", "Position: " + position);
        TaskModel taskModel = arrayList.get(position);
        Log.d("Task Adapter","Task Model Task: "+ taskModel.getTaskTxt());
        holder.recyclerTaskTxt.setText(taskModel.getTaskTxt());
        holder.recyclerDateTxt.setText(taskModel.getDateTxt());
        holder.recyclerTimeTxt.setText(taskModel.getTimeTxt());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView recyclerTaskTxt;
        TextView recyclerDateTxt;
        TextView recyclerTimeTxt;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            recyclerTaskTxt = itemView.findViewById(R.id.recyclerTaskTxt);
            recyclerDateTxt = itemView.findViewById(R.id.recyclerDateTxt);
            recyclerTimeTxt = itemView.findViewById(R.id.recyclerTimetxt);


        }
    }
}
