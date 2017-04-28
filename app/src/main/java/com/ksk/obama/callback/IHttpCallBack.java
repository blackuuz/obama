package com.ksk.obama.callback;

/**
 * Created by Administrator on 2016/9/21.
 */

public interface IHttpCallBack {
    void OnSucess(String jsonText);
    void OnFail(String message);
}
