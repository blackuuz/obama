package com.ksk.obama.activity;

import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ksk.obama.adapter.DetialsAdapter;
import com.ksk.obama.application.MyApp;
import com.ksk.obama.model.PrintPage;
import com.ksk.obama.utils.SharedUtil;
import com.ksk.obama.utils.Utils;
import com.lkl.cloudpos.aidl.AidlDeviceService;
import com.lkl.cloudpos.aidl.printer.AidlPrinter;
import com.lkl.cloudpos.aidl.printer.AidlPrinterListener;
import com.lkl.cloudpos.aidl.printer.PrintItemObj;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.weipass.pos.sdk.Printer;


/**
 * Created by Administrator on 2016/9/13.
 */
public class BasePrintActivity extends BaseTypeActivity {

    public static final byte ESC = 0x1B;
    public static final byte FS = 0x1C;
    /* 设置汉字打印模式 */
    public static final byte[] ESC_CN_FONT = new byte[]{FS, '&'};
    /* 标准大小 */
    public static final byte[] FS_FONT_ALIGN = new byte[]{FS, 0x21, 1, ESC, 0x21, 1};
    /* 横向纵向都放大一倍 */
    public static final byte[] FS_FONT_DOUBLE = new byte[]{FS, 0x21, 12, ESC, 0x21, 48};
    /* 靠左打印命令 */
    public static final byte[] ESC_ALIGN_LEFT = new byte[]{0x1b, 'a', 0x00};
    /* 居中打印命令 */
    public static final byte[] ESC_ALIGN_CENTER = new byte[]{0x1b, 'a', 0x01};
    /* 居右打印命令 */
    public static final byte[] ESC_ALIGN_RIGHT = new byte[]{0x1b, 'a', 0x02};
    //切纸
    public static final byte[] ESC_0 = new byte[]{ESC, 0x69};

    protected AidlPrinter printerDev = null;
    protected String orderNumber = "";

