package com.cbd.teammate.holders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cbd.teammate.R;

public class VenuesViewHolder extends RecyclerView.ViewHolder {

    View view;

    public VenuesViewHolder(@NonNull View itemView) {
        super(itemView);

        view = itemView;
    }

    public void setDetails(String venueName, Double latitude, Double longitude) {
        TextView textViewVenueName = view.findViewById(R.id.search_venue_name);
        TextView textViewLatitude = view.findViewById(R.id.search_latitude_value);
        TextView textViewLongitude = view.findViewById(R.id.search_longitude_value);

        textViewVenueName.setText(venueName);
        textViewLatitude.setText(latitude.toString());
        textViewLongitude.setText(longitude.toString());
    }
}
