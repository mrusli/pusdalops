package mil.pusdalops.persistence.kejadian.jenis.dao.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

import mil.pusdalops.domain.kejadian.KejadianJenis;
import mil.pusdalops.persistence.common.dao.hibernate.DaoHibernate;
import mil.pusdalops.persistence.kejadian.jenis.dao.KejadianJenisDao;

public class KejadianJenisHibernate extends DaoHibernate implements KejadianJenisDao {

	@Override
	public KejadianJenis findKejadianJenisById(long id) throws Exception {

		return (KejadianJenis) super.findById(KejadianJenis.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<KejadianJenis> findAllKejadianJenis(boolean asc) throws Exception {
		Session session = getSessionFactory().openSession();
		
		Criteria criteria = session.createCriteria(KejadianJenis.class);
		criteria.addOrder(asc ? Order.asc("namaJenis") : Order.desc("namaJenis"));
		
		try {
			
			return criteria.list();
			
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
	}

	@Override
	public Long save(KejadianJenis kejadianJenis) throws Exception {
	
		return super.save(kejadianJenis);
	}

	@Override
	public void update(KejadianJenis kejadianJenis) throws Exception {

		super.update(kejadianJenis);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<KejadianJenis> findAllKejadianJenisOrderBy(boolean ascending) throws Exception {
		Session session = getSessionFactory().openSession();
		
		Criteria criteria = session.createCriteria(KejadianJenis.class);
		criteria.addOrder(ascending ? Order.asc("namaJenis") : Order.desc("namaJenis"));
		
		try {
			return criteria.list();
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
	}

}
