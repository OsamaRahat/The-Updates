package com.android.theupdates.entites;

/**
 * Created by osamarahat on 14/11/2016.
 */

public class Announcement {
    private String AlertId;
    private String AlertType;
    private String AlertText;

    public String getAlertId() {
        return AlertId;
    }

    public void setAlertId(String alertId) {
        AlertId = alertId;
    }

    public String getAlertType() {
        return AlertType;
    }

    public void setAlertType(String alertType) {
        AlertType = alertType;
    }

    public String getAlertText() {
        return AlertText;
    }

    public void setAlertText(String alertText) {
        AlertText = alertText;
    }
}
