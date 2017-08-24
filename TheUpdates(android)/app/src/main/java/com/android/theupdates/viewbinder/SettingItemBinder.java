package com.android.theupdates.viewbinder;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.android.theupdates.R;

/**
 * Created by osamarahat on 16/10/2016.
 */

public class SettingItemBinder extends ViewBinder<String> {

    public SettingItemBinder() {
        super(R.layout.list_item_settings);
    }

    @Override
    public BaseViewHolder createViewHolder(View view) {
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.txtSettingLabel = (TextView) view.findViewById(R.id.txtSettingLabel);
        return viewHolder;
    }

    @Override
    public void bindView(String entity, int position, int grpPosition, View view, Activity activity) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        viewHolder.txtSettingLabel.setText(entity);
    }


    public class ViewHolder extends BaseViewHolder {
        private TextView txtSettingLabel;

    }
}
