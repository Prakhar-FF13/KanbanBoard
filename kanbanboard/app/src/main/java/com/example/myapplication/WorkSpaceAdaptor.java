package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
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
        WorkSpaceModel workspace = workspaceList.get(position);
        holder.mworkspace_name.setText(workspace.getWorkspace_name());
        holder.mworkspace_member_count.setText(Integer.toString(workspace.getWorkspace_member_count()));
        holder.mprojectLeader.setText("Leader : @" + workspace.getCreatedBy());
        holder.mworkspace_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(context.getApplicationContext(), view);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.workspace_menu_items, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.add_collaborators:
                                Intent intent = new Intent(context, AddCollobarators.class);
                                intent.putExtra("wid", workspace.getWid());
                                intent.putExtra("leader", workspace.getCreatedBy());
                                intent.putExtra("projectname", workspace.getWorkspace_name());
                                context.startActivity(intent);
                                return true;
                            case R.id.delete_workspace:
                                workspaceList.remove(position);
//                                delete workspace from db
                                notifyDataSetChanged();
                                return true;
                            default:
                                return  false;
                        }
                    }
                });
                popup.show();
            }
        });
        holder.mcardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // @TODO show workspace details activity. Intent
                Intent intent = new Intent(context, WorkspaceDetailActivity.class);
                intent.putExtra("wid", workspace.getWid());
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
        public  TextView mprojectLeader;
        public CardView mcardView;
        public ImageButton mworkspace_menu;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.mworkspace_name = itemView.findViewById(R.id.workspace_name);
            this.mworkspace_member_count = itemView.findViewById(R.id.workspace_member_count);
            this.mcardView = itemView.findViewById(R.id.card_view);
            this.mworkspace_menu = itemView.findViewById(R.id.workspace_menus);
            this.mprojectLeader = itemView.findViewById(R.id.projectleader);
        }
    }
}
