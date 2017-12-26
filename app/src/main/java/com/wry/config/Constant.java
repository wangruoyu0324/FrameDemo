package com.wry.config;

import android.os.Environment;

import com.wry.MyApplication;


/**
 * Created by Administrator on 2017/10/24 0024.
 */

public class Constant {
    public static final String BASE_URL = "https://msg.ac57.cn/";//请求数据域名
    //    public static final String BASE_URL = "https://api.ac57.cn/";//请求数据域名
    public static final String STORE_PATH = Environment.getExternalStorageDirectory() + "/Android/data/" + MyApplication.mContext.getPackageName() + "/YunShangHui/";//图片保存文件夹
    public static final long CONNECT_TIME_OUT = 15;//网络连接超时时间
    public static final long READ_TIME_OUT = 20;//网络读取内容超时时间
    public static final long WRITE_TIME_OUT = 20;//网络写入内容超时时间
    public static final String OKHTTP_CACHE_DIR = "okhttp_cache_dir";//网络请求数据缓存
    public static final long MAX_CACHE_SIZE_INBYTES = 1024 * 1024 * 50;//网络请求数据缓存大小 50M

    public static final String HTTP_SIGN = "9305304045322310858564011856356157307354665486554495331901675949";//网络请求数据header签名

}
