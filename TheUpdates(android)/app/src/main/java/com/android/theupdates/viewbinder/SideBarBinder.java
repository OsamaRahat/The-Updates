package com.android.theupdates.viewbinder;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.theupdates.R;
import com.android.theupdates.entites.SideBarItem;

/**
 * Created by osamarahat on 16/10/2016.
 */

public class SideBarBinder extends ViewBinder<SideBarItem> {

    public SideBarBinder() {
        super(R.layout.list_item_sidebar);
    }

    @Override
    public BaseViewHolder createViewHolder(View view) {
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.imgDot = (ImageView) view.findViewById(R.id.imgUpdate);
        viewHolder.txtSideBarCaption = (TextView)view.findViewById(R.id.txtSideUpdate);
        viewHolder.sideBarSeparator = view.findViewById(R.id.sideBarSeparator);
        return viewHolder;
    }

    @Override
    public void bindView(SideBarItem entity, int position, int grpPosition, View view, Activity activity) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        viewHolder.txtSideBarCaption.setText(entity.getStrCaption());
        viewHolder.imgDot.setImageResource(entity.getDrawableColor());

        if(entity.getDrawableColor() == R.drawable.darkblue_dots_siderbar)
        {
            viewHolder.sideBarSeparator.setVisibility(View.INVISIBLE);
        }
        else
        {
            viewHolder.sideBarSeparator.setVisibility(View.VISIBLE);
        }
    }


    public class ViewHolder extends BaseViewHolder
    {
        private ImageView imgDot;
        private TextView txtSideBarCaption;
        private View sideBarSeparator;

    }
}
