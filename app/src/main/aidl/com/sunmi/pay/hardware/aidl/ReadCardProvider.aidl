// ReadCardProvider.aidl
package com.sunmi.pay.hardware.aidl;

// Declare any non-default types here with import statements
/**
*
* @TODO 读卡器模块，包含：检卡，终止检卡，检查NFC是否移走，检查IC卡是否移走，IC卡APDU接口，NFC卡APDU接口
*
*
*/
interface ReadCardProvider {
     /**
        * 检卡
        * @param cardType 需要进行检卡的卡类型，CARDTYPE_MAG、CARDTYPE_IC、CARDTYPE_NFC，
        *                 支持检测一种卡或两种卡的组合，两种卡时采用与的方式传入，即CARDTYPE_MAG| CARDTYPE_IC；
        * @param deviceClass 具体的设备类别代码，如MPOS_DEVICE、READER_DEVICE；
        * @param appendData 具体的应用方数据；
        * @param appendDataLength 具体的应用方数据长度；
        * @param timeout 检卡超时时间，单位为秒；
        */
       void checkCard(int cardType, int deviceClass, in byte[] appendData, int appendDataLength, int timeout);

       /**
        * 中止检卡
        */
       void abortCheckCard();


       /**
        * 检查IC卡是否移走
        * @return true：IC卡还在设备中；flase：IC卡已经移走；
        */
       boolean isICRemoved ();


       /**
        * 检查NFC 卡是否移走
        * @return true：NFC卡还在设备中； flase：NFC卡已经移走；
        */
       boolean isNFCRemoved ();


       /**
        * IC卡APDU接口
        * @param tagIn APDU发送数据；
        * @param tagInLength APDU发送数据长度；
        * @param tagOut tagOut[0]等于0表示APDU命令成功，tagOut[1…]表示APDU返回数据。
        * @return 大于0表示ICCSendRecv 执行成功，APDU有数据返回
        */
       int ICCSendRecv(in byte[] tagIn, int tagInLength, inout byte[] tagOut);


       /**
        * NFC卡APDU接口
        * @param tagIn APDU发送数据
        * @param tagInLength APDU发送数据长度；
        * @param tagOut tagOut[0]等于0表示APDU命令成功，tagOut[1…]表示APDU返回数据。
        * @return  大于0表示NFCSendRecv执行成功，APDU有数据返回
        */
       int NFCSendRecv(in byte[] tagIn, int tagInLength, inout byte[] tagOut);

}
