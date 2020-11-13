package mil.pusdalops.persistence.gelaroperasi.dao.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import mil.pusdalops.domain.gelarops.GelarOperasi;
import mil.pusdalops.persistence.common.dao.hibernate.DaoHibernate;
import mil.pusdalops.persistence.gelaroperasi.dao.GelarOperasiDao;

public class GelarOperasiHibernate extends DaoHibernate implements GelarOperasiDao {

	@Override
	public GelarOperasi findGelarOperasiById(long id) throws Exception {

		return (GelarOperasi) super.findById(GelarOperasi.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<GelarOperasi> findAllGelarOperasi() throws Exception {

		return super.findAll(GelarOperasi.class);
	}

	@Override
	public Long save(GelarOperasi gelarOperasi) throws Exception {

		return super.save(gelarOperasi);
	}

	@Override
	public void update(GelarOperasi gelarOperasi) throws Exception {

		super.update(gelarOperasi);
	}

	@Override
	public GelarOperasi findGelarOperasiKotamaopsByProxy(long id) throws Exception {
		Session session = getSessionFactory().openSession();
		
		Criteria criteria = session.createCriteria(GelarOperasi.class);
		GelarOperasi gelarOperasi = 
				(GelarOperasi) criteria.add(Restrictions.idEq(id)).uniqueResult();
		
		try {
			Hibernate.initialize(gelarOperasi.getKotamaops());
			
			return gelarOperasi;
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
	}

}
