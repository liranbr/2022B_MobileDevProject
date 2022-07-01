package com.example.mobiledevproject;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.mobiledevproject.Objects.Report;
import com.example.mobiledevproject.Utility.UtilityMethods;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.UUID;

public class ReportActivity extends AppCompatActivity {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private ActivityResultLauncher<Intent> chooserLauncher;

    ImageView currentPhoto;
    ImageView newPhoto;

    TextView email;
    EditText description;

    Button submit;

    String imageName = "";

    Uri uploadedImgUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_menu);
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
                        }
                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });
        });

        setupImagePicker(newPhoto);
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null) {
            submit.setText("Submit");
            email.setText(user.getEmail());
            setSubmitListener("submit");
        }
        else {
            submit.setText("Login to submit");
            setSubmitListener("login");
        }

    }

    public void findViews() {
        currentPhoto = findViewById(R.id.report_menu_IMG_cur);
        newPhoto = findViewById(R.id.report_menu_IMG_new);
        submit = findViewById(R.id.report_menu_BTN_submit);
        email = findViewById(R.id.report_menu_TV_email);
        description = findViewById(R.id.report_menu_ET_reason);

    }

    public void setSubmitListener(String action) {

        View.OnClickListener loginListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UtilityMethods.switchActivity(ReportActivity.this, LoginActivity.class);
            }
        };

        View.OnClickListener submitListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String wpId = imageName.substring(0, imageName.lastIndexOf("-"));
                String direction = imageName.substring(imageName.lastIndexOf("-") + 1);
                String uploadedImageName = String.valueOf(UUID.randomUUID());

                Report rep = new Report();
                rep.setReporterEmail(email.getText().toString());
                rep.setText(description.getText().toString());
                rep.setImageName(uploadedImageName);
                rep.setWaypointId(wpId);
                rep.setDirection(direction);

                FireBaseManager.addReport(rep);

                FireBaseManager.uploadImage(uploadedImgUri, uploadedImageName);

                finish();
            }
        };

        if (action.equals("login")) {
            submit.setOnClickListener(loginListener);
        } else if (action.equals("submit")) {
            submit.setOnClickListener(submitListener);
        }
    }

    Bitmap scaleToFitWidth(Bitmap b, int width) {

        float factor = width / (float)b.getWidth();
        return Bitmap.createScaledBitmap(b, width, (int)(b.getHeight() * factor), true);
    }

    public void setupImagePicker(ImageView img) {
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

                    uploadedImgUri = uri;
                }
        );
        img.setOnClickListener(event -> this.chooserLauncher.launch(chooser));
    }


}
