package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.widget.ImageView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;


public class UploadImage extends AppCompatActivity {
    private String selectedImageUri = null;
    CircleImageView imageView;
    Button upload_button, skip_button;
    boolean upl = false;
    Uri uri = null;
    JSONObject jsonObject;
    Bitmap bitmap = null;
    InputStream inputStream;

    public static HashMap<String ,String > image_Data = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);
        imageView = findViewById(R.id.image_view);
        upload_button = findViewById(R.id.button_upload);
        skip_button = findViewById(R.id.button_skip);
        String u = getIntent().getStringExtra("user");
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(UploadImage.this)
                        .crop()
                        .compress(1024)
                        .maxResultSize(1080, 1080)
                        .saveDir(getExternalFilesDir(Environment.DIRECTORY_DCIM))
                        .start(101);
            }
        });

        upload_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(upl==false){
                    Toast.makeText(getApplicationContext(), "Please choose a picture", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "New Profile Picture Applied", Toast.LENGTH_SHORT).show();
                    Intent workspaceintent = new Intent(getApplicationContext(), WorkSpace.class);
                    workspaceintent.putExtra("user", u);
                    try {
                         jsonObject = new JSONObject(getIntent().getStringExtra("user"));
                        image_Data.put("username", jsonObject.get("username").toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.i("Workspace", "Error in converting user object from intent.extra");
                    }

                    image_Data.put("uri", uri.toString());
                    JSONObject creds = new JSONObject(image_Data);
                    OkHttpClient client = new OkHttpClient();
                    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                    RequestBody data = RequestBody.create(creds.toString(), JSON);
//                    Request rq = new Request.Builder().url(ServerURL.imgUpld).post(data).build();
                    startActivity(workspaceintent);
                }
            }

        });


        skip_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent workspaceintent = new Intent(getApplicationContext(), WorkSpace.class);
                workspaceintent.putExtra("user", u);
                startActivity(workspaceintent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        upl=true;
        if(resultCode==RESULT_OK){
            uri = data.getData();
            imageView.setImageURI(uri);
            try {
                inputStream = getContentResolver().openInputStream(uri);
                bitmap = BitmapFactory.decodeStream(inputStream);
            }catch (FileNotFoundException e){
                Log.e("hello",e.getMessage().toString());
            }
        }
    }

}
