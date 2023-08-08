
package com.schoofi.utils;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.schoofi.activitiess.R;

import java.text.DecimalFormat;

/**
 * This IValueFormatter is just for convenience and simply puts a "%" sign after
 * each value. (Recommeded for PieChart)
 *
 * @author Philipp Jahoda
 */
public class PercentFormatter1 implements IValueFormatter, IAxisValueFormatter
{

    protected DecimalFormat mFormat;
    String Rs;

    public PercentFormatter1(String Rs) {
        this.Rs = Rs;
        mFormat = new DecimalFormat("###,###,###.##");
    }

    /**
     * Allow a custom decimalformat
     *
     * @param format
     */
    public PercentFormatter1(DecimalFormat format) {
        this.mFormat = format;
    }

    // IValueFormatter
    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {


        return Rs+mFormat.format(value);
    }

    // IAxisValueFormatter
    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return Rs+mFormat.format(value);
    }

    public int getDecimalDigits() {
        return 1;
    }
}
