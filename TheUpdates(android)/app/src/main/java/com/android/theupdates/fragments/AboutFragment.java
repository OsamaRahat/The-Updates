package com.android.theupdates.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.theupdates.R;
import com.android.theupdates.entites.ContentText;
import com.android.theupdates.webapi.CustomWebResponse;
import com.android.theupdates.webapi.WebResponseList;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

import static com.android.theupdates.constants.Constants.SLUG_ABOUT;

/**
 * Created by osamarahat on 14/10/2016.
 */

public class AboutFragment extends BaseFragment {


    @BindView(R.id.txtContent)
    TextView txtContent;

    WebResponseList<ContentText> webResponse;

    public static AboutFragment getInstance() {
        return new AboutFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_privacypolicy, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        if(webResponse != null)
        {
            txtContent.setText(webResponse.getData().get(0).getContentText());
            return;
        }

        getMainActivityContext().onLoadingStarted();

        Call<WebResponseList<ContentText>> response = getWebServiceInstance().getUpdatesContent(getUserId(),SLUG_ABOUT, 0);
        response.enqueue(new CustomWebResponse<WebResponseList<ContentText>>(getMainActivityContext()) {
            @Override
            public void onResponse(Call<WebResponseList<ContentText>> call, Response<WebResponseList<ContentText>> response) {

                getMainActivityContext().onLoadingFinished();
                webResponse = response.body();
                if (webResponse != null && webResponse.isSucceed(response, getMainActivityContext())) {
                    txtContent.setText(webResponse.getData().get(0).getContentText());
                }
            }

            @Override
            public void onFailure(Call<WebResponseList<ContentText>> call, Throwable t) {
                super.onFailure(call, t);
            }
        });


    }


    @Override
    public void setTitleBar(Toolbar toolbar) {
        getMainActivityContext().mainToolbarTitle.setText("About");
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.activity_main_drawer, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.action_logout:
                if (AccessToken.getCurrentAccessToken() != null) {
                    LoginManager.getInstance().logOut();
                }
                getMainActivityContext().preferenceHelper.putUser(null);
                getMainActivityContext().emptyBackStack();
                getMainActivityContext().emtpyNavigationList();
                getMainActivityContext().addFragmentOnce(SignInFragment.getInstance().getClass().getName(), SignInFragment.getInstance());
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
