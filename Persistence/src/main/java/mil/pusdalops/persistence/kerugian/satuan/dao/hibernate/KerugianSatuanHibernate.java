package mil.pusdalops.persistence.kerugian.satuan.dao.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

import mil.pusdalops.domain.kerugian.KerugianSatuan;
import mil.pusdalops.persistence.common.dao.hibernate.DaoHibernate;
import mil.pusdalops.persistence.kerugian.satuan.dao.KerugianSatuanDao;

public class KerugianSatuanHibernate extends DaoHibernate implements KerugianSatuanDao {

	@Override
	public KerugianSatuan findKerugianSatuanById(long id) throws Exception {
		
		return (KerugianSatuan) super.findById(KerugianSatuan.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<KerugianSatuan> findAllKerugianSatuan(boolean asc) throws Exception {
		Session session = getSessionFactory().openSession();
		
		Criteria criteria = session.createCriteria(KerugianSatuan.class);
		criteria.addOrder(asc ? Order.asc("satuan") : Order.desc("satuan"));
		
		try {
			
			return criteria.list();
			
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
	}

	@Override
	public Long save(KerugianSatuan kerugianSatuan) throws Exception {
		
		return super.save(kerugianSatuan);
	}

	@Override
	public void update(KerugianSatuan kerugianSatuan) throws Exception {

		super.update(kerugianSatuan);
	}

}
