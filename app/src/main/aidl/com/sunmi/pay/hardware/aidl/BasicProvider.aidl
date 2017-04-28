// BasicProvider.aidl
package com.sunmi.pay.hardware.aidl;

// Declare any non-default types here with import statements
/**
*
* @TODO 基础模块，包含：查询当前设备状态，查询SDK版本，查询设备硬件版本，查询设备固件版本，控制设备蜂鸣器，控制设备LED灯
*
*/
interface BasicProvider {
    /*1.	查询SDK版本
    返回：
        String的版本信息，如SDK Ver1.2。*/
    String getSDKVer();

    /*2.	查询设备硬件版本
    返回：
    String的版本信息，如Hardware Ver1.0。*/
    String getHardwareVer();

    /*3.	查询设备固件版本
    返回：
        String的版本信息，如Firmware Ver1.1。*/
    String getFirmwareVer();
    /*4.	设备上的蜂鸣器响
     返回：
        等于0表示成功；其他值为错误码；
     参数：
        times: 设备上的蜂鸣器响的次数，1~10。*/

    int buzzerOnDevice(int times);

    /*5.	设备上的LED灯状态
    返回：
        等于0表示成功；其他值为错误码。
    参数：
        ledIndex: 设备上的LED索引，1~4；1-红，2-绿，3-黄，4-蓝
        ledStatus：LED状态，1表示LED灭，0表示LED亮；*/
    int ledStatusOnDevice(int ledIndex, int ledStatus);

}
