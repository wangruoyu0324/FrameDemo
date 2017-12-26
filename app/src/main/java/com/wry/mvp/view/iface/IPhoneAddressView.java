package com.wry.mvp.view.iface;


import com.wry.base.IBaseView;
import com.wry.mvp.model.AddressBean;

/**
 * 手机归属地页面view接口
 *
 * @author ZhongDaFeng
 */

public interface IPhoneAddressView extends IBaseView {

    //显示结果
    void showResult(AddressBean bean);

}
