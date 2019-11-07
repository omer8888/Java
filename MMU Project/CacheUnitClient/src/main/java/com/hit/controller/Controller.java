package com.hit.controller;

import java.util.Observable;

public interface Controller extends java.util.Observer {

	public void update(Observable obs, Object obj);
	
}
