package com.hit.dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import com.hit.dm.DataModel;
//import com.sun.java.util.jar.pack.Package.File;

public class DaoFileImpl<T> implements IDao<Long, DataModel<T>> {

	private String filePath;	// Address of the file
	File file = null;
	public DaoFileImpl(String filePath) { // C'tor
		this.filePath = filePath;
		file = new File(filePath);
		if (!file.exists()) // if file doesn't exists, initialize it with 1000 empty keys
			for (int i = 1; i <= 1000 ; i++)
				this.save(new DataModel<T>((long) i, null));
	}

	public String getfilePath() {
		return filePath;
	}

	@Override
	public void save(DataModel<T> t) {
		FileOutputStream out = null;
		try {

			this.delete(find(t.getDataModelId())); // if id exists in the file it will replace it
			out = new FileOutputStream(filePath, true);
			out.write((t.toString() + "\n").getBytes());  // "id content"
														  // every row is a data model, example: 1 a
			out.close();
		} catch (IOException e) {
			System.out.println("Error");
		} 
	}

	@Override                                    
	public void delete(DataModel<T> t) {
		try {
			File file = new File(filePath);
			if (t != null) {
				BufferedReader br = new BufferedReader(new FileReader(file));
				String[] st_id;
				String st;															//new file
				PrintWriter input = new PrintWriter(new BufferedWriter(new FileWriter(filePath + ".txt")));
				while ((st = br.readLine()) != null) {
					st_id = st.split(" "); // st_id[0] will hold only the id
					if (!st_id[0].equals(t.getDataModelId().toString())) // st_id[0] =? t.id
						input.println(st_id[0] + " " + st_id[1]);
				}
				br.close();
				input.close();
				
				if (!file.delete())
					System.out.println("Couldn't delete original file");
				new File(filePath + ".txt").renameTo(new File(filePath)); //rename the new file to original name
			}
		} catch (IOException ioe) {
			System.out.println(filePath + " couldn't be opened ");
		}
	}
	
		
	@Override
	public DataModel<T> find(Long id) {
		DataModel<T> t1 = null;
		String idtoserch = Long.toString(id); //casting Long to String
		String[] parts;
		String line;
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(filePath));
			while ((line = reader.readLine()) != null) { // read line from the file
				parts = line.split(" "); // id content
				if (parts[0].equals(idtoserch)) { // parts[0] is the current id
					String part2 = parts[1];      // parts[1] is the current value
					t1 = new DataModel<T>(id, (T) part2); //creating DM with the findings 
					reader.close();
					return t1;
				}
			}
		reader.close();
		} catch (IOException ioe) {
			System.out.println(" IOException ");
		} catch (IllegalArgumentException e) {
			System.out.println("id is not found or id is null");
		} 
		return t1;
	}
}