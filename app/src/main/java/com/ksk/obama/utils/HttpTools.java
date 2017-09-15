package com.ksk.obama.utils;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * Created by Administrator on 2016/7/27.
 */
public class HttpTools {
    private static Call finaCall = null;

    public static void cancel() {
        if (finaCall != null) {
            finaCall.cancel();
            finaCall = null;
        }
    }

    public static void postMethod(final Handler mHandler, String url, Map<String, String> map) {
        Log.e("uuz", url);
        OkHttpClient client = OKHttpSingleton.getInstance();
        FormBody.Builder builder = new FormBody.Builder();
        Set keySet = map.keySet();
        for (Iterator<String> iter = keySet.iterator(); iter.hasNext(); ) {
            String key = iter.next();
            String value = map.get(key);
            Log.d("uuz", key + "=" + value);
            builder.add(key, value);
        }
        RequestBody body = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        finaCall = client.newCall(request);
        finaCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {


                Message msg = Message.obtain();
                msg.what = 0;
                msg.obj = "服务器连接失败,请稍后重试";
                mHandler.sendMessage(msg);
                Log.d("httptool", "fali"+e.getMessage()+"^^^"+e);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Message msg = Message.obtain();
                if (response.isSuccessful()) {
                    msg.what = 110;
                    msg.obj = response.body().string();
                    Log.d("httptool", "successful!!!"+msg.obj);
                } else {
                    msg.what = 0;
                    msg.obj = "服务器访问失败,请稍后重试";
                    Log.e("uuz", "successful!!!"+msg.obj);
                }
                Log.d("httptool", "response ");
                mHandler.sendMessage(msg);
            }
        });

    }

    public static void postToHttps(final int i, final Handler mHandler, String url, Map<String, String> map) {
        Log.e("uuz", url);
        OkHttpClient client = OKHttpSingleton.getInstance();
        FormBody.Builder builder = new FormBody.Builder();
        Set keySet = map.keySet();
        for (Iterator<String> iter = keySet.iterator(); iter.hasNext(); ) {
            String key = iter.next();
            String value = map.get(key);
            Log.d("uuz", key + "=" + value);
            if(value == null){
                Log.e("uuz", "postToHttps变量 "+key+"的值为空" );
                Message msg = Message.obtain();
                msg.what = -1;
                msg.obj = "!!变量 "+key+"的值为空";
                mHandler.sendMessage(msg);
                return;
            }
            builder.add(key, value);
        }
        RequestBody body = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        finaCall = client.newCall(request);
        finaCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message msg = Message.obtain();
                msg.what = -1;
                msg.obj = "服务器连接失败,请稍后重试";
                mHandler.sendMessage(msg);
                Log.d("httptool", "fali");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Message msg = Message.obtain();
                if (response.isSuccessful()) {
                    msg.what = 120;
                    msg.arg1 = i;
                    msg.obj = response.body().string();
                } else {
                    msg.what = -1;
                    msg.obj = "服务器访问失败,请稍后重试";
                }
                mHandler.sendMessage(msg);
            }
        });
    }


    public static void sendToServer(final Handler mHandler, String url, Map<String, String> map, File file, ProgressListener progressListener) {
        Log.e("djy", url);
        OkHttpClient client = OKHttpSingleton.getInstance();
        Request request = getFileRequest(url, file, map, progressListener);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message = Message.obtain();
                message.what = 0;
                mHandler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("djy", response.body().string());
                Message message = Message.obtain();
                if (response.isSuccessful()) {
                    message.what = 1;
                    message.obj = response.body().string().trim();
                } else {
                    message.what = 0;
                }
                mHandler.sendMessage(message);

            }
        });
    }

    public static Request getFileRequest(String url, File file, Map<String, String> maps, ProgressListener progressListener) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
//        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        RequestBody fileBody = RequestBody.create(MediaType.parse("text/plain; charset=utf-8"), file);
        if (maps == null) {
            builder.addPart(fileBody);
        } else {
            for (String key : maps.keySet()) {
                builder.addFormDataPart(key, maps.get(key));
            }
            builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"file\";filename=\"file.jpg\""), fileBody);
        }
        RequestBody body = builder.build();
        ProgressRequestBody progressRequestBody = new ProgressRequestBody(body, progressListener);
        return new Request.Builder().url(url).post(progressRequestBody).build();

    }

    //自定义的RequestBody，能够显示进度
    public static class ProgressRequestBody extends RequestBody {
        //实际的待包装请求体
        private final RequestBody requestBody;
        //进度回调接口
        private final ProgressListener progressListener;
        //包装完成的BufferedSink
        private BufferedSink bufferedSink;

        /**
         * 构造函数，赋值
         *
         * @param requestBody      待包装的请求体
         * @param progressListener 回调接口
         */
        public ProgressRequestBody(RequestBody requestBody, ProgressListener progressListener) {
            this.requestBody = requestBody;
            this.progressListener = progressListener;
        }

        /**
         * 重写调用实际的响应体的contentType
         *
         * @return MediaType
         */
        @Override
        public MediaType contentType() {
            return requestBody.contentType();
        }

        /**
         * 重写调用实际的响应体的contentLength
         *
         * @return contentLength
         * @throws IOException 异常
         */
        @Override
        public long contentLength() throws IOException {
            return requestBody.contentLength();
        }

        /**
         * 重写进行写入
         *
         * @param sink BufferedSink
         * @throws IOException 异常
         */
        @Override
        public void writeTo(BufferedSink sink) throws IOException {
            if (bufferedSink == null) {
                //包装
                bufferedSink = Okio.buffer(sink(sink));
            }
            //写入
            requestBody.writeTo(bufferedSink);
            //必须调用flush，否则最后一部分数据可能不会被写入
            bufferedSink.flush();
        }

        /**
         * 写入，回调进度接口
         *
         * @param sink Sink
         * @return Sink
         */
        private Sink sink(Sink sink) {
            return new ForwardingSink(sink) {
                //当前写入字节数
                long bytesWritten = 0L;
                //总字节长度，避免多次调用contentLength()方法
                long contentLength = 0L;

                @Override
                public void write(Buffer source, long byteCount) throws IOException {
                    super.write(source, byteCount);
                    if (contentLength == 0) {
                        //获得contentLength的值，后续不再调用
                        contentLength = contentLength();
                    }
                    //增加当前写入的字节数
                    bytesWritten += byteCount;
                    //回调
                    progressListener.update(bytesWritten, contentLength, bytesWritten == contentLength);
                }
            };
        }
    }

    //进度回调接口
    public interface ProgressListener {
        void update(long bytesRead, long contentLength, boolean done);
    }
}
