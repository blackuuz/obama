// TransactionListener.aidl
package com.sunmi.pay.hardware.aidl;

import java.util.Map;

interface TransactionListener {



        /**
         * 等待用户刷卡
         */
        void onWaitingForCardSwipe();

        /** i：输入当前检测到的卡类型，CARDTYPE_MAG、CARDTYPE_IC或CARDTYPE_NFC；
         *  i1：输入当前检测卡的状态，0表示成功，其他值表示检卡错误；
         *  s：若成功检测到磁卡，输入磁卡账户信息；
         *  s1：若成功检测到磁卡，输入二磁道数据；
         *  hashMap：若检卡成功，额外的信息将在additonal中输入，其中，一磁道数据的key为“TRACK1”，value为String类型；
         *           三磁道数据的key为“TRACK3”，value为String类型；卡片有效期的key为“EXPIRE”，value为String类型；
         *           磁片是否带IC的key为“isICCARD”，value为boolean类型，带IC为true、不带ic为false；
         *           卡片国家代码的key为“COUNTRYCODE”，value为String类型；持卡人姓名的key为“CARDHOLDER”，value为String类型；
         *           IC卡的ATR数据的key为“ATR”，value为String类型；NFC卡UUID的key为“UUID”，value为String类型。
         */
        int onCheckCardCompleted(int i, int i1, String s, String s1, in Map hashMap);


        /**
         *  用户刷卡、插卡、放卡超时回调
         */
        void onTimeout();


        /**
         *  i：输入当前进度的百分数值，0~100表示更新进度；
         *  小于0表示更新错误；当达到100后，
         *  设备将进行校验，校验成功输入10000，
         *  设备自动重启更新；校验失败输入9999，设备忽略下载的固件。
         *
         */
        void onUpdateProcess(int i);


        /**
         * i: 0 表示由应用实现明文键盘，待用户输入后通过data返回ASCII码键值;
         *    1 表示由data传入0~9的随机序列，ASCII字符，应用实现标准密码键盘界面布局后，返回键盘绝对坐标值和参数到data;
         *    2 表示用户按下一个数字键，需要应用在界面上的密码输入栏显示一个星号;
         *    3 表示用户按下删除键，需要应用清除界面上的密码输入栏显示;
         *    4 表示用户按下取消键，需要应用关闭密码键盘界面;
         *    5 表示用户按下确定键，需要应用关闭密码键盘界面;
         *    6 表示用户输入超时，需要应用关闭密码键盘界面;
         *
         * @return 参数i：      返回值:
         *             0           大于0：PIN的长度；
         *                         等于0：用户没有输入PIN，直接按确定键返回；
         *                         小于0：用户按取消键返回；
         *
         *           非0           忽略
         */
        int onInputPIN(int i, inout byte[] data);

        /**
         *
         *
         *
         */
        int onGetECC(inout byte[] bytes, int i, int i1, inout byte[] bytes1,inout byte[] bytes2);


        /**
         *  tag：输出所有终端参数，包括终端类型、终端性能、附加终端性能、终端国家代码、接口设备序列号
         *      TAG名称         TAG值           备注
         *      终端类型         9F35
         *      终端性能	        9F33
         *      附加终端性能	    9F40
         *      终端国家代码	    9F1A
         *      接口设备序列号	9F1E
         *      是否强制联机	    DF14	        长度为1字节，1表示强制联机；0表示不强制联机
         *      设备显示语言	    DF70	        长度为1字节， 0表示英文显示；1表示中午显示；
         *      非接卡交易限额	DF19
         *      非接脱机交易限额	DF20
         *      CVM限额	        DF21
         *      终端交易属性	    9F66
         */
        int onGetTerminalTag(inout byte[] tag);


        /**
         *	type：支付内核显示信息类型，0x01为只显示，待用户按确认键后返回；0x02为带返回值显示，用户可按确认键或取消键返回；
         * 	messageID：输入显示信息ID,详细见messageID列表
         * 	messageData：显示信息内容；
         *	@return：
         * 	    等于0表示无需用户确认，普通确认键返回；等于1表示需要用户确认，用户按确认键返回；等于-1表示需要用户确认，用户按取消键返回。
         */
        int onKernelMessage(int type, int messageID,inout byte[] messageData);


        /**
         *  i：输入卡片上的总应用数；
         *  strings：输入卡片上各应用的名称，从0到total-1；
         *  @return：
         *      用户选择的应用号，0~total-1。
         */
        int onAppSelect(int i, inout String[] strings);

        /**
         * 	info：SDK输出的调试信息；
         * 	说明：
         * 	该接口只用于开发过程中的调试，应用程序不用实现。
         */
        void onDebug(String info);

        /**
         * 	RID：CRL中的RID；
         * 	Index：CRL中的Index；
         * 	SN：CRL中的SN；
         * 	@return：
         * 	    等于0表示不存在；非0不是存在；
         * 	说明：
         * 	    该接口主要用于检查CRL，对于没有CRL的应用，可以不用实现。
         */
        int onCheckCRL(String RID, String Index, String SN);

}
