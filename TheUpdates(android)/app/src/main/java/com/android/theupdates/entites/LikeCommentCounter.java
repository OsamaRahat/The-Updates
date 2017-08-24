package com.android.theupdates.entites;

/**
 * Created by osamarahat on 01/11/2016.
 */

public class LikeCommentCounter {
    private int TotalLiked;
    private int TotalComments;
    private int TotalCommentLikes;

    public int getTotalCommentLikes() {
        return TotalCommentLikes;
    }

    public void setTotalCommentLikes(int totalCommentLikes) {
        TotalCommentLikes = totalCommentLikes;
    }

    public int getTotalLiked() {
        return TotalLiked;
    }

    public void setTotalLiked(int totalLiked) {
        TotalLiked = totalLiked;
    }

    public int getTotalComments() {
        return TotalComments;
    }

    public void setTotalComments(int totalComments) {
        TotalComments = totalComments;
    }
}
