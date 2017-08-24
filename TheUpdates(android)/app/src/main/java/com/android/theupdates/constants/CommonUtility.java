package com.android.theupdates.constants;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.format.DateFormat;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.theupdates.activities.MainActivity;
import com.android.theupdates.helper.TheUpdatesPreferenceHelper;
import com.android.theupdates.webapi.WebResponse;
import com.android.theupdates.webapi.WebServiceFactory;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommonUtility {

	public static void NetworkThread() {

		try {

			Class<?> strictModeClass = Class.forName("android.os.StrictMode");

			Class<?> strictModeThreadPolicyClass = Class
					.forName("android.os.StrictMode$ThreadPolicy");

			Object laxPolicy = strictModeThreadPolicyClass.getField("LAX").get(
					null);

			Method method_setThreadPolicy = strictModeClass.getMethod(
					"setThreadPolicy", strictModeThreadPolicyClass);

			method_setThreadPolicy.invoke(null, laxPolicy);

		} catch (Exception e) {
		}

	}

	public static int getItemHeightofListView(ListView listView, int items) {

		ListAdapter mAdapter = listView.getAdapter();

		int listviewElementsheight = 0;

		for (int i = 0; i < items; i++) {

			View childView = mAdapter.getView(i, null, listView);
			childView.measure(
					MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
					MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
			listviewElementsheight += childView.getMeasuredHeight();
		}

		return listviewElementsheight;

	}

	public static int getListPreferredItemHeight(Activity a) {
		final TypedValue typedValue = new TypedValue();

		// Resolve list item preferred height theme attribute into typedValue
		a.getTheme().resolveAttribute(android.R.attr.listPreferredItemHeight, typedValue, true);

		// Create a new DisplayMetrics object
		final DisplayMetrics metrics = new DisplayMetrics();

		// Populate the DisplayMetrics
		a.getWindowManager().getDefaultDisplay().getMetrics(metrics);

		// Return theme value based on DisplayMetrics
		return (int) typedValue.getDimension(metrics);
	}

	public static boolean isOnline(Context cntxt) {
		ConnectivityManager cm = (ConnectivityManager) cntxt
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

	public static File setSaveImage(Bitmap bitData) {
		// TODO CHECK SDCARD IS MOUNTED OR NOT..
		System.gc();
		Bitmap bitmap = null;
		bitmap = bitData;// Bitmap.createScaledBitmap(bitData, 300, 150,
		// false);//bitData;
		File imageFile = null;
		if (isSDCARDMounted()) {
			Calendar calendar = Calendar.getInstance();
			String dateFormat = (String) DateFormat.format(
					"dd_MM_yyyy_hh_mm_ss", calendar);

			OutputStream fout = null;

			String pathDir = "";
			File swopDirectory = new File(Environment
					.getExternalStorageDirectory().getAbsolutePath()
					+ "/IVote_Images");
			if (!swopDirectory.exists()) {
				swopDirectory.mkdirs();
				pathDir = swopDirectory.toString().trim();
				imageFile = new File(pathDir, dateFormat + ".png");

			} else {
				pathDir = swopDirectory.toString().trim();
				imageFile = new File(pathDir, dateFormat + ".png");
			}

			try {
				fout = new FileOutputStream(imageFile);
				bitmap.compress(CompressFormat.PNG, 90, fout);
				fout.flush();
				fout.close();

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			}
		}

		bitmap = null;

		return imageFile;
	}

	public static File setSaveGoogleImage(Bitmap bitData) {
		// TODO CHECK SDCARD IS MOUNTED OR NOT..
		System.gc();
		Bitmap bitmap = null;
		bitmap = bitData;
		File imageFile = null;
		if (isSDCARDMounted()) {
			Calendar calendar = Calendar.getInstance();
			String dateFormat = (String) DateFormat.format(
					"dd_MM_yyyy_hh_mm_ss", calendar);

			OutputStream fout = null;

			String pathDir = "";
			File swopDirectory = new File(Environment
					.getExternalStorageDirectory().getAbsolutePath()
					+ "/IVote_Images");
			if (!swopDirectory.exists()) {
				swopDirectory.mkdirs();
				pathDir = swopDirectory.toString().trim();
				// imageFile = new File(pathDir,dateFormat+".png");
				imageFile = new File(pathDir, dateFormat + ".jpg");

			} else {
				pathDir = swopDirectory.toString().trim();
				// imageFile = new File(pathDir,dateFormat+".png");
				imageFile = new File(pathDir, dateFormat + ".jpg");
			}

			try {
				fout = new FileOutputStream(imageFile);
				// bitmap.compress(Bitmap.CompressFormat.PNG, 90, fout);
				bitmap.compress(CompressFormat.JPEG, 100, fout);
				fout.flush();
				fout.close();

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			}
		}

		if (bitmap != null) {
			bitmap.recycle();
			bitmap = null;
		}

		return imageFile;
	}

	public static boolean isSDCARDMounted() {
		String status = Environment.getExternalStorageState();

		if (status.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		}
		return false;
	}

	public void customLinkify(TextView mText, String input, Context cntxt) {
		input = input.concat(" ");
		SpannableStringBuilder builder = new SpannableStringBuilder(input);
		Pattern pattern = Pattern.compile("#[A-Z0-9a-z]+");
		Matcher matcher = pattern.matcher(input);

		while (matcher.find()) {
			int start = matcher.start();
			int end = matcher.end();

			String text = input.subSequence(start, end).toString();

			ClickableURLSpan url = new ClickableURLSpan(text, cntxt);
			builder.setSpan(url, start, end, 0);
		}

		mText.setText(builder);
		mText.setMovementMethod(LinkMovementMethod.getInstance());
	}

	public class ClickableURLSpan extends URLSpan {

		Context cntxt;

		public ClickableURLSpan(String url, Context cntxt) {
			super(url);
			this.cntxt = cntxt;

		}

		@Override
		public void onClick(View widget) {
			String clickedText = getURL();
			// START your activity here
			//Constants.GETAG = clickedText.replaceAll("#", "").trim();
			// Toast.makeText(cntxt, Constants.GETAG, Toast.LENGTH_SHORT)
			// .show();

		}

		@Override
		public void updateDrawState(TextPaint ds) {
			super.updateDrawState(ds);

			// ds.setColor(cntxt.getResources().getColor(R.color.ORANGE));//set
			// text color
			ds.setUnderlineText(true); // set to false to remove underline
		}
	}

	// public static Bundle sentUserCredToResFrag(DummyPoll
	// itemFromList,Popularity popObj) {
	//
	// if (popObj == null) {
	// popObj = new Popularity();
	// popObj.setPoll_option("0");
	// }
	//
	// Bundle bundle = new Bundle();
	// bundle.putString(Constants.POLL_ID, itemFromList.getPoll().getPoll_id());
	// bundle.putString(Constants.PROFILE_PIC,
	// URL.profilePicThumb+itemFromList.getPoll().getUser_profilepic());
	// bundle.putString(Constants.USERNAME,
	// itemFromList.getPoll().getUser_name());
	// bundle.putString(Constants.POLL_DATE,
	// itemFromList.getPoll().getPoll_datecreated());
	// bundle.putString(Constants.POLL_QS,
	// itemFromList.getPoll().getPoll_question());
	// bundle.putString(Constants.POLL_COUNT_1,
	// itemFromList.getPoll().getOption1_like());
	// bundle.putString(Constants.POLL_COUNT_2,
	// itemFromList.getPoll().getOption2_like());
	// bundle.putString(Constants.LEFT_PIC,
	// URL.imgMainPath+itemFromList.getPoll().getPoll_option1_image());
	// bundle.putString(Constants.RIGHT_PIC,
	// URL.imgMainPath+itemFromList.getPoll().getPoll_option2_image());
	// bundle.putString(Constants.LEFT_PIC_THUMB,
	// URL.imgMainPath+itemFromList.getPoll().getPoll_option1_image());
	// bundle.putString(Constants.RIGHT_PIC_THUMB,
	// URL.imgMainPath+itemFromList.getPoll().getPoll_option2_image());
	// bundle.putString(Constants.LEFT_QS,
	// itemFromList.getPoll().getPoll_option1_title());
	// bundle.putString(Constants.RIGHT_QS,
	// itemFromList.getPoll().getPoll_option2_title());
	// bundle.putString(Constants.LEFT_THUMB_STATE, popObj.poll_option);
	// bundle.putString(Constants.RIGHT_THUMB_STATE,popObj.poll_option);
	// bundle.putString(Constants.LIKE_STATE,popObj.poll_option);
	// return bundle;
	// }
	//
	//
	// public static Bundle sentUserCredToCommentScreen(ActivityObject
	// itemFromList,Context cntxt) {
	//
	// PolarPreferenceHelper prefhelper = new PolarPreferenceHelper(cntxt);
	// Bundle bundle = new Bundle();
	// bundle.putString(Constants.POLL_ID,
	// itemFromList.getObjAct().getPoll_id());
	// bundle.putString(Constants.PROFILE_PIC,
	// URL.profilePicThumb+itemFromList.getObjAct().getUser_profilepic());
	// bundle.putString(Constants.USERNAME,prefhelper.getUser().getUsername());
	// bundle.putString(Constants.POLL_DATE,
	// itemFromList.getObjAct().getDatetime());
	// bundle.putString(Constants.POLL_QS,
	// itemFromList.getObjAct().getActivity_alertmessage());
	// bundle.putString(Constants.LEFT_PIC_THUMB,
	// URL.imgMainPath+itemFromList.getObjAct().getOption1_image());
	// bundle.putString(Constants.RIGHT_PIC_THUMB,
	// URL.imgMainPath+itemFromList.getObjAct().getOption2_image());
	//
	// return bundle;
	// }

	public static Bitmap getImageOrientation(String _path, Bitmap bitmap) {
		ExifInterface exif = null;
		System.gc();
		Bitmap bmp = null;
		try {
			exif = new ExifInterface(_path);

			int exifOrientation = exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);

			int rotate = 0;

			switch (exifOrientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					rotate = -90;
					break;

				case ExifInterface.ORIENTATION_ROTATE_180:
					rotate = -180;
					break;

				case ExifInterface.ORIENTATION_ROTATE_270:
					rotate = -270;
					break;

				case ExifInterface.ORIENTATION_NORMAL:
					rotate = 0;
					break;
			}

			int w = bitmap.getWidth();
			int h = bitmap.getHeight();
			if (rotate != 0) {

				Matrix mtx = new Matrix();
				mtx.setRotate(rotate);
				mtx.preRotate(rotate);
				mtx.postRotate(rotate);

				// Rotating Bitmap & convert to ARGB_8888, required by tess
				// if(rotate == 0)
				// bmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, null, true);
				// else

				bmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
				// bmp = bitmap.copy(Bitmap.Config.ARGB_8888, true);
				return bmp;
			} else {
				bmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, null, true);
				return bmp;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		System.gc();
		return null;
	}

	public static final int BUFFER_SIZE = 1024 * 8;

	static Uri writeExternalToCache(Bitmap bitmap, String path) {
		try {
			Uri uri = null;
			File file = new File(path);
			file.createNewFile();

			FileOutputStream fos = new FileOutputStream(file);
			final BufferedOutputStream bos = new BufferedOutputStream(fos,
					BUFFER_SIZE);
			bitmap.compress(CompressFormat.JPEG, 100, bos);
			bos.flush();
			bos.close();
			fos.close();
			uri = Uri.fromFile(file);
			return uri;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			return null;
		}

	}

	public static void sendDeviceToken(Context context) {
		final TheUpdatesPreferenceHelper helper = new TheUpdatesPreferenceHelper(context);

		if (helper.getUser() == null)
			return;

		final String token = FirebaseInstanceId.getInstance().getToken();
		Call<WebResponse<String>> response = WebServiceFactory.getInstance().pushRegistration(helper.getUser().getUserId(), "", token, "android");
		response.enqueue(new Callback<WebResponse<String>>() {
			@Override
			public void onResponse(Call<WebResponse<String>> call, Response<WebResponse<String>> response) {
				if (response.body() != null) {
					helper.putDeviceToken(token);
					Log.e("PUSH_MSG", response.body().getMessage());
				}
			}

			@Override
			public void onFailure(Call<WebResponse<String>> call, Throwable t) {

				Log.e("PUSH_ERROR", t.getMessage());

			}
		});


	}
}
