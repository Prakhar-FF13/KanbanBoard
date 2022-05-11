package com.example.myapplication;

import static java.util.concurrent.TimeUnit.DAYS;
import static java.util.concurrent.TimeUnit.HOURS;
import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Model.CommentModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private ArrayList<CommentModel> commentModelArrayList;
    private Context context;

    public CommentAdapter(ArrayList<CommentModel> commentModelArrayList){
        this.commentModelArrayList = commentModelArrayList;
    }
    @NonNull
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        context = parent.getContext();
        View view = layoutInflater.inflate(R.layout.card_comment, parent, false);
        ViewHolder viewHolder = new CommentAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String currentUser="";
        try {
            currentUser = WorkSpace.user.getString("username");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(commentModelArrayList.get(position).getAuthor().equals(currentUser)){
            holder.commentAuthorTV.setText("You");
        }else{
            holder.commentAuthorTV.setText(commentModelArrayList.get(position).getAuthor());
        }
        holder.commentTV.setText(commentModelArrayList.get(position).getComment());
        String timestamp = commentModelArrayList.get(position).getTimestamp();
        String relativeTimeFromPosting = String.valueOf(getRelativeTime(Long.parseLong(timestamp)));
        holder.timeFromPostingTV.setText(relativeTimeFromPosting);


        if(commentModelArrayList.get(position).getAuthor().equals(currentUser)){
            holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext())
                            .setTitle("Delete Comment")
                            .setMessage("Are you sure want to delete?")
                            .setIcon(R.drawable.ic_baseline_delete_24)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    try {
                                        JSONObject x = new JSONObject();
                                        x.put("cid", commentModelArrayList.get(holder.getLayoutPosition()).getCid());
                                        Log.i("test", x.toString());

                                        OkHttpClient client = new OkHttpClient();
                                        // media type to json, to inform the data is in json format
                                        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                                        // request body.
                                        RequestBody data = RequestBody.create(x.toString(), JSON);
                                        // create request.
                                        Request rq = new Request.Builder().url(ServerURL.deleteComment).post(data).build();

                                        client.newCall(rq).enqueue(new Callback() {
                                            @Override
                                            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                                Log.i("DeleteComment", "Failed to delete comment.");
                                            }

                                            @Override
                                            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                                String res = response.body().string();
                                                try {
                                                    JSONObject x = new JSONObject(res);
                                                    commentModelArrayList.remove(holder.getAdapterPosition());
                                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            notifyItemRemoved(holder.getAdapterPosition());
                                                        }
                                                    });
                                                } catch (Exception e) {
                                                    Log.i("DeleteComment", "Error in onResponse of delete comment");
                                                    Log.i("DeleteComment", e.getMessage());
                                                }
                                            }
                                        });

                                    } catch (Exception e) {
                                        Log.i("DeleteComment", "Error preparing to delete comment");
                                        Log.i("DeleteComment", e.getMessage());
                                    }

                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                    alert.show();
                }
            });
        }
        else{
            holder.deleteBtn.setVisibility(View.GONE);
            holder.deleteBtn.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
        }

    }


    @Override
    public int getItemCount() {
        return commentModelArrayList.size();
    }

    class ViewHolder extends  RecyclerView.ViewHolder{
        public TextView commentAuthorTV;
        public TextView commentTV;
        public TextView timeFromPostingTV;
        public CircleImageView authorImageCIV;
        public ImageButton deleteBtn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.commentAuthorTV = itemView.findViewById(R.id.idTVCommentAuthor);
            this.commentTV = itemView.findViewById(R.id.idTVComment);
            this.timeFromPostingTV = itemView.findViewById(R.id.idTVCommentTimeDiff);
            this.authorImageCIV = itemView.findViewById(R.id.idCIVAuthorPic);
            this.deleteBtn = itemView.findViewById(R.id.idBtnCommentDelete);
        }
    }

    public static final List<Long> times = Arrays.asList(
            DAYS.toMillis(365),
            DAYS.toMillis(30),
            DAYS.toMillis(7),
            DAYS.toMillis(1),
            HOURS.toMillis(1),
            MINUTES.toMillis(1),
            SECONDS.toMillis(1)
    );

    public static final List<String> timesString = Arrays.asList(
            "yr", "mo", "wk", "day", "hr", "min", "sec"
    );

    public static CharSequence getRelativeTime(final long date) {
        return toDuration( Math.abs(System.currentTimeMillis() - date) );
    }

    private static String toDuration(long duration) {
        StringBuilder sb = new StringBuilder();
        for(int i=0;i< times.size(); i++) {
            Long current = times.get(i);
            long temp = duration / current;
            if (temp > 0) {
                sb.append(temp)
                        .append(" ")
                        .append(timesString.get(i))
                        .append(temp > 1 ? "s" : "")
                        .append(" ago");
                break;
            }
        }
        return sb.toString().isEmpty() ? "just now" : sb.toString();
    }

}
