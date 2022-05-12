package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONObject;

import java.io.IOException;
import org.json.JSONException;

import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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
                                JSONObject x = new JSONObject();
                                try {
                                    x.put("wid", workspaceList.get(holder.getAdapterPosition()).getWid());
                                } catch (Exception e) {
                                    Log.e("DeleteWorkspace", e.toString());
                                }
                                try {
                                    if(workspace.getCreatedBy().equals(WorkSpace.user.get("username"))){
                                        Log.i("DeleteWorkspace", x.toString());
                                        // client to send request.
                                        OkHttpClient client = new OkHttpClient();
                                        // media type to json, to inform the data is in json format
                                        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                                        // request body.
                                        RequestBody data = RequestBody.create(x.toString(), JSON);
                                        // create request.
                                        Request rq = new Request.Builder().url(ServerURL.deleteWorkspace).post(data).build();

                                        client.newCall(rq).enqueue(new Callback() {
                                            @Override
                                            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                                Log.i("DeleteWorkspace", "Failed to delete workspace.");

                                            }

                                            @Override
                                            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                                String res = response.body().string();
                                                try {
                                                    JSONObject x = new JSONObject(res);
                                                } catch (Exception e) {
                                                    Log.i("DeleteWorkspace", "Error in onResponse of delete workspace");
                                                    Log.i("DeleteWorkspace", e.getMessage());
                                                }
                                            }
                                        });

                                        workspaceList.remove(holder.getAdapterPosition());
                                        notifyDataSetChanged();
                                    }else {
                                        Toast.makeText(context, "You are not Leader", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (Exception e) {
                                    Log.i("DeleteWorkspace", "Error deleting workspace");
                                    Log.i("DeleteWorkspace", e.getMessage());
                                }
                                return true;
                            case R.id.show_collaborators:
                                Intent intent1 = new Intent(context, showCollobarators.class);
                                intent1.putExtra("wid",workspace.getWid());
                                context.startActivity(intent1);
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
