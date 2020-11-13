package mil.pusdalops.persistence.pejabat.dao.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import mil.pusdalops.domain.kotamaops.Kotamaops;
import mil.pusdalops.domain.pejabat.Pejabat;
import mil.pusdalops.persistence.common.dao.hibernate.DaoHibernate;
import mil.pusdalops.persistence.pejabat.dao.PejabatDao;

public class PejabatHibernate extends DaoHibernate implements PejabatDao {

	@Override
	public Pejabat findPejabatById(long id) throws Exception {

		return (Pejabat) super.findById(Pejabat.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Pejabat> findAllPejabat() throws Exception {

		return super.findAll(Pejabat.class);
	}

	@Override
	public Long save(Pejabat pejabat) throws Exception {

		return super.save(pejabat);
	}

	@Override
	public void update(Pejabat pejabat) throws Exception {

		super.update(pejabat);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Pejabat> findAllPejabatByKotamaops(boolean asc, Kotamaops kotamaops) throws Exception {
		Session session = getSessionFactory().openSession();

		Criteria criteria = session.createCriteria(Pejabat.class);
		criteria.add(Restrictions.eq("kotamaops", kotamaops));
		criteria.addOrder(asc ? Order.asc("nama") : Order.desc("nama"));
		
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
	public List<Pejabat> findAllPejabat(boolean asc) throws Exception {
		Session session = getSessionFactory().openSession();

		Criteria criteria = session.createCriteria(Pejabat.class);
		criteria.addOrder(asc ? Order.asc("nama") : Order.desc("nama"));
		
		try {
			
			return criteria.list();
			
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
	}

	@Override
	public Pejabat findPejabatKotamaopsByProxy(long id) throws Exception {
		Session session = getSessionFactory().openSession();

		Criteria criteria = session.createCriteria(Pejabat.class);
		Pejabat pejabat = 
				(Pejabat) criteria.add(Restrictions.idEq(id)).uniqueResult();
		
		try {
			Hibernate.initialize(pejabat.getKotamaops());
			
			return pejabat;
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
	}
}
