package com.cbd.teammate.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.cbd.teammate.R;
import com.cbd.teammate.RegisterActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {

    private FirebaseAuth auth;
    private Button logout;
    private TextView textMail;
    private TextView textNumber;
    private ImageView profileImage;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        textMail = view.findViewById(R.id.eMail);
        textNumber = view.findViewById(R.id.telNumber);
        profileImage = view.findViewById(R.id.profilePicture);

        auth = FirebaseAuth.getInstance();
        FirebaseUser theUser = auth.getCurrentUser();

        if (theUser == null) {
            textMail.setText("No Login");
            textNumber.setText("No Login");
        } else {
            textMail.setText(theUser.getEmail());
            textNumber.setText(theUser.getPhoneNumber());
            if (theUser.getPhotoUrl() != null)
                Picasso.get().load(theUser.getPhotoUrl()).into(profileImage);
            else
                profileImage.setImageDrawable(getResources().getDrawable(R.drawable.profile_icon_black));

        }

        logout = view.findViewById(R.id.logout);
        addListener(logout);

        return view;
    }

    private void addListener(Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout(view, view.getContext());
            }
        });
    }

    public void logout(View view, Context context) {

        FirebaseAuth.getInstance().signOut();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Toast.makeText(context.getApplicationContext(), "still not null", Toast.LENGTH_SHORT);

        }
        startActivity(new Intent(context, RegisterActivity.class));

    }

}
