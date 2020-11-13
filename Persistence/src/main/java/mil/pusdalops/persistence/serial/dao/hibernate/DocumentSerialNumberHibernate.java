package mil.pusdalops.persistence.serial.dao.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import mil.pusdalops.domain.serial.DocumentSerialNumber;
import mil.pusdalops.persistence.common.dao.hibernate.DaoHibernate;
import mil.pusdalops.persistence.serial.dao.DocumentSerialNumberDao;

public class DocumentSerialNumberHibernate extends DaoHibernate implements DocumentSerialNumberDao {

	public DocumentSerialNumber findDocumentSerialNumberById(long id) throws Exception {

		return (DocumentSerialNumber) super.findById(DocumentSerialNumber.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<DocumentSerialNumber> findAllDocumentSerialNumber() throws Exception {

		return new ArrayList<DocumentSerialNumber>(super.findAll(DocumentSerialNumber.class));
	}

	public long save(DocumentSerialNumber documentSerialNumber) throws Exception {

		return super.save(documentSerialNumber);
	}

	public void update(DocumentSerialNumber documentSerialNumber) throws Exception {
		
		super.update(documentSerialNumber);
	}

	public DocumentSerialNumber findLastDocumentSerialNumberByDocumentType(String documentCode) throws Exception {
		Session session = super.getSessionFactory().openSession();
		
		Criteria criteria = session.createCriteria(DocumentSerialNumber.class);
		criteria.add(Restrictions.eq("documentCode", documentCode));
		criteria.addOrder(Order.desc("id"));

		try {
			
			if (criteria.list().isEmpty()) {
				return null;
			} else {
				
				return (DocumentSerialNumber) criteria.list().get(0);
				
			}
			
		} catch (Exception e) {
			throw e;
			
		} finally {
			session.close();
			
		}

	}

	@Override
	public DocumentSerialNumber findLastDocumentSerialNumber() throws Exception {
		Session session = super.getSessionFactory().openSession();
		
		Criteria criteria = session.createCriteria(DocumentSerialNumber.class);
		criteria.add(Restrictions.isNull("documentCode"));
		criteria.addOrder(Order.desc("id"));

		try {
			
			if (criteria.list().isEmpty()) {
				return null;
			} else {
				
				return (DocumentSerialNumber) criteria.list().get(0);
				
			}
			
		} catch (Exception e) {
			throw e;
			
		} finally {
			session.close();
			
		}
	}

}
