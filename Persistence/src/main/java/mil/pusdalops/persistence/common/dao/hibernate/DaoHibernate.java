package mil.pusdalops.persistence.common.dao.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import mil.pusdalops.persistence.common.dao.Dao;

public class DaoHibernate implements Dao {

	private SessionFactory sessionFactory;
	
	@SuppressWarnings("rawtypes")
	public Object findById(Class target, Long id) throws Exception {
		Session session = getSessionFactory().openSession();
		
		Criteria criteria = session.createCriteria(target);
		
		try {
			
			return criteria.add(Restrictions.idEq(id)).uniqueResult();			
			
		} catch (Exception e) {
			throw e;
			
		} finally {
			session.close();
		}
	}

	@SuppressWarnings("rawtypes")
	public List findAll(Class target) throws Exception {
		Session session = getSessionFactory().openSession();
		
		Criteria criteria = session.createCriteria(target);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		try {

			return criteria.list();
			
		} catch (Exception e) {
			throw e;
			
		} finally {
			session.close();

		}
	}

	public Long save(Object object) throws Exception {
		Long id = -1L;
		
		Session session = getSessionFactory().openSession();
		
		Transaction transaction = session.beginTransaction();
		
		try {
			id = (Long) session.save(object);
			
			transaction.commit();
			
			return id;
		} catch (Exception e) {
			throw e;
			
		} finally {
			session.close();
			
		}
	}

	public void update(Object object) throws Exception {
		Session session = getSessionFactory().openSession();
		
		Transaction transaction = session.beginTransaction();

		try {
			session.update(object);
			
			transaction.commit();
			
		} catch (Exception e) {
			throw e;
			
		} finally {
			session.close();
			
		}		
	}

	public void delete(Object object) throws Exception {
		Session session = getSessionFactory().openSession();
		
		Transaction transaction = session.beginTransaction();

		try {
			session.delete(object);
			
			transaction.commit();
			
		} catch (Exception e) {
			throw e;
			
		} finally {
			session.close();
			
		}
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

}
