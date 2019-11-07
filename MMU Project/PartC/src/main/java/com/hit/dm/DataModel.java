package com.hit.dm;

public class DataModel<T> implements java.io.Serializable {

	private T content;
	private Long id;

	public DataModel(Long id, T content) { // C'tor
		this.id = id;
		this.content = content;
	}

	public void setContent(T content) {
		this.content = content;
	}

	public void setDataModelId(Long id) {
		this.id = id;
	}

	public T getContent() {
		return content;
	}

	public Long getDataModelId() {
		return id;
	}


	@Override
	public boolean equals(Object obj) {
		// returns true if this object same as inputs object
		// false otherwise.
		if (obj == this) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (obj instanceof DataModel) {
			DataModel<T> other = (DataModel<T>) obj;
			return other.getContent().equals(this.getContent()) 
				&& other.getDataModelId().equals(this.getDataModelId());
		} else {
			return false;
		}
	}

	@Override 
	public int hashCode() {
		return (int) (id * content.hashCode()); // return id.hashCode()
	}

	@Override
	public String toString() {
		return (id + " " + content);
	}

}
