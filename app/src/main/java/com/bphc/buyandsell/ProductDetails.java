package com.bphc.buyandsell;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bphc.buyandsell.Fragments.Buy_Fragment;
import com.bphc.buyandsell.userVerification.SignInActivity;
import com.bumptech.glide.Glide;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class ProductDetails extends AppCompatActivity {

    int mPosition;
    private ImageView image_product;
    Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        Intent intent = getIntent();
        mPosition = intent.getIntExtra("position", -1);

        Products mProduct = Buy_Fragment.buy_arrayList.get(mPosition);

        image_product = findViewById(R.id.image_product);
        TextView textView_description = findViewById(R.id.product_description);
        TextView textView_name = findViewById(R.id.product_name);
        TextView textView_price = findViewById(R.id.price_product);
        TextView product_owner = findViewById(R.id.product_owner);

        Glide.with(this).load(Constants.BASE_URL + mProduct.getProduct_image()).into(image_product);
        textView_description.setText(mProduct.getProduct_description());
        textView_name.setText(mProduct.getProduct_name());

        String price = "Price: " + mProduct.getProduct_price();
        textView_price.setText(price);

        String owner = "Owner: " + mProduct.getProduct_owner();
        product_owner.setText(owner);

    }
}
