package com.cbd.teammate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cbd.maps.LocationProvider;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserInfo;

public class SignedActivity extends AppCompatActivity {
    private TextView nameofuser;


    private final static int ALL_PERMISSIONS_RESULT = 101;
    LocationProvider lp;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case ALL_PERMISSIONS_RESULT:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this,
                            "This app requires location permissions to be granted",
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
    }



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

        lp = new LocationProvider();
        lp.setup(this, this);
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

    @Override
    protected void onResume() {
        super.onResume();

        lp.resumeLocationUpdates();
    }

    @Override
    protected void onPause() {
        super.onPause();

        lp.pauseLocationUpdates();
    }

}
