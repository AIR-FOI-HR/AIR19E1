package com.cbd.teammate.fragments;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cbd.database.entities.Activity;
import com.cbd.database.entities.Venue;
import com.cbd.teammate.R;
import com.cbd.teammate.holders.ActivityHolder;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class VenueViewFragment extends Fragment {

    private FirestoreRecyclerAdapter<Activity, ActivityHolder> firestoreActivityRecyclerAdapter;
    private RecyclerView activitiesList;
    private View view;
    private Venue venue;
    private FirebaseFirestore db;
    private String testPhoto = "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fi.pinimg.com%2Foriginals%2Fb0%2Ff6%2F33%2Fb0f633defac418a63ce25792bc881028.png&f=1&nofb=1";

    public VenueViewFragment(Venue venue) {
        this.venue = venue;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_venue, container, false);

        activitiesList = view.findViewById(R.id.venue_view_activities);
        activitiesList.setLayoutManager(new LinearLayoutManager(getContext()));

        initialiseDB();

        if (venue != null) {
            setViewAttributes();
        }

        return view;
    }

    private void initialiseDB() {
        db = FirebaseFirestore.getInstance();
    }

    private void setViewAttributes() {
        TextView name = view.findViewById(R.id.venue_view_name);
        TextView lat = view.findViewById(R.id.venue_view_latitude);
        TextView lon = view.findViewById(R.id.venue_view_longitude);
        ImageView image = view.findViewById(R.id.venue_view_image);

        configureVenueRecyclerView(venue.getActivities());

        name.setText(venue.getName());
        lat.setText(venue.getLatitude().toString());
        lon.setText(venue.getLongitude().toString());
        Picasso.get()
                .load(testPhoto).into(image);
    }

    private void configureVenueRecyclerView(List<String> activities) {

        if (activities != null) {

            Query query = db.collection("activities")
                    .whereIn(FieldPath.documentId(), activities);
            FirestoreRecyclerOptions<Activity> options = new FirestoreRecyclerOptions.Builder<Activity>()
                    .setQuery(query, Activity.class)
                    .build();

            firestoreActivityRecyclerAdapter =
                    new FirestoreRecyclerAdapter<Activity, ActivityHolder>(options) {
                        @Override
                        protected void onBindViewHolder(@NonNull ActivityHolder holder, int position, @NonNull Activity model) {
                            if (hasExpired(model.getDate())) {
                                holder.setDetails(model);
                                holder.itemView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        FragmentTransaction fragmentTransaction = getActivity()
                                                .getSupportFragmentManager()
                                                .beginTransaction();
                                        fragmentTransaction.replace(R.id.fragment_above_nav,
                                                new ActivityViewFragment(model, venue.getName(), testPhoto));
                                        fragmentTransaction.commit();
                                    }
                                });
                            }
                        }

                        @NonNull
                        @Override
                        public ActivityHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                            View view = LayoutInflater.from(parent.getContext())
                                    .inflate(R.layout.list_layout_venue, parent, false);

                            return new ActivityHolder(view);
                        }
                    };
            activitiesList.setAdapter(firestoreActivityRecyclerAdapter);
            firestoreActivityRecyclerAdapter.startListening();
        }
    }

    private boolean hasExpired(String dateString) {
        boolean res = false;

        try {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm");
            Date date = format.parse(dateString);

            if (date.after(new Date())) {
                res = true;
            }
        } catch (ParseException e) {
            Log.w("Parse exception", e.toString());
        } catch (Throwable e) {
            Log.w("Error", e.toString());
        }

        return res;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (firestoreActivityRecyclerAdapter != null) {
            firestoreActivityRecyclerAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (firestoreActivityRecyclerAdapter != null) {
            firestoreActivityRecyclerAdapter.stopListening();
        }
    }
}
