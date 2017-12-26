package com.wry.http;

import com.google.gson.Gson;
import com.wry.config.Constant;
import com.wry.mvp.model.BaseModel;
import com.wry.utils.Common;

/**
 * Created by Administrator on 2017/10/25 0025.
 */

public class SignUtil {
//    private Context mContext;

//    public SignUtil(Context mContext) {
//        this.mContext = mContext;
//    }

    public static  String getSign(BaseModel model) {
        String pParam = Common.encryptMode(new Gson().toJson(model));
        String pSign = Common.TransportLetter(Common.md5(String.valueOf(pParam) + Constant.HTTP_SIGN));
        return pSign;
    }
}
