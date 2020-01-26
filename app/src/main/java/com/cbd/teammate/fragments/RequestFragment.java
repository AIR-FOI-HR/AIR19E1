package com.cbd.teammate.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.cbd.database.entities.Activity;
import com.cbd.database.entities.Player;
import com.cbd.database.entities.Request;
import com.cbd.teammate.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class RequestFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private EditText desc;
    private String descText;
    private String level;
    private Spinner spinnerLevel;
    private Button signUpBut;
    private Activity activity;
    private String uId;
    private Player player;
    private boolean alreadyApplied;

    public RequestFragment(Activity activity, Player player) {
        this.activity = activity;
        this.uId = activity.getCreatorId();
        this.player = player;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_request, container, false);
        alreadyApplied = false;
        desc = view.findViewById(R.id.description_request);
        spinnerLevel = view.findViewById(R.id.skill_level);
        signUpBut = view.findViewById(R.id.sign_to_activity);
        level = "Amateur";


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.player_levels, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLevel.setAdapter(adapter);
        spinnerLevel.setOnItemSelectedListener(this);
        addListener(signUpBut);
        return view;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        level = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), level, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void addListener(Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addRequest();
            }
        });
    }

    public com.google.firebase.firestore.Query createQuery() {
        return FirebaseFirestore.getInstance()
                .collection("requests")
                .whereEqualTo("activity", this.activity)
                .whereEqualTo("player", this.player);
    }

    private void DuplicateCheck() {
        com.google.firebase.firestore.Query query1 = createQuery();
        query1.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    alreadyApplied = true;
                }
            }
        });
    }

    private void addRequest() {
        if (desc.getText().toString().length() > 20) {
            DuplicateCheck();
            if (!alreadyApplied) {
                this.descText = desc.getText().toString();
                this.level = spinnerLevel.getSelectedItem().toString();
                Request request = new Request(uId, level, descText, Boolean.FALSE, activity, player);
                        FirebaseFirestore.getInstance().collection("requests")
                        .add(request);
            }
        } else {
            Toast.makeText(getContext(), "Enter description minimum 20 letters long", Toast.LENGTH_SHORT).show();

        }


    }
}

