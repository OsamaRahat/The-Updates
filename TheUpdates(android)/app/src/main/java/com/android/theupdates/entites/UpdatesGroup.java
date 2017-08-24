package com.android.theupdates.entites;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by osamarahat on 23/10/2016.
 */

public class UpdatesGroup implements Parcelable {

    private String GroupId;
    private String GroupName;
    private String GroupSlug;
    private String GroupColor;
    private String GroupStatus;
    private String GroupHexColor;
    private String PPR;

    public String getPPR() {
        return PPR;
    }

    public void setPPR(String PPR) {
        this.PPR = PPR;
    }

    public String getGroupHexColor() {
        return "#"+GroupHexColor;
    }

    public void setGroupHexColor(String groupHexColor) {
        GroupHexColor = groupHexColor;
    }

    public String getGroupId() {
        return GroupId;
    }

    public void setGroupId(String groupId) {
        GroupId = groupId;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }

    public String getGroupSlug() {
        return GroupSlug;
    }

    public void setGroupSlug(String groupSlug) {
        GroupSlug = groupSlug;
    }

    public String getGroupColor() {
        return GroupColor;
    }

    public void setGroupColor(String groupColor) {
        GroupColor = groupColor;
    }

    public String getGroupStatus() {
        return GroupStatus;
    }

    public void setGroupStatus(String groupStatus) {
        GroupStatus = groupStatus;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.GroupId);
        dest.writeString(this.GroupName);
        dest.writeString(this.GroupSlug);
        dest.writeString(this.GroupColor);
        dest.writeString(this.GroupStatus);
        dest.writeString(this.PPR);
    }

    public UpdatesGroup() {
    }

    protected UpdatesGroup(Parcel in) {
        this.GroupId = in.readString();
        this.GroupName = in.readString();
        this.GroupSlug = in.readString();
        this.GroupColor = in.readString();
        this.GroupStatus = in.readString();
        this.PPR = in.readString();
    }

    public static final Parcelable.Creator<UpdatesGroup> CREATOR = new Parcelable.Creator<UpdatesGroup>() {
        @Override
        public UpdatesGroup createFromParcel(Parcel source) {
            return new UpdatesGroup(source);
        }

        @Override
        public UpdatesGroup[] newArray(int size) {
            return new UpdatesGroup[size];
        }
    };
}
