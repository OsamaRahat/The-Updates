package com.android.theupdates.viewbinder;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.theupdates.R;
import com.android.theupdates.entites.PostItem;
import com.android.theupdates.helper.DateHelper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import static com.android.theupdates.helper.DateHelper.getPostFormattedDate;

/**
 * Created by osamarahat on 16/10/2016.
 */

public class UpdateMsgBinder extends ViewBinder<PostItem> {

    private Context cntxt;
    private View.OnClickListener onClickListener;

    public UpdateMsgBinder(Context cntxt, View.OnClickListener onClickListener) {
        super(R.layout.list_item_updates);
        this.cntxt = cntxt;
        this.onClickListener = onClickListener;
    }

    @Override
    public BaseViewHolder createViewHolder(View view) {
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.linPicsContainer = (LinearLayout) view.findViewById(R.id.linPicsContainer);
        viewHolder.linPostImages = (LinearLayout) view.findViewById(R.id.linPostImages);
        viewHolder.viwUpdateBar = view.findViewById(R.id.viwUpdateBar);
        viewHolder.txtUpdateMore = (TextView) view.findViewById(R.id.txtUpdateMore);
        viewHolder.txtLinkUpdate = (TextView) view.findViewById(R.id.txtLinkUpdate);
        viewHolder.imgUser = (ImageView) view.findViewById(R.id.imgUser);
        viewHolder.imgUpdate1 = (ImageView) view.findViewById(R.id.imgUpdate1);
        viewHolder.imgUpdate2 = (ImageView) view.findViewById(R.id.imgUpdate2);
        viewHolder.imgUpdate3 = (ImageView) view.findViewById(R.id.imgUpdate3);
        viewHolder.imgArchieve= (ImageView) view.findViewById(R.id.imgArchieve);
        viewHolder.txtCommnetCount = (TextView)view.findViewById(R.id.txtCommnetCount);
        viewHolder.txtUserName = (TextView)view.findViewById(R.id.txtUserName);
        viewHolder.txtUpdateTime = (TextView)view.findViewById(R.id.txtUpdateTime);
        viewHolder.txtDesc = (TextView)view.findViewById(R.id.txtDesc);
        viewHolder.txtLikesCount = (TextView)view.findViewById(R.id.txtLikesCount);
        viewHolder.txtComment = (TextView)view.findViewById(R.id.txtComment);
        viewHolder.txtLikes = (TextView)view.findViewById(R.id.txtLikes);
        viewHolder.txtReport = (TextView)view.findViewById(R.id.txtReport);

        viewHolder.imgUser.setOnClickListener(onClickListener);
        viewHolder.imgUpdate1.setOnClickListener(onClickListener);
        viewHolder.imgUpdate2.setOnClickListener(onClickListener);
        viewHolder.imgUpdate3.setOnClickListener(onClickListener);
        viewHolder.imgArchieve.setOnClickListener(onClickListener);
        viewHolder.txtCommnetCount.setOnClickListener(onClickListener);
        viewHolder.txtLikesCount.setOnClickListener(onClickListener);
        viewHolder.txtComment.setOnClickListener(onClickListener);
        viewHolder.txtUpdateMore.setOnClickListener(onClickListener);
        viewHolder.txtLikes.setOnClickListener(onClickListener);
        viewHolder.txtReport.setOnClickListener(onClickListener);
        viewHolder.txtLinkUpdate.setOnClickListener(onClickListener);


        return viewHolder;
    }

    @Override
    public void bindView(PostItem entity, int position, int grpPosition, View view, Activity activity) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();
        viewHolder.imgUser.setTag(position);
        viewHolder.imgUpdate1.setTag(position);
        viewHolder.imgUpdate2.setTag(position);
        viewHolder.imgUpdate3.setTag(position);
        viewHolder.imgArchieve.setTag(position);
        viewHolder.txtCommnetCount.setTag(position);
        viewHolder.txtLikesCount.setTag(position);
        viewHolder.txtComment.setTag(position);
        viewHolder.txtUpdateMore.setTag(position);
        viewHolder.txtLikes.setTag(position);
        viewHolder.txtReport.setTag(position);
        viewHolder.txtLinkUpdate.setTag(position);

        //TODO MUST BE WORK ON IT..
       //ADD POST LINK TO POST IMAGES
        //KINDLY SEE IN GETPOSTIMAGES METHOD..

