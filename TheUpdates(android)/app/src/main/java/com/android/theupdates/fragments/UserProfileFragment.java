package com.android.theupdates.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.theupdates.R;
import com.android.theupdates.adapter.ArrayListAdapter;
import com.android.theupdates.callbacks.FollowingCallBack;
import com.android.theupdates.callbacks.ReportCallBack;
import com.android.theupdates.callbacks.commentCallBack;
import com.android.theupdates.callbacks.followerCallBack;
import com.android.theupdates.dialogfragments.ReportDialogFragemnt;
import com.android.theupdates.entites.LikeCommentCounter;
import com.android.theupdates.entites.PostItem;
import com.android.theupdates.entites.UserProfile;
import com.android.theupdates.viewbinder.UpdateMsgBinder;
import com.android.theupdates.webapi.CustomWebResponse;
import com.android.theupdates.webapi.WebResponse;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

import static android.widget.AbsListView.OnScrollListener.SCROLL_STATE_IDLE;
import static com.android.theupdates.constants.Constants.POST_ID;

/**
 * Created by osamarahat on 14/10/2016.
 */

public class UserProfileFragment extends BaseFragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, commentCallBack, followerCallBack, FollowingCallBack {

    @BindView(R.id.lstViwUpdateMsg)
    ListView lstViwUpdateMsg;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    ArrayListAdapter<PostItem> listAdapter;

    public int currentVisibleItemCount = 0;
    public int currentFirstVisibleItem;
    public int currentScrollState;
    @BindView(R.id.imgUserPic)
    ImageView imgUserPic;
    @BindView(R.id.txtUserLabel)
    TextView txtUserLabel;
    @BindView(R.id.linFollowers)
    LinearLayout linFollowers;
    @BindView(R.id.linFollowing)
    LinearLayout linFollowing;
    @BindView(R.id.txtFollowersCount)
    TextView txtFollowersCount;
    @BindView(R.id.txtFollowingCount)
    TextView txtFollowingCount;
    @BindView(R.id.txtPostCount)
    TextView txtPostCount;
    @BindView(R.id.linPostContainer)
    LinearLayout linPostContainer;
    @BindView(R.id.linUser)
    LinearLayout linUser;
    @BindView(R.id.linHeader)
    LinearLayout linHeader;
    private int offset = 1;

    private String userId;

    WebResponse<UserProfile> webResponse;

    private int mLastFirstVisibleItem;
    private boolean mIsScrollingUp;

