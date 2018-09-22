package com.oliverdwang.pong_tilt;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

public class DynamicTextView extends AppCompatTextView implements DynamicNumber.DynamicNumberListener {

    public DynamicTextView(Context context) {
        super(context);
    }

    public DynamicTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void onValueChanged(float newValue) {
        setText(Float.toString(newValue));
    }
}
