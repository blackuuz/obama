// PinPadProvider.aidl
package com.sunmi.pay.hardware.aidl;

// Declare any non-default types here with import statements

interface PinPadProvider {

    /**
     * @parame   panBlock panBlock数据
     * @parame   panBlockLength panBlock数据长度
     * @parame   pinBlock 银联标准算法的pinBlock数据
     */
    int inputOnlinePIN(inout byte[] panBlock, int panBlockLength, inout byte[] pinBlock);
}
