package com.lw.looklook.api;

import com.lw.looklook.GlobalApplication;
import com.lw.looklook.utils.NetWorkUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by lw on 2017/2/1.
 */

public class ApiManager {

    public static ApiManager apiManager;

    public ZhihuApi zhihuApi;

    private Object zhihuMonitor = new Object();

    /**
     * 创建拦截器
     * 使用拦截器控制缓存时间
     */
    private static final Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Response response = chain.proceed(chain.request());
            if (NetWorkUtil.getCurrentNetWorkState(GlobalApplication.getContext())) {// 当前网络可用
                int maxAge = 60;
                return response.newBuilder()
                        .removeHeader("Pragma")
                        .removeHeader("Cache-Control")
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        .build();
            } else {
                int maxAge = 60 * 60 * 24 * 28;
                return response.newBuilder()
                        .removeHeader("Pragma")
                        .removeHeader("Cache-Control")
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        .build();
            }
        }
    };

    /**
     * 创建OkHttpClient
     */
    private static OkHttpClient client = new OkHttpClient.Builder()
            .addNetworkInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR) // 注册网络拦截器
            .addInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR) // 注册应用拦截器
            .build();

    /**
     * 获取ZhihuApi对象
     *
     * @return
     */
    public ZhihuApi getZhihuApiService() {
        if (zhihuApi == null) {
            synchronized (zhihuMonitor) {
                if (zhihuApi == null) {
                    zhihuApi = new Retrofit.Builder()
                            .baseUrl("http://news-at.zhihu.com")
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                            .client(client)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build().create(ZhihuApi.class);
                }
            }
        }
        return zhihuApi;
    }


    /**
     * 单例
     *
     * @return
     */
    public static ApiManager getInstance() {
        if (apiManager == null) {
            synchronized (ApiManager.class) {
                if (apiManager == null) {
                    apiManager = new ApiManager();
                }
            }
        }
        return apiManager;
    }

}
