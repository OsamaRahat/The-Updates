package com.android.theupdates.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.theupdates.R;
import com.android.theupdates.adapter.PagerAdapter;
import com.android.theupdates.entites.PostItem;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.android.theupdates.constants.Constants.POST_ID;
import static com.android.theupdates.constants.Constants.UPDATE_OBJ;

/**
 * Created by osamarahat on 14/10/2016.
 */

public class ImageSliderFragment extends BaseFragment implements View.OnClickListener {


    @BindView(R.id.pagerSlider)
    ViewPager pagerSlider;

    private PostItem postItem;
    private int selectImg;

    public static ImageSliderFragment getInstance(PostItem postItem,int selectImg) {
        ImageSliderFragment imageSliderFragment = new ImageSliderFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(UPDATE_OBJ, postItem);
        bundle.putInt(POST_ID,selectImg);
        imageSliderFragment.setArguments(bundle);
        return imageSliderFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postItem = (PostItem) getArguments().get(UPDATE_OBJ);
        selectImg = (int) getArguments().get(POST_ID);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_imageslider, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        postItem.getPostImages().addAll(postItem.getPostLinkImages());
        pagerSlider.setAdapter(new PagerAdapter(getFragmentManager(),postItem.getPostImages(),postItem));
        pagerSlider.setCurrentItem(selectImg,true);

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


    @Override
    public void onClick(View view) {
        switch (view.getId())
        {

        }
    }
}
