// UpdateProvider.aidl
package com.sunmi.pay.hardware.aidl;

// Declare any non-default types here with import statements
/**
*
*TODO 更新相关接口 更新终端参数(deivce 专有)，
*
*
*/

interface UpdateProvider {

    /**
      * 更新设备固件
      * @param filePath 固件的文件完整路径；
      */
    void updateFirmware(String filePath);
}








