package com.schoofi.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class CustomTextViewRobotto extends TextView{

    public CustomTextViewRobotto(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CustomTextViewRobotto(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomTextViewRobotto(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/RobotoCondensed-Bold.ttf");
        setTypeface(tf);
    }

}
