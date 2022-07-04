package com.example.memomia;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MyTags extends Fragment {

    private MainActivity myact;
    Context cntx;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myview = inflater.inflate(R.layout.fragment_my_tags, container, false);

        cntx = getActivity().getApplicationContext();
        myact = (MainActivity) getActivity();
        myact.getSupportActionBar().setTitle("My Tags");

        return myview;
    }
}