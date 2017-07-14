package com.ksk.obama.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.ksk.obama.R;
import com.ksk.obama.utils.SharedUtil;
import com.ksk.obama.utils.Utils;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_splash);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permissionCheck = 0;
            int xx, yy, zz, oo, pp;
            //xx = permissionCheck = this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);//准确位置权限 无法保留状态
            yy = permissionCheck += this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
            //permissionCheck += this.checkSelfPermission(Manifest.permission.BLUETOOTH_PRIVILEGED);//蓝牙权限不是危险权限  但是蓝牙设备可能遇见问题？？
            zz = permissionCheck += this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            oo = permissionCheck += this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            pp = permissionCheck += this.checkSelfPermission(Manifest.permission.CAMERA);
           // int a = permissionCheck += this.checkSelfPermission(Manifest.permission.BLUETOOTH_PRIVILEGED);
            Log.d("uuz", "终端机型的Android版本 大于6.0 ，权限值为(0为拥有权限)："+permissionCheck +" y:"+yy+" z:"
            +zz+" o:"+oo+" p:"+pp);

            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                //注册权限
                Log.d("uuz", "注册权限—————— ");
                this.requestPermissions(
                        new String[]{
                                //Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                //Manifest.permission.BLUETOOTH_PRIVILEGED,  //蓝牙权限不是危险权限  但是蓝牙设备可能遇见问题？？
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA,}, 1001); //Any number
            } else {
                changeActivity();
            }
        } else {
            changeActivity();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //  super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1001:
                Log.d("uuz", "onRequestPermissionsResult: " + grantResults.length + "--");
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(SplashActivity.this, "请授予软件权限,以便正常使用" + grantResults[i] + "没有权限", Toast.LENGTH_LONG).show();
                            Log.d("uuz", "请授予软件权限,以便正常使用" + i + "没有权限");
                            try {
                                Thread.sleep(2000);
                                finish();
                                return;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    changeActivity();
                } else {
                    Utils.showToast(SplashActivity.this, "请授予软件权限,以便正常使用");
                    try {
                        Thread.sleep(2000);
                        finish();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    Thread thread = new Thread() {
        @Override
        public void run() {
            try {
                sleep(2000);
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };


    /**
     * 判断是否设置好 如果可以设置好了 跳转到登录界面 点击图标跳转到设置机型界面
     */
    private void changeActivity() {
        if (TextUtils.isEmpty(SharedUtil.getSharedData(SplashActivity.this, "isSet"))) {
            startActivity(new Intent(SplashActivity.this, TypeSettingActivity.class));
            finish();
        } else {
            thread.start();
            findViewById(R.id.iv).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    thread.interrupt();
                    startActivity(new Intent(SplashActivity.this, TypeSettingActivity.class));
                    finish();
                }
            });
        }
    }
}
