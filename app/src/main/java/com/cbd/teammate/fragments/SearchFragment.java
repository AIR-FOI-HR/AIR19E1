package com.cbd.teammate.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cbd.core.VenueUtil;
import com.cbd.database.entities.Venue;
import com.cbd.maps.LocationProvider;
import com.cbd.teammate.DistanceCalculator;
import com.cbd.teammate.R;
import com.cbd.teammate.holders.VenuesViewHolder;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment implements VenueUtil {

    private List<String> list;
    private HashMap<String, Double> listAll;
    private List<String> sortedVenueDistance;
    private FirestoreRecyclerAdapter<Venue, VenuesViewHolder> firestoreRecyclerAdapter;
    private EditText toSearch;
    private RecyclerView recyclerList;
    private LocationProvider lp;
    private View view;
    private DistanceCalculator calculator;

    public SearchFragment(LocationProvider lp) {
        this.calculator = new DistanceCalculator();
        this.lp = lp;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_search, container, false);

        toSearch = view.findViewById(R.id.to_search);
        recyclerList = view.findViewById(R.id.result_list);
        recyclerList.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerList.setHasFixedSize(true);

        return view;
    }


    @Override
    public void onClickListener(View view, Venue model) {
        view.setOnClickListener(view1 -> {
            FragmentTransaction fragmentTransaction = Objects.requireNonNull(getActivity())
                    .getSupportFragmentManager()
                    .beginTransaction();
            fragmentTransaction.replace(R.id.fragment_above_nav, new VenueViewFragment(model,lp));
            fragmentTransaction.commit();
        });
    }

    @Override
    public void setFirestoreRecyclerAdapter(FirestoreRecyclerOptions<Venue> options) {
        firestoreRecyclerAdapter = new FirestoreRecyclerAdapter<Venue, VenuesViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull VenuesViewHolder holder, int position, @NonNull Venue model) {
                try {
                    holder.setDetails(model.getName(), model.getLatitude(), model.getLongitude(), model.getPictureReference(), lp.getLatLng());
                    onClickListener(holder.itemView, model);
                } catch (Throwable oops) {
                    Toast.makeText(view.getContext().getApplicationContext(), "Oops! Something went wrong, please try again.", Toast.LENGTH_SHORT).show();
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
    public com.google.firebase.firestore.Query createQuery() {
        return FirebaseFirestore.getInstance()
                .collection("venues")
                //  .whereIn("name", this.sortedVenueDistance)
                .orderBy("name", com.google.firebase.firestore.Query.Direction.ASCENDING);
    }

    @Override
    public FirestoreRecyclerOptions<Venue> setOptions(com.google.firebase.firestore.Query query) {
        return new FirestoreRecyclerOptions.Builder<Venue>()
                .setQuery(query, Venue.class)
                .build();
    }


}

