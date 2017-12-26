package com.wry;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.squareup.leakcanary.RefWatcher;
import com.wry.config.Constant;
import com.wry.database.AppConfigManager;
import com.wry.database.AppConfigPB;
import com.wry.database.UpdateManager;
import com.wry.fresco.ImagePipelineConfigFactory;
import com.wry.utils.Common;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author wry
 * @time 2016.10.10 15:15
 */

public class MyApplication extends MultiDexApplication {

    private final String TAG = "MyApplication";

    public static Context mContext;

    public static AppConfigPB appConfigPB;

    public static Retrofit mRetrofit;
    //LeakCanary展现Android中的内存泄露 https://github.com/SOFTPOWER1991/leakcanarySample_androidStudio
    private RefWatcher refWatcher;
    // APIS
    private final HashMap<Class, Object> apis = new HashMap<>();

    private List<Activity> activityList = new ArrayList<Activity>();
    // 单例模式
    private static MyApplication instance;


    /**
     * 单例模式中获取唯一的MyApp实例
     *
     * @return
     */
    public static MyApplication getInstance() {
        if (null == instance) {
            instance = new MyApplication();
        }
        return instance;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onCreate() {
        mContext = getApplicationContext();

//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return;
//        }
//        refWatcher = LeakCanary.install(this);//LeakCanary展现Android中的内存泄露

        ImagePipelineConfig imagePipelineConfig = ImagePipelineConfigFactory.getOkHttpImagePipelineConfig(mContext);
        Fresco.initialize(this, imagePipelineConfig);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

        new Thread(new Runnable() {
            @Override
            public void run() {
                appConfigPB = AppConfigManager.getInitedAppConfig();
                try {
                    appConfigPB.loadFromPref();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                UpdateManager.newUpdateManager();

            }
        }).start();
        initRetrofit2();
        super.onCreate();
    }


    public static Retrofit initRetrofit2() {
        if (mRetrofit == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            /**
             *    设置缓存
             */
            //缓存机制  无网络时，也能显示数据。
            File cacheFile = new File(mContext.getExternalCacheDir(), Constant.OKHTTP_CACHE_DIR);
            Cache cache = new Cache(cacheFile, Constant.MAX_CACHE_SIZE_INBYTES);
            Interceptor cacheInterceptor = new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request();
                    if (!Common.isNetWorkConnected(mContext)) {
                        request = request.newBuilder()
                                .cacheControl(CacheControl.FORCE_CACHE)
                                .build();
                    }
                    Response response = chain.proceed(request);
                    if (Common.isNetWorkConnected(mContext)) {
                        int maxAge = 0;
                        // 有网络时 设置缓存超时时间0个小时
                        response.newBuilder()
                                .header("Cache-Control", "public, max-age=" + maxAge)
                                .removeHeader("Demo")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                                .build();
                    } else {
                        // 无网络时，设置超时为4周
                        int maxStale = 60 * 60 * 24 * 28;
                        response.newBuilder()
                                .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                                .removeHeader("nyn")
                                .build();
                    }
                    return response;
                }
            };
            builder.cache(cache).addInterceptor(cacheInterceptor);
            /**
             *  公共参数
             */
//            Interceptor addQueryParameterInterceptor = new Interceptor() {
//                @Override
//                public Response intercept(Chain chain) throws IOException {
//                    Request originalRequest = chain.request();
//                    Request request;
//                    String method = originalRequest.method();
//                    Headers headers = originalRequest.headers();
//                    HttpUrl modifiedUrl = originalRequest.url().newBuilder()
//                            // Provide your custom parameter here
//                            .addQueryParameter("pSign", Common.TransportLetter(Common.md5(String.valueOf("value") + "9305304045322310858564011856356157307354665486554495331901675949")))
//                            .build();
//                    request = originalRequest.newBuilder().url(modifiedUrl).build();
//                    return chain.proceed(request);
//                }
//            };
//            builder.addInterceptor(addQueryParameterInterceptor);

            /**
             * 设置头
             */
            Interceptor headerInterceptor = new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request originalRequest = chain.request();
                    Request.Builder requestBuilder = originalRequest.newBuilder()
                            .header("AppType", "TPOS")
                            .header("Content-Type", "application/json")
                            .header("Accept", "application/json")
                            .method(originalRequest.method(), originalRequest.body());
                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            };
            builder.addInterceptor(headerInterceptor);


            /**
             * Log信息拦截器
             */
            //设置拦截器
            if (BuildConfig.DEBUG) {
                //Log信息拦截器
                HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                //设置Log
                builder.addInterceptor(loggingInterceptor);
            }

            /**
             * 设置cookie
             */
//            CookieManager cookieManager = new CookieManager();
//            cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
//            builder.cookieJar(new JavaNetCookieJar(cookieManager));

            /**
             * 设置超时和重连
             */
            //设置超时时间
            builder.connectTimeout(Constant.CONNECT_TIME_OUT, TimeUnit.SECONDS);
            builder.readTimeout(Constant.READ_TIME_OUT, TimeUnit.SECONDS);
            builder.writeTimeout(Constant.WRITE_TIME_OUT, TimeUnit.SECONDS);
            //错误重连
            builder.retryOnConnectionFailure(true);

            //以上设置完成
            OkHttpClient okHttpClient = builder.build();

            mRetrofit = new Retrofit.Builder()
                    .baseUrl(Constant.BASE_URL)
                    //设置 Json 转换器
                    .addConverterFactory(GsonConverterFactory.create())
                    //RxJava 适配器
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
        return mRetrofit;
    }


