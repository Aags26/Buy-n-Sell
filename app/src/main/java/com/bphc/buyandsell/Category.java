package com.bphc.buyandsell;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bphc.buyandsell.userVerification.SignInActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Category extends AppCompatActivity {

    private static Bitmap bitmap;
    private ImageView sellProduct_image;
    private EditText name, price, description;
    private TextView add;
    private String category, encodedImage = "";
    private static final int STORAGE_PERMISSION_CODE = 7;
    private static final int ADD_IMAGE_REQUEST = 343;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        requestStoragePermission();

        progressDialog = Progress.getProgressDialog(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        category = intent.getStringExtra("category");

        name = findViewById(R.id.edit_text_product_name);
        price = findViewById(R.id.edit_text_product_price);
        description = findViewById(R.id.edit_text_product_description);
        LinearLayout imageLayout = findViewById(R.id.add_image);
        sellProduct_image = findViewById(R.id.sellProduct_image);
        add = findViewById(R.id.text_add);

        Button button_upload = findViewById(R.id.button_upload);

        imageLayout.setOnClickListener(v -> addImage());
        button_upload.setOnClickListener(v -> {
            if (!name.getText().toString().trim().isEmpty() && !price.getText().toString().trim().isEmpty()
                    && !description.getText().toString().trim().isEmpty() && !encodedImage.trim().isEmpty())
                uploadImage();
            else
                Toast.makeText(Category.this, "One or more fields empty!", Toast.LENGTH_SHORT).show();
        });

    }


    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                Toast.makeText(this, "Permission is not granted", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    private void addImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Add product picture"), ADD_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                add.setVisibility(View.GONE);
                sellProduct_image.setImageBitmap(bitmap);
                encodeImage();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /* private String getAbsolutePath(Uri uri) {
        if (uri == null)
            return null;
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        return uri.getPath();
    }

    private Bitmap editOrientation(String absolutePath) throws IOException {

        ExifInterface ei = new ExifInterface(absolutePath);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotate(bitmap, 90);

            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotate(bitmap, 180);

            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotate(bitmap, 270);

            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                return flip(bitmap, true, false);

            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                return flip(bitmap, false, true);

            default:
                return bitmap;
        }
    }

    public static Bitmap rotate(Bitmap bitmap, float degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static Bitmap flip(Bitmap bitmap, boolean horizontal, boolean vertical) {
        Matrix matrix = new Matrix();
        matrix.preScale(horizontal ? -1 : 1, vertical ? -1 : 1);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    } */


    private void encodeImage() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] b = byteArrayOutputStream.toByteArray();
        encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
    }


    private void uploadImage() {

        Progress.showProgress(true, "Uploading... may take a while");

        Retrofit retrofit = APIClient.getRetrofitInstance();
        WebServices webServices = retrofit.create(WebServices.class);

        Call<UserResponse> call = webServices.addProduct(SignInActivity.user_email, name.getText().toString(),
                description.getText().toString(), Integer.parseInt(price.getText().toString()), category,
                encodedImage, SignInActivity.id_token, 1);

        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                UserResponse mUserResponse;
                mUserResponse = response.body();
                if (mUserResponse.getCode() == 201) {
                    Toast.makeText(Category.this, "created", Toast.LENGTH_SHORT).show();
                    Progress.dismissProgress(progressDialog);
                    name.setText("");
                    price.setText("");
                    description.setText("");
                    encodedImage = "";
                } else {
                    Toast.makeText(Category.this, "Some unknown error occurred, please try again", Toast.LENGTH_SHORT).show();
                    Progress.dismissProgress(progressDialog);
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                t.printStackTrace();
                Progress.dismissProgress(progressDialog);
                Toast.makeText(Category.this, "Not uploaded, please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