    public static UserProfileFragment getInstance(String userId) {
        Bundle bundle = new Bundle();
        bundle.putString(POST_ID, userId);
        UserProfileFragment profileFragment = new UserProfileFragment();
        profileFragment.setArguments(bundle);
        return profileFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userId = getArguments().getString(POST_ID);
        listAdapter = new ArrayListAdapter<PostItem>(getMainActivityContext(), new UpdateMsgBinder(getMainActivityContext(), this));

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_userprofile, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        linFollowers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FollowUserFragment followUserFragment = FollowUserFragment.getInstance("0");
                getMainActivityContext().addFragment(followUserFragment.getClass().getName(), followUserFragment);
                followUserFragment.setFollowerListener(new followerCallBack() {
                    @Override
                    public void iUpdateFollowersCount(int followersCount) {
                        //if (followersCount != 0) {
                        webResponse.getData().setFollowing(followersCount + "");
                        //}

                    }
                });
            }
        });
        linFollowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FollowUserFragment followUserFragment = FollowUserFragment.getInstance("1");
                getMainActivityContext().addFragment(followUserFragment.getClass().getName(), followUserFragment);
                followUserFragment.setFollowingListener(new FollowingCallBack() {
                    @Override
                    public void iUpdateFollowingCount(int followingCount) {
                        //if (followingCount != 0) {
                        webResponse.getData().setFollowing(followingCount + "");
                        //}

                    }
                });
            }
        });

        swipeRefreshLayout.setOnRefreshListener(this);
        lstViwUpdateMsg.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView lw, int scrollState) {

                //SCROLL UP TO HIDE HEADER..
                final int currentFirstVisibleItem = lw.getFirstVisiblePosition();

                if (currentFirstVisibleItem > mLastFirstVisibleItem) {
                    mIsScrollingUp = false;
                    linHeader.setVisibility(View.GONE);
                } else if (currentFirstVisibleItem < mLastFirstVisibleItem) {
                    mIsScrollingUp = true;
                    linHeader.setVisibility(View.VISIBLE);
                }

                mLastFirstVisibleItem = currentFirstVisibleItem;

                //SWIPE UP OR DOWN FOR DATA REFRESHING...
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

        lstViwUpdateMsg.setAdapter(listAdapter);
        lstViwUpdateMsg.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

        if (listAdapter.getCount() == 0) {
            getMainActivityContext().onLoadingStarted();
            getPostList();
        } else {
            ImageLoader.getInstance().displayImage(webResponse.getData().getUser().getUserPicture(), imgUserPic, getDefaultOptions());
            txtUserLabel.setText(webResponse.getData().getUser().getUsername());
            txtPostCount.setText(webResponse.getData().getTotalposts());
            txtFollowersCount.setText(webResponse.getData().getFollowers());
            txtFollowingCount.setText(webResponse.getData().getFollowing());
        }

    }

    private void isScrollCompleted() {
        if (this.currentVisibleItemCount > lstViwUpdateMsg.getChildCount() && this.currentScrollState == SCROLL_STATE_IDLE) {
            currentVisibleItemCount = 0;
            getPostList();

        }
    }


    private void getPostList() {

        Call<WebResponse<UserProfile>> response = getWebServiceInstance().getProfile(getUserId(), userId, offset);
        response.enqueue(new CustomWebResponse<WebResponse<UserProfile>>(getMainActivityContext()) {
            @Override
            public void onResponse(Call<WebResponse<UserProfile>> call, Response<WebResponse<UserProfile>> response) {
                getMainActivityContext().onLoadingFinished();
                webResponse = response.body();
                if (webResponse != null && webResponse.isSucceed(response, getMainActivityContext())) {
                    if (offset == 1) {
                        listAdapter.getList().clear();
                    }
                    listAdapter.addAll(webResponse.getData().getPosts());
                    swipeRefreshLayout.setRefreshing(false);
                    ImageLoader.getInstance().displayImage(webResponse.getData().getUser().getUserPicture(), imgUserPic, getDefaultOptions());
                    txtUserLabel.setText(webResponse.getData().getUser().getUsername());
                    txtFollowersCount.setText(webResponse.getData().getFollowers());
                    txtFollowingCount.setText(webResponse.getData().getFollowing());
                    txtPostCount.setText(webResponse.getData().getTotalposts());
                    offset++;
                } else
                    swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<WebResponse<UserProfile>> call, Throwable t) {
                super.onFailure(call, t);
                swipeRefreshLayout.setRefreshing(false);
            }
        });


    }

    int postObjIndex = 0;

    @Override
    public void onClick(View view) {

        postObjIndex = (int) view.getTag();

        PostItem postItemObj = listAdapter.getList().get(postObjIndex);
        switch (view.getId()) {
            case R.id.imgArchieve:
                setInstanceSliderFragment(postItemObj, 0);
                break;
            case R.id.imgUpdate1:
                setInstanceSliderFragment(postItemObj, 1);
                break;
            case R.id.imgUpdate2:
                setInstanceSliderFragment(postItemObj, 2);
                break;
            case R.id.imgUpdate3:
                setInstanceSliderFragment(postItemObj, 3);
                break;

            case R.id.txtComment:
            case R.id.txtCommnetCount:
                CommentsFragment commentsFragment = CommentsFragment.getInstance(postItemObj.getPostId(), postItemObj);
                getMainActivityContext().addFragment(commentsFragment.getClass().getName(), commentsFragment);
                commentsFragment.setOnCommentListener(this);
                break;
            case R.id.txtLikesCount:
                getMainActivityContext().addFragment(LikeUserFragment.getInstance(postItemObj.getPostId()).getClass().getName(), LikeUserFragment.getInstance(postItemObj.getPostId()));
                break;
            case R.id.txtUpdateMore:
                setInstanceSliderFragment(postItemObj, 0);
                break;
            case R.id.txtLikes:
                setLikePost(postItemObj.getPostId(), postItemObj.getIsLiked().equalsIgnoreCase("0") ? 1 : 0, postObjIndex);
                break;
            case R.id.txtReport:
                ReportDialogFragemnt dialogFragemnt = ReportDialogFragemnt.newInstance(postItemObj.getPostId(),postItemObj.getPostByUserId(),true);
                dialogFragemnt.setOnReportCallBackDialogListener(new ReportCallBack() {
                    @Override
                    public void reportUserNPost(String postId, String toUserId,String reason) {
                        reportUserPost(postId,toUserId,reason);

                    }
                });
                if(!(userId.equalsIgnoreCase(getMainActivityContext().preferenceHelper.getUser().getUserId())))
                dialogFragemnt.show(getFragmentManager(),dialogFragemnt.getClass().getName());
                break;

        }
    }

    private void setInstanceSliderFragment(PostItem postItemObj, int index) {
        ImageSliderFragment sliderFragment = ImageSliderFragment.getInstance(postItemObj, index);
        getMainActivityContext().addFragmentWithAnimation(sliderFragment.getClass().getName(), sliderFragment);
    }

    @Override
    public void setTitleBar(Toolbar toolbar) {
        toolbar.setNavigationIcon(R.drawable.menu);
        getMainActivityContext().mainToolbarTitle.setText("Profile");
        toolbar.setBackgroundResource(R.color.green_app);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMainActivityContext().drawer.openDrawer(GravityCompat.START);
            }
        });

        setStatusBarColor(R.color.green_app);


    }


    @Override
    public void onRefresh() {
        offset = 1;
        swipeRefreshLayout.setRefreshing(true);
        getPostList();

    }


    private void setLikePost(String postId, final int liked, final int index) {

        getMainActivityContext().onLoadingStarted();
        Call<WebResponse<LikeCommentCounter>> response = getWebServiceInstance().setLikePost(getUserId(), postId, liked);
        response.enqueue(new CustomWebResponse<WebResponse<LikeCommentCounter>>(getMainActivityContext()) {
            @Override
            public void onResponse(Call<WebResponse<LikeCommentCounter>> call, Response<WebResponse<LikeCommentCounter>> response) {
                getMainActivityContext().onLoadingFinished();
                WebResponse<LikeCommentCounter> webResponse = response.body();
                if (webResponse != null && webResponse.isSucceed(response, getMainActivityContext())) {
                    listAdapter.getList().get(index).setTotalLiked(webResponse.getData().getTotalLiked() + "");
                    listAdapter.getList().get(index).setIsLiked(liked + "");
                    listAdapter.notifyDataSetChanged();
                }
            }


        });


    }

    @Override
    public void iUpdateCommentCount(int commentCount) {
        if (commentCount > 0) {
            listAdapter.getList().get(postObjIndex).setTotalComments(commentCount + "");
            listAdapter.notifyDataSetChanged();
        }
    }

    private DisplayImageOptions getDefaultOptions() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .displayer(new RoundedBitmapDisplayer(150))
                .cacheOnDisk(true)
                .build();

        return defaultOptions;
    }

    @Override
    public void iUpdateFollowingCount(int followingCount) {
        txtFollowingCount.setText(followingCount + "");
    }

    @Override
    public void iUpdateFollowersCount(int followersCount) {
        txtFollowersCount.setText(followersCount + "");
    }
}
