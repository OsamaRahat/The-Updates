package com.android.theupdates.entites;

/**
 * Created by osamarahat on 16/10/2016.
 */

public class UpdateMsg {

    private String updateText = "";
    private boolean isPic = false;
    private int updateDrawableLine = 0;

    public UpdateMsg(String updateText,boolean isPic,int updateDrawableLine)
    {
        this.updateText = updateText;
        this.isPic = isPic;
        this.updateDrawableLine = updateDrawableLine;
    }

    public String getUpdateText() {
        return updateText;
    }

    public void setUpdateText(String updateText) {
        this.updateText = updateText;
    }

    public boolean isPic() {
        return isPic;
    }

    public void setPic(boolean pic) {
        isPic = pic;
    }

    public int getUpdateDrawableLine() {
        return updateDrawableLine;
    }

    public void setUpdateDrawableLine(int updateDrawableLine) {
        this.updateDrawableLine = updateDrawableLine;
    }
}
