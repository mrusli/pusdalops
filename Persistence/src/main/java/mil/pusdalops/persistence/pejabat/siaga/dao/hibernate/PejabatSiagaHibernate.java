package mil.pusdalops.persistence.pejabat.siaga.dao.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import mil.pusdalops.domain.siaga.PejabatSiaga;
import mil.pusdalops.persistence.common.dao.hibernate.DaoHibernate;
import mil.pusdalops.persistence.pejabat.siaga.dao.PejabatSiagaDao;

public class PejabatSiagaHibernate extends DaoHibernate implements PejabatSiagaDao {

	@Override
	public PejabatSiaga findPejabatSiagaById(long id) throws Exception {
		
		return (PejabatSiaga) super.findById(PejabatSiaga.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PejabatSiaga> findAllPejabatSiaga() throws Exception {

		return super.findAll(PejabatSiaga.class);
	}

	@Override
	public Long save(PejabatSiaga pejabatSiaga) throws Exception {

		return super.save(pejabatSiaga);
	}

	@Override
	public void update(PejabatSiaga pejabatSiaga) throws Exception {

		super.update(pejabatSiaga);
	}

	@Override
	public PejabatSiaga findPejabatSiagaPejabatByProxy(long id) throws Exception {
		Session session = getSessionFactory().openSession();
		
		Criteria criteria = session.createCriteria(PejabatSiaga.class);
		PejabatSiaga pejabatSiaga = 
				(PejabatSiaga) criteria.add(Restrictions.idEq(id)).uniqueResult();
		
		try {
			Hibernate.initialize(pejabatSiaga.getPejabat());
			
			return pejabatSiaga;
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}		
	}

}
