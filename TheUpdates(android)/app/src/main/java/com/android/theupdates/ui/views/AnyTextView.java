package com.android.theupdates.ui.views;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.TextView;

public class AnyTextView extends TextView {

    public AnyTextView(Context context) {
        super(context);
    }

    public AnyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (!isInEditMode()) {
            Util.setTypeface(attrs, this);
        }
    }

    public AnyTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        if (!isInEditMode()) {
            Util.setTypeface(attrs, this);
        }
        
        
        
    	//initTextWatcher(); //
    }

	private void initTextWatcher() {
		TextWatcher m = new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count ) {
				// TODO Auto-generated method stub
				setError( null );
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after ) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged( Editable s ) {
				// TODO Auto-generated method stub
				
			}
		};
		
		
		addTextChangedListener( m );
	}
    

    @Override
    public void addTextChangedListener( TextWatcher watcher ) {
    	
    	

    	
		 
		
	super.addTextChangedListener( watcher );
}
    
}
