package com.android.theupdates.entites;

/**
 * Created by osamarahat on 06/11/2016.
 */

public class CommentPost {

    private String UserId;
    private String UserName;
    private String UserPicture;
    private String PostCommentId;
    private String PostComment;
    private String CommentDate;
    private String TotalCommentLikes;
    private String isLikedByMe;

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserPicture() {
        return UserPicture;
    }

    public void setUserPicture(String userPicture) {
        UserPicture = userPicture;
    }

    public String getPostCommentId() {
        return PostCommentId;
    }

    public void setPostCommentId(String postCommentId) {
        PostCommentId = postCommentId;
    }

    public String getPostComment() {
        return PostComment;
    }

    public void setPostComment(String postComment) {
        PostComment = postComment;
    }

    public String getCommentDate() {
        return CommentDate;
    }

    public void setCommentDate(String commentDate) {
        CommentDate = commentDate;
    }

    public String getTotalCommentLikes() {
        return TotalCommentLikes;
    }

    public void setTotalCommentLikes(String totalCommentLikes) {
        TotalCommentLikes = totalCommentLikes;
    }

    public String getIsLikedByMe() {
        return isLikedByMe;
    }

    public void setIsLikedByMe(String isLikedByMe) {
        this.isLikedByMe = isLikedByMe;
    }
}
