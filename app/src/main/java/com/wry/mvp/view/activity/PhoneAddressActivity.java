package com.wry.mvp.view.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wry.R;
import com.wry.base.BaseActivity;
import com.wry.mvp.model.AddressBean;
import com.wry.mvp.presenter.PhoneAddressPresenter;
import com.wry.mvp.view.iface.IPhoneAddressView;
import com.wry.utils.ToastUtil;
import com.wry.widget.RLoadingDialog;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 手机号码归属地查询页面
 *
 * @author ZhongDaFeng
 */

public class PhoneAddressActivity extends BaseActivity implements IPhoneAddressView {


    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_province)
    TextView tvProvince;
    @BindView(R.id.tv_city)
    TextView tvCity;
    @BindView(R.id.tv_operator)
    TextView tvOperator;

    private PhoneAddressPresenter mPhonePst = new PhoneAddressPresenter(this, this);
    private RLoadingDialog mLoadingDialog;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_phone_address;
    }

    @Override
    protected void init() {
        mLoadingDialog = new RLoadingDialog(this, true);
    }

    @Override
    protected void initBundleData() {

    }


    @OnClick({R.id.submit})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
                attemptSubmit();
                break;
        }
    }

    /**
     * 尝试提交
     */
    private void attemptSubmit() {
        String phone = etPhone.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(mContext, getString(R.string.hint_phone), Toast.LENGTH_SHORT).show();
        }

        mPhonePst.getInfo(phone);

    }

    @Override
    public void showResult(AddressBean bean) {
        if (bean == null) {
            return;
        }
        tvPhone.setText(bean.getMobileNumber());
        tvCity.setText(bean.getCity());
        tvProvince.setText(bean.getProvince());
        tvOperator.setText(bean.getOperator());
    }

    @Override
    public void showLoading() {
        mLoadingDialog.show();
    }

    @Override
    public void closeLoading() {
        mLoadingDialog.dismiss();
    }

    @Override
    public void showToast(String msg) {
        ToastUtil.showShort(msg);
    }
}
