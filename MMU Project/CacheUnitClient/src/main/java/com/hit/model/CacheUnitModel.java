package com.hit.model;

import java.util.Observable;

public class CacheUnitModel extends Observable implements Model {

	CacheUnitClient cacheUnitClient;
	
	public CacheUnitModel() { // C'tor
		System.out.println("In: CacheUnitModel C'tor");
		cacheUnitClient = new CacheUnitClient();
	}
	
	@Override
	public <T> void updateModelData(T t) {
		System.out.println("In: CacheUnitModel 'updateModelData'");
		System.out.println(t);
		
		String response = cacheUnitClient.send(t.toString());
		System.out.println(response);
		
		setChanged();
		notifyObservers(response);
	}
}
