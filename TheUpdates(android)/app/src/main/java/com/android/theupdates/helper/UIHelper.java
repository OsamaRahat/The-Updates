package com.android.theupdates.helper;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils.TruncateAt;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.theupdates.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;


public class UIHelper {
	
	public static void showLongToastInCenter(Context ctx, int messageId ) {
		Toast toast = Toast.makeText( ctx, messageId, Toast.LENGTH_SHORT );
		toast.setGravity( Gravity.CENTER, 0, 0 );
		toast.show();
	}
	
	public static void showLongToastInCenter(Context ctx, String message ) {
		//message = Strings.nullToEmpty( message );
		Toast toast = Toast.makeText( ctx, message, Toast.LENGTH_SHORT );
		toast.setGravity( Gravity.CENTER, 0, 0 );
		toast.show();
	}

    public static void showLongToastInCenter(Context ctx, String message , boolean isLong) {
        //message = Strings.nullToEmpty( message );
        Toast toast = Toast.makeText( ctx, message, Toast.LENGTH_LONG );
        toast.setGravity( Gravity.CENTER, 0, 0 );
        toast.show();
    }
	
	public static String replace(Activity activity, Bitmap bmp, String paths) {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.JPEG, 70, bytes);
		String path = paths;
		File file = new File(path);
		try {
			file.createNewFile();
			FileOutputStream ostream = new FileOutputStream(file);
			ostream.write(bytes.toByteArray());
			ostream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return path;
	}
	
	public static void showConnectionFailedToast( Context ctx ) {
		showLongToastInCenter( ctx, R.string.msg_connection_failed );
	}
	
	public static void showConnectionErrorToast( Context ctx ) {
		showLongToastInCenter( ctx, R.string.msg_connection_error );
	}
	
	public static void hideSoftKeyboard(Context context, EditText editText ) {
		
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService( Context.INPUT_METHOD_SERVICE );
		imm.hideSoftInputFromWindow( editText.getWindowToken(), 0 );
		
	}
	
	public static void hideSoftKeyboard(Context context, View view ) {
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService( Context.INPUT_METHOD_SERVICE );
		
		if(view != null)
		imm.hideSoftInputFromWindow( view.getWindowToken(), 0 );
		
	}
	

	
	public static Rect locateView(View v ) {
		int[] loc_int = new int[2];
		if ( v == null )
			return null;
		try {
			v.getLocationOnScreen( loc_int );
		} catch ( NullPointerException npe ) {
			// Happens when the view doesn't exist on screen anymore.
			return null;
		}
		Rect location = new Rect();
		location.left = loc_int[0];
		location.top = loc_int[1];
		location.right = location.left + v.getWidth();
		location.bottom = location.top + v.getHeight();
		return location;
	}
	
