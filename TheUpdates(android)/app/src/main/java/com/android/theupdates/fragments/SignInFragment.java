package com.android.theupdates.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.android.theupdates.AppLoader;
import com.android.theupdates.R;
import com.android.theupdates.callbacks.RegistrationPushNotification;
import com.android.theupdates.constants.CommonUtility;
import com.android.theupdates.entites.UserInfo;
import com.android.theupdates.helper.UIHelper;
import com.android.theupdates.webapi.CustomWebResponse;
import com.android.theupdates.webapi.WebResponse;
import com.facebook.AccessToken;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by osamarahat on 14/10/2016.
 */

public class SignInFragment extends BaseFragment implements View.OnClickListener, RegistrationPushNotification {


    @BindView(R.id.login_button)
    LoginButton loginButton;
    @BindView(R.id.imgBtnFb)
    ImageButton imgBtnFb;

    public static SignInFragment getInstance() {
        return new SignInFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signin, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getMainActivityContext().setSideMenu(false);

        imgBtnFb.setOnClickListener(this);

        loginButton.setReadPermissions("email,public_profile");
        loginButton.setFragment(this);

        AppLoader.setIsAdzAvailable(true);
        getMainActivityContext().setDisableAdz();


    }

    private void setFbBtnClick() {


        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
        loginButton.registerCallback(getMainActivityContext().callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                getMainActivityContext().onLoadingStarted();

                Set<String> str = loginResult.getAccessToken().getDeclinedPermissions();

                Call<WebResponse<UserInfo>> response = getWebServiceInstance().userSignUp(loginResult.getAccessToken().getUserId(), loginResult.getAccessToken().getToken());
                response.enqueue(new CustomWebResponse<WebResponse<UserInfo>>(getMainActivityContext()) {
                    @Override
                    public void onResponse(Call<WebResponse<UserInfo>> call, Response<WebResponse<UserInfo>> response) {

                        getMainActivityContext().onLoadingFinished();

                        if (response.body() != null && response.body().isSucceed(response, getMainActivityContext())) {
                            getMainActivityContext().preferenceHelper.putUser(response.body().getData());
                            getMainActivityContext().setNavigationList();
                            getMainActivityContext().addFragment(TermsConditionFragment.getInstance().getClass().getName(), TermsConditionFragment.getInstance());
                            registerPushNotification();
                        } else if (getMainActivityContext() != null && response.body() != null && !(response.body().isSucceed(response, getMainActivityContext()))) {
                            UIHelper.showLongToastInCenter(getMainActivityContext(), response.body().getMessage());

                        }
                    }

                    @Override
                    public void onFailure(Call<WebResponse<UserInfo>> call, Throwable t) {
                        super.onFailure(call, t);
                    }
                });


            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException exception) {

                UIHelper.showLongToastInCenter(getMainActivityContext(), exception.getMessage());

                if (exception instanceof FacebookAuthorizationException) {
                    if (AccessToken.getCurrentAccessToken() != null) {
                        LoginManager.getInstance().logOut();
                    }
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getMainActivityContext().callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBtnFb:
                if (!CommonUtility.isOnline(getMainActivityContext())) {
                    UIHelper.showLongToastInCenter(getMainActivityContext(), "No Internet Found..");
                    return;
                }
                if (getMainActivityContext().preferenceHelper.getUser() != null) {
                    getMainActivityContext().addFragment(TermsConditionFragment.getInstance().getClass().getName(), TermsConditionFragment.getInstance());

                } else {

                    setFbBtnClick();
                }
                break;
        }
    }

    @Override
    public void setTitleBar(Toolbar toolbar) {
        getMainActivityContext().mainToolbarTitle.setText("Sign In");
        toolbar.setBackgroundResource(R.color.green_app);
        setStatusBarColor(R.color.green_app);
        toolbar.setNavigationIcon(null);
    }


    @Override
    public void registerPushNotification() {

        CommonUtility.sendDeviceToken(getMainActivityContext());
    }


}
