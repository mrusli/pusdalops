package mil.pusdalops.persistence.kecamatan.dao.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import mil.pusdalops.domain.wilayah.Kecamatan;
import mil.pusdalops.persistence.common.dao.hibernate.DaoHibernate;
import mil.pusdalops.persistence.kecamatan.dao.KecamatanDao;

public class KecamatanHibernate extends DaoHibernate implements KecamatanDao {

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
	public Kecamatan findKecamatanById(long id) throws Exception {
		
		return (Kecamatan) super.findById(Kecamatan.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Kecamatan> findAllKecamatan() throws Exception {

		return super.findAll(Kecamatan.class);
	}

	@Override
	public Long save(Kecamatan kecamatan) throws Exception {

		return super.save(kecamatan);
	}

	@Override
	public void update(Kecamatan kecamatan) throws Exception {

		super.update(kecamatan);
	}

	@Override
	public Kecamatan findKelurahanByProxy(long id) throws Exception {
		Session session = getSessionFactory().openSession();
		
		Criteria criteria = session.createCriteria(Kecamatan.class);
		Kecamatan kecamatan = (Kecamatan) criteria.add(Restrictions.idEq(id)).uniqueResult();
		
		try {
			Hibernate.initialize(kecamatan.getKelurahans());
			
			return kecamatan;
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
	}

}
