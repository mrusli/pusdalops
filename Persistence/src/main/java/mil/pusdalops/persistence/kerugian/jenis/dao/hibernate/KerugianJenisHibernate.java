package mil.pusdalops.persistence.kerugian.jenis.dao.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

import mil.pusdalops.domain.kerugian.KerugianJenis;
import mil.pusdalops.persistence.common.dao.hibernate.DaoHibernate;
import mil.pusdalops.persistence.kerugian.jenis.dao.KerugianJenisDao;

public class KerugianJenisHibernate extends DaoHibernate implements KerugianJenisDao {

	@Override
	public KerugianJenis findKerugianJenisById(long id) throws Exception {

		return (KerugianJenis) super.findById(KerugianJenis.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<KerugianJenis> findAllKerugianJenis(boolean asc) throws Exception {
		Session session = getSessionFactory().openSession();
		
		Criteria criteria = session.createCriteria(KerugianJenis.class);
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
	public Long save(KerugianJenis kerugianJenis) throws Exception {
		
		return super.save(kerugianJenis);
	}

	@Override
	public void update(KerugianJenis kerugianJenis) throws Exception {

		super.update(kerugianJenis);
	}

}
