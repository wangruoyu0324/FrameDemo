/*******************************************************************************
 * Copyright (c) 2014 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * <p/>
 * Contributors:
 * IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.wry.database;

import android.content.Context;

import java.io.Serializable;

/**
 * app全局参数设置
 */
public class AppConfigPB extends PreferenceBeanHelper implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public static final String ID = "id";//id
    private int id = 0;
    public static final String ACCOUNT = "account";//账号
    private String account = "1";

    public void init(Context context) {
        super.init(context);
        try {
            loadFromPref();
        } catch (Exception e) {
            LogFactory.createLog().error(e);
        }
    }

    public void initNoSync(Context context) {
        super.init(context);
    }

    public boolean isDataInvalid() {
        if (id == 0) {
            return true;
        } else
            return false;
    }

    public void setDataInvalid() {//设置本地数据无效，清除本地数据（退出账号时）
        this.id = 0;
        try {
            clearPref();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateAll() {//更新所有本地数据
        try {
            super.updatePreferAll();
        } catch (Exception e) {
            LogFactory.createLog().error(e);
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
