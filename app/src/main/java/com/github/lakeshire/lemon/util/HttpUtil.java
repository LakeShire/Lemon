package com.github.lakeshire.lemon.util;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by liuhan on 2016/3/23.
 */
public class HttpUtil {

    private static HttpUtil sHttpUtil;

    public static HttpUtil getInstance() {
        if (sHttpUtil == null) {
            synchronized (HttpUtil.class) {
                if (sHttpUtil == null) {
                    sHttpUtil = new HttpUtil();
                }
            }
        }
        return sHttpUtil;
    }

    private HttpUtil() {

    }

    public interface Callback {
        void onFail(String error);
        void onSuccess(String response);
    }

    /**
     * 异步GET
     * @param url
     * @param cb
     * @param timeout
     */
    public void get(String url, final Callback cb, int timeout) {
        OkHttpClient client;
        if (timeout == 0) {
            client = new OkHttpClient();
        } else {
            client = new OkHttpClient().newBuilder().connectTimeout(timeout, TimeUnit.MILLISECONDS).build();
        }
        Request request = new Request.Builder().url(url).build();
        Call call = client.newCall(request);
        call.enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (cb != null) {
                    cb.onFail(e.getMessage());
                }

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (cb != null) {
                    cb.onSuccess(response.body().string());
                }
            }
        });
    }

    /**
     * 同步GET
     * @param url
     * @return
     */
    public String getSync(String url, int timeout) {
        OkHttpClient client;
        if (timeout == 0) {
            client = new OkHttpClient();
        } else {
            client = new OkHttpClient().newBuilder().connectTimeout(timeout, TimeUnit.MILLISECONDS).build();
        }
        Request request = new Request.Builder().url(url).build();
        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            if (response != null) {
                return response.body().string();
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
