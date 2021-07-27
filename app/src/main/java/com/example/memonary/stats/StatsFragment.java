package com.example.memonary.stats;

import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import android.provider.UserDictionary;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.memonary.MainActivity;
import com.example.memonary.R;
import com.example.memonary.dictionary.WordWrapper;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.renderer.BarChartRenderer;
import com.github.mikephil.charting.renderer.XAxisRenderer;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class StatsFragment extends Fragment {

    private static final String[] MONTHS = new String[]{
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    };

    private BarChart chart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_stats, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI(view);
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).liveWords.observe(getViewLifecycleOwner(), new Observer<List<WordWrapper>>() {
                @Override
                public void onChanged(List<WordWrapper> wordWrappers) {
                    setData(wordWrappers);
                }
            });
        }
    }

    private void initUI(View root) {
        chart = root.findViewById(R.id.chart);

        chart.setTouchEnabled(false);
        chart.setClickable(false);

        chart.getLegend().setEnabled(false);
        chart.getDescription().setEnabled(false);

        chart.getXAxis().setDrawGridLines(false);
        chart.getXAxis().setDrawAxisLine(false);

        chart.getAxisLeft().setEnabled(false);
        chart.getAxisRight().setEnabled(false);
        chart.setRenderer(new BarChartRenderer(chart, chart.getAnimator(), chart.getViewPortHandler()) {
            @Override
            public void drawValue(Canvas c, String valueText, float x, float y, int color) {
                c.save();
                c.translate(0, -5);
//                c.rotate(90, x, y);
                super.drawValue(c, valueText, x, y, color);
                c.restore();
            }
        });

        chart.setExtraBottomOffset(32);


        XAxis xAxis = chart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setCenterAxisLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelRotationAngle(90);

        //Here pass the words list to draw the chart
//            setData(DummyStatsProvider.createDummyStats());

    }

    @Override
    public void onResume() {
        super.onResume();
        HashMap<String, WordWrapper> map = MainActivity.savedWords;
        if (MainActivity.savedWords != null) {
            System.out.println(map.size());
            for (String s : map.keySet()) {
                //if (map.get(s).getState().equals("LEARNED")) {
                System.out.println(s + "\t" + MainActivity.savedWords.get(s).getDateStart());
                //}
            }
        }
    }

    private void setData(List<WordWrapper> words) {
        Collections.sort(words, new WordWrapper.DateComprator());
        TreeMap<String, Integer> monthToWordCount = new TreeMap<>();
        for (WordWrapper word : words) {
            String key = "" + (1900 + word.getDateStart().getYear()) +
                    ((word.getDateStart().getMonth() + 1) < 10 ? "0" : "")
                    + (word.getDateStart().getMonth() + 1);
            Integer value = monthToWordCount.get(key);
            if (value == null) {
                value = 0;
            }
            value += 1;
            monthToWordCount.put(key, value);
        }

        ArrayList<BarEntry> values = new ArrayList<>();
        int count = 0;
        for (Map.Entry<String, Integer> entry : monthToWordCount.entrySet()) {
            count++;
            values.add(new BarEntry(count, entry.getValue()));
        }


        BarDataSet dataSet = new BarDataSet(values, null);
        dataSet.setColor(ContextCompat.getColor(getActivity(), R.color.purple_500));
        BarData data = new BarData(dataSet);
        data.setValueTextSize(10f);
        data.setBarWidth(0.9f);
        data.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.valueOf((int) value);
            }

        });
        chart.setData(data);
        ArrayList<String> labels = new ArrayList<>();
        for (String ym : monthToWordCount.keySet()) {
            String year = ym.substring(0, 4);
            String month = ym.substring(4);
            if (month.charAt(0) == '0') {
                month = month.substring(1);
            }
            int monthIndex = Integer.parseInt(month) - 1;
            labels.add(MONTHS[monthIndex] + " " + year);
        }
        chart.getXAxis().setLabelCount(labels.size());
        chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels) {

        });
        chart.invalidate();
    }
}