package com.ksk.obama.zxing;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.ksk.obama.R;
import com.ksk.obama.activity.BaseActivity;
import com.ksk.obama.callback.IHttpCallBack;
import com.ksk.obama.utils.HttpTools;
import com.ksk.obama.utils.NetworkUrl;
import com.ksk.obama.utils.OKHttpSingleton;
import com.ksk.obama.utils.Utils;
import com.ksk.obama.zxing.camera.CameraManager;
import com.ksk.obama.zxing.decoding.CaptureActivityHandler;
import com.ksk.obama.zxing.decoding.InactivityTimer;
import com.ksk.obama.zxing.view.ViewfinderView;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Initial the camera
 *
 * @author Ryan.Tang
 */
public class MipcaActivityCapture extends BaseActivity implements Callback {

    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate;
    private Timer timer;
    private boolean isSelect = true;
    private RelativeLayout rl_sk;
    private RelativeLayout ll_sm;
    private ImageView iv_sm;
    private String type;
    private String payStyle;
    private String groupId;
    private String money;
    private String goods;
    private String order;
    private String imgName;
    private String codeUrl;
    private Button btn_get;
    private boolean isPay;
    private Button btn1;
    private Button btn2;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);
        CameraManager.init(getApplication());
        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);

        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
        timer = new Timer();
        initData();
        initView();
    }

    private void initView() {
        rl_sk = (RelativeLayout) findViewById(R.id.rl_sk);
        ll_sm = (RelativeLayout) findViewById(R.id.ll_sm);
        TextView tv = (TextView) findViewById(R.id.title_name);
        tv.setText("二维码/条形码");
        TextView back = (TextView) findViewById(R.id.tv_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timer.cancel();
                finish();
            }
        });

        if (isPay) {
            btn1 = (Button) findViewById(R.id.btn1);
            btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isSelect = true;
                    btn1.setBackgroundResource(R.drawable.sao_ma_yes);
                    btn2.setBackgroundResource(R.drawable.sao_ma_no);
                    rl_sk.setVisibility(View.VISIBLE);
                    ll_sm.setVisibility(View.GONE);
                }
            });
            btn2 = (Button) findViewById(R.id.btn2);
            btn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isSelect = false;
                    btn2.setBackgroundResource(R.drawable.sao_ma_yes);
                    btn1.setBackgroundResource(R.drawable.sao_ma_no);
                    rl_sk.setVisibility(View.GONE);
                    ll_sm.setVisibility(View.VISIBLE);
                    getQrcode();
                }
            });
        } else {
            findViewById(R.id.ll_select).setVisibility(View.GONE);
        }
        iv_sm = (ImageView) findViewById(R.id.iv_sm);
        findViewById(R.id.tv_commit).setVisibility(View.GONE);

        btn_get = (Button) findViewById(R.id.btn_get);
        btn_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSelect = false;
                rl_sk.setVisibility(View.GONE);
                ll_sm.setVisibility(View.VISIBLE);
                getQrcode();
            }
        });
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            isPay = intent.getBooleanExtra("isPay", false);
            type = intent.getStringExtra("type1");
            groupId = intent.getStringExtra("groupId");
            money = intent.getStringExtra("money");
            goods = intent.getStringExtra("goods");
            order = intent.getStringExtra("order");
        } else {
            Utils.showToast(MipcaActivityCapture.this, "获取二维码信息失败，请重试");
        }
    }

    private void getQrcode() {
        Map<String, String> map = new HashMap<>();
        map.put("type", type + "");
        map.put("payStyle", "qr");
        map.put("groupId", groupId);
        map.put("goods", goods);
        map.put("money", money);
        map.put("order", order);
        postToHttp(NetworkUrl.PAYCODE, map, new IHttpCallBack() {

            @Override
            public void OnSucess(String jsonText) {
                Logger.e(jsonText);
                try {
                    JSONObject object = new JSONObject(jsonText);
                    String msg = object.getString("result");
                    if (msg.equals("SUCCESS")) {
                        codeUrl = object.getString("code_url");
                        imgName = object.getString("code_name");
                        getCodeImage(codeUrl);
                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void OnFail(String message) {

            }
        });

    }

    private Bitmap bitmap;
    private int timeCount = 0;

    private void getCodeImage(String url) {
        OkHttpClient client = OKHttpSingleton.getInstance();
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Utils.showToast(MipcaActivityCapture.this, "网络连接有问题,请稍后重试");
                Logger.e("失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
//                    Logger.e(response.body().string());
                    byte[] b = response.body().bytes();
                    bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            iv_sm.setImageBitmap(bitmap);
                            TimerTask task = new TimerTask() {
                                @Override
                                public void run() {
                                    if (timeCount > 300) {
                                        timer.cancel();
                                        Utils.showToast(MipcaActivityCapture.this, "支付失败，请重新支付");
                                    } else {
                                        timeCount += 1;
                                        pay();
                                    }
                                }
                            };
                            timer.schedule(task, 1, 1000);
                        }
                    });

                }
            }
        });
    }

    private void pay() {
        Map<String, String> map = new HashMap<>();
        map.put("type", type + "");
        map.put("groupId", groupId);
        map.put("order", order);
        postToHttp(NetworkUrl.queryOrder, map, new IHttpCallBack() {
            @Override
            public void OnSucess(String jsonText) {
                try {
                    JSONObject object1 = new JSONObject(jsonText);
                    String code = object1.getString("result");
                    if (code.equals("SUCCESS")) {
                        timer.cancel();
                        String msg = object1.getString("result_msg");
                        String memoBillNum = object1.getString("memoBillNum");
                        Utils.showToast(MipcaActivityCapture.this, msg);
                        final Intent resultIntent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putInt("type", 1);
                        bundle.putString("memoBillNum", memoBillNum);
                        resultIntent.putExtras(bundle);
                        setResult(RESULT_OK, resultIntent);
                        delCode();
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MipcaActivityCapture.this, "测试++++", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void OnFail(String message) {

            }
        }, 0);
    }

    private void delCode() {
        Map<String, String> map = new HashMap<>();
        map.put("codeName", imgName);
        postToHttp(NetworkUrl.deleteCode, map, null);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        timer.cancel();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        if (isSelect == false && !TextUtils.isEmpty(imgName)) {
            delCode();
        }
        HttpTools.cancel();
        super.onDestroy();
    }

    public void handleDecode(Result result) {
        if (isSelect) {
            Log.d("djy", result.getText().toString());
            inactivityTimer.onActivity();
            playBeepSoundAndVibrate();
            String resultString = result.getText();
            if (resultString.equals("")) {
                Toast.makeText(MipcaActivityCapture.this, "获取信息失败,请重试", Toast.LENGTH_SHORT).show();
            } else {
                Intent resultIntent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("result", resultString);
                bundle.putInt("type", 0);
                resultIntent.putExtras(bundle);
                this.setResult(RESULT_OK, resultIntent);
            }
            MipcaActivityCapture.this.finish();
        }
    }


    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats,
                    characterSet);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;

    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();

    }

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(
                    R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(),
                        file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final OnCompletionListener beepListener = new OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

}