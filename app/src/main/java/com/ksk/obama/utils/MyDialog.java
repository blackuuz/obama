package com.ksk.obama.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ksk.obama.R;

/**
 * Created by Administrator on 2016/10/21.
 */

public class MyDialog extends Dialog {

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            HttpTools.cancel();
            if (isShowing()) {
                dismiss();
            }
        }
    };

    public MyDialog(Context context, int themeResId) {
        super(context, themeResId);
        initView(context);
    }
    private void initView(Context context) {
      //  LinearLayout ll = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.loading_dialog_view,null);
        // 首先得到整个View
        View view = LayoutInflater.from(context).inflate(
                R.layout.loading_dialog_view,null);
        // 获取整个布局
        LinearLayout layout = (LinearLayout) view
                .findViewById(R.id.dialog_view);
        // 页面中的Img
        ImageView img = (ImageView) view.findViewById(R.id.img);
        // 页面中显示文本
        TextView tipText = (TextView) view.findViewById(R.id.tipTextView);

        // 加载动画，动画用户使img图片不停的旋转
        Animation animation = AnimationUtils.loadAnimation(context,
                R.anim.dialog_load_animation);
        // 显示动画
        img.startAnimation(animation);
        // 显示文本
        tipText.setText("正在加载...");
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        setContentView(layout);
        DisplayMetrics dm = new DisplayMetrics();
        Window dialogWindow = getWindow();
        dialogWindow.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int w = dm.widthPixels;
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.width = (int) (w * 0.5); // 宽度设置为屏幕的0.65，根据实际情况调整
        p.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(p);

    }

    @Override
    public void show() {
        super.show();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
            }
        }, 10000);
    }
}
