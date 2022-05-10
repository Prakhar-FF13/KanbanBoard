package com.example.myapplication;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class showCollobaratorsAdapter extends RecyclerView.Adapter<showCollobaratorsAdapter.ViewHolder> {


    private final ArrayList<collabListModel> collablist;
    private Context context;

    public showCollobaratorsAdapter(ArrayList<collabListModel> collablist) {
        this.collablist = collablist;
    }

    @NonNull
    @Override
    public showCollobaratorsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        context = parent.getContext();
        View view = layoutInflater.inflate(R.layout.collab_item, parent, false );
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull showCollobaratorsAdapter.ViewHolder holder, int position) {
    collabListModel collab = collablist.get(position);
    holder.mcollabname.setText(collab.getCollabname());
        try {
            String muser = WorkSpace.user.getString("username");
            Log.i("muser", muser);
            Log.i("designation", collab.getDesignation());
            if(muser.equals(collab.getCollabname())){
                holder.mcollabdesignation.setText("Leader");
            }else{
                holder.mcollabdesignation.setText("Member");
            }
            holder.mremovecollab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(muser.equals(collab.getDesignation())){
                        deletecollab(collab.getWid(), collab.getCollabname());
                    }else{
                        Toast.makeText(context, "You are not Leader", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }




    }

    @Override
    public int getItemCount() {
        return collablist.size();
    }

    private void deletecollab(int wid, String username) {
        try {
            JSONObject x = new JSONObject();
            x.put("wid", wid);
            x.put("username", username);

//            Log.i("UpdateTask", x.toString());
            // client to send request.
            OkHttpClient client = new OkHttpClient();
            // media type to json, to inform the data is in json format
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            // request body.
            RequestBody data = RequestBody.create(x.toString(), JSON);
            // create request.
            Request rq = new Request.Builder().url(ServerURL.removecollab).post(data).build();

            client.newCall(rq).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Log.i("AddTaskInWorkspace", "Failed to create task.");

                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    String res = response.body().string();
                    try {
                        JSONObject x = new JSONObject(res);
                        Log.i(TAG, "Task Updated Successfully!");
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                notifyDataSetChanged();
                                Toast.makeText(context, "Member deleted Successfully!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (Exception e) {
                        Log.i("UpdateTask", "Error in onResponse of update task");
                        Log.i("UpdateTask", e.getMessage());
                    }
                }
            });
        } catch (Exception e) {
            Log.i("UpdateTask", "Failed Updating task");
            Log.i("UpdateTask", e.getMessage());
        }
    }
    class ViewHolder extends  RecyclerView.ViewHolder{
        public TextView mcollabname;
        public Button mremovecollab;
        public TextView mcollabdesignation;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.mcollabname = itemView.findViewById(R.id.collab_name);
            this.mremovecollab = itemView.findViewById(R.id.collab_remove);
            this.mcollabdesignation = itemView.findViewById(R.id.collab_designation);
        }
    }
}
