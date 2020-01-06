package com.cbd.teammate.holders;

import android.util.Pair;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cbd.teammate.R;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import de.hdodenhof.circleimageview.CircleImageView;

public class VenuesViewHolder extends RecyclerView.ViewHolder {

    private View view;
    private TextView venueName;
    private TextView venueDistance;
    private RelativeLayout parentLayout;

    public VenuesViewHolder(@NonNull View itemView) {
        super(itemView);

        view = itemView;
    }

    public void setDetails(String nameVenue, Double latitude, Double longitude, String pictureReference, Pair<Double, Double> latLong) {

        String finalUrl = "http://i.imgur.com/DvpvklR.png";
        try {
            finalUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + URLEncoder.encode(pictureReference, "UTF-8") + "&key=" + URLEncoder.encode(view.getResources().getString(R.string.api_key), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        venueName = view.findViewById(R.id.search_venue_name);

        Double distance = calculateDistance(latitude, latLong.first, longitude, latLong.second);

        venueDistance = view.findViewById(R.id.search_distance_value);
        CircleImageView imageView = view.findViewById(R.id.venueImage);
        Picasso.get().load(finalUrl).into(imageView);

        parentLayout = itemView.findViewById(R.id.parentLayout);

        venueName.setText(nameVenue);
        venueDistance.setText(distance.toString().subSequence(0, 7) + " km");
    }

    private Double calculateDistance(Double lat1, Double lat2, Double lon1, Double lon2) {
        Double res;

        Double dLon = deg2rad(lon2 - lon1);
        Double dLat = deg2rad(lat2 - lat1);

        Double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        res = 6370 * c;

        return res;
    }

    private double deg2rad(double value) {
        return value * (Math.PI / 180);
    }
}

