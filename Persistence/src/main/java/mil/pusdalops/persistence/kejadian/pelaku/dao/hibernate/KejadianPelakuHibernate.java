package mil.pusdalops.persistence.kejadian.pelaku.dao.hibernate;

import java.util.List;

import mil.pusdalops.domain.kejadian.KejadianPelaku;
import mil.pusdalops.persistence.common.dao.hibernate.DaoHibernate;
import mil.pusdalops.persistence.kejadian.pelaku.dao.KejadianPelakuDao;

public class KejadianPelakuHibernate extends DaoHibernate implements KejadianPelakuDao {

	@Override
	public KejadianPelaku findKejadianPelakuById(long id) throws Exception {

		return (KejadianPelaku) super.findById(KejadianPelaku.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<KejadianPelaku> findAllKejadianPelaku() throws Exception {

		return super.findAll(KejadianPelaku.class);
	}

	@Override
	public Long save(KejadianPelaku kejadianPelaku) throws Exception {
		
		return super.save(kejadianPelaku);
	}

	@Override
	public void update(KejadianPelaku kejadianPelaku) throws Exception {

		super.update(kejadianPelaku);
	}

}
