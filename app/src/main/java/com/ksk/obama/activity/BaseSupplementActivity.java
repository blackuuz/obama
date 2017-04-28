package com.ksk.obama.activity;

import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import com.ksk.obama.application.MyApp;
import com.ksk.obama.callback.IHttpCallBackS;
import com.ksk.obama.model.PrintPage;
import com.ksk.obama.utils.HttpTools;
import com.ksk.obama.utils.SharedUtil;
import com.ksk.obama.utils.Utils;
import com.lkl.cloudpos.aidl.AidlDeviceService;
import com.lkl.cloudpos.aidl.printer.AidlPrinter;
import com.lkl.cloudpos.aidl.printer.AidlPrinterListener;
import com.lkl.cloudpos.aidl.printer.PrintItemObj;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.weipass.pos.sdk.Printer;

import static com.ksk.obama.activity.BasePrintActivity.ESC_0;
import static com.ksk.obama.activity.BasePrintActivity.ESC_ALIGN_CENTER;
import static com.ksk.obama.activity.BasePrintActivity.ESC_ALIGN_LEFT;
import static com.ksk.obama.activity.BasePrintActivity.ESC_CN_FONT;
import static com.ksk.obama.activity.BasePrintActivity.FS_FONT_ALIGN;
import static com.ksk.obama.activity.BasePrintActivity.FS_FONT_DOUBLE;

/**
 * Created by Administrator on 2016/10/27.
 */

public class BaseSupplementActivity extends BaseTypeActivity {
    private IHttpCallBackS callBack;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String str = (String) msg.obj;

