package com.ksk.obama.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ksk.obama.R;
import com.ksk.obama.utils.OKHttpSingleton;
import com.ksk.obama.utils.SharedUtil;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ChangeWechatActivity extends BaseActivity {

    private ImageView image;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            loadingDialog.dismiss();
            switch (msg.what) {
                case -1:
                    btn.setVisibility(View.VISIBLE);
                    btn.setEnabled(true);
                    break;

                case 2:
                    try {
                        JSONObject object = new JSONObject((String) msg.obj);
                        String message = object.getString("result");
                        String imageUrl = object.getString("codeurl");
                        if (message.equals("ok")) {
                            btn.setEnabled(false);
                            btn.setVisibility(View.GONE);
                            byte[] decode = Base64.decode(imageUrl, Base64.DEFAULT);

                            bitmap = BitmapFactory.decodeByteArray(decode, 0, decode.length);
                            image.setImageBitmap(bitmap);
                        } else {
                            btn.setEnabled(true);
                            btn.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }

        }
    };
    private Button btn;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_wechat);
        initTitale();
        image = (ImageView) findViewById(R.id.change_ewm);
        btn = (Button) findViewById(R.id.btn_image);
        sendData();
    }

    private void initTitale() {
        TextView title_name = (TextView) findViewById(R.id.title_name);
        TextView tv_print = (TextView) findViewById(R.id.tv_commit);
        tv_print.setEnabled(false);
        findViewById(R.id.tv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("erweima", "yes");
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        tv_print.setVisibility(View.INVISIBLE);
        title_name.setText("微信扫码");
    }

    private void sendData() {
        if (getIntent() != null) {
            String memid = getIntent().getStringExtra("memid");
            loadingDialog.show();
            String gid = SharedUtil.getSharedData(ChangeWechatActivity.this, "groupid");
            String sid = SharedUtil.getSharedData(ChangeWechatActivity.this, "shopid");
            String str = "http://123.207.146.244/yideweb/index.php/Home/WeChat/er_appcode?" +
                    "userid=" + memid + "&purpose=绑定&type=临时&groupId=" + gid + "&ShopID=" + sid + "&expires=2500000&remarks=";
            Log.e("djy", str);
            postMethod(str);
        }
    }

    public void postMethod(String url) {
        OkHttpClient client = OKHttpSingleton.getInstance();
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message = Message.obtain();
                message.what = -1;
                message.obj = "网络连接有问题,请稍后重试";
                handler.sendMessage(message);
                Logger.e("失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Message message = new Message();
                String str = response.body().string();
                Logger.e(str);
                if (response.isSuccessful()) {
                    message.what = 2;
                    message.obj = str;
                    handler.sendMessage(message);
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("erweima", "yes");
        setResult(RESULT_OK,intent);
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bitmap != null) {
            bitmap.recycle();
        }
    }
}
