package com.cbd.teammate.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cbd.database.entities.Venue;
import com.cbd.teammate.R;
import com.cbd.teammate.holders.VenuesViewHolder;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

/**
 * A simple {@link Fragment} subclass.
 */
public class NearbyFragment extends Fragment {
    private View venuesView;
    private RecyclerView myVenuesView;
    private FirestoreRecyclerAdapter<Venue, VenuesViewHolder> firestoreRecyclerAdapter;
    private DatabaseReference venueRef;

    public NearbyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        venuesView = inflater.inflate(R.layout.fragment_nearby, container, false);

        myVenuesView = venuesView.findViewById(R.id.recycler_view);
        myVenuesView.setLayoutManager(new LinearLayoutManager(getContext()));
        myVenuesView.setHasFixedSize(true);

        requestVenues();

        return venuesView;
    }

    private void requestVenues() {
        Query query = FirebaseFirestore.getInstance()
                .collection("venues")
                .limit(20);
        FirestoreRecyclerOptions<Venue> options = new FirestoreRecyclerOptions.Builder<Venue>()
                .setQuery(query, Venue.class)
                .build();
        firestoreRecyclerAdapter
                = new FirestoreRecyclerAdapter<Venue, VenuesViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull VenuesViewHolder holder, int position, @NonNull Venue model) {
                try {
                    holder.setDetails(model.getName(), model.getLatitude(), model.getLongitude());
                } catch (Throwable oops) {
                    Toast.makeText(venuesView.getContext().getApplicationContext(), "Oops! Something went wrong, please try again.", Toast.LENGTH_SHORT).show();
                }

            }

            @NonNull
            @Override
            public VenuesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.list_layout, parent, false);
                return new VenuesViewHolder(view);
            }
        };
        myVenuesView.setAdapter(firestoreRecyclerAdapter);
        firestoreRecyclerAdapter.startListening();
    }

}