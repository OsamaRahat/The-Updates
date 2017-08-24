package com.android.theupdates.dialogfragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.android.theupdates.R;
import com.android.theupdates.callbacks.ReportCallBack;
import com.android.theupdates.constants.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by osamarahat on 27/04/2015.
 */
public class ReportUserPostDialogFragemnt extends DialogFragment implements View.OnClickListener {

    @BindView(R.id.btnCancel)
    TextView btnCancel;
    @BindView(R.id.txtMsg)
    TextView txtMsg;
    @BindView(R.id.btnDone)
    TextView btnDone;

    ReportCallBack reportCallBack;

    private String postId = "";
    private String msg;
    private String toUserId;
    private String reason = "";

    public static ReportUserPostDialogFragemnt newInstance(String postId, String toUserId, String msg) {
        ReportUserPostDialogFragemnt dialogFragemnt = new ReportUserPostDialogFragemnt();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.TXT_MSG, msg);
        bundle.putString(Constants.POST_ID, postId);
        bundle.putString(Constants.USER_ID, toUserId);
        dialogFragemnt.setArguments(bundle);
        return dialogFragemnt;
    }

    public static ReportUserPostDialogFragemnt newInstance(String postId, String toUserId, String msg, String reason) {
        ReportUserPostDialogFragemnt dialogFragemnt = new ReportUserPostDialogFragemnt();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.TXT_MSG, msg);
        bundle.putString(Constants.POST_ID, postId);
        bundle.putString(Constants.USER_ID, toUserId);
        bundle.putString(Constants.SLUG_ABOUT, reason);
        dialogFragemnt.setArguments(bundle);
        return dialogFragemnt;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postId = getArguments().getString(Constants.POST_ID);
        msg = getArguments().getString(Constants.TXT_MSG);
        toUserId = getArguments().getString(Constants.USER_ID);
        reason = getArguments().containsKey(Constants.SLUG_ABOUT) ? getArguments().getString(Constants.SLUG_ABOUT) : "";
    }

    public void setOnReportCallBackDialogListener(ReportCallBack iGenericCallBack) {
        this.reportCallBack = iGenericCallBack;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Dialog_NoActionBar);
        View view = inflater.inflate(R.layout.dialogfragment_permission, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txtMsg.setText(msg);

        btnDone.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnDone:
                reportCallBack.reportUserNPost(postId, toUserId,reason);
                dismiss();
                break;

            case R.id.btnCancel:
                dismiss();
                break;
        }
    }


}
