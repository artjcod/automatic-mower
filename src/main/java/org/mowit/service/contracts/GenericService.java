package org.mowit.service.contracts;

import java.util.Collection;


public interface GenericService {
	
	public void merge(Object obj);
	public void delete(Object obj);
	public Collection<? extends Object> findAll();
	public Object findById(Object id);

}
