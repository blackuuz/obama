package com.ksk.obama.model;

/**
 * Created by djy on 2017/3/16.
 */

public class BlueTool {
    private String name;
    private String address;
    private boolean isConnect = false;
    private boolean isMate = false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isConnect() {
        return isConnect;
    }

    public void setConnect(boolean connect) {
        isConnect = connect;
    }

    public boolean isMate() {
        return isMate;
    }

    public void setMate(boolean mate) {
        isMate = mate;
    }
}
