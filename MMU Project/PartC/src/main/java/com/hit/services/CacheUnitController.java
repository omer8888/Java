package com.hit.services;

import com.hit.dm.DataModel;

//the operations we can do with the request
public class CacheUnitController<T> {

	private CacheUnitService<T> cacheUnitService;

	public CacheUnitController() { // C'tor
		System.out.println("@@ In: Controller C'tor");
		cacheUnitService = new CacheUnitService<T>();
	}

	public boolean update(DataModel<T>[] dataModels) {
		System.out.println("@@ In: Controller UPDATE");
		return cacheUnitService.update(dataModels);
	}

	public boolean delete(DataModel<T>[] dataModels) {
		System.out.println("@@ In: Controller DELETE");
		return cacheUnitService.delete(dataModels);
	}

	public DataModel<T>[] get(DataModel<T>[] dataModels) {
		System.out.println("@@ In: Controller GET");
		return cacheUnitService.get(dataModels);
	}

	public String getStatistics() {
		System.out.println("@@ In: Controller Statistics");
		return cacheUnitService.getStatistics();
	}
	
}
