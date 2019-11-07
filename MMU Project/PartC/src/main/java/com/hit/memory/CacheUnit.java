package com.hit.memory;
//&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
import java.util.concurrent.atomic.AtomicInteger;

import com.hit.algorithm.IAlgoCache;
import com.hit.dao.IDao;
import com.hit.dm.DataModel;

public class CacheUnit<T> {
	
	private IAlgoCache<Long, DataModel<T>> algo;  //RAM
	private IDao<Long, DataModel<T>> dao;         //HDD
	private static AtomicInteger swaps = new AtomicInteger(0);
	
	public CacheUnit(IAlgoCache<Long, DataModel<T>> algo, IDao<Long, DataModel<T>> dao) { // C'tor
		this.dao = dao;
		this.algo = algo;
	}
					// Pseudo code //
	public DataModel<T>[] getDataModels(Long[] ids) // input: ids array, output: DataModels array   
           throws java.lang.ClassNotFoundException, java.io.IOException {

		DataModel<T>[] arr = new DataModel[ids.length];
		DataModel<T> temp;
		
		for(int i = 0 ; i < ids.length ; i++) {
			if(algo.getElement(ids[i]) == null) { // if the id is not in the RAM (AlgoCache)
				arr[i] = dao.find(ids[i]);        // get the DataModel from the HDD (file)
				dao.delete(arr[i]);               // delete the DataModel from the HDD (file)
				temp = algo.putElement(ids[i], arr[i]); //enter the DataModel to the RAM (AlgoCache)
				if(temp != null)  	// if cache was full: temp will be the returned DataModel (=V)
				{
					dao.save(temp);	// saves in the HDD the item that got replaced in RAM
					swaps.incrementAndGet();
				}
			} 

			else {  // If the id is in the RAM (AlgoCache)
				arr[i] = algo.getElement(ids[i]);	// copy it to the returning array
					// 8.5.18 was: new DataModel<T>(ids[i], (algo.getElement(ids[i])).getContent());
			}
		}
		return arr;   
	}
	
	public int getSwaps() {//&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
		return swaps.intValue();
	}
}//&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
