package com.example.mobiledevproject;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;

public class ReportActivity extends AppCompatActivity {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private ActivityResultLauncher<Intent> chooserLauncher;

    ImageView currentPhoto;
    ImageView newPhoto;

    String imageName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_menu);
    }
    @Override
    public void onStart() {
        super.onStart();
        findViews();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            imageName = extras.getString("key0");
            Log.d("Tagu", "imageName: " + imageName);
        }

        FireBaseManager.downloadImage("waypoint-images/" + imageName + ".jpg", (image) -> {

            Glide.with(this)
                    .asBitmap()
                    .load(image)
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            Bitmap scaledBitMap = scaleToFitWidth(resource, Resources.getSystem().getDisplayMetrics().widthPixels);
                            currentPhoto.setImageBitmap(scaledBitMap);
                            newPhoto.setMaxHeight(currentPhoto.getHeight());
                            newPhoto.setMinimumHeight(currentPhoto.getHeight());

                        }
                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });
        });

        chooseImage(newPhoto);
    }

    public void findViews() {
        currentPhoto = findViewById(R.id.report_menu_IMG_cur);
        newPhoto = findViewById(R.id.report_menu_IMG_new);

    }

    public void setListeners() {
    }

    Bitmap scaleToFitWidth(Bitmap b, int width) {

        float factor = width / (float)b.getWidth();
        return Bitmap.createScaledBitmap(b, width, (int)(b.getHeight() * factor), true);
    }

    public void chooseImage(ImageView img) {
        Intent chooser = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        chooser.addCategory(Intent.CATEGORY_OPENABLE);
        chooser.setType("image/*");

        this.chooserLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() != Activity.RESULT_OK)
                        throw new RuntimeException("Error choosing image!");

                    Intent data = result.getData();

                    assert data != null;
                    Uri uri = data.getData();

                    Glide.with(this)
                            .asBitmap()
                            .load(uri)
                            .into(new CustomTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                    Bitmap scaledBitMap = scaleToFitWidth(resource, Resources.getSystem().getDisplayMetrics().widthPixels);
                                    img.setImageBitmap(scaledBitMap);
                                }
                                @Override
                                public void onLoadCleared(@Nullable Drawable placeholder) {
                                }
                            });


                    //img.setImageURI(uri);
                    //this.profileImageUri = uri;
                }
        );
        img.setOnClickListener(event -> this.chooserLauncher.launch(chooser));
    }


}
