package com.example.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Model.TaskModel;

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
        holder.cardCreatedDateTV.setText(taskModelArrayList.get(position).getDate());
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

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext())
                .setTitle("Delete Task")
                .setMessage("Are you sure want to delete?")
                .setIcon(R.drawable.ic_baseline_delete_24)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                JSONObject x = new JSONObject();
                                try {
                                    x.put("id", taskModelArrayList.get(holder.getAdapterPosition()).getId());
                                } catch (Exception e) {
                                    Log.e("DeleteTask", e.toString());
                                }

                                Log.i("DeleteTask", x.toString());
                                // client to send request.
                                OkHttpClient client = new OkHttpClient();
                                // media type to json, to inform the data is in json format
                                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                                // request body.
                                RequestBody data = RequestBody.create(x.toString(), JSON);
                                // create request.
                                Request rq = new Request.Builder().url(ServerURL.deleteWorkspaceTask).post(data).build();

                                client.newCall(rq).enqueue(new Callback() {
                                    @Override
                                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                        Log.i("DeleteTask", "Failed to delete task.");

                                    }

                                    @Override
                                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                        String res = response.body().string();
                                        try {
                                            JSONObject x = new JSONObject(res);
                                        } catch (Exception e) {
                                            Log.i("DeleteTask", "Error in onResponse of delete task");
                                            Log.i("DeleteTask", e.getMessage());
                                        }
                                    }
                                });
                                taskModelArrayList.remove(holder.getAdapterPosition());
                                notifyItemRemoved(holder.getAdapterPosition());
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                alert.show();
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView cardTitleTV,cardAssigneeTV,cardPriorityTV,cardCreatedDateTV;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardTitleTV = itemView.findViewById(R.id.idTVCardTitle);
            cardAssigneeTV = itemView.findViewById(R.id.idTVCardAssignee);
            cardPriorityTV = itemView.findViewById(R.id.idTVCardPriority);
            cardCreatedDateTV = itemView.findViewById(R.id.idTVCardCreatedOn);
        }

        @Override
        public void onClick(View view) {

        }
    }

    public interface itemClickListener{
        public void onItemClick(TaskModel taskModel);
    }
}
