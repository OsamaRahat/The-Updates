package com.android.theupdates.entites;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by osamarahat on 16/10/2016.
 */

public class SideBarItem implements Parcelable {
    private int id;
    private String strCaption;
    private boolean isPress = false;
    private int colorBackground;
    private String PPR = "";
    private String GroupStatus;
    private String labelStatusUpdates;

    public String getLabelStatusUpdates() {
        return labelStatusUpdates;
    }

    public void setLabelStatusUpdates(String labelStatusUpdates) {
        this.labelStatusUpdates = labelStatusUpdates;
    }

    public String getPPR() {
        return PPR;
    }

    public void setPPR(String PPR) {
        this.PPR = PPR;
    }

    public String getGroupStatus() {
        return GroupStatus;
    }

    public void setGroupStatus(String groupStatus) {
        GroupStatus = groupStatus;
    }

    public int getColorBackground() {
        return colorBackground;
    }

    public void setColorBackground(int colorBackground) {
        this.colorBackground = colorBackground;
    }

    public int getDrawableColor() {
        return drawableColor;
    }

    public void setDrawableColor(int drawableColor) {
        this.drawableColor = drawableColor;
    }

    private int drawableColor = 0;

    public SideBarItem(int id,String strCaption,boolean isPress,int drawableColor,int colorBackground,String labelStatusUpdates)
    {
        this.id = id;
        this.strCaption = strCaption;
        this.isPress = isPress;
        this.drawableColor = drawableColor;
        this.colorBackground = colorBackground;
        this.labelStatusUpdates = labelStatusUpdates;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStrCaption() {
        return strCaption;
    }

    public void setStrCaption(String strCaption) {
        this.strCaption = strCaption;
    }

    public boolean isPress() {
        return isPress;
    }

    public void setPress(boolean press) {
        isPress = press;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.strCaption);
        dest.writeByte(this.isPress ? (byte) 1 : (byte) 0);
        dest.writeInt(this.colorBackground);
        dest.writeInt(this.drawableColor);
        dest.writeString(this.PPR);
        dest.writeString(this.GroupStatus);
    }

    protected SideBarItem(Parcel in) {
        this.id = in.readInt();
        this.strCaption = in.readString();
        this.isPress = in.readByte() != 0;
        this.colorBackground = in.readInt();
        this.drawableColor = in.readInt();
        this.PPR = in.readString();
        this.GroupStatus = in.readString();
    }

    public static final Parcelable.Creator<SideBarItem> CREATOR = new Parcelable.Creator<SideBarItem>() {
        @Override
        public SideBarItem createFromParcel(Parcel source) {
            return new SideBarItem(source);
        }

        @Override
        public SideBarItem[] newArray(int size) {
            return new SideBarItem[size];
        }
    };
}
