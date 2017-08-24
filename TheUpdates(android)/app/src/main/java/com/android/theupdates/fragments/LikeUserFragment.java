package com.android.theupdates.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.android.theupdates.R;
import com.android.theupdates.adapter.ArrayListAdapter;
import com.android.theupdates.entites.FollowUser;
import com.android.theupdates.entites.LikeUser;
import com.android.theupdates.viewbinder.LikePostBinder;
import com.android.theupdates.webapi.CustomWebResponse;
import com.android.theupdates.webapi.WebResponse;
import com.android.theupdates.webapi.WebResponseList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

import static android.widget.AbsListView.OnScrollListener.SCROLL_STATE_IDLE;
import static com.android.theupdates.constants.Constants.POST_ID;

/**
 * Created by osamarahat on 14/10/2016.
 */

public class LikeUserFragment extends BaseFragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.lstViwLikesUser)
    ListView lstViwLikesUser;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    ArrayListAdapter<LikeUser> listAdapter;

    public int currentVisibleItemCount = 0;
    public int currentFirstVisibleItem;
    public int currentScrollState;
    private int offset = 1;

    private String postId;

    public static LikeUserFragment getInstance(String postId) {
        Bundle bundle = new Bundle();
        bundle.putString(POST_ID, postId);
        LikeUserFragment group = new LikeUserFragment();
        group.setArguments(bundle);
        return group;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postId = getArguments().getString(POST_ID);
        listAdapter = new ArrayListAdapter<LikeUser>(getMainActivityContext(), new LikePostBinder(getMainActivityContext(),this));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_likeuser, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        swipeRefreshLayout.setOnRefreshListener(this);
        lstViwLikesUser.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                currentScrollState = scrollState;
                isScrollCompleted();
            }

            @Override
            public void onScroll(AbsListView lw, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                currentFirstVisibleItem = firstVisibleItem;
                if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
                    currentVisibleItemCount = firstVisibleItem + visibleItemCount;
                }

            }
        });

        lstViwLikesUser.setAdapter(listAdapter);

        if (listAdapter.getCount() == 0) {
            getMainActivityContext().onLoadingStarted();
            getLikeList();
        }

    }

    private void isScrollCompleted() {
        if (this.currentVisibleItemCount > lstViwLikesUser.getChildCount() && this.currentScrollState == SCROLL_STATE_IDLE) {
            currentVisibleItemCount = 0;
            getLikeList();

        }
    }


    private void getLikeList() {

        Call<WebResponseList<LikeUser>> response = getWebServiceInstance().getLikes(getUserId(), postId, offset);
        response.enqueue(new CustomWebResponse<WebResponseList<LikeUser>>(getMainActivityContext()) {
            @Override
            public void onResponse(Call<WebResponseList<LikeUser>> call, Response<WebResponseList<LikeUser>> response) {
                getMainActivityContext().onLoadingFinished();
                WebResponseList<LikeUser> webResponse = response.body();
                if (webResponse != null && webResponse.isSucceed(response, getMainActivityContext())) {
                    if (offset == 1) {
                        listAdapter.getList().clear();
                    }
                    listAdapter.addAll(webResponse.getData());
                    swipeRefreshLayout.setRefreshing(false);
                    offset++;
                }
                else
                    swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<WebResponseList<LikeUser>> call, Throwable t) {
                super.onFailure(call, t);
                swipeRefreshLayout.setRefreshing(false);
            }
        });


    }


    @Override
    public void setTitleBar(Toolbar toolbar) {
        getMainActivityContext().mainToolbarTitle.setText("Likes");
        toolbar.setBackgroundResource(R.color.green_app);

        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMainActivityContext().onBackPressed();
            }
        });
        setStatusBarColor(R.color.green_app);
    }


    @Override
    public void onClick(View view) {
        LikeUser likeUserObj = listAdapter.getList().get((Integer) view.getTag());
        switch (view.getId()) {
            case R.id.imgUser:
                UserProfileFragment profileFragment = UserProfileFragment.getInstance(likeUserObj.getUserId());
                getMainActivityContext().addFragmentWithFlipAnimation(profileFragment.getClass().getName(),profileFragment);
               break;
            case R.id.txtFollow:
                getMainActivityContext().onLoadingStarted();
                setFollowUser(likeUserObj, (Integer) view.getTag());
                break;


        }
    }

    private void setFollowUser(LikeUser likeUserObj, final int index) {

        final int isFollow = likeUserObj.getIsFollowed().equalsIgnoreCase("1")?0:1;

        Call<WebResponse<FollowUser>> response = getWebServiceInstance().followUser(getUserId(), likeUserObj.getUserId(), isFollow);
        response.enqueue(new CustomWebResponse<WebResponse<FollowUser>>(getMainActivityContext()) {
            @Override
            public void onResponse(Call<WebResponse<FollowUser>> call, Response<WebResponse<FollowUser>> response) {
                getMainActivityContext().onLoadingFinished();
                WebResponse<FollowUser> webResponse = response.body();
                if (webResponse != null && webResponse.isSucceed(response, getMainActivityContext())) {
                    listAdapter.getList().get(index).setIsFollowed(String.valueOf(isFollow));;
                    listAdapter.notifyDataSetChanged();

                }

            }


        });


    }

    @Override
    public void onRefresh() {
        offset = 1;
        getLikeList();
    }
}
