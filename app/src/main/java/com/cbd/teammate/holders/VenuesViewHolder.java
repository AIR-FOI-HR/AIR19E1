package com.cbd.teammate.holders;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cbd.teammate.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class VenuesViewHolder extends RecyclerView.ViewHolder {

    View view;
    CircleImageView image;
    TextView venueName;
    TextView venueLatitude;
    TextView venueLongitude;
    RelativeLayout parentLayout;

    public VenuesViewHolder(@NonNull View itemView) {
        super(itemView);

        view = itemView;
    }

    public void setDetails(String nameVenue, Double latitude, Double longitude) {
        venueName = view.findViewById(R.id.search_venue_name);
        venueLatitude = view.findViewById(R.id.search_latitude_value);
        venueLongitude = view.findViewById(R.id.search_longitude_value);
        CircleImageView imageView = view.findViewById(R.id.venueImage);
        parentLayout= itemView.findViewById(R.id.parentLayout);

        venueName.setText(nameVenue);
        venueLatitude.setText(latitude.toString());
        venueLongitude.setText(longitude.toString());

    }
}