    @Override
    public void onDeviceConnected(AidlDeviceService serviceManager) {
        try {
            printerDev = AidlPrinter.Stub.asInterface(serviceManager.getPrinter());
        } catch (RemoteException e) {
            Utils.showToast(BasePrintActivity.this, "打印机打开失败,请重试");
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
int a = 0;
    protected synchronized void printPage(final String str, final List<String> list, final List<PrintPage> sonList, final boolean flag) {
        if (list != null && list.size() > 0) {
            switch (robotType) {
                case 1:
                    Log.d("uuz", "printPage: "+a++);
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
            buffer.append(getCenter(SharedUtil.getSharedData(BasePrintActivity.this, "name1")));
            buffer.append("\n");
            buffer.append("\n");
            if (flag) {
                buffer.append("消费店面: ");
                buffer.append(SharedUtil.getSharedData(BasePrintActivity.this, "shopname"));
                buffer.append("\n");
                buffer.append("手持序列号: ");
                buffer.append(terminalSn);
                buffer.append("\n");
                buffer.append("操作员: ");
                buffer.append(SharedUtil.getSharedData(BasePrintActivity.this, "username"));
                buffer.append("\n");
            }


//            printer.printText(str, Printer.FontFamily.SONG, Printer.FontSize.LARGE,
//                    Printer.FontStyle.NORMAL, Printer.Gravity.CENTER);
//            printer.printText(SharedUtil.getSharedData(BasePrintActivity.this, "name1"), Printer.FontFamily.SONG, Printer.FontSize.LARGE,
//                    Printer.FontStyle.NORMAL, Printer.Gravity.CENTER);
//            if (flag) {
//                printer.printText("消费店面: " + SharedUtil.getSharedData(BasePrintActivity.this, "shopname"), Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
//                        Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);
//                printer.printText("手持序列号: " + terminalSn, Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
//                        Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);
//                printer.printText("操作员: " + SharedUtil.getSharedData(BasePrintActivity.this, "username") , Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
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
            buffer.append(getCenter(SharedUtil.getSharedData(BasePrintActivity.this, "name2")));
            buffer.append(getCenter(SharedUtil.getSharedData(BasePrintActivity.this, "name3")));
            buffer.append("\n");
            buffer.append("\n");
            buffer.append("\n");
            buffer.append("\n");
            buffer.append("\n");
            buffer.append("\n");
            printer.printText(buffer.toString(), Printer.FontFamily.SONG, Printer.FontSize.LARGE,
                    Printer.FontStyle.NORMAL, Printer.Gravity.LEFT);

//            printer.printText("\n", Printer.FontFamily.SONG, Printer.FontSize.MEDIUM,
//                    Printer.FontStyle.NORMAL, Printer.Gravity.CENTER);
//            printer.printText(SharedUtil.getSharedData(BasePrintActivity.this, "name2") , Printer.FontFamily.SONG, Printer.FontSize.LARGE,
//                    Printer.FontStyle.NORMAL, Printer.Gravity.CENTER);
//            printer.printText(SharedUtil.getSharedData(BasePrintActivity.this, "name3"), Printer.FontFamily.SONG, Printer.FontSize.LARGE,
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
                mOutputStream.write((SharedUtil.getSharedData(BasePrintActivity.this, "name1") + "\n").getBytes("GBK"));
                mOutputStream.write("\n".getBytes());
                mOutputStream.write((str + "\n").getBytes("GBK"));
                mOutputStream.write(FS_FONT_ALIGN);
                mOutputStream.write(ESC_ALIGN_LEFT);

                if (flag) {
                    mOutputStream.write(("消费店面: " + SharedUtil.getSharedData(BasePrintActivity.this, "shopname") + "\n").getBytes("GBK"));
                    mOutputStream.write(("手持序列号:" + terminalSn + "\n").getBytes("GBK"));
                    mOutputStream.write(("操作员 :" + SharedUtil.getSharedData(BasePrintActivity.this, "username") + "\n").getBytes("GBK"));
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
                mOutputStream.write((SharedUtil.getSharedData(BasePrintActivity.this, "name2") + "\n").getBytes("GBK"));
                mOutputStream.write((SharedUtil.getSharedData(BasePrintActivity.this, "name3") + "\n").getBytes("GBK"));
                mOutputStream.write("\n".getBytes());
                mOutputStream.write("\n".getBytes());
                mOutputStream.write("\n".getBytes());
                mOutputStream.write("\n".getBytes());
                mOutputStream.write("\n".getBytes());
                mOutputStream.write("\n".getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    mOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Utils.showToast(BasePrintActivity.this, "蓝牙未打开或蓝牙打印机未连接");
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
                woyouService.printTextWithFont(SharedUtil.getSharedData(BasePrintActivity.this, "name1") + "\n", "", 42, callback);
                woyouService.printTextWithFont(str + "\n", "", 36, callback);
                woyouService.setAlignment(0, callback);
                if (flag) {
                    woyouService.printText("消费店面: " + SharedUtil.getSharedData(BasePrintActivity.this, "shopname") + "\n", callback);
                    woyouService.printText("手持序列号:" + terminalSn + "\n", callback);
                    woyouService.printText("操作员 :" + SharedUtil.getSharedData(BasePrintActivity.this, "username") + "\n", callback);
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
                // woyouService.printQRCode("东方Project",10,1,callback);
                woyouService.setAlignment(1, callback);
                woyouService.printTextWithFont(SharedUtil.getSharedData(BasePrintActivity.this, "name2") + "\n", "", 30, callback);
                woyouService.printTextWithFont(SharedUtil.getSharedData(BasePrintActivity.this, "name3") + "\n", "", 30, callback);
                woyouService.printTextWithFont(SharedUtil.getSharedData(BasePrintActivity.this, "name4") + "\n", "", 30, callback);
                woyouService.printText("　      _        　" + "\n", callback);
                woyouService.printText("　      _        　" + "\n", callback);
                woyouService.printText("　      _        　" + "\n", callback);
                woyouService.printText("　      _        　" + "\n", callback);
                woyouService.printText("　      _        　" + "\n", callback);
                woyouService.printText("　      _        　" + "\n", callback);
                woyouService.printText("　      _        　" + "\n", callback);
                woyouService.lineWrap(4, callback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
//                }
//            });
        } else {
            Utils.showToast(BasePrintActivity.this, "打印机故障,请退出重试");
        }
    }

    private void LKLprint(final String str, final List<String> list, final List<PrintPage> sonList, final boolean flag) {
        try {
            printerDev.printText(new ArrayList<PrintItemObj>() {
                {
                    add(new PrintItemObj("                            ", 24));
                    add(new PrintItemObj(SharedUtil.getSharedData(BasePrintActivity.this, "name1"), 24, true, PrintItemObj.ALIGN.CENTER));
                    add(new PrintItemObj(str, 23, true, PrintItemObj.ALIGN.CENTER));
                    if (flag) {
                        add(new PrintItemObj("消费店面: " + SharedUtil.getSharedData(BasePrintActivity.this, "shopname")));
                        add(new PrintItemObj("操作员 :" + SharedUtil.getSharedData(BasePrintActivity.this, "username")));
                        add(new PrintItemObj("手持序列号:" + terminalSn));
                    }
                    // TODO: 2017/6/22  格式化打印小票  如果不格式化 商品价格错位 就显得很丑
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).equals("son") && sonList != null) {
                            if (sonList.size() > 0) {
                                add(new PrintItemObj("商品名\t\t\t\t单价\t\t\t\t数量\t\t\t\t小计"));
                                for (int j = 0; j < sonList.size(); j++) {
                                    String str = "";
                                    String str1 = "";
                                    switch (sonList.get(j).getPrice().length()) {
                                        case 1:
                                        case 0:
                                            str1 = "\t\t\t\t\t\t\t\t";
                                            break;
                                        case 2:
                                            str1 = "\t\t\t\t\t\t\t";
                                            break;
                                        case 3:
                                            str1 = "\t\t\t\t\t\t";
                                            break;
                                        case 4:
                                            str1 = "\t\t\t\t\t";
                                            break;
                                        default:
                                            str1 = "\t\t\t";
                                            break;
                                    }
                                    switch (sonList.get(j).getName().length()) {
                                        case 0:
                                        case 1:
                                            str = "\t\t\t\t\t\t";
                                            break;
                                        case 2:
                                            str = "\t\t\t\t\t";
                                            break;
                                        case 3:
                                            str = "\t\t\t\t";
                                            break;
                                        case 4:
                                            str = "\t\t";
                                            break;
                                        default:
                                            str = "\n\t\t\t\t\t\t\t\t\t";
                                            break;
                                    }
                                    add(new PrintItemObj(sonList.get(j).getName() + str + sonList.get(j).getPrice()
                                            + str1 + sonList.get(j).getNum() + "\t\t\t" + sonList.get(j).getMoney()));
                                }
                            }
                        } else {
                            if (list.get(i).substring(0, 3).equals("序号-")) {
                                add(new PrintItemObj(list.get(i), 16, true, PrintItemObj.ALIGN.LEFT));
                            } else {
                                add(new PrintItemObj(list.get(i) + ""));
                            }

                        }
                    }

                    add(new PrintItemObj(SharedUtil.getSharedData(BasePrintActivity.this, "name2"), 23, true, PrintItemObj.ALIGN.CENTER));
                    add(new PrintItemObj(SharedUtil.getSharedData(BasePrintActivity.this, "name3"), 23, true, PrintItemObj.ALIGN.CENTER));
                    add(new PrintItemObj(SharedUtil.getSharedData(BasePrintActivity.this, "name4"), 23, true, PrintItemObj.ALIGN.CENTER));
                    add(new PrintItemObj("                            ", 24));
                    add(new PrintItemObj("                            ", 24));
                    // TODO: 2017/6/14 打印图片 
//                   bmp =  BitmapFactory.decodeResource(getResources(), R.mipmap.xs);
//                    printerDev.printBmp(20,250,250,bmp, new AidlPrinterListener.Stub() {
//                    @Override
//                    public void onError(int i) throws RemoteException {}
//
//                    @Override
//                    public void onPrintFinish() throws RemoteException {
//                    }
//                });
                }

            }, new AidlPrinterListener.Stub() {

                @Override
                public void onPrintFinish() throws RemoteException {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Utils.showToast(BasePrintActivity.this, "打印完成");
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

    protected void changeListViewHeight(DetialsAdapter adapter, ListView listView) {

        // 获取ListView对应的Adapter
        if (adapter == null) {

            return;

        }

        int totalHeight = 0;

        for (int i = 0; i < adapter.getCount(); i++) { // listAdapter.getCount()返回数据项的数目

            View listItem = adapter.getView(i, null, listView);

            listItem.measure(0, 0); // 计算子项View 的宽高

            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度

        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height = totalHeight
                + (listView.getDividerHeight() * (adapter.getCount() - 1));

        // listView.getDividerHeight()获取子项间分隔符占用的高度

        // params.height最后得到整个ListView完整显示需要的高度

        listView.setLayoutParams(params);

    }

    protected String getOrderNum(String first) {
        String str = "";
        switch (robotType) {
            case 1:
                str = terminalSn.substring(9);
                break;

            case 3:
                str = terminalSn.substring(8);
                break;
            case 4:
                str = terminalSn.substring(10);
                break;
            case 8:
                str = terminalSn.substring(11);
                break;

        }
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMddHHmmss");
        String time = simpleDateFormat.format(date);
        orderNumber = first + time + "1" + str;
        return orderNumber;
    }

    protected synchronized void bluetoothPrint(List<PrintPage> sonList, String ordernum) {

        OutputStream mOutputStream = ((MyApp) getApplication()).getOutputStream();
        if (mOutputStream != null) {
            try {
                int n = SharedUtil.getSharedInt(BasePrintActivity.this, "num");
                String str = SharedUtil.getSharedData(BasePrintActivity.this, "day");
                Date date = new Date(System.currentTimeMillis());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String time = simpleDateFormat.format(date);
                if (!str.equals(time.substring(8, 10))) {
                    n = 0;
                    SharedUtil.setSharedData(BasePrintActivity.this, "day", time.substring(8, 10));
                }
                n += 1;
                String num = "";
                if (n < 10) {
                    num = terminalSn.substring(terminalSn.length() - 2) + "00" + n;
                } else if (n < 100) {
                    num = terminalSn.substring(terminalSn.length() - 2) + "0" + n;
                } else {
                    num = terminalSn.substring(terminalSn.length() - 2) + n;
                }

                mOutputStream.write(ESC_CN_FONT);
                mOutputStream.write(FS_FONT_DOUBLE);
                mOutputStream.write(ESC_ALIGN_CENTER);
                mOutputStream.write(("后厨\n").getBytes("GBK"));
                mOutputStream.write(FS_FONT_DOUBLE);
                mOutputStream.write(ESC_ALIGN_LEFT);
                mOutputStream.write(("序号 :" + num + "\n").getBytes("GBK"));
                mOutputStream.write(FS_FONT_ALIGN);
                mOutputStream.write(ESC_ALIGN_LEFT);
                mOutputStream.write(("时间: " + time + "\n").getBytes("GBK"));
                mOutputStream.write(("手持序列号:" + terminalSn + "\n").getBytes("GBK"));
                mOutputStream.write(("操作员 :" + SharedUtil.getSharedData(BasePrintActivity.this, "username") + "\n").getBytes("GBK"));
                mOutputStream.write(("订单号 :" + ordernum + "\n").getBytes("GBK"));
                mOutputStream.write(ESC_ALIGN_CENTER);
                mOutputStream.write("--------------------------\n".getBytes());
                mOutputStream.write("菜品              数量\n".getBytes("GBK"));
                for (int i = 0; i < sonList.size(); i++) {
                    mOutputStream.write((sonList.get(i).getName() + "              " + sonList.get(i).getNum() + "\n").getBytes("GBK"));
                }
                mOutputStream.write("--------------------------\n".getBytes());
                mOutputStream.write("\n".getBytes());
                mOutputStream.write("\n".getBytes());
                mOutputStream.write("\n".getBytes());
                mOutputStream.write("\n".getBytes());
                SharedUtil.setSharedInt(BasePrintActivity.this, "num", n);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Utils.showToast(BasePrintActivity.this, "蓝牙未打开或蓝牙打印机未连接");
        }
    }

    private String getNum() {
        int n = SharedUtil.getSharedInt(BasePrintActivity.this, "num");
        String str = SharedUtil.getSharedData(BasePrintActivity.this, "day");
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd");
        String time = simpleDateFormat.format(date);
        if (!str.equals(time)) {
            n = 0;
            SharedUtil.setSharedData(BasePrintActivity.this, "day", time);
        }
        n += 1;
        String num = "";
        if (n < 10) {
            num = terminalSn.substring(terminalSn.length() - 2) + "00" + n;
        } else if (n < 100) {
            num = terminalSn.substring(terminalSn.length() - 2) + "0" + n;
        } else {
            num = terminalSn.substring(terminalSn.length() - 2) + n;
        }
        return num;
    }

}
