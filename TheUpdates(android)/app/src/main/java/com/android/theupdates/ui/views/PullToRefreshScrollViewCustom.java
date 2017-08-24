package com.android.theupdates.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class PullToRefreshScrollViewCustom extends ScrollView {

	private OnScrollListener onScrollListener = null;

	public PullToRefreshScrollViewCustom(Context context) {
		super(context, null);
	}

	public PullToRefreshScrollViewCustom(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setOnScrollListener(OnScrollListener onScrollListener) {
		this.onScrollListener = onScrollListener;
	}

	@Override
	protected void onScrollChanged(int x, int y, int oldx, int oldy) {
		super.onScrollChanged(x, y, oldx, oldy);
		if (onScrollListener != null) {
			onScrollListener.onScrollChanged(x, y, oldx, oldy);
		}
	}

	public interface OnScrollListener {
		void onScrollChanged(int x, int y, int oldx, int oldy);
	}
}
