package com.caac.android.caacdevicecontrol.entity;

import cn.bmob.v3.BmobUser;

/**
 * Created by YHT on 2016/7/26.
 */
public class User extends BmobUser {
    private String realName;
    private String group;


    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

}
