package com.bphc.buyandsell;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.bphc.buyandsell.Fragments.Buy_Fragment;
import com.bphc.buyandsell.Fragments.MyProfile_Fragment;
import com.bphc.buyandsell.Fragments.Sell_Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new Buy_Fragment())
                .commit();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            item -> {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.nav_buy:
                        selectedFragment = new Buy_Fragment();
                        break;
                    case R.id.nav_sell:
                        selectedFragment = new Sell_Fragment();
                        break;
                    case R.id.nav_my_account:
                        selectedFragment = new MyProfile_Fragment();
                        break;
                }
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
                return true;
            };

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?").setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.super.onBackPressed();
                    }
                })

                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /*****************  This function used by adapter ****************/
    public void onItemClick(int mPosition) {

        Intent intent = new Intent(this, ProductDetails.class);
        intent.putExtra("position", mPosition);
        startActivity(intent);

    }


}
