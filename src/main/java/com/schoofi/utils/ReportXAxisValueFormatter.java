package com.schoofi.utils;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Schoofi on 18-05-2018.
 */

public class ReportXAxisValueFormatter implements IAxisValueFormatter {

    public ArrayList<String> labels;
    public BarLineChartBase<?> chart;

    public ReportXAxisValueFormatter(BarLineChartBase<?> chart,ArrayList<String> labels) {
        this.chart = chart;
        this.labels = labels;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        try {
            int index = (int) value;
            return labels.get(index);
        } catch (Exception e) {
            return "";
        }
    }
}
