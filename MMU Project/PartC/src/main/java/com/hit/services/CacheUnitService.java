package com.hit.services;
import java.io.IOException;

import com.hit.algorithm.IAlgoCache;
import com.hit.algorithm.LRUAlgoCacheImpl;
import com.hit.algorithm.MRUAlgoCacheImpl;
import com.hit.algorithm.RandomAlgoCacheImpl;
import com.hit.algorithm.SecondChanceAlgoCacheImpl;
import com.hit.dao.DaoFileImpl;
import com.hit.dm.DataModel;
import com.hit.memory.CacheUnit;

public class CacheUnitService<T> {

	private CacheUnit<T> cacheUnit;
	boolean status;
	DataModel<T>[] arr;
	Long[] ids;
	int i;
	private int capacity=5;
	private static int funcTotalRequests=0;
	private static int funcDataModelHandles=0;
	
	/// @@@ Need to add Statistics varaibles (should be syncronized in all threads) @@@ ///
	
	public CacheUnitService() { // C'tor
		
		System.out.println("@@ In: Service C'tor");
		cacheUnit = new CacheUnit<T>(new SecondChanceAlgoCacheImpl<>(capacity),
									 new DaoFileImpl<>("src/main/resources/datasource.txt"));
		status = false; // true/false
		arr = null; // array to return
		ids = null;
		i = 0;
	}

	// build []id array that have same length of the input []DataModel array
	// filling the ids from the input []DataModel array into the []id array
	// requesting DataModels from algo\dao using cacheUnit.getDataModels(ids)
	// into new array
	// running on the new array and do the commend: (update\delete\get)
	
	public boolean update(DataModel<T>[] dataModels) {
		funcTotalRequests++;//&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
		funcDataModelHandles += dataModels.length;
		
		System.out.println("@@ In: Service UPDATE");
		
		//resave all dataModels so its update any content that got changed
		ids = new Long[dataModels.length]; // new ids array, same length as input
		i = 0;

		for (DataModel<T> dm : dataModels) {
			ids[i] = dm.getDataModelId();	// filling the ids array with
			i++;							// the ids of the DataModels array
		}
		try {
			arr = cacheUnit.getDataModels(ids); // using the pseudo code
												// to find all the DataModels using the ids array
			
			System.out.println("ID: " + arr[0].getDataModelId() 
								+ " content: " 
								+ arr[0].getContent());
			
			for(i = 0; i < arr.length; i++)  // updating the DM array from the input []dataModels
				arr[i].setContent(dataModels[i].getContent());
			status = true;
			
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		return status;	
	}
	
	public boolean delete(DataModel<T>[] dataModels) {
		funcTotalRequests++;
		funcDataModelHandles += dataModels.length;
		
		System.out.println("@@ In: Service DELETE");
		
		// search for all of the input DataModel array (HDD or RAM) and delete them
		// return true if all done ok
		ids = new Long[dataModels.length]; // new id array, same length as input
		i = 0;
		
		for (DataModel<T> dm : dataModels) {
			ids[i] = dm.getDataModelId();	// filling the ids array with
			i++;							// the ids of the DataModels array
		}
		try {
			arr = cacheUnit.getDataModels(ids); // using the pseudo code
												// to find all the DataModels using the ids array

			System.out.println("ID: " + arr[0].getDataModelId() 
								+ " content: " 
								+ arr[0].getContent());

			for(i = 0 ; i < ids.length; i++)    
				arr[i].setContent((T)"null");        // for every dataModel, content = null (empty)
			status = true;
			
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		
		return status;
	}

	public DataModel<T>[] get(DataModel<T>[] dataModels) {
		funcTotalRequests++;
		funcDataModelHandles += dataModels.length;
		
		System.out.println("@@ In: Service GET");
		
		ids = new Long[dataModels.length]; // new id array, same length as input
		i = 0;
		
		for (DataModel<T> dm : dataModels) {
			ids[i] = dm.getDataModelId();	// filling the ids array
			i++;							// with the ids of the DataModels array
		}
		try {
			arr = cacheUnit.getDataModels(ids); // using the pseudo code
												// to find all the DataModels using the ids array
			
			System.out.println("ID: " + arr[0].getDataModelId() 
								+ " content: " 
								+ arr[0].getContent());
			
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		return arr;
	}
	
	public String getStatistics() {
		
		String stat = "Statistics: \n " + "Capacity: " + capacity + "\n"+
					  "Algorithm: " + "Secound Chance" + "\n"+
					  "Total number of requests: " + funcTotalRequests +"\n"+
					  "Total number of DataModels (GET/UPDATE/DELETE requests): " + funcDataModelHandles + "\n"+
					  "Total number of DataModels swaps: " + cacheUnit.getSwaps()+"\n";
		return stat;
	}
}
