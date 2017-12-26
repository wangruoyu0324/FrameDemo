package com.wry.mvp.model;

import com.google.gson.Gson;

import java.io.Serializable;

public class BaseModel implements Serializable {
	
	public String toJson() {
		return new Gson().toJson(this).toString();
	}


}
