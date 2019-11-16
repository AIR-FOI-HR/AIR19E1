package com.cbd.teammate;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cbd.maps.LocationProvider;

public class MainActivity extends AppCompatActivity {

    // These are Google Services needed to
    // obtain the location and update it
    private final static int ALL_PERMISSIONS_RESULT = 101;

    LocationProvider lp;

    Button button;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lp = new LocationProvider();
        lp.setup(this, this);

        textView = findViewById(R.id.location);
        button = findViewById(R.id.getlocation);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText(lp.getLatLng().toString());
            }
        });
    }

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
    protected void onResume() {
        super.onResume();

        lp.resumeLocationUpdates();
    }

    @Override
    protected void onPause() {
        super.onPause();

        lp.pauseLocationUpdates();
    }

    /*@Override
    protected void onStart() {
        super.onStart();

        if (lp.getmGoogleApiClient() != null) {
            lp.getmGoogleApiClient().connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (lp.getmGoogleApiClient() != null) {
            lp.getmGoogleApiClient().disconnect();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (lp.getmGoogleApiClient().isConnected())
            lp.getmGoogleApiClient().disconnect();

        lp.removeLocationUpdates();
    }*/

}
