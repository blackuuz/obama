// DeviceProvide.aidl
package com.sunmi.pay.hardware.aidl;

import com.sunmi.pay.hardware.aidl.BasicProvider;
import com.sunmi.pay.hardware.aidl.ReadCardProvider;
import com.sunmi.pay.hardware.aidl.SecurityProvider;
import com.sunmi.pay.hardware.aidl.UpdateProvider;
import com.sunmi.pay.hardware.aidl.PinPadProvider;
import com.sunmi.pay.hardware.aidl.TransactionListener;
import com.sunmi.pay.hardware.aidl.EMVProvider;

// Declare any non-default types here with import statements

/**
*
* TODO 获取设备操作的统一句柄，返回接口实现类
*
*/
interface DeviceProvide {

    /**获取基础操作模块*/
    BasicProvider getBasicProvider();
    /**获取读卡模块*/
    ReadCardProvider getReadCardProvider();
    /**获取安全相关模块*/
    SecurityProvider getSecurityProvider();
    /**获取更新操作模块*/
    UpdateProvider getUpdateProvider();
    /**获取PinPad操作模块*/
    PinPadProvider getPinPadProvider();
    /**获取EMV操作模块*/
    EMVProvider getEMVProvider();
    /**注册交易回调*/
    void registerTransactionCallback(TransactionListener listener);
    /**反注册交易回调*/
    void unRegisterTransactionCallback(TransactionListener listener);
}
