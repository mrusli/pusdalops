package mil.pusdalops.persistence.userrole.dao;

import java.util.List;

import mil.pusdalops.domain.authorization.UserRole;

public interface UserRoleDao {

	/**
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public UserRole findUserRoleById(long id) throws Exception;
	
	/**
	 * @return
	 * @throws Exception
	 */
	public List<UserRole> findAllUserRole() throws Exception;
	
	/**
	 * @param userRole
	 * @return
	 * @throws Exception
	 */
	public Long save(UserRole userRole) throws Exception;
	
	/**
	 * @param userRole
	 * @throws Exception
	 */
	public void update(UserRole userRole) throws Exception;
	
}
