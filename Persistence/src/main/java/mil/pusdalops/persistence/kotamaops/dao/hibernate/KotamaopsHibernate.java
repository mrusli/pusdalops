package mil.pusdalops.persistence.kotamaops.dao.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import mil.pusdalops.domain.kotamaops.Kotamaops;
import mil.pusdalops.domain.kotamaops.KotamaopsType;
import mil.pusdalops.persistence.common.dao.hibernate.DaoHibernate;
import mil.pusdalops.persistence.kotamaops.dao.KotamaopsDao;

public class KotamaopsHibernate extends DaoHibernate implements KotamaopsDao {

	@Override
	public Kotamaops findKotamaopsById(long id) throws Exception {

		return (Kotamaops) super.findById(Kotamaops.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Kotamaops> findAllKotamaops() throws Exception {

		return super.findAll(Kotamaops.class);
	}

	@Override
	public Long save(Kotamaops kotamaops) throws Exception {

		return super.save(kotamaops);
	}

	@Override
	public void update(Kotamaops kotamaops) throws Exception {

		super.update(kotamaops);
	}

	@Override
	public Kotamaops findKotamaopsPropinsiByProxy(long id) throws Exception {
		Session session = getSessionFactory().openSession();
		
		Criteria criteria = session.createCriteria(Kotamaops.class);
		Kotamaops kotamaops = (Kotamaops) criteria.add(Restrictions.idEq(id)).uniqueResult();
		
		try {
			Hibernate.initialize(kotamaops.getPropinsis());
			
			return kotamaops;
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
	}

	@Override
	public Kotamaops findKotamaopsKotamaopsByProxy(long id) throws Exception {
		Session session = getSessionFactory().openSession();
		
		Criteria criteria = session.createCriteria(Kotamaops.class);
		Kotamaops kotamaops = (Kotamaops) criteria.add(Restrictions.idEq(id)).uniqueResult();
		
		try {
			Hibernate.initialize(kotamaops.getKotamaops());
			
			return kotamaops;
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
	}

	@Override
	public void delete(Kotamaops kotamaops) throws Exception {

		super.delete(kotamaops);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Kotamaops> findKotamaopsByKotamaopsTypeMatra(KotamaopsType kotamaopsTypeMatra) throws Exception {
		Session session = getSessionFactory().openSession();
		
		Criteria criteria = session.createCriteria(Kotamaops.class);
		criteria.add(Restrictions.eq("kotamaopsType", kotamaopsTypeMatra));
		
		try {

			return criteria.list();
			
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
	}
}
