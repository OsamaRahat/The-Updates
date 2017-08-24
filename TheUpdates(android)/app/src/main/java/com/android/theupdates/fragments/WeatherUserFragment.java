package com.android.theupdates.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.theupdates.R;
import com.android.theupdates.adapter.ArrayListAdapter;
import com.android.theupdates.callbacks.ReportCallBack;
import com.android.theupdates.callbacks.commentCallBack;
import com.android.theupdates.callbacks.iGenericCallBack;
import com.android.theupdates.dialogfragments.ReportDialogFragemnt;
import com.android.theupdates.entites.LikeCommentCounter;
import com.android.theupdates.entites.PostItem;
import com.android.theupdates.entites.SideBarItem;
import com.android.theupdates.helper.UIHelper;
import com.android.theupdates.viewbinder.UpdateMsgBinder;
import com.android.theupdates.webapi.CustomWebResponse;
import com.android.theupdates.webapi.WebResponse;
import com.android.theupdates.webapi.WebResponseList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

import static android.widget.AbsListView.OnScrollListener.SCROLL_STATE_IDLE;
import static com.android.theupdates.constants.Constants.UPDATE_OBJ;

/**
 * Created by osamarahat on 14/10/2016.
 */

public class WeatherUserFragment extends BaseFragment implements View.OnClickListener, commentCallBack, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.lstViwUpdateMsg)
    ListView lstViwUpdateMsg;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.viwHorizontalBar)
    View viwHorizontalBar;
    @BindView(R.id.txtShareUpdate)
    TextView txtShareUpdate;
    @BindView(R.id.relShareContainer)
    RelativeLayout relShareContainer;

    WebResponseList<PostItem> webResponse;
    ArrayListAdapter<PostItem> listAdapter;
    ArrayListAdapter<PostItem> listSearchAdapter;

    public int currentVisibleItemCount = 0;
    public int currentFirstVisibleItem;
    public int currentScrollState;
    private int offset = 1;
    private int offsetSearch = 1;
    private SideBarItem sideBarItemObj;

    public boolean isUpdateFragment = false;

    public static WeatherUserFragment getInstance(SideBarItem sideBarItemObj) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(UPDATE_OBJ, sideBarItemObj);
        WeatherUserFragment group = new WeatherUserFragment();
        group.setArguments(bundle);
        return group;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        sideBarItemObj = (SideBarItem) getArguments().get(UPDATE_OBJ);

        listAdapter = new ArrayListAdapter<PostItem>(getMainActivityContext(), new UpdateMsgBinder(getMainActivityContext(), this));
        listSearchAdapter = new ArrayListAdapter<PostItem>(getMainActivityContext(), new UpdateMsgBinder(getMainActivityContext(), this));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_groupinfo, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        swipeRefreshLayout.setOnRefreshListener(this);

        relShareContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sideBarItemObj.getPPR().equalsIgnoreCase("3")) {
                    UIHelper.showLongToastInCenter(getMainActivityContext(), "Permission required for post creation..");
                    return;
                }
                ShareUpdateFragment updateFragment = ShareUpdateFragment.getInstance(sideBarItemObj);
                updateFragment.setOnUpdateFragment(new iGenericCallBack() {
                    @Override
                    public void iBackResult(boolean isApply) {
                        isUpdateFragment = true;
                    }
                });
                getMainActivityContext().addFragmentWithFlipAnimation(updateFragment.getClass().getName(), updateFragment);

            }
        });

        txtShareUpdate.setText(sideBarItemObj.getLabelStatusUpdates());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            viwHorizontalBar.setBackgroundColor(getMainActivityContext().getResources().getColor(sideBarItemObj.getColorBackground(), null));
        } else {
            viwHorizontalBar.setBackgroundColor(getMainActivityContext().getResources().getColor(sideBarItemObj.getColorBackground()));
        }


        lstViwUpdateMsg.setOnScrollListener(new AbsListView.OnScrollListener() {

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

        if (searchView != null && !(searchView.isIconified())) {
            lstViwUpdateMsg.setAdapter(listSearchAdapter);
        }
        else {
            lstViwUpdateMsg.setAdapter(listAdapter);
        }

        lstViwUpdateMsg.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

        if (listAdapter.getCount() == 0 || isUpdateFragment) {
            getMainActivityContext().onLoadingStarted();
            offset = 1;
            isUpdateFragment = false;
            getPostList();
        }
        else if (isUpdateFragment && listAdapter.getList().size() > 0) {
            isUpdateFragment = false;
            getMainActivityContext().onLoadingStarted();
            offset = 1;
            getPostList();
        }

    }

    private void isScrollCompleted() {
        if (this.currentVisibleItemCount > listAdapter.getList().size() && this.currentScrollState == SCROLL_STATE_IDLE) {
            currentVisibleItemCount = 0;
            if (searchView != null && searchView.isIconified())
                getPostList();
            else
                getSearchList(this.query);

        }
    }


    private void getPostList() {

        Call<WebResponseList<PostItem>> response = getWebServiceInstance().getPosts(getUserId(), sideBarItemObj.getId() + "", offset,"1");
        response.enqueue(new CustomWebResponse<WebResponseList<PostItem>>(getMainActivityContext()) {
            @Override
            public void onResponse(Call<WebResponseList<PostItem>> call, Response<WebResponseList<PostItem>> response) {
                getMainActivityContext().onLoadingFinished();
                WebResponseList<PostItem> webResponse = response.body();
                if (webResponse != null && webResponse.isSucceed(response, getMainActivityContext())) {
                    if (offset == 1) {
                        listAdapter.getList().clear();
                    }


                    //IF DATA CONTAIN PIN..
                    if(offset==1 &&webResponse.getPinned().size() > 0)
                    {
                        listAdapter.addAll(webResponse.getPinned(),0);
                    }
                    listAdapter.addAll(webResponse.getData());

                    swipeRefreshLayout.setRefreshing(false);
                    offset++;
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                    if (webResponse != null && getMainActivityContext() != null && !(webResponse.isSucceed(response, getMainActivityContext()))) {
                        UIHelper.showLongToastInCenter(getMainActivityContext(), webResponse.getMessage());
                    }
                }

            }

            @Override
            public void onFailure(Call<WebResponseList<PostItem>> call, Throwable t) {
                super.onFailure(call, t);
                swipeRefreshLayout.setRefreshing(false);
            }
        });


    }

    private void getSearchList(String query) {

        Call<WebResponseList<PostItem>> response = getWebServiceInstance().searchPost(getUserId(), sideBarItemObj.getId() + "", offsetSearch, query);
        response.enqueue(new CustomWebResponse<WebResponseList<PostItem>>(getMainActivityContext()) {
            @Override
            public void onResponse(Call<WebResponseList<PostItem>> call, Response<WebResponseList<PostItem>> response) {
                getMainActivityContext().onLoadingFinished();
                WebResponseList<PostItem> webResponse = response.body();
                if (webResponse != null && webResponse.isSucceed(response, getMainActivityContext())) {
                    if (offsetSearch == 1) {
                        listSearchAdapter.getList().clear();
                    }
                    listSearchAdapter.addAll(webResponse.getData());
                    swipeRefreshLayout.setRefreshing(false);
                    offsetSearch++;
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                    if (webResponse != null && getMainActivityContext() != null && !(webResponse.isSucceed(response, getMainActivityContext()))) {
                        UIHelper.showLongToastInCenter(getMainActivityContext(), webResponse.getMessage());
                    }
                }

            }

            @Override
            public void onFailure(Call<WebResponseList<PostItem>> call, Throwable t) {
                super.onFailure(call, t);
                swipeRefreshLayout.setRefreshing(false);
            }
        });


    }


    Toolbar toolbar = null;

    @Override
    public void setTitleBar(Toolbar toolbar) {
//        this.toolbar = toolbar;
//        getMainActivityContext().mainToolbarTitle.setText(sideBarItemObj.getStrCaption());
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            toolbar.setBackgroundColor(getMainActivityContext().getResources().getColor(sideBarItemObj.getColorBackground(), null));
//        } else {
//            toolbar.setBackgroundColor(getMainActivityContext().getResources().getColor(sideBarItemObj.getColorBackground()));
//        }
//        toolbar.setNavigationIcon(R.drawable.menu);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                getMainActivityContext().drawer.openDrawer(GravityCompat.START);
//            }
//        });
//        setStatusBarColor(sideBarItemObj.getColorBackground());

    }


    int postObjIndex = 0;

    @Override
    public void onClick(View view) {

        postObjIndex = (int) view.getTag();
        PostItem postItemObj = listAdapter.getList().get(postObjIndex);
        switch (view.getId()) {
            case R.id.imgUser:
                UserProfileFragment profileFragment = UserProfileFragment.getInstance(postItemObj.getPostByUserId());
                getMainActivityContext().addFragmentWithFlipAnimation(profileFragment.getClass().getName(), profileFragment);
                break;

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
                ReportDialogFragemnt dialogFragemnt = ReportDialogFragemnt.newInstance(postItemObj.getPostId(),postItemObj.getPostByUserId());
                dialogFragemnt.setOnReportCallBackDialogListener(new ReportCallBack() {
                    @Override
                    public void reportUserNPost(String postId, String toUserId,String reason) {
                        reportUserPost(postId,toUserId);

                    }
                });
                dialogFragemnt.show(getFragmentManager(),dialogFragemnt.getClass().getName());
                break;


        }
    }

    private void setInstanceSliderFragment(PostItem postItemObj, int index) {
        ImageSliderFragment sliderFragment = ImageSliderFragment.getInstance(postItemObj, index);
        getMainActivityContext().addFragmentWithAnimation(sliderFragment.getClass().getName(), sliderFragment);
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
            if (searchView != null && !(searchView.isIconified())) {
                listSearchAdapter.getList().get(postObjIndex).setTotalComments(commentCount + "");
                listSearchAdapter.notifyDataSetChanged();
            }
            else {
                listAdapter.getList().get(postObjIndex).setTotalComments(commentCount + "");
                listAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onRefresh() {
        offset = 1;
        offsetSearch = 1;
        swipeRefreshLayout.setRefreshing(true);
        if (searchView != null && !(searchView.isIconified())) {
            getSearchList(query);
        } else
            getPostList();

    }

    private SearchView searchView;
    private SearchView.OnQueryTextListener queryTextListener;
    private String query;

    @Override
    public void onCreateOptionsMenu(final Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_search, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();

        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
            searchView.setQueryHint("Search in "+sideBarItemObj.getStrCaption());
            queryTextListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextChange(String query) {
                    return true;
                }

                @Override
                public boolean onQueryTextSubmit(String queries) {
                    query = queries;
                    offsetSearch = 1;
                    lstViwUpdateMsg.setAdapter(listSearchAdapter);
                    searchItem.collapseActionView();
                    getMainActivityContext().onLoadingStarted();
                    getSearchList(query);
                    UIHelper.hideSoftKeyboard(getMainActivityContext(), searchView);
                    return true;
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);

            searchView.setOnCloseListener(new SearchView.OnCloseListener() {
                @Override
                public boolean onClose() {
                    searchItem.collapseActionView();
                    lstViwUpdateMsg.setAdapter(null);
                    lstViwUpdateMsg.setAdapter(listAdapter);
                    listAdapter.notifyDataSetChanged();
                    return false;
                }
            });
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        searchView.setOnQueryTextListener(queryTextListener);
        return super.onOptionsItemSelected(item);
    }


}
