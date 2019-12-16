package com.cbd.teammate.holders;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cbd.database.entities.Venue;
import com.cbd.teammate.R;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import de.hdodenhof.circleimageview.CircleImageView;

public class VenuesViewHolder extends RecyclerView.ViewHolder {

    private View view;
    CircleImageView image;
    private TextView venueName;
    private TextView venueLatitude;
    private TextView venueLongitude;
    private RelativeLayout parentLayout;

    public VenuesViewHolder(@NonNull View itemView) {
        super(itemView);

        view = itemView;
    }

    public void setDetails(String nameVenue, Double latitude, Double longitude, String pictureReference) {

        String finalUrl = "http://i.imgur.com/DvpvklR.png";
        try {
           finalUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + URLEncoder.encode(pictureReference, "UTF-8") + "&key=" + URLEncoder.encode(view.getResources().getString(R.string.api_key), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        venueName = view.findViewById(R.id.search_venue_name);
        venueLatitude = view.findViewById(R.id.search_latitude_value);
        venueLongitude = view.findViewById(R.id.search_longitude_value);
        CircleImageView imageView = view.findViewById(R.id.venueImage);
        Picasso.get().load(finalUrl).into(imageView);

        parentLayout = itemView.findViewById(R.id.parentLayout);

        venueName.setText(nameVenue);
        venueLatitude.setText(latitude.toString());
        venueLongitude.setText(longitude.toString());

    }
}

