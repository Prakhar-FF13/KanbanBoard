package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class WorkSpaceAdaptor extends RecyclerView.Adapter<WorkSpaceAdaptor.ViewHolder> {

    private final ArrayList<WorkSpaceModel> workspaceList;
    private Context context;

    public WorkSpaceAdaptor(ArrayList<WorkSpaceModel> symptomsList) {
        this.workspaceList = symptomsList;
    }

    @NonNull
    @Override
    public WorkSpaceAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        context = parent.getContext();
        View view = layoutInflater.inflate(R.layout.workspace_item, parent, false );
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull WorkSpaceAdaptor.ViewHolder holder, int position) {
        holder.mworkspace_name.setText(workspaceList.get(position).getWorkspace_name());
        holder.mworkspace_member_count.setText(Integer.toString(workspaceList.get(position).getWorkspace_member_count()));
        holder.mcardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {

        return workspaceList.size();
    }

    class ViewHolder extends  RecyclerView.ViewHolder{
        public TextView mworkspace_name;
        public  TextView mworkspace_member_count;
        public CardView mcardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.mworkspace_name = itemView.findViewById(R.id.workspace_name);
            this.mworkspace_member_count = itemView.findViewById(R.id.workspace_member_count);
            this.mcardView = itemView.findViewById(R.id.card_view);
        }
    }
}
