package com.bphc.buyandsell;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

public class EditUserDetails extends AppCompatActivity implements CustomDialog.CustomDialogListener {

    public static final String ADD_PROFILE = "Add a Profile Picture";
    public static final String CHANGE_PROFILE = "Change Profile Picture";

    public static final int STORAGE_PERMISSION_CODE = 7;
    public static final int ADD_IMAGE_REQUEST = 343;

    CustomDialog dialog = null;

    public TextView addImage;
    ImageView profileImage;
    RequestPermissions permissions = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_details);

        permissions = new RequestPermissions(STORAGE_PERMISSION_CODE, ADD_IMAGE_REQUEST);
        permissions.requestStoragePermission(this);

        Toolbar toolbar = findViewById(R.id.toolbar_edit_profile);
        setSupportActionBar(toolbar);

        addImage = findViewById(R.id.text_add_image);
        profileImage = findViewById(R.id.profile_image);

        updateDetails();

        addImage.setOnClickListener(v -> {
            dialog = new CustomDialog(addImage);
            dialog.show(getSupportFragmentManager(), "Profile Dialog");
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.edit_profile, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        if (item.getItemId() == R.id.update) {
            updateDetails();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateDetails() {
        if (profileImage.getDrawable() == null) {
            addImage.setText(ADD_PROFILE);
        } else {
            addImage.setText(CHANGE_PROFILE);
        }
    }

    @Override
    public void editProfile(String edit_Profile) {
        if (edit_Profile.equals(CustomDialog.ADD_PIC) || edit_Profile.equals(CustomDialog.CHANGE_PIC)) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Add product picture"), ADD_IMAGE_REQUEST);
        } else {
            profileImage.setImageDrawable(null);
            updateDetails();
        }
        dialog.dismiss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                profileImage.setImageBitmap(bitmap);
                updateDetails();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

