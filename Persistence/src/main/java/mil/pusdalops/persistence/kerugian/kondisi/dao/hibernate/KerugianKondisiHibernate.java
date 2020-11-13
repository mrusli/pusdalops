package mil.pusdalops.persistence.kerugian.kondisi.dao.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

import mil.pusdalops.domain.kerugian.KerugianKondisi;
import mil.pusdalops.persistence.common.dao.hibernate.DaoHibernate;
import mil.pusdalops.persistence.kerugian.kondisi.dao.KerugianKondisiDao;

public class KerugianKondisiHibernate extends DaoHibernate implements KerugianKondisiDao {

	@Override
	public KerugianKondisi findKerugianKondisiById(long id) throws Exception {
		
		return (KerugianKondisi) super.findById(KerugianKondisi.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<KerugianKondisi> findAllKerugianKondisi(boolean asc) throws Exception {
		Session session = getSessionFactory().openSession();
		
		Criteria criteria =session.createCriteria(KerugianKondisi.class);
		criteria.addOrder(asc ? Order.asc("namaKondisi") : Order.desc("namaKondisi"));
		
		try {

			return criteria.list();
			
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
		
	}

	@Override
	public Long save(KerugianKondisi kerugianKondisi) throws Exception {

		return super.save(kerugianKondisi);
	}

	@Override
	public void update(KerugianKondisi kerugianKondisi) throws Exception {

		super.update(kerugianKondisi);
	}


}
