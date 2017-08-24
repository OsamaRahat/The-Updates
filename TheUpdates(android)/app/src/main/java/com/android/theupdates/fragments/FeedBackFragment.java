package com.android.theupdates.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.android.theupdates.R;
import com.android.theupdates.helper.UIHelper;
import com.android.theupdates.webapi.CustomWebResponse;
import com.android.theupdates.webapi.WebResponse;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by osamarahat on 14/10/2016.
 */

public class FeedBackFragment extends BaseFragment implements View.OnClickListener {


    @BindView(R.id.txtName)
    EditText txtName;
    @BindView(R.id.txtEmail)
    EditText txtEmail;
    @BindView(R.id.txtComments)
    EditText txtComments;
    @BindView(R.id.txtBtnSubmit)
    TextView txtBtnSubmit;

    public static FeedBackFragment getInstance() {
        return new FeedBackFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feedback, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtBtnSubmit.setOnClickListener(this);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getMainActivityContext().callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txtBtnSubmit:
                submitFeedBack();
                break;
        }
    }

    private void submitFeedBack() {
        if (userNameValidation(txtName)) {
            if (emailValidator(txtEmail)) {
                if (userNameValidation(txtComments)) {

                    getMainActivityContext().onLoadingStarted();

                    Call<WebResponse<String>> response = getWebServiceInstance().sendFeedBack(getUserId(),getEditTxt(txtName), getEditTxt(txtEmail), getEditTxt(txtComments));
                    response.enqueue(new CustomWebResponse<WebResponse<String>>(getMainActivityContext()) {
                        @Override
                        public void onResponse(Call<WebResponse<String>> call, Response<WebResponse<String>> response) {

                            getMainActivityContext().onLoadingFinished();

                            if (response.body() != null && response.body().isSucceed(response, getMainActivityContext())) {
                                UIHelper.showLongToastInCenter(getMainActivityContext(), response.body().getData());
                                getMainActivityContext().onBackPressed();
                            }
                        }

                        @Override
                        public void onFailure(Call<WebResponse<String>> call, Throwable t) {
                            super.onFailure(call, t);
                        }
                    });
                }
            }

        }
    }

    private void setEditTextDrawable() {
        txtName.setError(null, null);
        txtEmail.setError(null, null);
        txtComments.setError(null, null);
    }

    @Override
    public void setTitleBar(Toolbar toolbar) {
        getMainActivityContext().mainToolbarTitle.setText("Feedback");
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

    public boolean emailValidator(EditText autoTxt) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(autoTxt.getText().toString().trim());
        if (autoTxt.getText().toString().isEmpty()) {
            autoTxt.setError(getMainActivityContext().getResources().getString(R.string.validation_email_invalid));
            UIHelper.hideSoftKeyboard(getMainActivityContext(), autoTxt);
            return false;
        } else if (matcher.matches() == false) {
            autoTxt.setError(getMainActivityContext().getResources().getString(R.string.validation_email_invalid));
            UIHelper.hideSoftKeyboard(getMainActivityContext(), autoTxt);
            return false;
        } else
            return matcher.matches();
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
}
