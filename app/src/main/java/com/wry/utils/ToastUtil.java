package com.wry.utils;

import android.widget.Toast;

import com.wry.MyApplication;

/**
 * @author wry
 */
public class ToastUtil {

    private static Toast mToast;

    public static void showShort(int resid) {
        show(MyApplication.mContext.getString(resid), Toast.LENGTH_SHORT);
    }

    public static void showShort(String msg) {
        show(msg, Toast.LENGTH_SHORT);
    }

    public static void showLong(int resid) {
        show(MyApplication.mContext.getString(resid), Toast.LENGTH_LONG);
    }

    public static void showLong(String msg) {
        show(msg, Toast.LENGTH_LONG);
    }

    public static void show(String msg, int duration) {
        if (Common.empty(msg)) {
            return;
        }
        if (mToast == null) {
            mToast = Toast.makeText(MyApplication.mContext, msg, duration);
            // 设置消息颜色
//            TextView textView = (TextView) mToast.getView().findViewById(android.R.id.message);
//            textView.setTextColor(Color.WHITE);
//            textView.setTextSize(Common.px2sp(IApp.mContext, IApp.mContext.getResources().getDimensionPixelSize(R.dimen.x13)));
//            textView.setPadding(IApp.mContext.getResources().getDimensionPixelSize(R.dimen.x20),
//                    0,
//                    IApp.mContext.getResources().getDimensionPixelSize(R.dimen.x20),
//                    0);
            // 设置背景颜色
//            mToast.getView().setBackgroundColor(ContextCompat.getColor(IApp.mContext, R.color.colorAccent));
        } else {
            mToast.setText(msg);
            mToast.setDuration(duration);
        }

        mToast.show();
    }

}
