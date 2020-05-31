package com.bphc.buyandsell.userVerification;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import android.view.View;

import com.bphc.buyandsell.APIClient;
import com.bphc.buyandsell.Constants;
import com.bphc.buyandsell.MainActivity;
import com.bphc.buyandsell.Progress;
import com.bphc.buyandsell.R;
import com.bphc.buyandsell.UserResponse;
import com.bphc.buyandsell.WebServices;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SignInActivity extends AppCompatActivity {


    GoogleSignInClient mGoogleSignInClient;
    SignInButton signInButton;
    int RC_SIGN_IN = 0;
    int user_reg_status;
    public static String user_name;
    public static String user_email;
    public static String id_token;
    public static Uri photo_url;

    ProgressDialog progressDialog;

    WebServices webServices;

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            signInButton.setVisibility(View.GONE);
            getProfileInfo();
            sendIdToken();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        progressDialog = Progress.getProgressDialog(this);

        signInButton = findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(v -> {
            signIn();
        });

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(Constants.CLIENT_ID)
                .requestEmail()
                .build();


        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            getProfileInfo();
            sendIdToken();


        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Error", "signInResult:failed code=" +
                    GoogleSignInStatusCodes.getStatusCodeString(e.getStatusCode()));
        }
    }


    private void getProfileInfo() {

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        user_name = acct.getDisplayName();
        user_email = acct.getEmail();
        id_token = acct.getIdToken();
        photo_url = acct.getPhotoUrl();
    }

    private void sendIdToken() {

        Retrofit retrofit = APIClient.getRetrofitInstance();
        webServices = retrofit.create(WebServices.class);

        Progress.showProgress(true, "Loading...");

        Call<UserResponse> call = webServices.postUserAuthDetails(user_email, id_token, 1);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {

                UserResponse mUserResponse = response.body();
                GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(SignInActivity.this);
                if (mUserResponse.getCode() == 200) {
                    user_reg_status = mUserResponse.getResult().getUser_reg_status();
                } else {
                    signInButton.setVisibility(View.VISIBLE);
                    Progress.dismissProgress(progressDialog);
                    account = null;
                }
                if (account != null) {

                    if (user_reg_status == 0) {
                        Intent intent = new Intent(SignInActivity.this, RegisterActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else if (user_reg_status == 1) {
                        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }

                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                t.printStackTrace();
                Progress.showProgress(false, "");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Progress.dismissProgress(progressDialog);
    }
}
