package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.dhaval2404.imagepicker.ImagePicker;

import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

//import com.google.android.gms.cast.framework.media.ImagePicker;


public class UploadImage extends AppCompatActivity {
    private String selectedImageUri = null;
    CircleImageView imageView;
    Button upload_button, skip_button;
    boolean upl = false;
    public static Uri uri = null;
    JSONObject jsonObject;
    Bitmap bitmap = null;

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
                    workspaceintent.putExtra("image",uri);
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
        }
    }

}