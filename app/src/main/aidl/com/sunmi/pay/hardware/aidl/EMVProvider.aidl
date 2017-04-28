// EMVProvider.aidl
package com.sunmi.pay.hardware.aidl;

// Declare any non-default types here with import statements

interface EMVProvider {


    /**
     * 保存AID
     */
    long saveAID(in byte[] aid);

    /**
     * 保存RID
     */
    long saveRID(in byte[] rid);

    /**
     * 查看RID列表
     */
    String getAllAID();

    /**
     * 查看AID列表
     */
     String getAllRID();

    /**
     * 删除AID
     * @parame 传入aid(TAG为9F06)
     */
    boolean deleteAID(in byte[] aid);

    /**
     * 删除RID
     * @parame 传入rid(TAG为9F06)
     * @parame 传入rid索引(TAG为9F22)
     */
    boolean deleteRID(in byte[] rid,in byte[] rid_index);

    /**
     * 删除AID
     * @parame 传入aid(TAG为9F06)
     */
    boolean deleteAllAID();

    /**
     * 删除RID
     * @parame 传入aid(TAG为9F06)
     */
    boolean deleteAllRID();

    /**
     *  deviceType: 设备类型, GENERAL_READER_DEVICE = 269484034;
     *  transactType：0表示Cash、1表示Goods、2表示Services、3表示Cashback；
     *  amount：交易金额，单位为分，如“1234”表示12.34元；
     *  optAmount:可选金额，单位为分，如“1234”表示12.34元;
     */
    int prepareTransaction(int deviceType, int transactType, String amount, String optAmount);

    /**
     *  tagOut[]：输出交易结果数据，
     *  其中：tagOut[0]表示交易结果：
     *  tagOut[0]等于0x00表示脱机交易完成，
     *  tagOut[1]表示脱机交易结果，
     *           0表示交易终止；
     *           1表示脱机交易成功；
     *           2表示脱机交易拒绝；
     *           5表示联机失败脱机交易成功；
     *           6表示联机失败脱机交易拒绝，
     *           后面tagOut[2……]为脱机交易的confirm包；
     *  tagOut[0]等于0xFB表示请求联机，
     *          tagOut[1]为数据包类型
     *          tagOut[1]固定为0x01，
     *          后面tagOut[2……]为55域的Request数据；其他为错误码；
     */
    int transactProcess(inout byte[] tagOut);


    /**
     *  isCommunicated：true表示与后台通讯成功；false表示与后台通讯失败；
        tagIn：下发授权数据，tagIn[0]表示数据包类型，tagIn[0]固定为0x03；tagIn[1……]为后台响应数据，TLV格式：内容如下：
        如下表：
        TAG名称	        TAG值	    长度（byte）
        授权响应码	    8A	        2
        授权码	        89	        6
        发卡行脚本1	    71	        var
        发卡行脚本2	    72	        var
        发卡行认证数据	91	        var（8～16）
        t
        agInLength：下发授权数据长度；
        tagOut： 输出二次授权结果数据，
                tagOut[0]表示二次授权结果包类型，
                         0x21表示后面数据tagOut[1……]只包含confirm包；
                         0x81表示后面数据tagOut[1……]只包含冲正包；
                         0xC1表示后面的数据既有冲正包又有confirm包，冲正包数据在前，confirm包数据在后，
                其中tagOut[1]和tagOut[2]表示冲正包数据长度，
                等于((tagOut[1]&0xFF)<<8) | (tagOut[2]&0xFF)，tagOut[3…N]表示冲正包数据；
                tagOut[N+1]和tagOut[N+2]表示confirm包数据长度，
                等于((tagOut[N+1]&0xFF)<<8) | (tagOut[N+2]&0xFF)，tagOut[N+3…]表示冲正包数据。
        返回：
        大于0表示二次授权结果数据；其他为错误码；

     *
     */
    int authorizationProcess(boolean isCommunicated, inout byte[] tagIn, int tagInLength,inout byte[] tagOut);


}
