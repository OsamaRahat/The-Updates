package com.android.theupdates.ui.views;



import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

public class AnyEditTextView extends EditText {

    public AnyEditTextView(Context context) {
        super(context);
    }

    public AnyEditTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (!isInEditMode()) {
            Util.setTypefaceEdtTxt( attrs, this);
        }
    }

    public AnyEditTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        if (!isInEditMode()) {
            Util.setTypefaceEdtTxt(attrs, this);
        }
    }
}
