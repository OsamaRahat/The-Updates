package com.android.theupdates.viewbinder;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.theupdates.R;
import com.android.theupdates.entites.CommentPost;
import com.android.theupdates.helper.DateHelper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/**
 * Created by osamarahat on 16/10/2016.
 */

public class CommentMsgBinder extends ViewBinder<CommentPost> {

    View.OnClickListener onClickListener;
    private Context context;

    public CommentMsgBinder(Context context,View.OnClickListener onClickListener) {
        super(R.layout.list_item_comments);
        this.onClickListener = onClickListener;
        this.context = context;
    }

    @Override
    public BaseViewHolder createViewHolder(View view) {
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.txtCountLikes = (TextView) view.findViewById(R.id.txtCountLikes);
        viewHolder.txtCommenter = (TextView) view.findViewById(R.id.txtCommenter);
        viewHolder.txtUserComments = (TextView) view.findViewById(R.id.txtUserComments);
        viewHolder.txtCommentTime = (TextView) view.findViewById(R.id.txtCommentTime);
        viewHolder.imgUser = (ImageView) view.findViewById(R.id.imgUser);

        viewHolder.txtCountLikes.setOnClickListener(onClickListener);
        viewHolder.imgUser.setOnClickListener(onClickListener);


        return viewHolder;
    }

    @Override
    public void bindView(CommentPost entity, int position, int grpPosition, View view, Activity activity) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        viewHolder.txtCountLikes.setTag(position);
        viewHolder.imgUser.setTag(position);

        if (entity.getIsLikedByMe().equalsIgnoreCase("1")) {
            viewHolder.txtCountLikes.setText(getSpannTextView((entity.getTotalCommentLikes()), Color.BLUE));

        }
        else {
            viewHolder.txtCountLikes.setText(getSpannTextView((entity.getTotalCommentLikes()),Color.DKGRAY));
        }





        viewHolder.txtCommenter.setText(entity.getUserName());
        viewHolder.txtCommentTime.setText(DateHelper.getElapsedInDays(entity.getCommentDate()));
        viewHolder.txtUserComments.setText(entity.getPostComment());

        ImageLoader.getInstance().displayImage(entity.getUserPicture(),viewHolder.imgUser,getRoundImageOptions());

    }

    public class ViewHolder extends BaseViewHolder {
        private TextView txtCountLikes;
        private TextView txtCommenter;
        private TextView txtUserComments;
        private TextView txtCommentTime;
        private ImageView imgUser;

    }

    private DisplayImageOptions getRoundImageOptions() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .displayer(new RoundedBitmapDisplayer(100))
                .cacheOnDisk(true)
                .build();

        return defaultOptions;
    }

    public SpannableString getSpannTextView(String updateLabel,int colorCode) {
        String str  =  "LIKE " + "("+updateLabel+")";
        final SpannableString sb = new SpannableString(str);
        final ForegroundColorSpan fcs = new ForegroundColorSpan(colorCode);
        sb.setSpan(fcs, 0,4, 0);
        return sb;


    }
}
