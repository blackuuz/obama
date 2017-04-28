// SecurityProvider.aidl
package com.sunmi.pay.hardware.aidl;

// Declare any non-default types here with import statements

/**
*
* TODO 安全相关接口，包含：包含更新 PIN,PAN,MAC,设置终端主密钥，保存秘钥，按银联标准加密PIN,计算8583包的MAC，数据加密
*
*/

interface SecurityProvider {
 /**
     * 更新PIN Key
     * @param encryptType PIN加密类型，1为明文；2为DES；3为3DES；
     * @param pinKey 后台下发的PIN Key
     * @param keyLength PIN Key长度；
     * @return 等于0表示更新成功；其他值为错误码
     */
    int updatePINKey(int encryptType,in byte[] pinKey, int keyLength);

     /**
       * 更新PAN Key
       * @param encryptType PAN加密类型，1为明文；2为DES；3为3DES；
       * @param panKey 后台下发的PAN Key
       * @param keyLength PAN Key长度；
       * @return 等于0表示更新成功；其他值为错误码
       */
    int updatePANKey(int encryptType,in byte[] panKey, int keyLength);

    /**
      * 更新MAC Key
      * @param encryptType MAC加密类型，1为明文；2为DES；3为3DES；
      * @param macKey 后台下发的MAC Key
      * @param keyLength MAC Key长度；
      * @return 等于0表示更新成功；其他值为错误码
      */
    int updateMACKey(int encryptType,in byte[] macKey, int keyLength);
   /**
     * 设置终端主密钥
     * @param plaintextKey 明文终端主密钥；
     * @param keyLength 终端主密钥的长度；
     * @return 0 表示成功
     */
    int setMasterKey(in byte[] plaintextKey, int keyLength);


    /**
     * 保存密钥
     * @param byteKeyIndex byteKeyIndex[0]: 需要保存的密钥索引行，0~255
     *                     byteKeyIndex[1]: 需要保存的密钥索引列，0~255
     *                     byteKeyIndex[2]: 密文解密密钥索引列，0~255
     * @param byteEncryptType 密钥密文的加密方式，2表示DES；3表示TDES；4表RSA公钥
     * @param key 密钥密文数据
     * @param keyLength 密钥密文数据长度
     * @return 等于0：密钥保存成功；其他：密钥保存错误。
     */
    int saveKey(in byte[] byteKeyIndex, in byte byteEncryptType,in byte[]key, int keyLength);

     /**
      * 计算8583包的MAC
      * @param tagIn 待计算MAC的8583包数据；
      * @param tagInlength 待计算MAC的8583包数据长度;
      * @param tagOut 计算完成的MAC数据;
      * @return 大于0：计算完成的MAC数据长度；
      */
    int calc8583MAC(int encryptType, in byte[] tagIn, int tagInlength, inout byte[] tagOut);


     /**
      * 数据加密
      * @param KeyIndex byteKeyIndex[0]: 加密密钥索引行，0~255    byteKeyIndex[1]: 加密密钥索引列，0~255；
      * @param byteEncryptType 加密方式，2表示DES；3表示TDES；4表示RSA公钥；
      * @param byteCryptData 待加密的明文数据;
      * @param iCryptDataLen 待加密的明文数据的长度；
      * @param byteOutputData  加密完的密文数据;
      * @return 大于0 ：加密完的密文数据长度；其他：数据加密错误。
      */
    int dataEncrypt(in byte[] KeyIndex, in byte byteEncryptType, in byte[] byteCryptData, int iCryptDataLen, inout byte[] byteOutputData);

}
