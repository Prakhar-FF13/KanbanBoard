package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Model.TaskModel;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private ArrayList<TaskModel> taskModelArrayList;
    private itemClickListener clickListener;
    public RecyclerViewAdapter(ArrayList<TaskModel> taskModelArrayList, itemClickListener clickListener){
        this.taskModelArrayList = taskModelArrayList;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_task,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.cardTitleTV.setText(taskModelArrayList.get(position).getTitle());
        holder.cardAssigneeTV.setText("@"+taskModelArrayList.get(position).getAssignee());
        String priority = taskModelArrayList.get(position).getPriority();
        holder.cardPriorityTV.setText(priority);
        switch (priority){
            case "low":
                holder.cardPriorityTV.setBackgroundResource(R.drawable.background_priority_low);
                break;
            case "medium":
                holder.cardPriorityTV.setBackgroundResource(R.drawable.background_priority_medium);
                break;
            case "high":
                holder.cardPriorityTV.setBackgroundResource(R.drawable.background_priority_high);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onItemClick(taskModelArrayList.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView cardTitleTV,cardAssigneeTV,cardPriorityTV;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardTitleTV = itemView.findViewById(R.id.idTVCardTitle);
            cardAssigneeTV = itemView.findViewById(R.id.idTVCardAssignee);
            cardPriorityTV = itemView.findViewById(R.id.idTVCardPriority);
        }

        @Override
        public void onClick(View view) {

        }
    }

    public interface itemClickListener{
        public void onItemClick(TaskModel taskModel);
    }
}
