package com.bphc.buyandsell;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class CustomDialog extends AppCompatDialogFragment {

    private TextView addImage;

    public static final String ADD_PIC = "Add a Profile Picture";
    public static final String CHANGE_PIC = "Change your Profile Picture";
    public static final String REMOVE_PIC = "Remove your Profile Picture";

    private CustomDialogListener listener;

    CustomDialog(TextView addImage) {
        this.addImage = addImage;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.layout_dialog, null);
        builder.setView(view)
                .setTitle("Edit Profile Picture");

        TextView pic_add = view.findViewById(R.id.pic_add);
        pic_add.setText(ADD_PIC);

        TextView pic_change = view.findViewById(R.id.pic_change);
        pic_change.setText(CHANGE_PIC);

        TextView pic_remove = view.findViewById(R.id.pic_remove);
        pic_remove.setText(REMOVE_PIC);

        if (addImage.getText().toString().equals(EditUserDetails.ADD_PROFILE)) {
            pic_remove.setVisibility(View.GONE);
            pic_change.setVisibility(View.GONE);
            pic_add.setVisibility(View.VISIBLE);
        } else {
            pic_add.setVisibility(View.GONE);
            pic_change.setVisibility(View.VISIBLE);
            pic_remove.setVisibility(View.VISIBLE);
        }

        pic_add.setOnClickListener(v -> listener.editProfile(pic_add.getText().toString()));
        pic_change.setOnClickListener(v -> listener.editProfile(pic_change.getText().toString()));
        pic_remove.setOnClickListener(v -> listener.editProfile(pic_remove.getText().toString()));

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (CustomDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement the custom dialog listener");
        }
    }

    public interface CustomDialogListener {
        void editProfile(String edit_Profile);
    }

}


