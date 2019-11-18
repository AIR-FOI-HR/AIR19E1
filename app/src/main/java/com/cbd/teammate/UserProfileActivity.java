package com.cbd.teammate;

import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class UserProfileActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private TextView textMail ;
    private TextView textNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        textMail = findViewById(R.id.eMail);
        textNumber = findViewById(R.id.telNumber);

        auth = FirebaseAuth.getInstance();
        FirebaseUser theUser = auth.getCurrentUser();

        if (theUser == null){
            textMail.setText("No Login");
            textNumber.setText("No Login");
        }
        else {
            textMail.setText(theUser.getEmail());
            textNumber.setText(theUser.getPhoneNumber());

        }

    }
}
