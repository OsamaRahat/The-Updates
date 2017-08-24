package com.android.theupdates.viewbinder;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.theupdates.R;
import com.android.theupdates.entites.LikeUser;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/**
 * Created by osamarahat on 16/10/2016.
 */

public class LikePostBinder extends ViewBinder<LikeUser> {

    private View.OnClickListener onClickListener;
    private Context context;

    public LikePostBinder(Context context, View.OnClickListener onClickListener) {
        super(R.layout.list_item_likeuser);
        this.onClickListener = onClickListener;
        this.context = context;
    }

    @Override
    public BaseViewHolder createViewHolder(View view) {
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.txtFollow = (TextView) view.findViewById(R.id.txtFollow);
        viewHolder.txtLikeUser = (TextView) view.findViewById(R.id.txtLikeUser);
        viewHolder.imgUser = (ImageView) view.findViewById(R.id.imgUser);

        viewHolder.imgUser.setOnClickListener(onClickListener);
        viewHolder.txtFollow.setOnClickListener(onClickListener);

        return viewHolder;
    }

    @Override
    public void bindView(LikeUser entity, int position, int grpPosition, View view, Activity activity) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        viewHolder.imgUser.setTag(position);
        viewHolder.txtFollow.setTag(position);

        ImageLoader.getInstance().displayImage(entity.getUserPicture(), viewHolder.imgUser, getRoundImageOptions());
        viewHolder.txtLikeUser.setText(entity.getUserName());

        if (entity.getIsFollowed().equals("1")) {
            viewHolder.txtFollow.setText("Following");
            viewHolder.txtFollow.setBackgroundResource(R.color.green_app);
            viewHolder.txtFollow.setTextColor(context.getResources().getColor(R.color.white));

        } else {
            viewHolder.txtFollow.setText("Follow");
            viewHolder.txtFollow.setBackgroundResource(R.color.white);
            viewHolder.txtFollow.setTextColor(context.getResources().getColor(R.color.green_app));

        }

    }

    public class ViewHolder extends BaseViewHolder {
        private TextView txtLikeUser;
        private ImageView imgUser;
        private TextView txtFollow;

    }

    private DisplayImageOptions getRoundImageOptions() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .displayer(new RoundedBitmapDisplayer(100))
                .cacheOnDisk(true)
                .build();

        return defaultOptions;
    }
}