    public static MyApplication from(Context context) {
        Context application = context.getApplicationContext();
        if (application instanceof MyApplication) {
            return (MyApplication) application;
        } else {
            throw new IllegalArgumentException("context must be from ImApp");
        }
    }

    private String buildAcceptLanguage() {
        Locale locale = Locale.getDefault();
        return String.format("%s-%s,%s;q=0.8,en-US;q=0.6,en;q=0.4",
                locale.getLanguage(), locale.getCountry(), locale.getLanguage());
    }


    /**
     * 使高版本API的代码在低版本SDK不报错,4.0
     * http://www.tuicool.com/articles/BreuUz
     *
     * @return
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private String buildUserAgent() {
        String userAgent = String.format("Retrofit %s Android (%d/%s)", BuildConfig.VERSION_NAME, Build.VERSION.SDK_INT, Build.VERSION.RELEASE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            DisplayMetrics metrics = new DisplayMetrics();
            WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
            wm.getDefaultDisplay().getRealMetrics(metrics);
            userAgent += String.format(" (%d; %dx%d)", metrics.densityDpi, metrics.widthPixels, metrics.heightPixels);
        }
        return userAgent;
    }

    /**
     * 创建一个api
     *
     * @param service
     * @param <T>
     * @return
     */
    public <T> T createCoreApi(Class<T> service) {
        if (!apis.containsKey(service)) {
            T instance = mRetrofit.create(service);
            apis.put(service, instance);
        }
        return (T) apis.get(service);
    }

    /**
     * 获取 api
     *
     * @param service
     * @param <T>
     * @return
     */

    public <T> T getApi(Class<T> service) {
        if (!apis.containsKey(service)) {
            T instance = mRetrofit.create(service);
            apis.put(service, instance);
        }
        return (T) apis.get(service);
    }

    /**
     * LeakCanary
     * https://github.com/SOFTPOWER1991/leakcanarySample_androidStudio
     *
     * @param context
     * @return
     */
    public static RefWatcher getRefWatcher(Context context) {
        MyApplication application = (MyApplication) context
                .getApplicationContext();
        return application.refWatcher;
    }
///////////////////////////////////////////////////////////////////////////
    /**
     * 用于全部退出 在每个Activity
     * 类中onCreate()方法中调用MyApp.getInstance().addActivity(Activity activity)方法。
     * 在任何一个Activity
     * 界面退出应用程序时，只要调用MyApp.getInstance().exit()方法，就可以在任何一个Activity中完全退出应用程序。
     */
    /**
     * 添加Activity 到容器中
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        System.out.println("@@@@addactivity" + activity.getPackageName()
                + activity.getClass().getName());
        activityList.add(activity);
    }

    public void remove(Activity activity) {
        System.out.println("@@@@remove" + activity.getPackageName()
                + activity.getClass().getName());
        activityList.remove(activity);
    }

    /**
     * 完全退出
     */
    public void finishActivity() {
        System.out.println(activityList.size() + "======");
        for (Activity activity : activityList) {
            System.out.println("=========" + activity.getLocalClassName());
            if (null != activity) {
                activity.finish();
            }
        }
        android.os.Process.killProcess(android.os.Process.myPid());//杀死线程
        System.exit(0);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}