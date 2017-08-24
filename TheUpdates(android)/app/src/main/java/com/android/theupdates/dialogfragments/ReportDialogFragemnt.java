package com.android.theupdates.dialogfragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.theupdates.R;
import com.android.theupdates.callbacks.ReportCallBack;
import com.android.theupdates.constants.Constants;
import com.android.theupdates.helper.UIHelper;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by osamarahat on 27/04/2015.
 */
public class ReportDialogFragemnt extends DialogFragment implements View.OnClickListener {

    @BindView(R.id.btnCancel)
    TextView btnCancel;
    @BindView(R.id.rdBtnUser)
    RadioButton rdBtnUser;
    @BindView(R.id.rdBtnPost)
    RadioButton rdBtnPost;
    @BindView(R.id.rdGrpReport)
    RadioGroup rdGrpReport;

    ReportCallBack iGenericCallBack;
    @BindView(R.id.edtReportComment)
    EditText edtReportComment;
    @BindView(R.id.btnDone)
    TextView btnDone;

    private String postId = "";
    private String toUserId;

    private boolean isHidePostReport = false;

    public static ReportDialogFragemnt newInstance(String postId, String toUserId) {
        ReportDialogFragemnt dialogFragemnt = new ReportDialogFragemnt();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.POST_ID, postId);
        bundle.putString(Constants.USER_ID, toUserId);
        dialogFragemnt.setArguments(bundle);
        return dialogFragemnt;
    }

    public static ReportDialogFragemnt newInstance(String postId, String toUserId, boolean isHidePostReport) {
        ReportDialogFragemnt dialogFragemnt = new ReportDialogFragemnt();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.POST_ID, postId);
        bundle.putString(Constants.USER_ID, toUserId);
        bundle.putBoolean(Constants.IS_SHOWN, isHidePostReport);
        dialogFragemnt.setArguments(bundle);
        return dialogFragemnt;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postId = getArguments().getString(Constants.POST_ID);
        toUserId = getArguments().getString(Constants.USER_ID);
        isHidePostReport = getArguments().containsKey(Constants.IS_SHOWN) ? getArguments().getBoolean(Constants.IS_SHOWN) : false;
    }

    public void setOnReportCallBackDialogListener(ReportCallBack iGenericCallBack) {
        this.iGenericCallBack = iGenericCallBack;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Dialog_NoActionBar);
        View view = inflater.inflate(R.layout.dialogfragment_report, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rdGrpReport.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rdBtnPost:
                        ReportUserPostDialogFragemnt postDialogFragemnt = ReportUserPostDialogFragemnt.newInstance(postId, toUserId, "Are you sure you want to report this post to the admin?");
                        postDialogFragemnt.setOnReportCallBackDialogListener(iGenericCallBack);
                        postDialogFragemnt.show(getFragmentManager(), postDialogFragemnt.getClass().getName());
                        dismiss();
                        break;
                    case R.id.rdBtnUser:
                        rdBtnUser.setVisibility(View.GONE);
                        edtReportComment.setVisibility(View.VISIBLE);
                        btnDone.setVisibility(View.VISIBLE);
                        break;
                }

            }
        });
        btnCancel.setOnClickListener(this);
        btnDone.setOnClickListener(this);

        if (isHidePostReport) {
            rdBtnPost.setVisibility(View.GONE);
            rdBtnUser.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCancel:
                dismiss();
                break;
            case R.id.btnDone:
                if (reasonTxtValidation(edtReportComment)) {
                    ReportUserPostDialogFragemnt userDialogFragemnt = ReportUserPostDialogFragemnt.newInstance(postId, toUserId, "Are you sure you want to report this user to admin?",edtReportComment.getText().toString());
                    userDialogFragemnt.setOnReportCallBackDialogListener(iGenericCallBack);
                    userDialogFragemnt.show(getFragmentManager(), userDialogFragemnt.getClass().getName());
                    dismiss();
                }

                break;
        }
    }

    public boolean reasonTxtValidation(EditText edtPass) {
        if (edtPass.getText().toString().trim().length() >= 2) {

            return true;
        } else {
            edtPass.setError(getResources().getString(R.string.name_validation));
            UIHelper.hideSoftKeyboard(getActivity(), edtPass);
            return false;
        }

    }


}
