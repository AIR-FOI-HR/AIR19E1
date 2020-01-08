package com.cbd.teammate.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cbd.core.VenueUtil;
import com.cbd.database.entities.Venue;
import com.cbd.maps.LocationProvider;
import com.cbd.teammate.R;
import com.cbd.teammate.holders.VenuesViewHolder;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class NearbyFragment extends Fragment implements VenueUtil {
    private View venuesView;
    private RecyclerView myVenuesView;
    private FirestoreRecyclerAdapter<Venue, VenuesViewHolder> firestoreRecyclerAdapter;
    private LocationProvider lp;

    public NearbyFragment(LocationProvider lp) {
        this.lp = lp;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        venuesView = inflater.inflate(R.layout.fragment_nearby, container, false);

        myVenuesView = venuesView.findViewById(R.id.recycler_view);
        myVenuesView.setLayoutManager(new LinearLayoutManager(getContext()));

        requestVenues();

        return venuesView;
    }

    private void requestVenues() {
        Query query = createQuery();
        FirestoreRecyclerOptions<Venue> options = setOptions(query);
        setFirestoreRecyclerAdapter(options);
        myVenuesView.setAdapter(firestoreRecyclerAdapter);
        if (firestoreRecyclerAdapter != null)
            firestoreRecyclerAdapter.startListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (firestoreRecyclerAdapter != null) {
            firestoreRecyclerAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (firestoreRecyclerAdapter != null) {
            firestoreRecyclerAdapter.stopListening();
        }
    }

    @Override
    public void onClickListener(View view, Venue model) {
        view.setOnClickListener(view1 -> {
            FragmentTransaction fragmentTransaction = Objects.requireNonNull(getActivity())
                    .getSupportFragmentManager()
                    .beginTransaction();
            fragmentTransaction.replace(R.id.fragment_above_nav, new VenueViewFragment(model));
            fragmentTransaction.commit();
        });
    }

    @Override
    public void setFirestoreRecyclerAdapter(FirestoreRecyclerOptions<Venue> options) {
        firestoreRecyclerAdapter
                = new FirestoreRecyclerAdapter<Venue, VenuesViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull VenuesViewHolder holder,
                                            int position, @NonNull Venue model) {
                try {
                    holder.setDetails(model.getName(), model.getLatitude(), model.getLongitude(), model.getPictureReference(), lp.getLatLng());
                    onClickListener(holder.itemView, model);
                } catch (Throwable oops) {
                    Toast.makeText(venuesView.getContext().getApplicationContext(),
                            "Oops! Something went wrong, please try again.", Toast.LENGTH_SHORT).show();
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
    }

    @Override
    public Query createQuery() {
        return FirebaseFirestore.getInstance()
                .collection("venues")
                .limit(20);
    }

    @Override
    public FirestoreRecyclerOptions<Venue> setOptions(Query query) {
        return new FirestoreRecyclerOptions.Builder<Venue>()
                .setQuery(query, Venue.class)
                .build();
    }
}
