package mil.pusdalops.persistence.laporanlain.dao.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import mil.pusdalops.domain.kotamaops.Kotamaops;
import mil.pusdalops.domain.laporanlain.LaporanLain;
import mil.pusdalops.persistence.common.dao.hibernate.DaoHibernate;
import mil.pusdalops.persistence.laporanlain.dao.LaporanLainDao;

public class LaporanLainHibernate extends DaoHibernate implements LaporanLainDao {

	@Override
	public LaporanLain findLaporanLainById(long id) throws Exception {
		
		return (LaporanLain) super.findById(LaporanLain.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LaporanLain> findAllLaporanLain() throws Exception {

		return super.findAll(LaporanLain.class);
	}

	@Override
	public Long save(LaporanLain laporanLain) throws Exception {

		return super.save(laporanLain);
	}

	@Override
	public void update(LaporanLain laporanLain) throws Exception {

		super.update(laporanLain);
	}

	@Override
	public LaporanLain findLaporanLainKotamaopsByProxy(long id) throws Exception {
		Session session = getSessionFactory().openSession();
		
		Criteria criteria = session.createCriteria(LaporanLain.class);
		LaporanLain laporanLain = (LaporanLain) criteria.add(Restrictions.idEq(id)).uniqueResult();
		
		try {
			Hibernate.initialize(laporanLain.getKotamaops());
			
			return laporanLain;
		} catch (Exception e) {
			throw e;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LaporanLain> findAllLaporanLainByKotamaops(Kotamaops kotamaops) throws Exception {
		Session session = getSessionFactory().openSession();
		
		Criteria criteria = session.createCriteria(LaporanLain.class);
		criteria.add(Restrictions.eq("kotamaops", kotamaops));
		
		try {
			
			return criteria.list();
			
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
	}

}
