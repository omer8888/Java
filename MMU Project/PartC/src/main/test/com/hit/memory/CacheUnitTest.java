package com.hit.memory;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.hit.algorithm.*;
import com.hit.dao.DaoFileImpl;
import com.hit.dao.IDao;
import com.hit.dm.DataModel;

import java.io.IOException;
import java.lang.String;

class CacheUnitTest {

	@Test
	public void getDataModelsTest() {

		IAlgoCache<Long, DataModel<String>> algo = new LRUAlgoCacheImpl<>(5);
		IDao<Long, DataModel<String>> dao = new DaoFileImpl<>("src/main/resources/datasource.txt");
		CacheUnit<String> cacheUnit = new CacheUnit<>(algo, dao);

		// HDD (save, find & delete test) //
		dao.save(new DataModel<String>((long) 1, "a"));
		dao.save(new DataModel<String>((long) 2, "b"));
		dao.save(new DataModel<String>((long) 3, "c"));
		dao.save(new DataModel<String>((long) 4, "d"));
		
		assertEquals(new DataModel<String>((long)4,"d"), dao.find((long)4), "Error") ; // 
		assertEquals(null, dao.find((long)5), "Error") ; // find 5, should be null because 5 not exists
		dao.delete(new DataModel<String>((long)4,"d")) ; // delete 4-d
		assertEquals(null, dao.find((long)4), "Error") ; // find 4-d, should be null because 4 not exists
		dao.delete(new DataModel<String>((long)2,"b")) ; // delete 2-b
		dao.save(new DataModel<String>((long) 4, "d")) ; // save 4-d
		// // // // // // // // // // // //
			
		// RAM
		algo.putElement((long) 11, new DataModel<String>((long) 11, "11"));
		algo.putElement((long) 12, new DataModel<String>((long) 12, "12"));
		algo.putElement((long) 13, new DataModel<String>((long) 13, "13"));
		algo.putElement((long) 14, new DataModel<String>((long) 14, "14"));
		algo.putElement((long) 15, new DataModel<String>((long) 15, "15"));
		assertEquals(new DataModel<String>((long)11,"11"), algo.getElement((long)11), "Error");
		// // // // // // // // // // // //

		Long ids[] = { (long) 1, (long) 14 }; // related to the pseudo code
		DataModel<String>[] models;

		try {
			models = cacheUnit.getDataModels(ids);
			assertEquals((long) 4, (long)dao.find((long) 4).getDataModelId(), "Error");
			assertEquals("a", algo.getElement((long) 1).getContent(), "Error");
			assertEquals("11", dao.find((long) 11).getContent(), "Error");
			
			for(DataModel<String> t : models)
				System.out.println(t.toString());
			
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
	}
}