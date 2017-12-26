package com.wry;

import android.content.Intent;
import android.view.View;

import com.wry.base.BaseActivity;
import com.wry.mvp.view.activity.LoginActivity;
import com.wry.mvp.view.activity.PhoneAddressActivity;

import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initBundleData() {
    }

    @OnClick({R.id.login, R.id.phone_address})
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.login:
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.phone_address:
                intent = new Intent(this, PhoneAddressActivity.class);
                startActivity(intent);
                break;
        }
    }

}

