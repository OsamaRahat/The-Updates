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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.theupdates.AppLoader;
import com.android.theupdates.R;
import com.android.theupdates.adapter.ArrayListAdapter;
import com.android.theupdates.callbacks.ReportCallBack;
import com.android.theupdates.callbacks.commentCallBack;
import com.android.theupdates.dialogfragments.ReportDialogFragemnt;
import com.android.theupdates.entites.Announcement;
import com.android.theupdates.entites.LikeCommentCounter;
import com.android.theupdates.entites.PostItem;
import com.android.theupdates.viewbinder.UpdateMsgBinder;
import com.android.theupdates.webapi.CustomWebResponse;
import com.android.theupdates.webapi.WebResponse;
import com.android.theupdates.webapi.WebResponseList;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

import static android.widget.AbsListView.OnScrollListener.SCROLL_STATE_IDLE;

/**
 * Created by osamarahat on 14/10/2016.
 */

public class HomeFragment extends BaseFragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener,commentCallBack {

    @BindView(R.id.lstViwUpdateMsg)
    ListView lstViwUpdateMsg;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    ArrayListAdapter<PostItem> listAdapter;
    ArrayList<Announcement> announcementArrayList;

    public int currentVisibleItemCount=0;
    public int currentFirstVisibleItem;
    public int currentScrollState;
    private int offset = 1;


    public static HomeFragment getInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
        listAdapter = new ArrayListAdapter<PostItem>(getMainActivityContext(), new UpdateMsgBinder(getMainActivityContext(),this));

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (AppLoader.isAdzAvailable()) {
            getMainActivityContext().setEnableAdz();
        }
        getMainActivityContext().setSideMenu(true);

