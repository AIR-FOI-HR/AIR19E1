package com.cbd.teammate.fragments;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Client;
import com.algolia.search.saas.CompletionHandler;
import com.algolia.search.saas.Index;
import com.algolia.search.saas.Query;
import com.cbd.database.entities.Venue;
import com.cbd.maps.LocationProvider;
import com.cbd.teammate.R;
import com.cbd.teammate.holders.VenuesViewHolder;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    private FirestoreRecyclerAdapter<Venue, VenuesViewHolder> firestoreRecyclerAdapter;

    private EditText toSearch;
    private RecyclerView recyclerList;
    private LocationProvider lp;
    private View view;

    public SearchFragment() {
        // Required empty public constructor
    }

    public SearchFragment(LocationProvider lp) {
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

        this.algoliaSearch();
        return view;
    }


    private void algoliaSearch() {
        Client client = new Client(view.getResources().getString(R.string.algolia_app_id), view.getResources().getString(R.string.algolia_api_key));
        Index index = client.getIndex(view.getResources().getString(R.string.algolia_index));

        this.toSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable ed) {
                Query query = new Query(ed.toString())
                        .setAttributesToRetrieve("name")
                        .setHitsPerPage(50);
                index.searchAsync(query, new CompletionHandler() {
                    @Override
                    public void requestCompleted(@Nullable JSONObject jsonObject, @Nullable AlgoliaException e) {
                        Log.d("JSOBJECT", jsonObject.toString());
                        try {
                            JSONArray hits = jsonObject.getJSONArray("hits");
                            List<String> list = new ArrayList<>();

                            Log.w("testik", "hodnota = " + hits.length() );

                            for (int i = 0; i < hits.length(); i++){
                                JSONObject object1 = hits.getJSONObject(i);
                                String name = object1.getString("name");
                                list.add(name);
                            }
                            // currently displays only first 10 results due to Firebase limitation
                            if (hits.length() != 0 && hits.length() <= 10) {
                            FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
                            com.google.firebase.firestore.Query query;
                            query = rootRef
                                    .collection("venues")
                                    .whereIn("name", list)
                                    .orderBy("name", com.google.firebase.firestore.Query.Direction.ASCENDING);

                            FirestoreRecyclerOptions<Venue> options = new FirestoreRecyclerOptions.Builder<Venue>()
                                    .setQuery(query, Venue.class)
                                    .build();

                            firestoreRecyclerAdapter
                                    = new FirestoreRecyclerAdapter<Venue, VenuesViewHolder>(options) {
                                @Override
                                protected void onBindViewHolder(@NonNull VenuesViewHolder holder, int position, @NonNull Venue model) {
                                    try {
                                        holder.setDetails(model.getName(), model.getLatitude(), model.getLongitude(), model.getPictureReference(), lp.getLatLng());
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


                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }
                    }
                });

            }
        });

    }

    private void createOnClickListener(ImageButton button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!toSearch.getText().toString().trim().isEmpty()) {
                    //firebaseVenueSearch(toSearch.getText().toString());
                }
            }
        });
    }

  /*  private void firebaseVenueSearch(String input) {
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
                    holder.setDetails(model.getName(), model.getLatitude(), model.getLongitude(), model.getPictureReference(), lp.getLatLng());
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
*/
}

