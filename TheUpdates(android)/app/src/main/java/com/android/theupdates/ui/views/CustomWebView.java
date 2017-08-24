package com.android.theupdates.ui.views;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * Created by osamarahat on 02/08/2015.
 */
public class CustomWebView extends WebView {

    public CustomWebView(Context context) {
        super(context);
    }

    public CustomWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }



    @Override
    public boolean onCheckIsTextEditor() {
        return true;
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(true, direction, previouslyFocusedRect);
    }

    boolean layoutChangedOnce = false;
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (!layoutChangedOnce)
        {
            super.onLayout(changed, l, t, r, b);
            layoutChangedOnce = true;
        }
    }
}
