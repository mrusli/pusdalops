package mil.pusdalops.persistence.userrole.dao.hibernate;

import java.util.List;

import mil.pusdalops.domain.authorization.UserRole;
import mil.pusdalops.persistence.common.dao.hibernate.DaoHibernate;
import mil.pusdalops.persistence.userrole.dao.UserRoleDao;

public class UserRoleHibernate extends DaoHibernate implements UserRoleDao {

	@Override
	public UserRole findUserRoleById(long id) throws Exception {

		return (UserRole) super.findById(UserRole.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserRole> findAllUserRole() throws Exception {

		return super.findAll(UserRole.class);
	}

	@Override
	public Long save(UserRole userRole) throws Exception {

		return super.save(userRole);
	}

	@Override
	public void update(UserRole userRole) throws Exception {

		super.update(userRole);
	}

}
