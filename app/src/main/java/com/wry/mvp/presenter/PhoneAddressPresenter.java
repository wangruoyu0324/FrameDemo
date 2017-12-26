package com.wry.mvp.presenter;

import com.google.gson.Gson;
import com.wry.base.BasePresenter;
import com.wry.http.Api.ApiUtils;
import com.wry.http.exception.ApiException;
import com.wry.http.observer.HttpRxObservable;
import com.wry.http.observer.HttpRxObserver;
import com.wry.http.retrofit.HttpRequest;
import com.wry.mvp.model.AddressBean;
import com.wry.mvp.view.activity.PhoneAddressActivity;
import com.wry.mvp.view.iface.IPhoneAddressView;
import com.wry.utils.LogUtils;

import java.util.Map;

import io.reactivex.disposables.Disposable;

/**
 * 手机号归属地Presenter
 *
 * @author ZhongDaFeng
 */

public class PhoneAddressPresenter extends BasePresenter<IPhoneAddressView, PhoneAddressActivity> {

    private final String TAG = PhoneAddressPresenter.class.getSimpleName();

    public PhoneAddressPresenter(IPhoneAddressView view, PhoneAddressActivity activity) {
        super(view, activity);
    }


    /**
     * 获取信息
     *
     * @author ZhongDaFeng
     */
    public void getInfo(String phone) {

        //构建请求数据
        Map<String, Object> request = HttpRequest.getRequest();
        request.put("phone", phone);

        HttpRxObserver httpRxObserver = new HttpRxObserver(TAG + "getInfo") {

            @Override
            protected void onStart(Disposable d) {
                if (getView() != null)
                    getView().showLoading();
            }

            @Override
            protected void onError(ApiException e) {
                LogUtils.w("onError code:" + e.getCode() + " msg:" + e.getMsg());
                if (getView() != null) {
                    getView().closeLoading();
                    getView().showToast(e.getMsg());
                }
            }

            @Override
            protected void onSuccess(Object response) {
                LogUtils.w("onSuccess response:" + response.toString());
                AddressBean bean = new Gson().fromJson(response.toString(), AddressBean.class);
                if (getView() != null) {
                    getView().closeLoading();
                    getView().showResult(bean);
                }
            }
        };

        HttpRxObservable.getObservable(ApiUtils.getPhoneApi().phoneQuery(request), getActivity()).subscribe(httpRxObserver);

        //取消请求
        /*if(!httpRxObserver.isDisposed()){
            httpRxObserver.cancel();
        }*/

    }


}
