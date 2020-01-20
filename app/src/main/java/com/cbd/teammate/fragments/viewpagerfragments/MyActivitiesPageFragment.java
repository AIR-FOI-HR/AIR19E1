package com.cbd.teammate.fragments.viewpagerfragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cbd.teammate.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyActivitiesPageFragment extends Fragment {

    private View view;

    public MyActivitiesPageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_activities_page, container, false);
        return view;
    }

}
