package com.caac.android.caacdevicecontrol.entity;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by z on 2016/10/4.
 */
public class Dynamic extends BmobObject {
    private String userId;
    private String userName;
    private String avatar;
    private String message;
    private List<String> images;
    private int leaveMsgCount;
    private String group;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public int getLeaveMsgCount() {
        return leaveMsgCount;
    }

    public void setLeaveMsgCount(int leaveMsgCount) {
        this.leaveMsgCount = leaveMsgCount;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
