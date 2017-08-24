package com.android.theupdates.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.theupdates.R;
import com.android.theupdates.entites.PostItem;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.android.theupdates.constants.Constants.IMG_URL;
import static com.android.theupdates.constants.Constants.UPDATE_OBJ;

/**
 * Created by osamarahat on 06/11/2016.
 */

public class ScreenSlidePageFragment extends BaseFragment {

    @BindView(R.id.imgUpdate)
    ImageView imgUpdate;
    private String imgUrl;

    private PostItem postItem;

    public static ScreenSlidePageFragment getInstance(String imgUrl, PostItem postItem) {
        ScreenSlidePageFragment pageFragment = new ScreenSlidePageFragment();
        Bundle bundle = new Bundle();
        bundle.putString(IMG_URL, imgUrl);
        bundle.putParcelable(UPDATE_OBJ,postItem);
        pageFragment.setArguments(bundle);
        return pageFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imgUrl = getArguments().getString(IMG_URL);
        postItem = (PostItem) getArguments().get(UPDATE_OBJ);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_screenslider, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //ONLY FOR VIDEO
        if(imgUrl.startsWith("drawable"))
        {
            ImageLoader.getInstance().displayImage(imgUrl, imgUpdate,getImageOptions());
            imgUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(postItem.getPostVideo()), "video/*");
                    startActivity(Intent.createChooser(intent, "Complete action using"));
                }
            });
            return;
        }

        ImageLoader.getInstance().displayImage(imgUrl, imgUpdate,getImageOptions());
    }

    @Override
    public void setTitleBar(Toolbar toolbar) {
        toolbar.setNavigationIcon(R.drawable.back);
        getMainActivityContext().mainToolbarTitle.setText(postItem.getPostGroupName());
        toolbar.setBackgroundColor(Color.parseColor(postItem.getPostGroupHexColor()));
        setStatusBarColor(postItem.getPostGroupHexColor());
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMainActivityContext().onBackPressed();
            }
        });
    }
}
