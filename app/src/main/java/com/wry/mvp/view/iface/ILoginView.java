package com.wry.mvp.view.iface;

import com.wry.base.IBaseView;
import com.wry.mvp.model.UserBean;

/**
 * 登录view
 *
 * @author ZhongDaFeng
 */

public interface ILoginView extends IBaseView {

    //显示结果
    void showResult(UserBean bean);

}
