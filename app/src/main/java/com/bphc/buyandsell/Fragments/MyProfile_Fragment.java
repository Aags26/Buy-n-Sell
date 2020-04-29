package com.bphc.buyandsell.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.bphc.buyandsell.APIClient;
import com.bphc.buyandsell.EditUserDetails;
import com.bphc.buyandsell.Progress;
import com.bphc.buyandsell.R;
import com.bphc.buyandsell.UserResponse;
import com.bphc.buyandsell.WebServices;
import com.bphc.buyandsell.RequestPermissions;
import com.bphc.buyandsell.userVerification.SignInActivity;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class MyProfile_Fragment extends Fragment {

    private GoogleSignInClient mGoogleSignInClient;
    private Button button_sign_out, button_edit;
    private EditText mobile_no_edit;
    private ImageView image_update, myProfImage;
    private WebServices webServices;
    private LinearLayout linearLayout;
    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_my_profile, container, false);


        Toolbar toolbar = view.findViewById(R.id.toolbar_my_profile);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        TextView username = view.findViewById(R.id.text_username);
        username.setText(SignInActivity.user_name);

        myProfImage = view.findViewById(R.id.myProfile);
        Glide.with(this).load(SignInActivity.photo_url).into(myProfImage);

        TextView user_email = view.findViewById(R.id.text_user_email);
        user_email.setText(SignInActivity.user_email);
        
        return view;

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        inflater.inflate(R.menu.my_profile, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.profile_edit:
                startActivity(new Intent(getActivity(), EditUserDetails.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        button_sign_out = view.findViewById(R.id.button_sign_out);

        button_sign_out.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Are you sure you want to logout?").setCancelable(true)
                    .setPositiveButton("Yes", (dialog, which) -> {
                        signOut();
                    })

                    .setNegativeButton("No", (dialog, which) -> dialog.cancel());
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });

        progressDialog = Progress.getProgressDialog(getActivity());

        button_edit = view.findViewById(R.id.button_edit);
        button_edit.setOnClickListener(v -> {

            linearLayout = view.findViewById(R.id.layout_update);
            mobile_no_edit = view.findViewById(R.id.edit_text_mobile);
            image_update = view.findViewById(R.id.image_update);

            linearLayout.setVisibility(View.VISIBLE);
            updateMobileNo();

        });
    }

    public void signOut() {
        mGoogleSignInClient
                .signOut()
                .addOnCompleteListener(getActivity(), task -> {
                    Intent intent = new Intent(getActivity(), SignInActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    getActivity().finish();
                });
    }

    private void updateMobileNo() {

        image_update.setOnClickListener(v -> {

            if (mobile_no_edit.getText().toString().trim().length() == 10) {

                Retrofit retrofit = APIClient.getRetrofitInstance();
                webServices = retrofit.create(WebServices.class);

                Progress.showProgress(true, "Updating...");

                Call<UserResponse> call = webServices.updateMobileNumber(SignInActivity.user_email, SignInActivity.id_token, 1,
                        mobile_no_edit.getText().toString());
                call.enqueue(new Callback<UserResponse>() {
                    @Override
                    public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                        UserResponse mUserResponse = response.body();
                        int code = mUserResponse.getCode();
                        if (code == 201) {
                            Progress.dismissProgress(progressDialog);
                            Toast.makeText(getActivity(), "Mobile number changed", Toast.LENGTH_LONG).show();
                            mobile_no_edit.setText("");
                            linearLayout.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<UserResponse> call, Throwable t) {
                        t.printStackTrace();
                        Progress.showProgress(false, "");
                    }
                });
            } else {
                Toast.makeText(getActivity(), "Invalid number", Toast.LENGTH_SHORT).show();
            }

        });
    }
}
