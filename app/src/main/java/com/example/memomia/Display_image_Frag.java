package com.example.memomia;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.ArrayList;

public class Display_image_Frag extends Fragment {
    ViewPagerAdapter mViewPagerAdapter;
    ViewPager mViewPager;
    protected ArrayList<String> images;
    private Display_activity parent;
    protected ImageButton edit;

    public Display_image_Frag(Display_activity act) {
        this.parent = act;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_display_image_, container, false);

        // Initializing the ViewPager Object
        mViewPager = view.findViewById(R.id.viewPagerMain);

        images = Display_activity.listUris;
        // Initializing the ViewPagerAdapter
        mViewPagerAdapter = new ViewPagerAdapter(getActivity(), images);

        // Adding the Adapter to the ViewPager
        mViewPager.setAdapter(mViewPagerAdapter);
        return view;
    }
}