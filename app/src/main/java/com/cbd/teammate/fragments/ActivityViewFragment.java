package com.cbd.teammate.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.cbd.database.entities.Activity;
import com.cbd.database.entities.Player;
import com.cbd.teammate.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActivityViewFragment extends Fragment {

    private Activity activity;
    private View view;
    private String venueName;
    private FirebaseFirestore db;
    private String pictureReference;

    public ActivityViewFragment(Activity activity, String venueName, String pictureRefernece) {
        this.activity = activity;
        this.venueName = venueName;
        this.pictureReference = pictureRefernece;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_activity_view, container, false);

        db = FirebaseFirestore.getInstance();

        setData();

        return view;
    }

    private String createPictureUrl() {
        String finalUrl;
        try {
            finalUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + URLEncoder.encode(this.pictureReference, "UTF-8") + "&key=AIzaSyA5SObTwWEGnFkubedir0EkJu40WGwDAzo";
        } catch (Throwable ee) {
            finalUrl = "http://i.imgur.com/DvpvklR.png";
        }
        return finalUrl;
    }
    private void setData() {
        ImageView venueImage = view.findViewById(R.id.activity_view_image);
        TextView sportName = view.findViewById(R.id.activity_sport);
        TextView venueName = view.findViewById(R.id.activity_venue);
        TextView activityDate = view.findViewById(R.id.activity_date);
        TextView activityPlayerType = view.findViewById(R.id.activity_type_players);
        TextView activityPrice = view.findViewById(R.id.activity_price);
        CircleImageView creatorImage = view.findViewById(R.id.creator_image);
        TextView creatorName = view.findViewById(R.id.creator_name);
        TextView creatorPhone = view.findViewById(R.id.creator_phone);
        ListView playerList = view.findViewById(R.id.activity_list_players);
        Button signupButton = view.findViewById(R.id.activity_signup_button);

        Picasso.get().load(createPictureUrl()).into(venueImage);
        sportName.setText(activity.getSport());
        venueName.setText(this.venueName);
        activityDate.setText(activity.getDate());
        activityPlayerType.setText(activity.getPlayerType());
        activityPrice.setText(activity.getPrice().toString() + "kn");

        if (activity.getPlayers().size() == activity.getPlayersNeeded()
                && !signupButton.isEnabled()) {
            signupButton.setEnabled(false);
        }

        db.collection("players")
                .whereEqualTo(FieldPath.documentId(), activity.getCreatorId()).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<Player> p = queryDocumentSnapshots.toObjects(Player.class);
                        if (!p.isEmpty()) {
                            Picasso.get().load(p.get(0).getPhotoLink()).into(creatorImage);
                            creatorName.setText(p.get(0).getName());
                            creatorPhone.setText(p.get(0).getPhone());
                        }
                    }
                });

        String useruid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.collection("players")
                .whereIn(FieldPath.documentId(), activity.getPlayers()).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<Player> players = queryDocumentSnapshots.toObjects(Player.class);
                        List<String> playerNames = new ArrayList<>();

                        for (Player p : players) {
                            playerNames.add(p.getName());

                            if (p.getUid().equals(useruid)) {
                                signupButton.setEnabled(false);
                            }
                        }

                        if (playerNames.isEmpty())
                            playerNames.add("No players yet!");

                        playerList.setAdapter(new ArrayAdapter<>(view.getContext(),
                                android.R.layout.simple_list_item_1,
                                playerNames));
                    }
                });
    }
}