        swipeRefreshLayout.setOnRefreshListener(this);
        lstViwUpdateMsg.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                currentScrollState = scrollState;
                isScrollCompleted();
            }

            @Override
            public void onScroll(AbsListView lw, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                currentFirstVisibleItem = firstVisibleItem;
                if((firstVisibleItem + visibleItemCount) ==  totalItemCount)
                {
                    currentVisibleItemCount = firstVisibleItem+visibleItemCount;
                }

        }
        });

        if(announcementArrayList!= null)
        {
            filterAnnouncement(announcementArrayList);
        }
        lstViwUpdateMsg.setAdapter(listAdapter);

        lstViwUpdateMsg.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

        if(listAdapter.getCount() == 0) {
            getMainActivityContext().onLoadingStarted();
            getPostList();
        }

    }

    private void isScrollCompleted() {
        if (this.currentVisibleItemCount > listAdapter.getList().size() && this.currentScrollState == SCROLL_STATE_IDLE) {
            currentVisibleItemCount = 0;
            getPostList();

        }
    }


    private void getPostList() {

        Call<WebResponseList<PostItem>> response = getWebServiceInstance().getPosts(getUserId(),"",offset);
        response.enqueue(new CustomWebResponse<WebResponseList<PostItem>>(getMainActivityContext()) {
            @Override
            public void onResponse(Call<WebResponseList<PostItem>> call, Response<WebResponseList<PostItem>> response) {
                getMainActivityContext().onLoadingFinished();
                WebResponseList<PostItem> webResponse = response.body();
                if (webResponse != null && webResponse.isSucceed(response, getMainActivityContext())) {
                    if (offset == 1) {
                        listAdapter.getList().clear();
                        announcementArrayList = webResponse.getAnnouncement();
                        filterAnnouncement(announcementArrayList);

                        getMainActivityContext().editNavigationList(webResponse.getSidebar());
                    }


                    //IF DATA CONTAIN PIN..
                    if(offset == 1 && webResponse.getPinned().size() > 0)
                    {
                        listAdapter.addAll(webResponse.getPinned());
                    }
                    if(webResponse.getData().size()>0)
                    listAdapter.addAll(webResponse.getData());


                    swipeRefreshLayout.setRefreshing(false);
                    offset++;
                }
                else
                    swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<WebResponseList<PostItem>> call, Throwable t) {
                super.onFailure(call, t);
                swipeRefreshLayout.setRefreshing(false);
            }
        });




    }

    public void filterAnnouncement(ArrayList<Announcement> announcementArrayList)
    {
        ArrayList<Announcement> tmpList = new ArrayList<>();
        tmpList.addAll(announcementArrayList);
         for(Announcement announcement : announcementArrayList)
         {
             for (String s : getMainActivityContext().preferenceHelper.getAnnouncementLst())
             {
                 if(announcement.getAlertId().equalsIgnoreCase(s))
                 {
                     tmpList.remove(announcement);
                     break;
                 }
             }
         }
        lstViwUpdateMsg.removeHeaderView(layoutAnnouncement);
        lstViwUpdateMsg.addHeaderView(attachAnnouncementToContainer(tmpList));


    }



    int postObjIndex = 0;
    @Override
    public void onClick(View view) {

        postObjIndex = (int) view.getTag();
        PostItem postItemObj = listAdapter.getList().get(postObjIndex);
        switch (view.getId()) {
            case R.id.imgUser:
                UserProfileFragment profileFragment = UserProfileFragment.getInstance(postItemObj.getPostByUserId());
                getMainActivityContext().addFragmentWithFlipAnimation(profileFragment.getClass().getName(),profileFragment);
                break;

            case R.id.imgArchieve:
                setInstanceSliderFragment(postItemObj,0);
                break;
            case R.id.imgUpdate1:
                setInstanceSliderFragment(postItemObj,1);
                break;
            case R.id.imgUpdate2:
                setInstanceSliderFragment(postItemObj,2);
                break;
            case R.id.imgUpdate3:
                setInstanceSliderFragment(postItemObj,3);
                break;

            case R.id.txtComment:
            case R.id.txtCommnetCount:
                CommentsFragment commentsFragment = CommentsFragment.getInstance(postItemObj.getPostId(),postItemObj);
                getMainActivityContext().addFragment(commentsFragment.getClass().getName(), commentsFragment);
                commentsFragment.setOnCommentListener(this);
                break;
            case R.id.txtLikesCount:
                getMainActivityContext().addFragment(LikeUserFragment.getInstance(postItemObj.getPostId()).getClass().getName(),LikeUserFragment.getInstance(postItemObj.getPostId()));
                break;
            case R.id.txtUpdateMore:
                setInstanceSliderFragment(postItemObj,0);
                break;
            case R.id.txtLikes:
                setLikePost(postItemObj.getPostId(),postItemObj.getIsLiked().equalsIgnoreCase("0")?1:0,postObjIndex);
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
            case R.id.txtLinkUpdate:
                getMainActivityContext().navigationToFragment(Integer.parseInt(postItemObj.getPostGroupId()),true);
                break;

        }
    }

    private void setInstanceSliderFragment(PostItem postItemObj,int index) {
        ImageSliderFragment sliderFragment = ImageSliderFragment.getInstance(postItemObj,index);
        getMainActivityContext().addFragmentWithAnimation(sliderFragment.getClass().getName(),sliderFragment);
    }

    @Override
    public void setTitleBar(Toolbar toolbar) {
        toolbar.setNavigationIcon(R.drawable.menu);
        getMainActivityContext().mainToolbarTitle.setVisibility(View.GONE);
        getMainActivityContext().imgTopLogo.setVisibility(View.VISIBLE);
        getMainActivityContext().toolbar.setBackgroundResource(R.color.green_app);
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
        Call<WebResponse<LikeCommentCounter>> response = getWebServiceInstance().setLikePost(getUserId(),postId,liked);
        response.enqueue(new CustomWebResponse<WebResponse<LikeCommentCounter>>(getMainActivityContext()) {
            @Override
            public void onResponse(Call<WebResponse<LikeCommentCounter>> call, Response<WebResponse<LikeCommentCounter>> response) {
                getMainActivityContext().onLoadingFinished();
                WebResponse<LikeCommentCounter> webResponse = response.body();
                if (webResponse != null && webResponse.isSucceed(response, getMainActivityContext())) {
                    listAdapter.getList().get(index).setTotalLiked(webResponse.getData().getTotalLiked()+"");
                    listAdapter.getList().get(index).setIsLiked(liked+"");
                    listAdapter.notifyDataSetChanged();
                }
            }


        });




    }

    @Override
    public void iUpdateCommentCount(int commentCount) {
        if(commentCount > 0)
        {
            listAdapter.getList().get(postObjIndex).setTotalComments(commentCount+"");
            listAdapter.notifyDataSetChanged();
        }
    }

    /**
     * only attach if offset is 1..
     */
    LinearLayout layoutAnnouncement = null;
    private LinearLayout attachAnnouncementToContainer(ArrayList<Announcement> announcementArrayList) {
        layoutAnnouncement = new LinearLayout(getMainActivityContext());
        layoutAnnouncement.setOrientation(LinearLayout.VERTICAL);
        for (int i = 0; i < announcementArrayList.size(); i++) {
            View v = LayoutInflater.from(getMainActivityContext()).inflate(R.layout.list_item_announcement, null, false);

            TextView txtAnnouncement = (TextView) v.findViewById(R.id.txtAnnouncement);
            TextView txtClose  = (TextView) v.findViewById(R.id.txtClose);

            txtAnnouncement.setText(announcementArrayList.get(i).getAlertText());

            txtClose.setTag(v);
            txtClose.setContentDescription(announcementArrayList.get(i).getAlertId());
            txtClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    layoutAnnouncement.removeView((View) view.getTag());
                    ArrayList<String> listDismiss = new ArrayList<String>();
                    listDismiss.add(String.valueOf(view.getContentDescription()));
                    getMainActivityContext().preferenceHelper.putAnnouncementLst(listDismiss);
                }
            });
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0,15,0,0);
            v.setLayoutParams(params);
            layoutAnnouncement.addView(v);
        }

        return layoutAnnouncement;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getMainActivityContext().imgTopLogo.setVisibility(View.GONE);
        getMainActivityContext().mainToolbarTitle.setVisibility(View.VISIBLE);

    }
}
