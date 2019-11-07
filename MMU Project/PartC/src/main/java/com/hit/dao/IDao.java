package com.hit.dao;

//what we can do with the HDD file 
public interface IDao<ID extends java.io.Serializable, T> {
	
	void save(T entity);
	/*
	Saves a given entity.
	Parameters:
	entity - given entity.
	*/
	
	void delete(T entity);
	/* 
		Deletes a given entity.
		Parameters:
		entity - given entity.
		Throws:
		java.lang.IllegalArgumentException - in case the given entity is null.
	*/
	
	T find(ID id);
	/*
		Retrieves an entity by its id.
		Parameters:
		id - must not be null.
		Returns:
		the entity with the given id or null if none found
		Throws:
		java.lang.IllegalArgumentException - if id is null
	*/
}