            switch (msg.what) {
                case 0:
                    Utils.showToast(BaseSupplementActivity.this, str);
                    if (callBack != null) {
                        callBack.OnFail(str);
                    } else {
                        Logger.e("调用postToHttp方法的第3个参数为 new IHttpCallBack(){...}");
                    }
                    break;

                case 120:
                    if (callBack != null) {
                        callBack.OnSucess(str, msg.arg1);
                    } else {
                        Logger.e("调用postToHttp方法的第3个参数为 new IHttpCallBack(){...}");
                    }
                    break;
            }
        }
    };

    protected void postToHttps(int i, String url, Map<String, String> map, IHttpCallBackS iHttpCallBack) {
        int n = (int) (Math.random() * (Integer.MAX_VALUE));
        long time = System.currentTimeMillis() / 1000;
        String sign = Utils.getMD5Code(n + BaseActivity.KEY + time).toUpperCase();
        map.put("nonceStr", n + "");
        map.put("sign", sign);
        map.put("timeStamp", time + "");
        map.put("groupId", SharedUtil.getSharedData(BaseSupplementActivity.this, "groupid"));
        map.put("equipmentType", robotType + "");
        map.put("equipmentNum", terminalSn);
        callBack = iHttpCallBack;
        HttpTools.postToHttps(i, mHandler, url, map);
    }

    protected AidlPrinter printerDev = null;

    @Override
    public void onDeviceConnected(AidlDeviceService serviceManager) {
        try {
            printerDev = AidlPrinter.Stub.asInterface(serviceManager.getPrinter());
        } catch (RemoteException e) {
            Utils.showToast(BaseSupplementActivity.this, "打印机打开失败,请重试");
            e.printStackTrace();
        }
    }

    protected synchronized void printPage(final String str, final List<String> list, final List<PrintPage> sonList, final boolean flag) {
        if (list != null && list.size() > 0) {
            switch (robotType) {
                case 1:
                    LKLprint(str, list, sonList, flag);
                    break;

                case 3:
                    SUNMIprint(str, list, sonList, flag);
                    break;
                case 4:
                    bluetoothPrint(str, list, sonList, flag);
                    break;
                case 8:
                    wangPosPrint(str, list, sonList, flag);
                    break;
            }

        }
    }

    public static String getCenter(String str) {
        int size = (16 - str.length()) / 2;
        String resultStr = "";
        for (int i = 0; i < size; i++) {
            resultStr += " ";
        }
        return resultStr + str;
    }

    private void wangPosPrint(String str, final List<String> list, final List<PrintPage> sonList, final boolean flag) {
        if (printer != null) {
            StringBuffer buffer = new StringBuffer();
            buffer.append(getCenter(str));
            buffer.append("\n");
            buffer.append(getCenter(SharedUtil.getSharedData(BaseSupplementActivity.this, "name1")));
            buffer.append("\n");
            buffer.append("\n");
            if (flag) {
                buffer.append("消费店面: ");
                buffer.append(SharedUtil.getSharedData(BaseSupplementActivity.this, "shopname"));
                buffer.append("\n");
                buffer.append("手持序列号: ");
                buffer.append(terminalSn);
                buffer.append("\n");
                buffer.append("操作员: ");
                buffer.append(SharedUtil.getSharedData(BaseSupplementActivity.this, "username"));
                buffer.append("\n");
            }


//            printer.printText(str, Printer.FontFamily.SONG, Printer.FontSize.LARGE,
//                    Printer.FontStyle.NORMAL, Printer.Gravity.CENTER);
//            printer.printText(SharedUtil.getSharedData(BaseSupplementActivity.this, "name1"), Printer.FontFamily.SONG, Printer.FontSize.LARGE,
//                    Printer.FontStyle.NORMAL, Printer.Gravity.CENTER);
//            if (flag) {
//                printer.printText("消费店面: " + SharedUtil.getSharedData(BaseSupplementActivity.this, "shopname"), Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
//                        Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);
//                printer.printText("手持序列号: " + terminalSn, Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
//                        Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);
//                printer.printText("操作员: " + SharedUtil.getSharedData(BaseSupplementActivity.this, "username") , Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
//                        Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);
//            }
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).equals("son") && sonList != null) {
                    if (sonList.size() > 0) {
                        buffer.append("商品名   单价   数量   小计");
                        buffer.append("\n");
//                        printer.printText("商品名   单价   数量   小计", Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
//                                Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);
                        for (int j = 0; j < sonList.size(); j++) {
                            buffer.append(sonList.get(j).getName());
                            buffer.append("    ");
                            buffer.append(sonList.get(j).getPrice());
                            buffer.append("    ");
                            buffer.append(sonList.get(j).getNum());
                            buffer.append("    ");
                            buffer.append(sonList.get(j).getMoney() + "\n");
//                            printer.printText(sonList.get(j).getName() + "    " + sonList.get(j).getPrice()
//                                            + "    " + sonList.get(j).getNum() + "    " + sonList.get(j).getMoney(), Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
//                                    Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);
                        }
                    }
                } else {
                    buffer.append(list.get(i) + "\n");
//                    printer.printText(list.get(i) , Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
//                            Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);
                }
            }
            buffer.append("\n");
            buffer.append(getCenter(SharedUtil.getSharedData(BaseSupplementActivity.this, "name2")));
            buffer.append(getCenter(SharedUtil.getSharedData(BaseSupplementActivity.this, "name3")));
            buffer.append("\n");
            buffer.append("\n");
            buffer.append("\n");
            buffer.append("\n");
            buffer.append("\n");
            buffer.append("\n");
            printer.printText(buffer.toString(), Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
                    Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);

