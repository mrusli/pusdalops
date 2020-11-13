package mil.pusdalops.persistence.user.dao;

import java.util.List;

import mil.pusdalops.domain.authorization.User;
import mil.pusdalops.domain.kotamaops.Kotamaops;

public interface UserDao {

	/**
	 * Return a User object given its id
	 * 
	 * @param id
	 * @return User
	 * @throws Exception
	 */
	public User findUserById(long id) throws Exception;
	
	/**
	 * Return a User object given its username
	 * 
	 * @param username
	 * @return User
	 * @throws Exception
	 */
	public User findUserByUsername(String username);
	
	/**
	 * Return all users
	 * 
	 * @return List<User>
	 * @throws Exception
	 */
	public List<User> findAllUsers() throws Exception;
	
	/**
	 * Save a new user object, return its id if it's successfully saved
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public Long save(User user) throws Exception;
	
	/**
	 * Update an existing user object
	 * 
	 * @param user
	 * @throws Exception
	 */
	public void update(User user) throws Exception;
	
	/**
	 * Return all users matching the kotamaops object
	 * 
	 * @param kotamaops
	 * @return List<User>
	 * @throws Exception
	 */
	public List<User> findAllUsersByKotamaops(Kotamaops kotamaops) throws Exception;

	/**
	 * Kotamaops is fectched Lazy.  Requires hibernate initialization.
	 * 
	 * @param id
	 * @return User
	 * @throws Exception
	 */
	public User findUserKotamaopsByProxy(long id) throws Exception;

	/**
	 * @param user
	 * @throws Exception
	 */
	public void delete(User user) throws Exception;

}
