package com.example.memomia;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;

public class Stats extends Fragment {

    protected DatabaseReference dbr;
    private String userId = Access.userEmail;

    private static final String SET_LABEL = "my mood";
    private static final String SET_LABEL2 = "my tags";
    private static String[] DAYS = {"   ","   ","   ","   ","   "};
    private int[] mood = new int[5];
    private HashMap<String, Integer> tags = new HashMap<String, Integer>();

    private BarChart chart;
    private BarChart chart2;
    private TextView text2;
    private TextView text3;
    private TextView text4;
    private TextView text5;
    private TextView text6;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stats, container, false);

        chart = view.findViewById(R.id.fragment_verticalbarchart_chart);
        chart2 = view.findViewById(R.id.fragment_verticalbarchart_chart2);


        ArrayList<BarEntry> values = new ArrayList<>();

        dbr = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = dbr.child("JournalEntry");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                mood = new int[5];
                for(DataSnapshot val : dataSnapshot.getChildren()){
                    if(val.child("userId").getValue(String.class).equals(userId)){
                        String moodinput = val.child("mood").getValue().toString();
                        switch (moodinput) {
                            case "1":
                                mood[0] ++;
                                break;
                            case "2":
                                mood[1] ++;
                                break;
                            case "3":
                                mood[2] ++;
                                break;
                            case "4":
                                mood[3] ++;
                                break;
                            case "5":
                                mood[4] ++;
                                break;
                        }
                    }
                }
                for (int i = 0; i < 5; i++) {
                    values.add(new BarEntry(i, mood[i]));
                }

                BarDataSet set1 = new BarDataSet(values, SET_LABEL);

                ArrayList<IBarDataSet> dataSets = new ArrayList<>();
                dataSets.add(set1);
                BarData data = new BarData(dataSets);
                configureChartAppearance();
                prepareChartData(data);
            }
            public void onCancelled(DatabaseError firebaseError) {
                Log.e(TAG, "Failed to read text from database", firebaseError.toException());
            }
        });


        text2 = view.findViewById(R.id.textView2);
        text3 = view.findViewById(R.id.textView3);
        text4 = view.findViewById(R.id.textView4);
        text5 = view.findViewById(R.id.textView5);
        text6 = view.findViewById(R.id.textView6);
        ArrayList<BarEntry> values2 = new ArrayList<>();
        DatabaseReference reftag = dbr.child("JournalEntry");
        reftag.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                tags.clear();
                for(DataSnapshot val : dataSnapshot.getChildren()){
                    if(val.child("userId").getValue(String.class).equals(userId)){
                        if (val.child("tags").exists()) {
                            String tagsArray = val.child("tags").getValue().toString();
                            StringTokenizer tagsToUse = new StringTokenizer(tagsArray);
                            while (tagsToUse.hasMoreTokens()) {
                                String check = tagsToUse.nextToken();
                                if (tags.containsKey(check)) {
                                    tags.put(check,tags.get(check)+1);
                                } else {
                                    tags.put(check,1);
                                }
                            }
                        }
                    }
                }
                ArrayList<String> mostUsedTags = new ArrayList<String>();
                ArrayList<Integer> mostUsedTagsCount = new ArrayList<Integer>();
                int count = 0;
                HashMap<String, Integer> anotherTags  = new HashMap<String,Integer>(tags);
                for (Map.Entry<String, Integer> entry : tags.entrySet()) {
                    int maxValueMap1 = (Collections.max(anotherTags.values()));
                    if ((entry.getValue()==maxValueMap1) && (count < 5)) {
                        mostUsedTags.add(entry.getKey());
                        mostUsedTagsCount.add(entry.getValue());
                        anotherTags.remove(entry.getKey());
                        count ++;
                    } else {
                       if (count == 5) {break;}
                    }
                }
                if (mostUsedTags.size() >= 5) {
                    for (int i = 0; i < 5; i++) {
                        values2.add(new BarEntry(i, mostUsedTagsCount.get(i)));
                    }
                } else {
                    for (int i = 0; i < mostUsedTags.size(); i++) {
                        values2.add(new BarEntry(i, mostUsedTagsCount.get(i)));
                    }
                    for (int i = mostUsedTags.size(); i < 5; i++) {
                        values2.add(new BarEntry(i, 0));
                        mostUsedTags.add("NULL");
                    }
                }
                text2.setText(mostUsedTags.get(0));
                text3.setText(mostUsedTags.get(1));
                text4.setText(mostUsedTags.get(2));
                text5.setText(mostUsedTags.get(3));
                text6.setText(mostUsedTags.get(4));

                BarDataSet set2 = new BarDataSet(values2, SET_LABEL2);

                ArrayList<IBarDataSet> dataSets2 = new ArrayList<>();
                dataSets2.add(set2);
                BarData data2 = new BarData(dataSets2);
                configureChartAppearance2();
                prepareChartData2(data2);
            }
            public void onCancelled(DatabaseError firebaseError) {
                Log.e(TAG, "Failed to read text from database", firebaseError.toException());
            }
        });


        return view;
    }

    private void configureChartAppearance() {
        chart.getDescription().setEnabled(false);
        chart.setDrawValueAboveBar(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return DAYS[(int) value];
            }
        });

        YAxis axisLeft = chart.getAxisLeft();
        axisLeft.setGranularity(10f);
        axisLeft.setAxisMinimum(0);

        YAxis axisRight = chart.getAxisRight();
        axisRight.setGranularity(10f);
        axisRight.setAxisMinimum(0);
    }
    private void prepareChartData(BarData data) {
        data.setValueTextSize(12f);
        chart.setData(data);
        chart.invalidate();
    }

    private void configureChartAppearance2() {
        chart2.getDescription().setEnabled(false);
        chart2.setDrawValueAboveBar(false);

        XAxis xAxis = chart2.getXAxis();
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return DAYS[(int) value];
            }
        });

        YAxis axisLeft = chart2.getAxisLeft();
        axisLeft.setGranularity(10f);
        axisLeft.setAxisMinimum(0);

        YAxis axisRight = chart2.getAxisRight();
        axisRight.setGranularity(10f);
        axisRight.setAxisMinimum(0);
    }
    private void prepareChartData2(BarData data) {
        data.setValueTextSize(12f);
        chart2.setData(data);
        chart2.invalidate();
    }

}
