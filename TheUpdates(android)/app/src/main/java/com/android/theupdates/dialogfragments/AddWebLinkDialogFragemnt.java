package com.android.theupdates.dialogfragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.android.theupdates.R;
import com.android.theupdates.callbacks.WebLinkCallBack;
import com.android.theupdates.helper.UIHelper;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by osamarahat on 27/04/2015.
 */
public class AddWebLinkDialogFragemnt extends DialogFragment implements View.OnClickListener {

    @BindView(R.id.txtMsg)
    EditText txtMsg;
    @BindView(R.id.btnDone)
    TextView btnDone;
    @BindView(R.id.btnCancel)
    TextView btnCancel;

    WebLinkCallBack webLinkCallBack;


    public static AddWebLinkDialogFragemnt newInstance() {
        AddWebLinkDialogFragemnt dialogFragemnt = new AddWebLinkDialogFragemnt();
        return dialogFragemnt;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public void setOnAddWebLinkDialogListener(WebLinkCallBack webLinkCallBack) {
        this.webLinkCallBack = webLinkCallBack;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Dialog_NoActionBar);
        View view = inflater.inflate(R.layout.dialogfragment_addweblink, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnDone.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnDone:
                if (txtMsg.getText().toString().length() > 0) {
                    webLinkCallBack.getWebLink(txtMsg.getText().toString());
                    dismiss();

                } else {
                    UIHelper.showLongToastInCenter(getActivity(), "Enter Web Url..");
                }
                break;

            case R.id.btnCancel:
                dismiss();
                break;
        }
    }



}
