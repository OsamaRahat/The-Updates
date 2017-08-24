package com.android.theupdates.entites;

import java.util.ArrayList;

/**
 * Created by osamarahat on 06/11/2016.
 */

public class UserProfile {

    private UserInfo user;
    private ArrayList<PostItem> posts;
    private String followers;
    private String following;
    private String totalposts;

    public String getFollowers() {
        return followers;
    }

    public void setFollowers(String followers) {
        this.followers = followers;
    }

    public String getFollowing() {
        return following;
    }

    public void setFollowing(String following) {
        this.following = following;
    }

    public String getTotalposts() {
        return totalposts;
    }

    public void setTotalposts(String totalposts) {
        this.totalposts = totalposts;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    public ArrayList<PostItem> getPosts() {
        return posts==null?new ArrayList<PostItem>():posts;
    }

    public void setPosts(ArrayList<PostItem> posts) {
        this.posts = posts;
    }
}
