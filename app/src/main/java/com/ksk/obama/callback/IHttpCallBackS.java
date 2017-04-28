package com.ksk.obama.callback;

/**
 * Created by Administrator on 2016/9/21.
 */

public interface IHttpCallBackS {
    void OnSucess(String jsonText,int n);
    void OnFail(String message);
}
