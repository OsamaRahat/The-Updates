package com.android.theupdates.entites;

/**
 * Created by osamarahat on 16/10/2016.
 */

public class CommentMsg {

    private String updateText = "";
    private int updateDrawableLine = 0;

    public CommentMsg(String updateText, int updateDrawableLine)
    {
        this.updateText = updateText;
        this.updateDrawableLine = updateDrawableLine;
    }

    public String getUpdateText() {
        return updateText;
    }

    public void setUpdateText(String updateText) {
        this.updateText = updateText;
    }


    public int getUpdateDrawableLine() {
        return updateDrawableLine;
    }

    public void setUpdateDrawableLine(int updateDrawableLine) {
        this.updateDrawableLine = updateDrawableLine;
    }
}
