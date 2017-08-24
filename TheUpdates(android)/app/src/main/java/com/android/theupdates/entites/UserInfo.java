package com.android.theupdates.entites;

import android.text.TextUtils;

/**
 * Created by osamarahat on 22/10/2016.
 */

public class UserInfo {

    private String UserId;
    private String UserFbId;
    private String UserFirstName;
    private String UserLastName;
    private String Username;
    private String UserPassword;
    private String UserEmail;
    private String UserDob;
    private String UserPhone;
    private String UserPicture;
    private String UserDeviceToken;
    private String UserDevicePlatform;
    private String UserLastLogin;
    private String UserCreatedDate;

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getUserFbId() {
        return UserFbId;
    }

    public void setUserFbId(String userFbId) {
        UserFbId = userFbId;
    }

    public String getUserFirstName() {
        return TextUtils.isEmpty(UserFirstName)?"":UserFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        UserFirstName = userFirstName;
    }

    public String getUserLastName() {
        return TextUtils.isEmpty(UserLastName)?"":UserLastName;
    }

    public void setUserLastName(String userLastName) {
        UserLastName = userLastName;
    }

    public String getUsername() {
        return TextUtils.isEmpty(Username)?getUserFirstName()+" "+getUserLastName():Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getUserPassword() {
        return UserPassword;
    }

    public void setUserPassword(String userPassword) {
        UserPassword = userPassword;
    }

    public String getUserEmail() {
        return UserEmail;
    }

    public void setUserEmail(String userEmail) {
        UserEmail = userEmail;
    }

    public String getUserDob() {
        return UserDob==null?"":UserDob;
    }

    public void setUserDob(String userDob) {
        UserDob = userDob;
    }

    public String getUserPhone() {
        return UserPhone==null?"":UserPhone;
    }

    public void setUserPhone(String userPhone) {
        UserPhone = userPhone;
    }

    public String getUserPicture() {
        return UserPicture==null?"":UserPicture;
    }

    public void setUserPicture(String userPicture) {
        UserPicture = userPicture;
    }

    public String getUserDeviceToken() {
        return UserDeviceToken;
    }

    public void setUserDeviceToken(String userDeviceToken) {
        UserDeviceToken = userDeviceToken;
    }

    public String getUserDevicePlatform() {
        return UserDevicePlatform;
    }

    public void setUserDevicePlatform(String userDevicePlatform) {
        UserDevicePlatform = userDevicePlatform;
    }

    public String getUserLastLogin() {
        return UserLastLogin;
    }

    public void setUserLastLogin(String userLastLogin) {
        UserLastLogin = userLastLogin;
    }

    public String getUserCreatedDate() {
        return UserCreatedDate;
    }

    public void setUserCreatedDate(String userCreatedDate) {
        UserCreatedDate = userCreatedDate;
    }
}