//            printer.printText("\n", Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
//                    Printer.FontStyle.NORMAL, Printer.Gravity.CENTER);
//            printer.printText(SharedUtil.getSharedData(BaseSupplementActivity.this, "name2") , Printer.FontFamily.SONG, Printer.FontSize.LARGE,
//                    Printer.FontStyle.NORMAL, Printer.Gravity.CENTER);
//            printer.printText(SharedUtil.getSharedData(BaseSupplementActivity.this, "name3"), Printer.FontFamily.SONG, Printer.FontSize.LARGE,
//                    Printer.FontStyle.NORMAL, Printer.Gravity.CENTER);
//            printer.printText("\n", Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
//                    Printer.FontStyle.NORMAL, Printer.Gravity.CENTER);

        }

    }


    private void bluetoothPrint(final String str, final List<String> list, final List<PrintPage> sonList, final boolean flag) {
        OutputStream mOutputStream = ((MyApp) getApplication()).getOutputStream();
        if (mOutputStream != null) {
            try {
                mOutputStream.write(ESC_CN_FONT);
                mOutputStream.write(ESC_ALIGN_CENTER);
                mOutputStream.write(FS_FONT_DOUBLE);
                mOutputStream.write((SharedUtil.getSharedData(BaseSupplementActivity.this, "name1") + "\n").getBytes("GBK"));
                mOutputStream.write("\n".getBytes());
                mOutputStream.write((str + "\n").getBytes("GBK"));
                mOutputStream.write(FS_FONT_ALIGN);
                mOutputStream.write(ESC_ALIGN_LEFT);

                if (flag) {
                    mOutputStream.write(("消费店面: " + SharedUtil.getSharedData(BaseSupplementActivity.this, "shopname") + "\n").getBytes("GBK"));
                    mOutputStream.write(("手持序列号:" + terminalSn + "\n").getBytes("GBK"));
                    mOutputStream.write(("操作员 :" + SharedUtil.getSharedData(BaseSupplementActivity.this, "username") + "\n").getBytes("GBK"));
                }

                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).equals("son") && sonList != null) {
                        if (sonList.size() > 0) {
                            mOutputStream.write("商品名   单价   数量   小计\n".getBytes("GBK"));
                            for (int j = 0; j < sonList.size(); j++) {
                                mOutputStream.write((sonList.get(j).getName() + "    " + sonList.get(j).getPrice()
                                        + "    " + sonList.get(j).getNum() + "    " + sonList.get(j).getMoney() + "\n").getBytes("GBK"));
                            }
                        }
                    } else {
                        mOutputStream.write((list.get(i) + "\n").getBytes("GBK"));
                    }
                }
                mOutputStream.write("\n".getBytes());
                mOutputStream.write(ESC_ALIGN_CENTER);
                mOutputStream.write((SharedUtil.getSharedData(BaseSupplementActivity.this, "name2") + "\n").getBytes("GBK"));
                mOutputStream.write((SharedUtil.getSharedData(BaseSupplementActivity.this, "name3") + "\n").getBytes("GBK"));
                mOutputStream.write("\n".getBytes());
                mOutputStream.write("\n".getBytes());
                mOutputStream.write("\n".getBytes());
                mOutputStream.write("\n".getBytes());
                mOutputStream.write("\n".getBytes());
                mOutputStream.write("\n".getBytes());
                mOutputStream.write(ESC_0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            Utils.showToast(BaseSupplementActivity.this,"蓝牙未打开或蓝牙打印机未连接");
        }

    }


    private void SUNMIprint(final String str, final List<String> list, final List<PrintPage> sonList, final boolean flag) {
        if (woyouService != null) {
//            ThreadPoolManager.getInstance().executeTask(new Runnable() {
//
//                @Override
//                public void run() {
            try {
                woyouService.lineWrap(2, callback);
                woyouService.setAlignment(1, callback);
                woyouService.printTextWithFont(SharedUtil.getSharedData(BaseSupplementActivity.this, "name1") + "\n", "", 42, callback);
                woyouService.printTextWithFont(str + "\n", "", 36, callback);
                woyouService.setAlignment(0, callback);
                if (flag) {
                    woyouService.printText("消费店面: " + SharedUtil.getSharedData(BaseSupplementActivity.this, "shopname") + "\n", callback);
                    woyouService.printText("手持序列号:" + terminalSn + "\n", callback);
                    woyouService.printText("操作员 :" + SharedUtil.getSharedData(BaseSupplementActivity.this, "username") + "\n", callback);
                }
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).equals("son") && sonList != null) {
                        if (sonList.size() > 0) {
                            woyouService.printText("商品名   单价   数量   小计\n", callback);
                            for (int j = 0; j < sonList.size(); j++) {
                                woyouService.printText(sonList.get(j).getName() + "    " + sonList.get(j).getPrice()
                                        + "    " + sonList.get(j).getNum() + "    " + sonList.get(j).getMoney() + "\n", callback);
                            }
                        }
                    } else {
                        woyouService.printText(list.get(i) + "\n", callback);
                    }
                }
                woyouService.setAlignment(1, callback);
                woyouService.printTextWithFont(SharedUtil.getSharedData(BaseSupplementActivity.this, "name2") + "\n", "", 30, callback);
                woyouService.printTextWithFont(SharedUtil.getSharedData(BaseSupplementActivity.this, "name3") + "\n", "", 30, callback);
                woyouService.printTextWithFont(SharedUtil.getSharedData(BaseSupplementActivity.this, "name4") + "\n", "", 30, callback);

                woyouService.lineWrap(4, callback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
//                }
//            });
        } else {
            Utils.showToast(BaseSupplementActivity.this, "打印机故障,请退出重试");
        }
    }

    private void LKLprint(final String str, final List<String> list, final List<PrintPage> sonList, final boolean flag) {
        try {
            printerDev.printText(new ArrayList<PrintItemObj>() {
                {
                    add(new PrintItemObj("                            ", 24));
                    add(new PrintItemObj(SharedUtil.getSharedData(BaseSupplementActivity.this, "name1"), 24, true, PrintItemObj.ALIGN.CENTER));
                    add(new PrintItemObj(str, 23, true, PrintItemObj.ALIGN.CENTER));
                    if (flag) {
                        add(new PrintItemObj("消费店面: " + SharedUtil.getSharedData(BaseSupplementActivity.this, "shopname")));
                        add(new PrintItemObj("操作员 :" + SharedUtil.getSharedData(BaseSupplementActivity.this, "username")));
                        add(new PrintItemObj("手持序列号:" + terminalSn));
                    }
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).equals("son") && sonList != null) {
                            if (sonList.size() > 0) {
                                add(new PrintItemObj("商品名   单价   数量   小计"));
                                for (int j = 0; j < sonList.size(); j++) {
                                    add(new PrintItemObj(sonList.get(j).getName() + "    " + sonList.get(j).getPrice()
                                            + "    " + sonList.get(j).getNum() + "    " + sonList.get(j).getMoney()));
                                }
                            }
                        } else {
                            add(new PrintItemObj(list.get(i) + ""));
                        }
                    }
                    add(new PrintItemObj(SharedUtil.getSharedData(BaseSupplementActivity.this, "name2"), 23, true, PrintItemObj.ALIGN.CENTER));
                    add(new PrintItemObj(SharedUtil.getSharedData(BaseSupplementActivity.this, "name3"), 23, true, PrintItemObj.ALIGN.CENTER));
                    add(new PrintItemObj(SharedUtil.getSharedData(BaseSupplementActivity.this, "name4"), 23, true, PrintItemObj.ALIGN.CENTER));
                    add(new PrintItemObj("                            ", 24));
                    add(new PrintItemObj("                            ", 24));

                }
            }, new AidlPrinterListener.Stub() {

                @Override
                public void onPrintFinish() throws RemoteException {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Utils.showToast(BaseSupplementActivity.this, "打印完成");
                            if (successCallback != null) {
                                successCallback.OnPrintSuccess();
                            } else {
                                Log.e("djy", "实现successCallback接口");
                            }
                        }
                    });
                }

                @Override
                public void onError(int arg0) throws RemoteException {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (errorCallback != null) {
                                errorCallback.OnPrintError();
                            } else {
                                Log.e("djy", "实现errorCallback接口");
                            }
                        }
                    });
                }
            });
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

}
