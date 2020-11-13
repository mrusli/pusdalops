package mil.pusdalops.persistence.laporanrutin.dao.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import mil.pusdalops.domain.kejadian.Kejadian;
import mil.pusdalops.domain.kejadian.KejadianJenis;
import mil.pusdalops.domain.kotamaops.Kotamaops;
import mil.pusdalops.persistence.laporanrutin.dao.LaporanRutinDao;

public class LaporanRutinHibernate implements LaporanRutinDao {
	
	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Kejadian> findAllKejadian(Date twAwal, Date twAkhir) {
		Session session = getSessionFactory().openSession();
		
		Criteria criteria = session.createCriteria(Kejadian.class);
		criteria.add(Restrictions.ge("twKejadianDateTime", twAwal));
		criteria.add(Restrictions.le("twKejadianDateTime", twAkhir));
		
		try {

			return criteria.list();
			
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Kejadian> findAllKejadianByKotamaops(Kotamaops kotamaops, Date twAwal, Date twAkhir) {
		Session session = getSessionFactory().openSession();
		
		Criteria criteria = session.createCriteria(Kejadian.class);
		criteria.add(Restrictions.eq("kotamaops", kotamaops));
		criteria.add(Restrictions.ge("twKejadianDateTime", twAwal));
		criteria.add(Restrictions.le("twKejadianDateTime", twAkhir));
		
		try {

			return criteria.list();
			
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Kejadian> findAllKejadianByKejadianJenis(KejadianJenis kejadianJenis, Date twAwal, Date twAkhir) {
		Session session = getSessionFactory().openSession();
		
		Criteria criteria = session.createCriteria(Kejadian.class);
		criteria.add(Restrictions.eq("jenisKejadian", kejadianJenis));
		criteria.add(Restrictions.ge("twKejadianDateTime", twAwal));
		criteria.add(Restrictions.le("twKejadianDateTime", twAkhir));
		
		try {

			return criteria.list();
			
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Kejadian> findAllKejadianByKotamaopsByKejadianJenis(Kotamaops kotamaops, KejadianJenis kejadianJenis, Date twAwal, Date twAkhir) {
		Session session = getSessionFactory().openSession();
		
		Criteria criteria = session.createCriteria(Kejadian.class);
		criteria.add(Restrictions.eq("kotamaops", kotamaops));
		criteria.add(Restrictions.eq("jenisKejadian", kejadianJenis));
		criteria.add(Restrictions.ge("twKejadianDateTime", twAwal));
		criteria.add(Restrictions.le("twKejadianDateTime", twAkhir));
		
		try {

			return criteria.list();
			
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
	}

	@Override
	public Kejadian findKejadianKotamaopsByProxy(Long id) throws Exception {
		Session session = getSessionFactory().openSession();
		
		Criteria criteria = session.createCriteria(Kejadian.class);
		Kejadian kejadian = (Kejadian) criteria.add(Restrictions.idEq(id)).uniqueResult();
		
		try {
			Hibernate.initialize(kejadian.getKotamaops());
			
			return kejadian;
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
	}

	@Override
	public Kejadian findKejadianPropinsiByProxy(Long id) throws Exception {
		Session session = getSessionFactory().openSession();
		
		Criteria criteria = session.createCriteria(Kejadian.class);
		Kejadian kejadian = (Kejadian) criteria.add(Restrictions.idEq(id)).uniqueResult();
		
		try {
			Hibernate.initialize(kejadian.getPropinsi());
			
			return kejadian;
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
	}

	@Override
	public Kejadian findKejadianKabupatenKotByProxy(Long id) throws Exception {
		Session session = getSessionFactory().openSession();
		
		Criteria criteria = session.createCriteria(Kejadian.class);
		Kejadian kejadian = (Kejadian) criteria.add(Restrictions.idEq(id)).uniqueResult();
		
		try {
			Hibernate.initialize(kejadian.getKabupatenKotamadya());
			
			return kejadian;
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
	}
	
}
