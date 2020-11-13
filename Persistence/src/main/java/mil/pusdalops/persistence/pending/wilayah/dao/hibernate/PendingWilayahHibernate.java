package mil.pusdalops.persistence.pending.wilayah.dao.hibernate;

import java.util.List;

import mil.pusdalops.domain.pending.PendingWilayah;
import mil.pusdalops.persistence.common.dao.hibernate.DaoHibernate;
import mil.pusdalops.persistence.pending.wilayah.dao.PendingWilayahDao;

public class PendingWilayahHibernate extends DaoHibernate implements PendingWilayahDao {

	@Override
	public PendingWilayah findPendingWilayahById(long id) throws Exception {

		return (PendingWilayah) super.findById(PendingWilayah.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PendingWilayah> findAllPendingWilayah() throws Exception {

		return super.findAll(PendingWilayah.class);
	}

	@Override
	public Long save(PendingWilayah pendingWilayah) throws Exception {

		return super.save(pendingWilayah);
	}

	@Override
	public void update(PendingWilayah pendingWilayah) throws Exception {

		super.update(pendingWilayah);
	}

}
