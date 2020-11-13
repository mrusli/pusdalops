package mil.pusdalops.persistence.common.dao;

import java.util.List;

public interface Dao {
	
	/**
	 * Find an object by its id, passing in the Class name.
	 * 
	 * @param target
	 * @param id
	 * @return Object
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public Object findById(Class target, Long id) throws Exception;
	
	/**
	 * Find all objects in a list, passing in the Class name.
	 * 
	 * @param target
	 * @return List
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public List findAll(Class target) throws Exception;
	
	/**
	 * Save an object
	 * 
	 * @param object
	 * @return Long
	 * @throws Exception
	 */
	public Long save(Object object) throws Exception;
	
	/**
	 * Update an object
	 * 
	 * @param object
	 * @throws Exception
	 */
	public void update(Object object) throws Exception;

	/**
	 * Delete an object
	 * 
	 * @param object
	 * @throws Exception
	 */
	public void delete(Object object) throws Exception;
	
}
