/*******************************************************************************
 * Copyright (c) 2014 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.wry.database;

import com.wry.MyApplication;

public class AppConfigManager {

	public static AppConfigPB getInitedAppConfig() {
		if (MyApplication.appConfigPB == null) {
			AppConfigPB appConfigPB = new AppConfigPB();
			appConfigPB.init(MyApplication.mContext);
			MyApplication.appConfigPB = appConfigPB;
		}
		return MyApplication.appConfigPB;
	}
}