	public static void textMarquee( TextView txtView ) {
		// Use this to marquee Textview inside listview
		
		txtView.setEllipsize( TruncateAt.END );
		// Enable to Start Scroll
		
		// txtView.setMarqueeRepeatLimit(-1);
		// txtView.setHorizontallyScrolling(true);
		// txtView.setSelected(true);
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	public static int getScreenWidth( Activity ctx ) {
		Display display = ctx.getWindowManager().getDefaultDisplay();

//		if ( OSHelper.hasHoneycombMR2() ) {
//			Point size = new Point();
//			display.getSize( size );
//			return size.x;
//		} else {
			return display.getWidth();
//		}
	}
	
	public static void animateRise( final ViewGroup mLayout ) {
		
		AnimationSet set = new AnimationSet( true );
		
		Animation animation = new AlphaAnimation( 0.0f, 1.0f );
		animation.setDuration( 250 );
		set.addAnimation( animation );
		
		animation = new TranslateAnimation( Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, -1.0f );
		animation.setDuration( 500 );
		set.addAnimation( animation );
		
		animation.setAnimationListener( new AnimationListener() {
			
			@Override
			public void onAnimationStart( Animation animation ) {
			}
			
			@Override
			public void onAnimationRepeat( Animation animation ) {
			}
			
			@Override
			public void onAnimationEnd( Animation animation ) {
				mLayout.setVisibility( View.INVISIBLE );
			}
		} );
		
		mLayout.startAnimation( set );
		
	}
	
	public static void animateFall( ViewGroup mLayout ) {
		
		AnimationSet set = new AnimationSet( true );
		
		Animation animation = new AlphaAnimation( 0.0f, 1.0f );
		animation.setDuration( 250 );
		set.addAnimation( animation );
		
		animation = new TranslateAnimation( Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				-1.0f, Animation.RELATIVE_TO_SELF, 0.0f );
		animation.setDuration( 500 );
		set.addAnimation( animation );
		
		mLayout.startAnimation( set );
		
	}
	
	public static void animateLayoutSlideDown( ViewGroup mLayout ) {
		
		AnimationSet set = new AnimationSet( true );
		
		Animation animation = new AlphaAnimation( 0.0f, 1.0f );
		animation.setDuration( 250 );
		set.addAnimation( animation );
		
		animation = new TranslateAnimation( Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				-1.0f, Animation.RELATIVE_TO_SELF, 0.0f );
		animation.setDuration( 150 );
		set.addAnimation( animation );
		
		LayoutAnimationController controller = new LayoutAnimationController(
				set, 0.25f );
		mLayout.setLayoutAnimation( controller );
		
	}
	
	
	
	public static void animateLayoutSlideToRight( ViewGroup mLayout ) {
		
		AnimationSet set = new AnimationSet( true );
		
		Animation animation = new AlphaAnimation( 0.0f, 1.0f );
		animation.setDuration( 750 );
		set.addAnimation( animation );
		
		animation = new TranslateAnimation( Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, 0.0f );
		
		animation.setDuration( 750 );
		set.addAnimation( animation );
		
		LayoutAnimationController controller = new LayoutAnimationController(
				set, 0.25f );
		mLayout.setLayoutAnimation( controller );
		
	}
	
	public static void animateLayoutSlideFromRight( ViewGroup mLayout ) {
		
		AnimationSet set = new AnimationSet( true );
		
		Animation animation = new AlphaAnimation( 0.0f, 1.0f );
		animation.setDuration( 750 );
		set.addAnimation( animation );
		
		animation = new TranslateAnimation( Animation.RELATIVE_TO_SELF, 1.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, 0.0f );
		
		animation.setDuration( 750 );
		set.addAnimation( animation );
		
		LayoutAnimationController controller = new LayoutAnimationController(
				set, 0.25f );
		mLayout.setLayoutAnimation( controller );
		
	}
	
	public static void animateLayoutSlideToLeft( ViewGroup mLayout ) {
		
		AnimationSet set = new AnimationSet( true );
		
		Animation animation = new AlphaAnimation( 0.0f, 1.0f );
		animation.setDuration( 750 );
		set.addAnimation( animation );
		
		animation = new TranslateAnimation( Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, 0.0f );
		
		animation.setDuration( 750 );
		set.addAnimation( animation );
		
		LayoutAnimationController controller = new LayoutAnimationController(
				set, 0.25f );
		mLayout.setLayoutAnimation( controller );
		
	}
	
	
	
	
	public static void animateFromRight( ViewGroup mLayout ) {
		
		AnimationSet set = new AnimationSet( true );
		
		Animation animation = new AlphaAnimation( 0.0f, 1.0f );
		animation.setDuration( 250 );
		set.addAnimation( animation );
		
		animation = new TranslateAnimation( Animation.RELATIVE_TO_SELF, 1.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, 0.0f );
		animation.setDuration( 500 );
		set.addAnimation( animation );
		
		mLayout.startAnimation( set );
		
	}
	
	
	public static void animateToRight( ViewGroup mLayout ) {
		
		AnimationSet set = new AnimationSet( true );
		
		Animation animation = new AlphaAnimation( 0.0f, 1.0f );
		animation.setDuration( 250 );
		set.addAnimation( animation );
		
		animation = new TranslateAnimation( Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, 0.0f );
		animation.setDuration( 500 );
		set.addAnimation( animation );
		
		mLayout.startAnimation( set );
		
	}
	
	public static String getPath(Uri uri, Activity c) {
		String selectedImagePath;
		// 1:MEDIA GALLERY --- query from MediaStore.Images.Media.DATA
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = c.managedQuery(uri, projection, null, null, null);
		if (cursor != null) {
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			selectedImagePath = cursor.getString(column_index);
		} else {
			selectedImagePath = null;
		}

		if (selectedImagePath == null) {
			// 2:OI FILE Manager --- call method: uri.getPath()
			selectedImagePath = uri.getPath();
		}
		return selectedImagePath;
	}

	public static void animateFadeInOut(final View mLayout ) {

		ObjectAnimator fadeOut = ObjectAnimator.ofFloat(mLayout, "alpha",  1f, .3f);
		fadeOut.setDuration(500);
		ObjectAnimator fadeIn = ObjectAnimator.ofFloat(mLayout, "alpha", .3f, 1f);
		fadeIn.setDuration(500);

		final AnimatorSet mAnimationSet = new AnimatorSet();

		mAnimationSet.play(fadeIn).after(fadeOut);

		mAnimationSet.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				super.onAnimationEnd(animation);
				mAnimationSet.start();
			}
		});
		mAnimationSet.start();


	}
 
	
 
	
}
