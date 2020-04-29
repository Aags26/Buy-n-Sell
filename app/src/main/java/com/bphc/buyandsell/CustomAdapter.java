package com.bphc.buyandsell;

import android.app.Activity;
import android.content.Context;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bphc.buyandsell.userVerification.SignInActivity;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CustomAdapter extends BaseAdapter implements View.OnClickListener {

    private Activity activity;
    private ArrayList<Products> arrayList;
    private static LayoutInflater inflater = null;
    public Resources res;
    Products mProduct = null;


    /*************  CustomAdapter Constructor *****************/
    public CustomAdapter(Activity activity, ArrayList<Products> arrayList, Resources res) {
        this.activity = activity;
        this.arrayList = arrayList;
        this.res = res;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public void onClick(View v) {
        Log.v("CustomAdapter", "=====Row button clicked=====");
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        ViewHolder holder;

        if (convertView == null) {

            /****** Inflate custom_list_view.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.custom_list_view, null);

            /****** View Holder Object to contain custom_list_view.xml file elements ******/

            holder = new ViewHolder();
            holder.name = vi.findViewById(R.id.text_View_product_name);
            holder.desc = vi.findViewById(R.id.text_View_product_description);
            holder.product_image = vi.findViewById(R.id.product_image);
            holder.product_price = vi.findViewById(R.id.product_price);
           // holder.product_stars = vi.findViewById(R.id.image_productStar);

            /************  Set holder with LayoutInflater ************/
            vi.setTag(holder);
        } else
            holder = (ViewHolder) vi.getTag();

        if (arrayList.size() <= 0) {
            Toast.makeText(activity, "No Data", Toast.LENGTH_SHORT).show();
        } else {
            /***** Get each Model object from Arraylist ********/
            mProduct = null;
            mProduct = arrayList.get(position);

            /************  Set Model values in Holder elements ***********/

            holder.name.setText(mProduct.getProduct_name());
            holder.desc.setText(mProduct.getProduct_description());

            Glide.with(activity).load(Constants.BASE_URL + mProduct.getProduct_image()).into(holder.product_image);
            holder.product_price.setText(mProduct.getProduct_price());

         /*   SharedPreferences preferences = activity.getSharedPreferences("com.bphc.buyandsell", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("Product stars", mProduct.getProduct_stars());
            editor.apply();

            String stars = preferences.getString("Product stars", null);

            if (stars.equals("1"))
                holder.product_stars.setImageResource(R.drawable.ic_starred);
            else
                holder.product_stars.setImageResource(R.drawable.ic_unstarred);

            holder.product_stars.setOnClickListener(v -> {

                Retrofit retrofit = APIClient.getRetrofitInstance();
                WebServices webServices = retrofit.create(WebServices.class);

                Call<UserResponse> call = webServices.updateStar(SignInActivity.user_email,
                        mProduct.getProduct_id(),
                        SignInActivity.id_token,
                        1);

                call.enqueue(new Callback<UserResponse>() {
                    @Override
                    public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                        UserResponse mUserResponse = response.body();
                        if (mUserResponse.getCode() == 201) {
                            if (holder.product_stars.getDrawable().getConstantState() == res.getDrawable(R.drawable.ic_unstarred).getConstantState()) {
                                holder.product_stars.setImageResource(R.drawable.ic_starred);
                                mProduct.setProduct_stars("1");
                            } else {
                                holder.product_stars.setImageResource(R.drawable.ic_unstarred);
                                mProduct.setProduct_stars("0");
                            }
                            editor.putString("Product stars", mProduct.getProduct_stars());
                            editor.apply();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserResponse> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
            }); */


            /******** Set Item Click Listener for LayoutInflater for each row *******/

            vi.setOnClickListener(new OnItemClickListener(position));

        }
        return vi;
    }


    /********* Called when Item click in ListView ************/
    private class OnItemClickListener implements View.OnClickListener {
        private int mPosition;

        OnItemClickListener(int position) {
            mPosition = position;
        }

        @Override
        public void onClick(View arg0) {
            MainActivity object = (MainActivity) activity;
            object.onItemClick(mPosition);
        }
    }


    public static class ViewHolder {

        public TextView name;
        public TextView desc;
        public ImageView product_image;
        public TextView product_price;

    }
}
