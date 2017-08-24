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
import com.android.theupdates.callbacks.FollowingCallBack;
import com.android.theupdates.callbacks.followerCallBack;
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

public class FollowUserFragment extends BaseFragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.lstViwLikesUser)
    ListView lstViwLikesUser;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    ArrayListAdapter<LikeUser> listAdapter;

    public int currentVisibleItemCount = 0;
    public int currentFirstVisibleItem;
    public int currentScrollState;
    private int offset = 1;

    private String isFollow;

    private FollowingCallBack followingListener;
    private followerCallBack followeCallBack;

    private WebResponse<FollowUser> webResponse;

    /**
     * if isfollow is 1 so getfollowing service
     * otherwise getfollowers service
     * @param isFollow
     * @return
     */
    public static FollowUserFragment getInstance(String isFollow) {
        Bundle bundle = new Bundle();
        bundle.putString(POST_ID, isFollow);
        FollowUserFragment group = new FollowUserFragment();
        group.setArguments(bundle);
        return group;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isFollow = getArguments().getString(POST_ID);
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
            getList();
        }

    }

    public void setFollowingListener(FollowingCallBack followingListener)
    {
        this.followingListener = followingListener;
    }

    public void setFollowerListener(followerCallBack followeCallBack)
    {
        this.followeCallBack = followeCallBack;
    }

    private void isScrollCompleted() {
        if (this.currentVisibleItemCount > lstViwLikesUser.getChildCount() && this.currentScrollState == SCROLL_STATE_IDLE) {
            currentVisibleItemCount = 0;
            getList();

        }
    }



    private void getList() {
        Call<WebResponseList<LikeUser>> response;

        if (isFollow.equalsIgnoreCase("1")) {
            response = getWebServiceInstance().getFollowingUser(getUserId(), offset);
        } else {
            response = getWebServiceInstance().getFollowerUser(getUserId(), offset);
        }
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
        if(isFollow.equalsIgnoreCase("1")) {
            getMainActivityContext().mainToolbarTitle.setText("Following");
        }
        else if(isFollow.equalsIgnoreCase("0")) {
            getMainActivityContext().mainToolbarTitle.setText("Followers");
        }
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

    int isUserFollow = 0;

    private void setFollowUser(LikeUser likeUserObj, final int index) {

        isUserFollow = likeUserObj.getIsFollowed().equalsIgnoreCase("1")?0:1;

        isUserFollow = isFollow.equalsIgnoreCase("1")?0:isUserFollow;

        Call<WebResponse<FollowUser>> response = getWebServiceInstance().followUser(getUserId(), likeUserObj.getUserId(), isUserFollow);
        response.enqueue(new CustomWebResponse<WebResponse<FollowUser>>(getMainActivityContext()) {
            @Override
            public void onResponse(Call<WebResponse<FollowUser>> call, Response<WebResponse<FollowUser>> response) {
                getMainActivityContext().onLoadingFinished();
                webResponse = response.body();
                if (webResponse != null && webResponse.isSucceed(response, getMainActivityContext())) {

                    if (isFollow.equalsIgnoreCase("1")) {
                        listAdapter.getList().remove(index);
                    } else {
                        listAdapter.getList().get(index).setIsFollowed(String.valueOf(isUserFollow));
                    }

                    listAdapter.notifyDataSetChanged();

                    if(followingListener != null && webResponse != null)
                        followingListener.iUpdateFollowingCount(Integer.parseInt(webResponse.getData().getTotalFollowing()));
                    else if(followeCallBack != null && webResponse != null)
                        followeCallBack.iUpdateFollowersCount(Integer.parseInt(webResponse.getData().getTotalFollowing()));

                }

            }


        });


    }

    @Override
    public void onRefresh() {
        offset = 1;
        getList();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();



    }
}
