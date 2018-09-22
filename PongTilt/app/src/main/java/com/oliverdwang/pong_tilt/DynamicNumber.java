package com.oliverdwang.pong_tilt;

public class DynamicNumber {

    private float mValue;
    private DynamicNumberListener mListener;

    //Create dynamic number object
    public DynamicNumber(float initialValue) {
        mValue = initialValue;
    }

    //Set listener for stored value
    public void setListener(DynamicNumberListener listener) {
        mListener = listener;
    }

    //Change stored value
    public void setValue(float newValue) {
        mValue = newValue;
        if (mListener != null) {
            mListener.onValueChanged(mValue);
        }
    }

    //Return stored value
    public float getValue() {
        return mValue;
    }

    //Listener for when value changes
    public static interface DynamicNumberListener {
        void onValueChanged(float newValue);
    }
}
