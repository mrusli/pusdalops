package mil.pusdalops.persistence.kerugian.dao.hibernate;

import java.util.List;

import mil.pusdalops.domain.kerugian.Kerugian;
import mil.pusdalops.persistence.common.dao.hibernate.DaoHibernate;
import mil.pusdalops.persistence.kerugian.dao.KerugianDao;

public class KerugianHibernate extends DaoHibernate implements KerugianDao {

	@Override
	public Kerugian findKerugianById(long id) throws Exception {

		return (Kerugian) super.findById(Kerugian.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Kerugian> findAllKerugian() throws Exception {

		return super.findAll(Kerugian.class);
	}

	@Override
	public Long save(Kerugian kerugian) throws Exception {

		return super.save(kerugian);
	}

	@Override
	public void update(Kerugian kerugian) throws Exception {

		super.update(kerugian);
	}

}
