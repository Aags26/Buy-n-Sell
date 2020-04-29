package com.bphc.buyandsell.userVerification;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bphc.buyandsell.APIClient;
import com.bphc.buyandsell.MainActivity;
import com.bphc.buyandsell.Progress;
import com.bphc.buyandsell.R;
import com.bphc.buyandsell.UserResponse;
import com.bphc.buyandsell.WebServices;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RegisterActivity extends AppCompatActivity {

    public EditText user_mobile_number;
    private ProgressDialog progressDialog;
    private RadioGroup radioGroup;
    private static RadioButton radio_gender;
    private ImageView user_image;
    private TextView add;
    private Bitmap bitmap;
    private String gender, encodedImage = "";
    private static final int ADD_IMAGE_REQUEST = 343;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        user_mobile_number = findViewById(R.id.user_mobile);
        Button button_register = findViewById(R.id.button_register);

        LinearLayout imageLayout = findViewById(R.id.add_image);

        add = findViewById(R.id.text_add);

        user_image = findViewById(R.id.sellProduct_image);
        imageLayout.setOnClickListener(v -> addImage());

        radioGroup = findViewById(R.id.radio_gender);
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            checkedId = radioGroup.getCheckedRadioButtonId();
            radio_gender = findViewById(checkedId);
            gender = radio_gender.getText().toString();
        });

        progressDialog = Progress.getProgressDialog(this);

        button_register.setOnClickListener(v -> {
            if (user_mobile_number.getText().toString().trim().isEmpty() && radioGroup.getCheckedRadioButtonId() == -1)
                Toast.makeText(this, "Please enter the fields!", Toast.LENGTH_SHORT).show();
            else
                UserRegistration();
        });

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
                user_image.setMaxWidth(150);
                user_image.setMaxHeight(150);
                user_image.setImageBitmap(bitmap);
                encodeImage();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void encodeImage() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] b = byteArrayOutputStream.toByteArray();
        encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
    }

    public void UserRegistration() {

        if (radioGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please select your appropriate gender", Toast.LENGTH_SHORT).show();
        } else {
            Progress.showProgress(true, "Registering...");
            if (user_mobile_number.getText().toString().trim().length() == 10) {

                Retrofit retrofit = APIClient.getRetrofitInstance();
                WebServices webServices = retrofit.create(WebServices.class);

                Call<UserResponse> call = webServices.postUserRegDetails(
                        SignInActivity.user_email,
                        SignInActivity.user_name.substring(0, SignInActivity.user_name.indexOf(' ')),
                        SignInActivity.user_name.substring((SignInActivity.user_name.lastIndexOf(' ') + 1)),
                        user_mobile_number.getText().toString(),
                        gender,
                        encodedImage,
                        SignInActivity.id_token,
                        1
                );

                call.enqueue(new Callback<UserResponse>() {
                    @Override
                    public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {

                        UserResponse mUserResponse = response.body();
                        int code = mUserResponse.getCode();
                        if (code == 201) {
                            Toast.makeText(RegisterActivity.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                            Progress.dismissProgress(progressDialog);
                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else {
                            Toast.makeText(RegisterActivity.this, "Some unknown error occurred...", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserResponse> call, Throwable t) {
                        Progress.showProgress(false, "");
                        Toast.makeText(RegisterActivity.this, "Unknown error occurred", Toast.LENGTH_SHORT).show();
                    }
                });

            } else {
                Progress.dismissProgress(progressDialog);
                if(user_mobile_number.getText().toString().trim().isEmpty())
                    Toast.makeText(this, "Please enter your mobile number", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "Invalid mobile number", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
