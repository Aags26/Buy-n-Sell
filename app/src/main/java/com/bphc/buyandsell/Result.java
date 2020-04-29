package com.bphc.buyandsell;

import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;

import java.util.ArrayList;

public class Result {

    @SerializedName("user_reg_status")
    private int user_reg_status;

    @SerializedName("products")
    private ArrayList<Products> products;

    @SerializedName("products_count")
    private int products_count;

    public int getUser_reg_status() {
        return user_reg_status;
    }

    public ArrayList<Products> getProducts() {
        return products;
    }

    public int getProducts_count() {
        return products_count;
    }
}
