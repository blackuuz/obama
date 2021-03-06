package com.ksk.obama.activity;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.ListPopupWindow;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ksk.obama.DB.BuyCountDb;
import com.ksk.obama.DB.BuyShopDb;
import com.ksk.obama.DB.OpenCardDb;
import com.ksk.obama.DB.QuickDelMoney;
import com.ksk.obama.DB.RechargeAgain;
import com.ksk.obama.R;
import com.ksk.obama.callback.IHttpCallBack;
import com.ksk.obama.fragment.MainFrag0;
import com.ksk.obama.fragment.MainFrag1;
import com.ksk.obama.fragment.MainFrag2;
import com.ksk.obama.model.LoginData;
import com.ksk.obama.service.OrderService;
import com.ksk.obama.utils.NetworkUrl;
import com.ksk.obama.utils.SharedUtil;
import com.ksk.obama.utils.Utils;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static com.ksk.obama.utils.SharedUtil.getSharedBData;
import static com.ksk.obama.utils.SharedUtil.getSharedData;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_shop_name, tv_vip_name;
    private TextView tv_0, tv_1, tv_2;
    private TextView line_0, line_1, line_2;
    private ViewPager rootVP;
    private MainFrag0 frag0;
    private MainFrag1 frag1;
    private MainFrag2 frag2;
    private List<Fragment> list = new ArrayList<>();
    private List<TextView> list_text = new ArrayList<>();
    private List<TextView> list_line = new ArrayList<>();
    public static List<LoginData.AuthorBean> list_pow = new ArrayList<>();
    public static List<LoginData.ModuleBean> list_module = new ArrayList<>();
    private ImageView iv_pop;
    private boolean isSupplement;
    private List<String> lists = new ArrayList<>();

    private PopupWindow window;
    private Dialog mdialog;


    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            OrderService.OrderBinder orderBinder = (OrderService.OrderBinder) service;
            OrderService orderService = orderBinder.getService();
            orderService.reSetTime();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lists.add("意见反馈");
        lists.add("系统设置");
        lists.add("蓝牙打印机");
        lists.add("退出");
        String str = SharedUtil.getSharedData(MainActivity.this, "exittime");
        if (TextUtils.isEmpty(str)) {
            BaseActivity.curTime = 0;
        } else {
            BaseActivity.curTime = Long.parseLong(str);
        }
        if (curTime != 0) {
            startTimer();
        }
        if (SharedUtil.getSharedData(MainActivity.this, "time").equals("0")) {
            isSupplement = false;
        } else {
            isSupplement = true;
        }

        if (isSupplement) {
            Intent intent = new Intent(this, OrderService.class);
            bindService(intent, connection, BIND_AUTO_CREATE);
            startService(intent);
        }
        initData();
        initView();
        initFragment();
        setVpListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean flag = isNetworkAvailable(MainActivity.this);
        queryDB(flag);
        if (isSupplement && isServiceRunning("com.ksk.obama.service.OrderService") == false) {
            startService(new Intent(MainActivity.this, OrderService.class));
        }

    }

    private void queryDB(boolean flag) {
        if (flag) {
            Connector.getDatabase();
            List<RechargeAgain> list1 = DataSupport.findAll(RechargeAgain.class);
            List<QuickDelMoney> list2 = DataSupport.findAll(QuickDelMoney.class);
            List<OpenCardDb> list3 = DataSupport.findAll(OpenCardDb.class);
            List<BuyCountDb> list4 = DataSupport.findAll(BuyCountDb.class);
            List<BuyShopDb> list5 = DataSupport.findAll(BuyShopDb.class);
            if (list1 != null && list1.size() > 0) {
                startActivity(new Intent(MainActivity.this, RechargeSupplementActivity.class));
            } else if (list2 != null && list2.size() > 0) {
                startActivity(new Intent(MainActivity.this, QuickDelMoneySupplementActivity.class));
            } else if (list3 != null && list3.size() > 0) {
                startActivity(new Intent(MainActivity.this, OpenCardSupplementActivity.class));
            } else if (list4 != null && list4.size() > 0) {
                startActivity(new Intent(MainActivity.this, BuyCountSupplementActivity.class));
            } else if (list5 != null && list5.size() > 0) {
                startActivity(new Intent(MainActivity.this, BuyShopSupplementActivity.class));
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isSupplement && isServiceRunning("com.ksk.obama.service.OrderService") == false) {
            startService(new Intent(MainActivity.this, OrderService.class));
        }
    }

    private void initData() {
        if (getIntent() != null) {
            Bundle bundle = getIntent().getExtras();
            list_pow = bundle.getParcelableArrayList("info");
            list_module = bundle.getParcelableArrayList("infom");
            for (int i = 0; i < list_pow.size(); i++) {
                String str = list_pow.get(i).getC_PopedomName();
                if(str.equals("POS:补单删除")){
                    SharedUtil.setSharedBData(MainActivity.this,"bdsc",true);
                }else {
                    SharedUtil.setSharedBData(MainActivity.this,"bdsc",false);
                }
                if(str.equals("POS:备份订单上传")){
                    SharedUtil.setSharedBData(MainActivity.this,"bfddsc",true);
                }else {
                    SharedUtil.setSharedBData(MainActivity.this,"bfddsc",false);
                }
            }
        }

        for (int i = 0; i < MainActivity.list_module.size(); i++) {
            String str = MainActivity.list_module.get(i).getC_ModuleName();
            if (str.equals("卡号激活")) {
                SharedUtil.setSharedBData(MainActivity.this,"khjh",true);
            }else {
                SharedUtil.setSharedBData(MainActivity.this,"khjh",false);

            }
        }
    }

    private void initView() {
        tv_shop_name = (TextView) findViewById(R.id.tv_main_shop_name);
        tv_vip_name = (TextView) findViewById(R.id.tv_main_vip_name);
        iv_pop = (ImageView) findViewById(R.id.iv_pop);
        tv_0 = (TextView) findViewById(R.id.tv_main_text_0);
        tv_1 = (TextView) findViewById(R.id.tv_main_text_1);
        tv_2 = (TextView) findViewById(R.id.tv_main_text_2);
        line_0 = (TextView) findViewById(R.id.tv_main_line_0);
        line_1 = (TextView) findViewById(R.id.tv_main_line_1);
        line_2 = (TextView) findViewById(R.id.tv_main_line_2);
        rootVP = (ViewPager) findViewById(R.id.vp_main);

        tv_0.setOnClickListener(this);
        tv_1.setOnClickListener(this);
        tv_2.setOnClickListener(this);
        iv_pop.setOnClickListener(this);

        list_text.add(tv_0);
        list_text.add(tv_1);
        list_text.add(tv_2);
        list_line.add(line_0);
        list_line.add(line_1);
        list_line.add(line_2);
        String str = getSharedData(MainActivity.this,"shopname");
        if(str.length()>5){
            str = str.substring(0,5)+"…";
        }
        tv_shop_name.setText(str);
        tv_vip_name.setText(getSharedData(MainActivity.this, "username"));

        findViewById(R.id.tv_help_order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timer == null) {
                    timer = new Timer();
                    TimerTask timerTask = new TimerTask() {
                        @Override
                        public void run() {
                            if (n < 10) {
                                n = 0;
                            }
                            timer.cancel();
                            timer = null;
                        }
                    };
                    timer.schedule(timerTask, 15000);
                }
                n++;
                if (n == 10) {
                    showAlertDB();
                }
            }
        });
    }

    private int n = 0;
    private Timer timer;

    private void showAlertDB() {
        timer.cancel();
        timer = null;
        if(!getSharedBData(MainActivity.this,"bfddsc")){
            showDialog();
        }else {
            startActivity(new Intent(MainActivity.this, OrderNumberActivity.class));
        }
        Log.d("uuz", "showAlertDB: "+n);
        n = 0;
    }

    // TODO: 2017/4/21 改名。。 
    private void initFragment() {
        frag0 = new MainFrag0();//日常业务
        frag1 = new MainFrag1();//会员管理
        frag2 = new MainFrag2();//查询统计
        list.add(frag0);
        list.add(frag1);
        list.add(frag2);

        rootVP.setAdapter(new MyVPAdapter(getSupportFragmentManager()));
        rootVP.setCurrentItem(0);
        changeColor(0);
    }

    private void setVpListener() {
        rootVP.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changeColor(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void changeColor(int n) {
        for (int i = 0; i < list_text.size(); i++) {
            if (i == n) {
                list_text.get(i).setTextColor(ContextCompat.getColor(this, R.color.text_blue));
                list_line.get(i).setVisibility(View.VISIBLE);
            } else {
                list_text.get(i).setTextColor(ContextCompat.getColor(this, R.color.text_black0));
                list_line.get(i).setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_pop:
                showPop(1);
                break;
            case R.id.tv_main_text_0:
                rootVP.setCurrentItem(0);
                changeColor(0);
                break;
            case R.id.tv_main_text_1:
                rootVP.setCurrentItem(1);
                changeColor(1);
                break;
            case R.id.tv_main_text_2:
                rootVP.setCurrentItem(2);
                changeColor(2);
                break;
        }
    }

    private void showPop() {
        final PopupWindow window = new PopupWindow();
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.main_pop, null);
        view.findViewById(R.id.tv_yjfk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, OpinionFeedbackActivity.class);
                startActivity(intent);
                window.dismiss();
            }
        });
        view.findViewById(R.id.tv_set).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
                window.dismiss();
            }
        });
        view.findViewById(R.id.tv_quict).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int w = dm.widthPixels;
        int h = dm.heightPixels;
        window.setContentView(view);
        window.setWidth(w / 3);
        window.setHeight((int) (h * 0.3));
        window.setFocusable(true);
        window.setOutsideTouchable(true);
        window.update();
        ColorDrawable dw = new ColorDrawable();
        window.setBackgroundDrawable(dw);
        window.showAsDropDown(iv_pop);
    }

    private void showPop(int n) {
        final ListPopupWindow mListPop = new ListPopupWindow(MainActivity.this);
        mListPop.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lists));
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int w = dm.widthPixels;
        mListPop.setWidth((int) (w * 0.4));
        mListPop.setHeight(LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
        mListPop.setAnchorView(iv_pop);//设置ListPopupWindow的锚点，即关联PopupWindow的显示位置和这个锚点
        mListPop.setModal(true);//设置是否是模式

        mListPop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String str = lists.get(position);
                if (str.equals("意见反馈")) {
                    Intent intent = new Intent(MainActivity.this, OpinionFeedbackActivity.class);
                    startActivity(intent);
                } else if (str.equals("系统设置")) {
                    Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                    startActivity(intent);
                } else if (str.equals("蓝牙打印机")) {
                    Intent intent = new Intent(MainActivity.this, BluetoothActivity.class);
                    startActivity(intent);
                } else if (str.equals("退出")) {
                    finish();
                }
                mListPop.dismiss();
            }
        });
        mListPop.show();
    }

    private class MyVPAdapter extends FragmentPagerAdapter {

        public MyVPAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (list_pow != null) {
            list_pow.clear();
        }
        if (list_module != null) {
            list_module.clear();
        }

        list_pow = null;
        list_module = null;
        if (isSupplement) {
            unbindService(connection);
        }
    }

    private long endTimes = 0;

    @Override
    public void onBackPressed() {
        long currentTimes = System.currentTimeMillis();
        if (currentTimes - endTimes < 2000) {
            super.onBackPressed();
            finish();
        } else {
            Utils.showToast(MainActivity.this, "再按一次退出");
        }
        endTimes = currentTimes;
    }

    private void getTemporary(final String num, String pwd) {
        Map<String, String> map = new HashMap<>();
        map.put("c_JobNumber", num);
        map.put("Password", pwd);
        map.put("dbName", getSharedData(MainActivity.this, "dbname"));
        postToHttp(NetworkUrl.TEMPORARY, map, new IHttpCallBack() {
            @Override
            public void OnSucess(String jsonText) {
                Logger.e(jsonText);
                try {
                    JSONObject j = new JSONObject(jsonText);
                    String s = j.getString("result_data");
                    int bfddsc = s.indexOf("POS:备份订单上传");
                    Logger.e("" + bfddsc);
                    if (bfddsc > 0) {
                        Utils.showToast(MainActivity.this, "授权成功");
                        startActivity(new Intent(MainActivity.this, OrderNumberActivity.class));
                    } else {
                        Utils.showToast(MainActivity.this, "您没有权限或密码错误");
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Utils.showToast(MainActivity.this, "服务器异常");
                }
            }
            @Override
            public void OnFail(String message) {
                Utils.showToast(MainActivity.this, "临时授权失败");
            }

        });
    }

    private void showDialog(){
        if(null == mdialog){
            mdialog = new Dialog(MainActivity.this, R.style.BottomDialogStyle);
        }
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.pop_temporary, null);
        final EditText et_num = (EditText) view.findViewById(R.id.et_num);
        final EditText et_pwd = (EditText) view.findViewById(R.id.et_pwd);
        ImageView back = (ImageView) view.findViewById(R.id.iv_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mdialog.dismiss();
            }
        });

        view.findViewById(R.id.btn_yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str1 = et_num.getText().toString();
                String str2 = et_pwd.getText().toString();
                if (TextUtils.isEmpty(str1)) {
                    Utils.showToast(MainActivity.this, "请输入工号");
                } //else if (TextUtils.isEmpty(str2)) {
//                    Utils.showToast(RechargeActivity.this, "请输入密码");
                else {
                    getTemporary(str1, str2);
                    mdialog.dismiss();
                }
            }
        });



        mdialog.setContentView(view);
        Window dialogWindow = mdialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        lp.width = (int) (dm.widthPixels * 0.8);
        lp.height = (int)(dm.heightPixels*0.4);

        dialogWindow.setAttributes(lp); //将属性设置给窗体
        mdialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

            }
        });
        if(!mdialog.isShowing()){
            mdialog.show();//显示对话框
        }
        //mdialog.setOutsideTouchable(false);
        mdialog.setCancelable(false);

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        if(window!=null&&window.isShowing()){
            return false;
        }
        return super.dispatchTouchEvent(ev);
    }
}
