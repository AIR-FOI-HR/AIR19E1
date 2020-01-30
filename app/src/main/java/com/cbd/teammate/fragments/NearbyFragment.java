package com.cbd.teammate.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cbd.database.entities.Venue;
import com.cbd.maps.LocationProvider;
import com.cbd.teammate.DistanceCalculator;
import com.cbd.teammate.HashMapSort;
import com.cbd.teammate.R;
import com.cbd.teammate.holders.VenuesViewHolder;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class NearbyFragment extends Fragment implements NearbyRecyclerViewAdapter.ItemClickListener {
    private View venuesView;
    private RecyclerView myVenuesView;
    private FirestoreRecyclerAdapter<Venue, VenuesViewHolder> firestoreRecyclerAdapter;
    private LocationProvider lp;
    private ArrayList<String> toJson;
    private RecyclerView.Adapter adapter;
    private HashMap<Venue, Double> hashMapVenues;

    public NearbyFragment(LocationProvider lp) {
        this.lp = lp;
        this.toJson = new ArrayList<>();
        this.hashMapVenues = new HashMap();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        venuesView = inflater.inflate(R.layout.fragment_nearby, container, false);

        myVenuesView = venuesView.findViewById(R.id.recycler_view);
        myVenuesView.setLayoutManager(new LinearLayoutManager(getContext()));

        requestVenues();
        this.getAllVenues();


        RecyclerView recyclerView = venuesView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));


        adapter = new RecyclerView.Adapter<VenuesViewHolder>() {
            @NonNull
            @Override
            public VenuesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.card_layout, parent, false);

                return new VenuesViewHolder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull VenuesViewHolder holder, int position) {
                for (Venue model:hashMapVenues.keySet()) {

                    try {
                        holder.setDetails(model.getName(), model.getLatitude(), model.getLongitude(), model.getPictureReference(), lp.getLatLng());
                        onClickListener(holder.itemView, model);
                    } catch (Throwable oops) {
                        Toast.makeText(venuesView.getContext().getApplicationContext(),
                                "Oops! Something went wrong, please try again.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public int getItemCount() {
                return 0;
            }
        };
       // adapter.listene
        recyclerView.setAdapter(adapter);
        return venuesView;
    }

    private void requestVenues() {

    }


    public void onStart() {
        super.onStart();

    }


    public void onStop() {
        super.onStop();

    }


    public void onClickListener(View view, Venue model) {
        view.setOnClickListener(view1 -> {
            FragmentTransaction fragmentTransaction = Objects.requireNonNull(getActivity())
                    .getSupportFragmentManager()
                    .beginTransaction();
            fragmentTransaction.replace(R.id.fragment_above_nav, new VenueViewFragment(model,lp));
            fragmentTransaction.commit();
        });
    }


    private void getAllVenues() {
        FirebaseFirestore.getInstance()
                .collection("venues")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Log.d("QuerySuccess", document.getId() + " => " + document.getData());

                                toJson.add(new Gson().toJson(document.getData()));

                            }
                        } else {
                            Log.d("QuerySuccess", "Error getting documents: ", task.getException());
                        }
                        createObjects();
                    }
                });


    }

    private void createObjects() {

        DistanceCalculator distanceCalculator = new DistanceCalculator();
        Double distance;
        Venue currentVenue;

        Gson gson = new Gson();
        for (int i = 0; i < toJson.size(); i++) {
            currentVenue = gson.fromJson(toJson.get(i), Venue.class);
            Log.d("MyAnd", currentVenue.getName() + " " + currentVenue.getLatitude() + " " + currentVenue.getLongitude());
            distance = distanceCalculator.calculateDistance(
                    lp.getLatLng().first,
                    currentVenue.getLatitude(),
                    lp.getLatLng().second,
                    currentVenue.getLongitude());
            hashMapVenues.put(currentVenue, distance);
        }
        Log.d("HashValues", "HM: " + new Gson().toJson(hashMapVenues));
        hashMapVenues = HashMapSort.sort(hashMapVenues);

    }


    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(getContext(), "You clicked", Toast.LENGTH_SHORT).show();
    }
}