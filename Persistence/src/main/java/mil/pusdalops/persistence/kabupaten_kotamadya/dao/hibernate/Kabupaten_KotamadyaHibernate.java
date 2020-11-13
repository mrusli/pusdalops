package mil.pusdalops.persistence.kabupaten_kotamadya.dao.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import mil.pusdalops.domain.wilayah.Kabupaten_Kotamadya;
import mil.pusdalops.persistence.common.dao.hibernate.DaoHibernate;
import mil.pusdalops.persistence.kabupaten_kotamadya.dao.Kabupaten_KotamadyaDao;

public class Kabupaten_KotamadyaHibernate extends DaoHibernate implements Kabupaten_KotamadyaDao {

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
	public Kabupaten_Kotamadya findKabupaten_KotamadyaById(long id) throws Exception {

		return (Kabupaten_Kotamadya) super.findById(Kabupaten_Kotamadya.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Kabupaten_Kotamadya> findAllKabupaten_Kotamadya() throws Exception {

		return super.findAll(Kabupaten_Kotamadya.class);
	}

	@Override
	public Long save(Kabupaten_Kotamadya kabupaten_kotamadya) throws Exception {

		return super.save(kabupaten_kotamadya);
	}

	@Override
	public void update(Kabupaten_Kotamadya kabupaten_kotamadya) throws Exception {

		super.update(kabupaten_kotamadya);
	}

	@Override
	public Kabupaten_Kotamadya findKecamatanByProxy(long id) throws Exception {
		Session session = getSessionFactory().openSession();
		
		Criteria criteria = session.createCriteria(Kabupaten_Kotamadya.class);
		Kabupaten_Kotamadya kabupaten = 
				(Kabupaten_Kotamadya) criteria.add(Restrictions.idEq(id)).uniqueResult();
		
		try {
			Hibernate.initialize(kabupaten.getKecamatans());
			
			return kabupaten;
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
		
	}

}
