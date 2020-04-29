package com.bphc.buyandsell.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.bphc.buyandsell.Category;
import com.bphc.buyandsell.R;
import com.bphc.buyandsell.productCategory.Apparel;
import com.bphc.buyandsell.productCategory.Books;
import com.bphc.buyandsell.productCategory.Homely;
import com.bphc.buyandsell.productCategory.Mobiles;
import com.bphc.buyandsell.productCategory.Sports;
import com.bphc.buyandsell.productCategory.moreProducts;


public class Sell_Fragment extends Fragment {

    private CardView book_cardView, mobiles_cardView, homely_cardView, sports_cardView, apparel_cardView,
            moreProducts_cardView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sell, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        book_cardView = view.findViewById(R.id.books_card_View);
        mobiles_cardView = view.findViewById(R.id.mobiles_card_View);
        homely_cardView = view.findViewById(R.id.homely_card_View);
        sports_cardView = view.findViewById(R.id.sports_card_View);
        apparel_cardView = view.findViewById(R.id.apparel_card_View);
        moreProducts_cardView = view.findViewById(R.id.more_card_View);

        book_cardView.setOnClickListener(categoryToAdd);
        mobiles_cardView.setOnClickListener(categoryToAdd);
        homely_cardView.setOnClickListener(categoryToAdd);
        sports_cardView.setOnClickListener(categoryToAdd);
        apparel_cardView.setOnClickListener(categoryToAdd);
        moreProducts_cardView.setOnClickListener(categoryToAdd);

    }

    private CardView.OnClickListener categoryToAdd = v -> {
        Intent intent = new Intent(getActivity(), Category.class);
        intent.putExtra("category", v.getContentDescription());
        startActivity(intent);
    };



}
