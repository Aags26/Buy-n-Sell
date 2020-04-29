package com.bphc.buyandsell;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

import static com.bphc.buyandsell.Constants.ADD_PRODUCT_URL;
import static com.bphc.buyandsell.Constants.AUTH_URL;
import static com.bphc.buyandsell.Constants.FETCH_PRODUCT_URL;
import static com.bphc.buyandsell.Constants.MOBILE_NO_CHANGE_URL;
import static com.bphc.buyandsell.Constants.REG_URL;
import static com.bphc.buyandsell.Constants.UPDATE_STAR;

public interface WebServices {

    @FormUrlEncoded
    @POST(AUTH_URL)
    Call<UserResponse> postUserAuthDetails(@Field("user_email") String user_email,
                                           @Field("id_token") String id_token,
                                           @Field("src") int src
    );

    @FormUrlEncoded
    @POST(REG_URL)
    Call<UserResponse> postUserRegDetails(@Field("user_email") String user_email,
                                          @Field("user_first_name") String user_first_name,
                                          @Field("user_last_name") String user_last_name,
                                          @Field("user_mobile_no") String user_mobile_no,
                                          @Field("user_gender") String user_gender,
                                          @Field("user_image") String user_image,
                                          @Field("id_token") String id_token,
                                          @Field("src") int src
    );

    @FormUrlEncoded
    @POST(MOBILE_NO_CHANGE_URL)
    Call<UserResponse> updateMobileNumber(@Field("user_email") String user_email,
                                          @Field("id_token") String id_token,
                                          @Field("src") int src,
                                          @Field("new_mobile_no") String new_mobile_no
    );

    @FormUrlEncoded
    @POST(ADD_PRODUCT_URL)
    Call<UserResponse> addProduct(@Field("user_email") String user_email,
                                  @Field("product_name") String product_name,
                                  @Field("product_description") String product_description,
                                  @Field("product_price") int product_price,
                                  @Field("product_category") String product_category,
                                  @Field("product_image") String product_image,
                                  @Field("id_token") String id_token,
                                  @Field("src") int src
    );

    @GET(FETCH_PRODUCT_URL)
    Call<UserResponse> fetchProducts(@Query("filter") String filter,
                                     @Query("order_by") String order_by,
                                     @Query("query") String query,
                                     @Query("start") int start,
                                     @Query("end") int end
    );

    @FormUrlEncoded
    @POST(UPDATE_STAR)
    Call<UserResponse> updateStar(@Field("user_email") String user_email,
                                  @Field("product_id") String product_id,
                                  @Field("id_token") String id_token,
                                  @Field("src") int src
    );

}
