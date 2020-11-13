package mil.pusdalops.persistence.kelurahan.dao.hibernate;

import java.util.List;

import org.hibernate.SessionFactory;

import mil.pusdalops.domain.wilayah.Kelurahan;
import mil.pusdalops.persistence.common.dao.hibernate.DaoHibernate;
import mil.pusdalops.persistence.kelurahan.dao.KelurahanDao;

public class KelurahanHibernate extends DaoHibernate implements KelurahanDao {

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
	public Kelurahan findKelurahanById(long id) throws Exception {

		return (Kelurahan) super.findById(Kelurahan.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Kelurahan> findAllKelurahan() throws Exception {

		return super.findAll(Kelurahan.class);
	}

	@Override
	public Long save(Kelurahan kelurahan) throws Exception {

		return super.save(kelurahan);
	}

	@Override
	public void update(Kelurahan kelurahan) throws Exception {

		super.update(kelurahan);
	}

}
