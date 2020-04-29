package com.bphc.buyandsell.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.bphc.buyandsell.APIClient;
import com.bphc.buyandsell.CustomAdapter;
import com.bphc.buyandsell.Products;
import com.bphc.buyandsell.Progress;
import com.bphc.buyandsell.R;
import com.bphc.buyandsell.UserResponse;
import com.bphc.buyandsell.WebServices;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class Buy_Fragment extends Fragment {

    private ListView buy_itemList;
    public static ArrayList<Products> productsList, buy_arrayList;
    private String filter = "", order_by = "";
    private int i = 1, productCount;
    private ImageView productStar;
    private boolean selected = false, flag_loading = false;
    private CustomAdapter buy_arrayAdapter;
    private ProgressDialog progressDialog;
    private UserResponse mUserResponse;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_buy, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbar_buy);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        buy_itemList = view.findViewById(R.id.buy_itemlist);
        progressDialog = Progress.getProgressDialog(getActivity());

        buy_arrayList = new ArrayList<>();
        loadData();

       /* buy_itemList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (productCount > 0) {
                    if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {
                        if (!flag_loading) {
                            buy_arrayList = new ArrayList<>();
                            flag_loading = true;
                            i++;
                            loadData();
                        }
                    }
                }
            }
        }); */

        return view;
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        inflater.inflate(R.menu.fetch_features, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() != R.id.filter) {
            switch (item.getItemId()) {
                case R.id.showAll:
                    filter = null;
                    Progress.showProgress(true, "Fetching all...");
                    break;
                case R.id.books:
                    filter = "books";
                    Progress.showProgress(true, "Getting you books...");
                    break;
                case R.id.tech:
                    filter = "tech";
                    Progress.showProgress(true, "Getting you tech products...");
                    break;
                case R.id.room:
                    filter = "room";
                    Progress.showProgress(true, "Getting you room products...");
                    break;
                case R.id.sports:
                    filter = "sports";
                    Progress.showProgress(true, "Getting you sports products...");
                    break;
                case R.id.apparel:
                    filter = "apparels";
                    Progress.showProgress(true, "Getting you apparels...");
                    break;
                case R.id.others:
                    filter = "others";
                    Progress.showProgress(true, "Getting you other products...");
                    break;
                case R.id.price:
                    order_by = "price";
                    Progress.showProgress(true, "Sorting by price...");
                    break;
                case R.id.stars:
                    order_by = "stars";
                    Progress.showProgress(true, "Sorting by stars...");
                    break;
                case R.id.posted_date:
                    order_by = "product posted date";
                    Progress.showProgress(true, "Sorting by posted date...");
                    break;
            }
            selected = true;
            buy_arrayList = new ArrayList<>();
            loadData();
        }
        return selected;
    }


    public void loadData() {


        if (!selected && !flag_loading)
            Progress.showProgress(true, "Fetching all...");

        Retrofit retrofit = APIClient.getRetrofitInstance();
        WebServices webServices = retrofit.create(WebServices.class);

        Call<UserResponse> call = webServices.fetchProducts(filter, order_by, null, 0, 15);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                mUserResponse = response.body();
                int code = mUserResponse.getCode();
                if (code == 200) {

                    productsList = mUserResponse.getResult().getProducts();
                    //productCount = mUserResponse.getResult().getProducts_count() - i * 10;
                    //Toast.makeText(getActivity(), "" + productCount, Toast.LENGTH_SHORT).show();

                    flag_loading = false;

                    for (int i = 0; i < productsList.size(); i++) {

                        Products mProduct = new Products();

                        String productName = productsList.get(i).getProduct_name();
                        String productDescription = productsList.get(i).getProduct_description();
                        String productImage = productsList.get(i).getProduct_image();
                        String productPrice = productsList.get(i).getProduct_price();
                        String productOwner = productsList.get(i).getProduct_owner();
                       // String productStar = productsList.get(i).getProduct_stars();
                       // String productId = productsList.get(i).getProduct_id();

                        mProduct.setProduct_name(productName);
                        mProduct.setProduct_description(productDescription);
                        mProduct.setProduct_image(productImage);
                        mProduct.setProduct_price("₹" + productPrice);
                        mProduct.setProduct_owner(productOwner);
                       // mProduct.setProduct_stars(productStar);
                       // mProduct.setProduct_id(productId);

                        buy_arrayList.add(mProduct);
                    }

                    Resources resources = getResources();

                    buy_arrayAdapter = new CustomAdapter(getActivity(), buy_arrayList, resources);
                    buy_itemList.setAdapter(buy_arrayAdapter);
                }
                Progress.dismissProgress(progressDialog);
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                t.printStackTrace();
                Progress.showProgress(false, "");
            }
        });
    }


}
