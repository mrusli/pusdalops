package mil.pusdalops.persistence.user.dao.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import mil.pusdalops.domain.authorization.User;
import mil.pusdalops.domain.kotamaops.Kotamaops;
import mil.pusdalops.persistence.common.dao.hibernate.DaoHibernate;
import mil.pusdalops.persistence.user.dao.UserDao;

public class UserHibernate extends DaoHibernate implements UserDao {

	private SessionFactory sessionFactory;
	
	/**
	 * @return the sessionFactory
	 */
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	/**
	 * @param sessionFactory the sessionFactory to set
	 */
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}	
	
	public User findUserById(long id) throws Exception {
		
		return (User) super.findById(User.class, id);
	}

	public User findUserByUsername(String username) {
		Session session = getSessionFactory().openSession();
		
		Criteria criteria = session.createCriteria(User.class);

		try {
			
			return (User) criteria.add(Restrictions.eq("userName", username)).uniqueResult();
			
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
	
	}

	@SuppressWarnings("unchecked")
	public List<User> findAllUsers() throws Exception {

		return new ArrayList<User>(super.findAll(User.class));
	}

	public Long save(User user) throws Exception {

		return super.save(user);
	}

	public void update(User user) throws Exception {

		super.update(user);
	}

	@SuppressWarnings("unchecked")
	public List<User> findAllUsersByKotamaops(Kotamaops kotamaops) throws Exception {
		Session session = getSessionFactory().openSession();
		
		Criteria criteria = session.createCriteria(User.class);
		criteria.add(Restrictions.eq("kotamaops", kotamaops));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		try {
			
			return criteria.list();
			
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
	}

	@Override
	public User findUserKotamaopsByProxy(long id) throws Exception {
		Session session = getSessionFactory().openSession();

		Criteria criteria = session.createCriteria(User.class);
		User user = (User) criteria.add(Restrictions.idEq(id)).uniqueResult();
		
		try {
			Hibernate.initialize(user.getKotamaops());
			
			return user;
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
	}

	@Override
	public void delete(User user) throws Exception {
		
		super.delete(user);
	}

}
