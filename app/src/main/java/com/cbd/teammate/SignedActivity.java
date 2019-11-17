package com.cbd.teammate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserInfo;

public class SignedActivity extends AppCompatActivity {
    private TextView nameofuser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signed);
        try {
            String uid = FirebaseAuth.getInstance().getCurrentUser().getEmail();
            nameofuser = (TextView) findViewById(R.id.user_name);
            nameofuser.setText(uid);


        } catch (NullPointerException e) {
            nameofuser = (TextView) findViewById(R.id.user_name);
            nameofuser.setText("null");
            FirebaseAuth.getInstance().signOut();

            startActivity(new Intent(this, RegisterActivity.class));
            finish();
        }
    }

    public void onLogout(View view) {

        FirebaseAuth.getInstance().signOut();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Toast.makeText(getApplicationContext(), "still not null", Toast.LENGTH_SHORT);

        }
        startActivity(new Intent(this, RegisterActivity.class));
        finish();
      //  FirebaseAuth.getInstance().signOut();
        //finish();
    }
}
