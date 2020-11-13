package mil.pusdalops.persistence.propinsi.dao.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import mil.pusdalops.domain.wilayah.Propinsi;
import mil.pusdalops.persistence.common.dao.hibernate.DaoHibernate;
import mil.pusdalops.persistence.propinsi.dao.PropinsiDao;

public class PropinsiHibernate extends DaoHibernate implements PropinsiDao {

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

	@Override
	public Propinsi findPropinsiById(long id) throws Exception {

		return (Propinsi) super.findById(Propinsi.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Propinsi> findAllPropinsi() throws Exception {

		return super.findAll(Propinsi.class);
	}

	@Override
	public Long save(Propinsi propinsi) throws Exception {
		
		return super.save(propinsi);
	}

	@Override
	public void update(Propinsi propinsi) throws Exception {

		super.update(propinsi);
	}

	@Override
	public Propinsi findKabupatenKotamadyaByProxy(long id) throws Exception {
		Session session = getSessionFactory().openSession();
		
		Criteria criteria = session.createCriteria(Propinsi.class);
		Propinsi propinsi = (Propinsi) criteria.add(Restrictions.idEq(id)).uniqueResult();
		
		try {
			Hibernate.initialize(propinsi.getKabupatenkotamadyas());
		
			return propinsi;
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
		
	}

}
