package mil.pusdalops.persistence.personel.dao.hibernate;

import java.util.List;

import mil.pusdalops.domain.personel.Personel;
import mil.pusdalops.persistence.common.dao.hibernate.DaoHibernate;
import mil.pusdalops.persistence.personel.dao.PersonelDao;

public class PersonelHibernate extends DaoHibernate implements PersonelDao {

	@Override
	public Personel findPersonelById(long id) throws Exception {

		return (Personel) super.findById(Personel.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Personel> findAllPersonel() throws Exception {

		return super.findAll(Personel.class);
	}

	@Override
	public Long save(Personel personel) throws Exception {

		return super.save(personel);
	}

	@Override
	public void update(Personel personel) throws Exception {

		super.update(personel);
	}

}
