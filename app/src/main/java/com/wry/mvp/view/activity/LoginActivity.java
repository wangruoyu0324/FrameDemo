package com.wry.mvp.view.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.wry.R;
import com.wry.base.BaseActivity;
import com.wry.mvp.model.UserBean;
import com.wry.mvp.presenter.LoginPresenter;
import com.wry.mvp.view.iface.ILoginView;
import com.wry.utils.ToastUtil;
import com.wry.widget.RLoadingDialog;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 登录
 */

public class LoginActivity extends BaseActivity implements ILoginView {

    @BindView(R.id.et_user_name)
    EditText etUserName;
    @BindView(R.id.et_password)
    EditText etPassword;

    private LoginPresenter mLoginPresenter = new LoginPresenter(this, this);

    private RLoadingDialog mLoadingDialog;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_login;
    }

    @Override
    protected void init() {
        mLoadingDialog = new RLoadingDialog(this, true);
    }

    @Override
    protected void initBundleData() {

    }

    @OnClick({R.id.login})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                String userName = etUserName.getText().toString();
                String password = etPassword.getText().toString();

                if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)) {
                    return;
                }

                mLoginPresenter.login(userName, password);
                break;
        }
    }


    @Override
    public void showResult(UserBean bean) {
        if (bean == null) {
            return;
        }
        showToast(bean.getUid());
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
