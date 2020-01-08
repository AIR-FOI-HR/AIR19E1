package com.cbd.core;

import android.view.View;

import com.cbd.database.entities.Venue;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

public interface VenueUtil {
    void onClickListener(View view, Venue model);

    void setFirestoreRecyclerAdapter(FirestoreRecyclerOptions<Venue> options);

    Query createQuery();

    FirestoreRecyclerOptions<Venue> setOptions(Query query);
}
