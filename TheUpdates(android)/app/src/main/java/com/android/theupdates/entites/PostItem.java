package com.android.theupdates.entites;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.android.theupdates.R;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by osamarahat on 01/11/2016.
 */

public class PostItem implements Parcelable {

    private String PostId;
    private String PostGroupId;
    private String PostGroupName;
    private String PostGroupColor;
    private String PostGroupHexColor;
    private String PostByUserId;
    private String PostByUserName;
    private String UserPicture;
    private String PostText;
    private String PostLink;
    private String PostVideoURL;
    private String PostDate;
    private String PostStatus;
    private String TotalLiked;
    private String TotalComments;
    private String PostImages;
    private String isLiked;
    private String PostVideo;

    public String getPostVideo() {
        return PostVideo;
    }

    public void setPostVideo(String postVideo) {
        PostVideo = postVideo;
    }

    public String getIsLiked() {
        return isLiked;
    }

    public void setIsLiked(String isLiked) {
        this.isLiked = isLiked;
    }

    public ArrayList<String> getPostImages() {

        ArrayList<String> arrImg = TextUtils.isEmpty(PostImages)?new ArrayList<String>(): new ArrayList<String>(Arrays.asList(PostImages.split(",")));
        arrImg.addAll(getPostLinkImages());
        if(!(TextUtils.isEmpty(getPostVideo())))
        {
            arrImg.add("drawable://" + R.drawable.play_button);
        }
        return arrImg;
    }

    public ArrayList<String> getPostLinkImages() {

        return TextUtils.isEmpty(PostLink)?new ArrayList<String>(): new ArrayList<String>(Arrays.asList(PostLink.split(" ")));
    }

    public void setPostImages(String postImages) {
        PostImages = postImages;
    }

    public String getPostId() {
        return PostId;
    }

    public void setPostId(String postId) {
        PostId = postId;
    }

    public String getPostGroupId() {
        return PostGroupId;
    }

    public void setPostGroupId(String postGroupId) {
        PostGroupId = postGroupId;
    }

    public String getPostGroupName() {
        return PostGroupName;
    }

    public void setPostGroupName(String postGroupName) {
        PostGroupName = postGroupName;
    }

    public String getPostGroupColor() {
        return PostGroupColor;
    }

    public void setPostGroupColor(String postGroupColor) {
        PostGroupColor = postGroupColor;
    }

    public String getPostGroupHexColor() {
        return "#"+PostGroupHexColor;
    }

    public void setPostGroupHexColor(String postGroupHexColor) {
        PostGroupHexColor = postGroupHexColor;
    }

    public String getPostByUserId() {
        return PostByUserId;
    }

    public void setPostByUserId(String postByUserId) {
        PostByUserId = postByUserId;
    }

    public String getPostByUserName() {
        return PostByUserName;
    }

    public void setPostByUserName(String postByUserName) {
        PostByUserName = postByUserName;
    }

    public String getUserPicture() {
        return UserPicture;
    }

    public void setUserPicture(String userPicture) {
        UserPicture = userPicture;
    }

    public String getPostText() {
        return PostText;
    }

    public void setPostText(String postText) {
        PostText = postText;
    }

    public String getPostLink() {
        return PostLink;
    }

    public void setPostLink(String postLink) {
        PostLink = postLink;
    }

    public String getPostVideoURL() {
        return PostVideoURL;
    }

    public void setPostVideoURL(String postVideoURL) {
        PostVideoURL = postVideoURL;
    }

    public String getPostDate() {
        return PostDate;
    }

    public void setPostDate(String postDate) {
        PostDate = postDate;
    }

    public String getPostStatus() {
        return PostStatus;
    }

    public void setPostStatus(String postStatus) {
        PostStatus = postStatus;
    }

    public String getTotalLiked() {
        return TotalLiked;
    }

    public void setTotalLiked(String totalLiked) {
        TotalLiked = totalLiked;
    }

    public String getTotalComments() {
        return TotalComments;
    }

    public void setTotalComments(String totalComments) {
        TotalComments = totalComments;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.PostId);
        dest.writeString(this.PostGroupId);
        dest.writeString(this.PostGroupName);
        dest.writeString(this.PostGroupColor);
        dest.writeString(this.PostGroupHexColor);
        dest.writeString(this.PostByUserId);
        dest.writeString(this.PostByUserName);
        dest.writeString(this.UserPicture);
        dest.writeString(this.PostText);
        dest.writeString(this.PostLink);
        dest.writeString(this.PostVideoURL);
        dest.writeString(this.PostDate);
        dest.writeString(this.PostStatus);
        dest.writeString(this.TotalLiked);
        dest.writeString(this.TotalComments);
        dest.writeString(this.PostImages);
        dest.writeString(this.isLiked);
        dest.writeString(this.PostVideo);
    }

    public PostItem() {
    }

    protected PostItem(Parcel in) {
        this.PostId = in.readString();
        this.PostGroupId = in.readString();
        this.PostGroupName = in.readString();
        this.PostGroupColor = in.readString();
        this.PostGroupHexColor = in.readString();
        this.PostByUserId = in.readString();
        this.PostByUserName = in.readString();
        this.UserPicture = in.readString();
        this.PostText = in.readString();
        this.PostLink = in.readString();
        this.PostVideoURL = in.readString();
        this.PostDate = in.readString();
        this.PostStatus = in.readString();
        this.TotalLiked = in.readString();
        this.TotalComments = in.readString();
        this.PostImages = in.readString();
        this.isLiked = in.readString();
        this.PostVideo = in.readString();
    }

    public static final Parcelable.Creator<PostItem> CREATOR = new Parcelable.Creator<PostItem>() {
        @Override
        public PostItem createFromParcel(Parcel source) {
            return new PostItem(source);
        }

        @Override
        public PostItem[] newArray(int size) {
            return new PostItem[size];
        }
    };
}
