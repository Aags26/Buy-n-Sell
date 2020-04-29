package com.bphc.buyandsell;

import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;

public class Products {

    @SerializedName("product_name")
    private String product_name;

    @SerializedName("product_description")
    private String product_description;

    @SerializedName("product_image")
    private String product_image;

    @SerializedName("product_price")
    private String product_price;

    @SerializedName("product_id")
    private String product_id;

    @SerializedName("product_category")
    private String product_category;

    @SerializedName("product_owner")
    private String product_owner;

    @SerializedName("product_posted_date")
    private String product_posted_date;

    @SerializedName("product_status")
    private String product_status;

    @SerializedName("product_stars")
    private String product_stars;


    public String getProduct_name() {
        return product_name;
    }

    public String getProduct_description() {
        return product_description;
    }

    public String getProduct_image() {
        return product_image;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public void setProduct_description(String product_description) {
        this.product_description = product_description;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_category() {
        return product_category;
    }

    public void setProduct_owner(String product_owner) {
        this.product_owner = product_owner;
    }

    public String getProduct_owner() {
        return product_owner;
    }

    public String getProduct_posted_date() {
        return product_posted_date;
    }

    public String getProduct_status() {
        return product_status;
    }

    public String getProduct_stars() {
        return product_stars;
    }

    public void setProduct_stars(String product_stars) {
        this.product_stars = product_stars;
    }

}
