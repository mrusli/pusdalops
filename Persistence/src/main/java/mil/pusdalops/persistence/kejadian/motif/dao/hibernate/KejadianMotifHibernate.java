package mil.pusdalops.persistence.kejadian.motif.dao.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

import mil.pusdalops.domain.kejadian.KejadianMotif;
import mil.pusdalops.persistence.common.dao.hibernate.DaoHibernate;
import mil.pusdalops.persistence.kejadian.motif.dao.KejadianMotifDao;

public class KejadianMotifHibernate extends DaoHibernate implements KejadianMotifDao {

	@Override
	public KejadianMotif findKejadianMotifById(long id) throws Exception {

		return (KejadianMotif) super.findById(KejadianMotif.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<KejadianMotif> findAllKejadianMotif() throws Exception {

		return super.findAll(KejadianMotif.class);
	}

	@Override
	public Long save(KejadianMotif kejadianMotif) throws Exception {

		return super.save(kejadianMotif);
	}

	@Override
	public void update(KejadianMotif kejadianMotif) throws Exception {

		super.update(kejadianMotif);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<KejadianMotif> findAllKejadianMotifOrderBy(boolean ascending) throws Exception {
		Session session = getSessionFactory().openSession();
		
		Criteria criteria = session.createCriteria(KejadianMotif.class);
		criteria.addOrder(ascending ? Order.asc("namaMotif") : Order.desc("namaMotif"));
		
		try {
			
			return criteria.list();
			
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
	}

}
