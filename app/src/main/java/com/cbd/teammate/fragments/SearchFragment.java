package com.cbd.teammate.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    private FirestoreRecyclerAdapter<Venue, VenuesViewHolder> firestoreRecyclerAdapter;

    private EditText toSearch;
    private RecyclerView recyclerList;

    private View view;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_search, container, false);

        toSearch = view.findViewById(R.id.to_search);
        ImageButton searchButton = view.findViewById(R.id.search_button);

        recyclerList = view.findViewById(R.id.result_list);
        recyclerList.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerList.setHasFixedSize(true);

        createOnClickListener(searchButton);

        return view;
    }

    private void createOnClickListener(ImageButton button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!toSearch.getText().toString().trim().isEmpty()) {
                    firebaseVenueSearch(toSearch.getText().toString());
                }
            }
        });
    }

    private void firebaseVenueSearch(String input) {
        Query query = FirebaseFirestore.getInstance()
                .collection("venues")
                .whereEqualTo("name", input)
                .limit(20);

        FirestoreRecyclerOptions<Venue> options = new FirestoreRecyclerOptions.Builder<Venue>()
                .setQuery(query, Venue.class)
                .build();

        firestoreRecyclerAdapter
                = new FirestoreRecyclerAdapter<Venue, VenuesViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull VenuesViewHolder holder, int position, @NonNull Venue model) {
                try {
                    holder.setDetails(model.getName(), model.getLatitude(), model.getLongitude(), model.getPictureReference());
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
        firestoreRecyclerAdapter.startListening();
        recyclerList.setAdapter(firestoreRecyclerAdapter);
    }

}

