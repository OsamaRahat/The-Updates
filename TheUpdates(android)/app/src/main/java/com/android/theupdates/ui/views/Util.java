package com.android.theupdates.ui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import com.android.theupdates.R;

import java.util.HashMap;
import java.util.Map;


public class Util {
	public static Map<String, Typeface> typefaceCache = new HashMap<String, Typeface>();

	public static void setTypeface(AttributeSet attrs, TextView textView) {
		Context context = textView.getContext();

		TypedArray values = context.obtainStyledAttributes(attrs,
				R.styleable.AnyTextView);
		String typefaceName = values
				.getString(R.styleable.AnyTextView_typeface);

		if(typefaceName == null)
			return;
        if(typefaceName.isEmpty())
            return;
		
		if (typefaceCache.containsKey(typefaceName)) {
			textView.setTypeface(typefaceCache.get(typefaceName));
		} else {
			Typeface typeface;
			try {
				typeface = Typeface.createFromAsset(textView.getContext()
						.getAssets(),
						context.getString(R.string.assets_fonts_folder)
								+ typefaceName);
			} catch (Exception e) {
				Log.v(context.getString(R.string.app_name), String.format(
						context.getString(R.string.typeface_not_found),
						typefaceName));
				return;
			}

			typefaceCache.put(typefaceName, typeface);
			textView.setTypeface(typeface);
		}
	}
	
	public static void setTypefaceEdtTxt(AttributeSet attrs, TextView textView) {
		Context context = textView.getContext();

		TypedArray values = context.obtainStyledAttributes(attrs,
				R.styleable.AnyEditTextView);
		String typefaceName = values
				.getString(R.styleable.AnyEditTextView_typefaceEdtTxt);

        if(typefaceName == null)
            return;
        if(typefaceName.isEmpty())
            return;
		
		if (typefaceCache.containsKey(typefaceName)) {
			textView.setTypeface(typefaceCache.get(typefaceName));
		} else {
			Typeface typeface;
			try {
				typeface = Typeface.createFromAsset(textView.getContext()
						.getAssets(),
						context.getString(R.string.assets_fonts_folder)
								+ typefaceName);
			} catch (Exception e) {
				Log.v(context.getString(R.string.app_name), String.format(
						context.getString(R.string.typeface_not_found),
						typefaceName));
				return;
			}

			typefaceCache.put(typefaceName, typeface);
			textView.setTypeface(typeface);
		}
	}
	
	
}