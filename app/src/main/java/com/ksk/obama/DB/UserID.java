package com.ksk.obama.DB;

import org.litepal.crud.DataSupport;

/**
 * Created by djy on 2017/1/5.
 */

public class UserID extends DataSupport {
    private int id;
    private String userId = "";
    private String time = "";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "UserID{" +
                "id=" + id +
                ", UserId='" + userId + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
