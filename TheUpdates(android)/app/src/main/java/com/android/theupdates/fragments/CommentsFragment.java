package com.android.theupdates.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.theupdates.R;
import com.android.theupdates.adapter.ArrayListAdapter;
import com.android.theupdates.callbacks.commentCallBack;
import com.android.theupdates.entites.CommentPost;
import com.android.theupdates.entites.LikeCommentCounter;
import com.android.theupdates.entites.PostItem;
import com.android.theupdates.helper.UIHelper;
import com.android.theupdates.viewbinder.CommentMsgBinder;
import com.android.theupdates.webapi.CustomWebResponse;
import com.android.theupdates.webapi.WebResponse;
import com.android.theupdates.webapi.WebResponseList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

import static com.android.theupdates.constants.Constants.POST_ID;
import static com.android.theupdates.constants.Constants.UPDATE_OBJ;

/**
 * Created by osamarahat on 14/10/2016.
 */

public class CommentsFragment extends BaseFragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {


    @BindView(R.id.lstViwComments)
    ListView lstViwComments;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    ArrayListAdapter<CommentPost> listAdapter;
    @BindView(R.id.txtPostComment)
    EditText txtPostComment;
    @BindView(R.id.txtPost)
    TextView txtPost;

    private PostItem postItem;
    private commentCallBack commentCallBack;

    private int offset = 1;

    private String postId;

    public static CommentsFragment getInstance(String postId, PostItem postItem) {
        Bundle bundle = new Bundle();
        bundle.putString(POST_ID, postId);
        bundle.putParcelable(UPDATE_OBJ, postItem);
        CommentsFragment fragment = new CommentsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postId = getArguments().getString(POST_ID);
        postItem = (PostItem) getArguments().get(UPDATE_OBJ);
        listAdapter = new ArrayListAdapter<CommentPost>(getMainActivityContext(), new CommentMsgBinder(getMainActivityContext(),this));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comments, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txtPost.setBackgroundColor(Color.parseColor(postItem.getPostGroupHexColor()));
        swipeRefreshLayout.setOnRefreshListener(this);
        lstViwComments.setAdapter(listAdapter);
        txtPost.setOnClickListener(this);

        if (listAdapter.getCount() == 0) {
            getMainActivityContext().onLoadingStarted();
            getCommentList();
        }
    }

    public void setOnCommentListener(commentCallBack commentCallBack)
    {
        this.commentCallBack = commentCallBack;
    }

    private void getCommentList() {

        Call<WebResponseList<CommentPost>> response = getWebServiceInstance().getComments(getUserId(), postId, offset);
        response.enqueue(new CustomWebResponse<WebResponseList<CommentPost>>(getMainActivityContext()) {
            @Override
            public void onResponse(Call<WebResponseList<CommentPost>> call, Response<WebResponseList<CommentPost>> response) {
                getMainActivityContext().onLoadingFinished();
                WebResponseList<CommentPost> webResponse = response.body();
                if (webResponse != null && webResponse.isSucceed(response, getMainActivityContext())) {
                    listAdapter.addAll(webResponse.getData(), 0);
                    swipeRefreshLayout.setRefreshing(false);
                    offset++;
                }
                else
                {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<WebResponseList<CommentPost>> call, Throwable t) {
                super.onFailure(call, t);
                swipeRefreshLayout.setRefreshing(false);
            }
        });


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.txtCountLikes:
                CommentPost commentPostObj = listAdapter.getList().get((Integer) view.getTag());
                setLikeComment(postItem.getPostId(),commentPostObj.getPostCommentId(),commentPostObj.getIsLikedByMe().equalsIgnoreCase("0")?1:0,(Integer) view.getTag());
                break;

            //TODO GO TO PROFILE..
            case R.id.imgUser:
                break;

            case R.id.txtPost:
                if(userNameValidation(txtPostComment)) {
                    UIHelper.hideSoftKeyboard(getMainActivityContext(),view);
                    postComment(postId, txtPostComment.getText().toString());
                }
                break;
        }
    }

    private void setLikeComment(String postId, String commentId, final int liked, final int index) {

        getMainActivityContext().onLoadingStarted();
        Call<WebResponse<LikeCommentCounter>> response = getWebServiceInstance().setLikeOnComments(getUserId(), postId, commentId, liked);
        response.enqueue(new CustomWebResponse<WebResponse<LikeCommentCounter>>(getMainActivityContext()) {
            @Override
            public void onResponse(Call<WebResponse<LikeCommentCounter>> call, Response<WebResponse<LikeCommentCounter>> response) {
                getMainActivityContext().onLoadingFinished();
                WebResponse<LikeCommentCounter> webResponse = response.body();
                if (webResponse != null && webResponse.isSucceed(response, getMainActivityContext())) {
                    listAdapter.getList().get(index).setTotalCommentLikes(webResponse.getData().getTotalCommentLikes() + "");
                    listAdapter.getList().get(index).setIsLikedByMe(liked+"");
                    listAdapter.notifyDataSetChanged();
                }
            }


        });
    }

    private void postComment(String postId, String comment) {
        getMainActivityContext().onLoadingStarted();
        Call<WebResponse<LikeCommentCounter>> response = getWebServiceInstance().setCommentPost(getUserId(), postId, comment.trim());
        response.enqueue(new CustomWebResponse<WebResponse<LikeCommentCounter>>(getMainActivityContext()) {
            @Override
            public void onResponse(Call<WebResponse<LikeCommentCounter>> call, Response<WebResponse<LikeCommentCounter>> response) {
                getMainActivityContext().onLoadingFinished();
                WebResponse<LikeCommentCounter> webResponse = response.body();
                if (webResponse != null && webResponse.isSucceed(response, getMainActivityContext())) {
                    UIHelper.showLongToastInCenter(getMainActivityContext(),webResponse.getMessage());
                    commentCallBack.iUpdateCommentCount(webResponse.getData().getTotalComments());
                    getMainActivityContext().onBackPressed();

                }

            }


        });
    }

    public boolean userNameValidation(EditText edtPass) {
        if (edtPass.getText().toString().trim().length() >= 2) {

            return true;
        } else {
            edtPass.setError(getMainActivityContext().getResources().getString(R.string.name_validation));
            UIHelper.hideSoftKeyboard(getMainActivityContext(), edtPass);
            return false;
        }

    }

    @Override
    public void setTitleBar(Toolbar toolbar) {
        getMainActivityContext().mainToolbarTitle.setText("Comments");
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMainActivityContext().onBackPressed();
            }
        });

        toolbar.setBackgroundColor(Color.parseColor(postItem.getPostGroupHexColor()));
        setStatusBarColor(postItem.getPostGroupHexColor());
    }


    @Override
    public void onRefresh() {
        getCommentList();
    }
}