        if (entity.getPostImages().size() > 0) {
            viewHolder.linPicsContainer.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(entity.getPostImages().get(0), viewHolder.imgArchieve, getImageOptions());
            for (int i = 0, j = 1; i < 4; i++, j++) {
                if (j < entity.getPostImages().size() && entity.getPostImages().get(j) != null) {
                    if(viewHolder.linPostImages.getChildAt(i) instanceof ImageView) {
                        ImageView imgViw = (ImageView) viewHolder.linPostImages.getChildAt(i);
                        imgViw.setVisibility(View.VISIBLE);
                        ImageLoader.getInstance().displayImage(entity.getPostImages().get(j), imgViw, getImageOptions());
                    } else {
                        ((TextView) viewHolder.linPostImages.getChildAt(i)).setVisibility(View.VISIBLE);
                    }
                }
                else {
                    viewHolder.linPostImages.getChildAt(i).setVisibility(View.GONE);
                }


            }
//            if(entity.getPostImages().length == 1)
//                viewHolder.linPostImages.setVisibility(View.GONE);
//            else if(entity.getPostImages().length == 0)
//                viewHolder.linPostImages.setVisibility(View.VISIBLE);
        } else {
            viewHolder.linPicsContainer.setVisibility(View.GONE);
        }

        if (entity.getPostImages().size() <= 1) {
            viewHolder.linPostImages.setVisibility(View.GONE);
        } else {
            viewHolder.linPostImages.setVisibility(View.VISIBLE);
        }

        viewHolder.viwUpdateBar.setBackgroundColor(Color.parseColor(entity.getPostGroupHexColor()));
        viewHolder.txtUpdateMore.setBackgroundColor(Color.parseColor(entity.getPostGroupHexColor()));
        viewHolder.txtLinkUpdate.setText(getSpannTextView(entity.getPostGroupName()));
        viewHolder.txtLinkUpdate.setTextColor(Color.parseColor(entity.getPostGroupHexColor()));
        viewHolder.txtUserName.setText(entity.getPostByUserName());

        String strDD = DateHelper.getElapsedInDays(entity.getPostDate());
        String pp = getPostFormattedDate(entity.getPostDate());
        String strDate = strDD.contains("Yesterday")?pp:strDD;

        viewHolder.txtUpdateTime.setText(strDate);
        if(strDD.contains("Yesterday"))
            viewHolder.txtUpdateTime.setCompoundDrawablesWithIntrinsicBounds(R.drawable.date_icon,0,0,0);
        else
            viewHolder.txtUpdateTime.setCompoundDrawablesWithIntrinsicBounds(R.drawable.time_icon,0,0,0);

        viewHolder.txtDesc.setText(entity.getPostText());
        viewHolder.txtCommnetCount.setText(entity.getTotalComments() + " Comments");
        viewHolder.txtLikesCount.setText(entity.getTotalLiked() + " Likes");
        ImageLoader.getInstance().displayImage(entity.getUserPicture(), viewHolder.imgUser, getRoundImageOptions());

        if (entity.getIsLiked().equals("0")) {
            viewHolder.txtLikes.setCompoundDrawablesWithIntrinsicBounds(R.drawable.like, 0, 0, 0);
        } else {
            viewHolder.txtLikes.setCompoundDrawablesWithIntrinsicBounds(R.drawable.like_hover, 0, 0, 0);
        }

    }

    public class ViewHolder extends BaseViewHolder {
        private View viwUpdateBar;
        private TextView txtUpdateMore;
        private LinearLayout linPicsContainer, linPostImages;
        private TextView txtLinkUpdate;
        private TextView txtUserName;
        private TextView txtUpdateTime;
        private TextView txtDesc;
        private TextView txtCommnetCount;
        private TextView txtLikesCount;
        private ImageView imgArchieve;
        private ImageView imgUpdate1, imgUpdate2, imgUpdate3;
        private ImageView imgUser;
        private TextView txtComment;
        private TextView txtLikes;
        private TextView txtReport;


    }

    public SpannableStringBuilder getSpannTextView(String updateLabel) {
        final SpannableStringBuilder sb = new SpannableStringBuilder("via " + updateLabel);
        final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.DKGRAY);
        sb.setSpan(fcs, 0, 3, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return sb;

    }

    private DisplayImageOptions getImageOptions() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.color.transparent)
                .showImageOnFail(R.color.transparent)
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .build();

        return defaultOptions;
    }

    private DisplayImageOptions getRoundImageOptions() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .displayer(new RoundedBitmapDisplayer(100))
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .build();

        return defaultOptions;
    }

}
