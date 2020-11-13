package mil.pusdalops.persistence.kejadian.dao.hibernate;

import java.util.List;

import org.apache.lucene.search.Query;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.query.dsl.QueryBuilder;

import mil.pusdalops.domain.kejadian.Kejadian;
import mil.pusdalops.domain.kotamaops.Kotamaops;
import mil.pusdalops.persistence.common.dao.hibernate.DaoHibernate;
import mil.pusdalops.persistence.kejadian.dao.KejadianDao;

public class KejadianHibernate extends DaoHibernate implements KejadianDao {

	@Override
	public Kejadian findKejadianById(long id) throws Exception {

		return (Kejadian) super.findById(Kejadian.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Kejadian> findAllKejadian(boolean desc) throws Exception {
		Session session = getSessionFactory().openSession();
		
		Criteria criteria = session.createCriteria(Kejadian.class);
		criteria.addOrder(desc ? 
				Order.desc("twPembuatanDateTime") : 
				Order.asc("twPembuatanDateTime"));
		
		try {
			
			return criteria.list();
			
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
	}

	@Override
	public Long save(Kejadian kejadian) throws Exception {
		
		return super.save(kejadian);
	}

	@Override
	public void update(Kejadian kejadian) throws Exception {

		super.update(kejadian);
	}

	@Override
	public Kejadian findKejadianKotamaopsByProxy(long id) throws Exception {
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
	public Kejadian findKejadianKerugiansByProxy(long id) throws Exception {
		Session session = getSessionFactory().openSession();
		
		Criteria criteria = session.createCriteria(Kejadian.class);
		Kejadian kejadian = (Kejadian) criteria.add(Restrictions.idEq(id)).uniqueResult();
		
		try {
			Hibernate.initialize(kejadian.getKerugians());
			
			return kejadian;
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Kejadian> findAllKejadianByKotamaops(Kotamaops kotamaops, boolean desc) throws Exception {
		Session session = getSessionFactory().openSession();
		
		Criteria criteria = session.createCriteria(Kejadian.class);
		criteria.add(Restrictions.eq("kotamaops", kotamaops));
		criteria.addOrder(desc ? 
				Order.desc("twPembuatanDateTime") :
				Order.asc("twPembuatanDateTime"));
		
		try {
			return criteria.list();
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
		
	}

	@Override
	public Kejadian findKejadianPropinsiByProxy(long id) throws Exception {
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
	public Kejadian findKejadianKabupatenByProxy(long id) throws Exception {
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

	@Override
	public Kejadian findKejadianKecamatanByProxy(long id) throws Exception {
		Session session = getSessionFactory().openSession();
		
		Criteria criteria = session.createCriteria(Kejadian.class);
		Kejadian kejadian = (Kejadian) criteria.add(Restrictions.idEq(id)).uniqueResult();
		
		try {
			Hibernate.initialize(kejadian.getKecamatan());
			
			return kejadian;
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}

	}

	@Override
	public Kejadian findKejadianKelurahanByProxy(long id) throws Exception {
		Session session = getSessionFactory().openSession();
		
		Criteria criteria = session.createCriteria(Kejadian.class);
		Kejadian kejadian = (Kejadian) criteria.add(Restrictions.idEq(id)).uniqueResult();
		
		try {
			Hibernate.initialize(kejadian.getKelurahan());
			
			return kejadian;
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Kejadian> findKotamaopsNonSynchronizedKejadian(Kotamaops kotamaops) throws Exception {
		Session session = getSessionFactory().openSession();
		
		Criteria criteria = session.createCriteria(Kejadian.class);
		criteria.add(Restrictions.eq("kotamaops", kotamaops));
		criteria.add(Restrictions.isNull("synchAt"));
		criteria.addOrder(Order.desc("twPembuatanDateTime"));
		
		try {
			return criteria.list();
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
	}

	@Override
	public void createIndexer() throws Exception {
		Session session = getSessionFactory().openSession();
		
		FullTextSession fullTextSession = Search.getFullTextSession(session);
		
		try {
			
			fullTextSession.createIndexer().startAndWait();
			
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
		
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Kejadian> searchKronologis(String searchString) throws Exception {
		Transaction tx = null;		
		Session session = getSessionFactory().openSession();
		
		FullTextSession fullTextSession = Search.getFullTextSession(session);
		
		try {
			tx = fullTextSession.beginTransaction();
			
			QueryBuilder qb = fullTextSession.getSearchFactory().buildQueryBuilder().forEntity(Kejadian.class).get();
			
			Query qr = qb.keyword()
					.onField("kronologis")
					.matching(searchString)
					.createQuery();
			
			FullTextQuery fullTextQuery = fullTextSession.createFullTextQuery(qr, Kejadian.class);
			
			tx.commit();

			return fullTextQuery.list();
			
		} catch (Exception e) {
			if (tx != null) tx.rollback();
			throw e;
		} finally {
			session.close();
		}
	}

}
