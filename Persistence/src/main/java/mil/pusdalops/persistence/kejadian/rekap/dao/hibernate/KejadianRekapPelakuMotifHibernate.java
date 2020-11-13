package mil.pusdalops.persistence.kejadian.rekap.dao.hibernate;

import java.math.BigInteger;
import java.time.LocalDateTime;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import mil.pusdalops.domain.kejadian.KejadianMotif;
import mil.pusdalops.domain.kejadian.KejadianPelaku;
import mil.pusdalops.domain.kotamaops.Kotamaops;
import mil.pusdalops.domain.wilayah.Kabupaten_Kotamadya;
import mil.pusdalops.domain.wilayah.Kecamatan;
import mil.pusdalops.domain.wilayah.Kelurahan;
import mil.pusdalops.domain.wilayah.Propinsi;
import mil.pusdalops.persistence.kejadian.rekap.dao.KejadianRekapPelakuMotifDao;

public class KejadianRekapPelakuMotifHibernate implements KejadianRekapPelakuMotifDao {

	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public BigInteger countKejadian(LocalDateTime twAwal, LocalDateTime twAkhir) {
		Session session = getSessionFactory().openSession();
		
		try {			
			SQLQuery sqlQuery = session.createSQLQuery("SELECT count(kejadian.id) FROM e010_k2_pusdalops.kejadian kejadian"
					+ " where "
					+ " kejadian.tw_kejadian_datetime<:twAkhirKejadian and "
					+ " kejadian.tw_kejadian_datetime>:twAwalKejadian");
			sqlQuery.setParameter("twAkhirKejadian", twAkhir.toString());
			sqlQuery.setParameter("twAwalKejadian", twAwal.toString());
			
			return sqlQuery.list().isEmpty() ? BigInteger.ZERO : (BigInteger) sqlQuery.list().get(0);
			
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
	}

	@Override
	public BigInteger countKejadian(Kotamaops kotamaops, LocalDateTime twAwal, LocalDateTime twAkhir) {
		Session session = getSessionFactory().openSession();
		
		try {			
			SQLQuery sqlQuery = session.createSQLQuery("SELECT count(kejadian.id) FROM e010_k2_pusdalops.kejadian kejadian"
					+ " where "
					+ " kejadian.kotamaops_id_fk=:kotamaops and "
					+ " kejadian.tw_kejadian_datetime<:twAkhirKejadian and "
					+ " kejadian.tw_kejadian_datetime>:twAwalKejadian");
			sqlQuery.setParameter("kotamaops", kotamaops.getId());
			sqlQuery.setParameter("twAkhirKejadian", twAkhir.toString());
			sqlQuery.setParameter("twAwalKejadian", twAwal.toString());
			
			return sqlQuery.list().isEmpty() ? BigInteger.ZERO : (BigInteger) sqlQuery.list().get(0);
			
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
	}

	@Override
	public BigInteger countKejadian(Kotamaops kotamaops, Propinsi propinsi, LocalDateTime twAwal, LocalDateTime twAkhir) {
		Session session = getSessionFactory().openSession();
		
		try {			
			SQLQuery sqlQuery = session.createSQLQuery("SELECT count(kejadian.id) FROM e010_k2_pusdalops.kejadian kejadian"
					+ " where "
					+ " kejadian.kotamaops_id_fk=:kotamaops and "
					+ " kejadian.propinsi_id_fk=:propinsi and "
					+ " kejadian.tw_kejadian_datetime<:twAkhirKejadian and "
					+ " kejadian.tw_kejadian_datetime>:twAwalKejadian");
			sqlQuery.setParameter("kotamaops", kotamaops.getId());
			sqlQuery.setParameter("propinsi", propinsi.getId());
			sqlQuery.setParameter("twAkhirKejadian", twAkhir.toString());
			sqlQuery.setParameter("twAwalKejadian", twAwal.toString());
			
			return sqlQuery.list().isEmpty() ? BigInteger.ZERO : (BigInteger) sqlQuery.list().get(0);
			
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
	}

	@Override
	public BigInteger countKejadian(Kotamaops kotamaops, Propinsi propinsi, Kabupaten_Kotamadya kabupatenKot,
			LocalDateTime twAwal, LocalDateTime twAkhir) {
		Session session = getSessionFactory().openSession();
		
		try {			
			SQLQuery sqlQuery = session.createSQLQuery("SELECT count(kejadian.id) FROM e010_k2_pusdalops.kejadian kejadian"
					+ " where "
					+ " kejadian.kotamaops_id_fk=:kotamaops and "
					+ " kejadian.propinsi_id_fk=:propinsi and "
					+ " kejadian.kabupaten_id_fk=:kabupatenKot and "
					+ " kejadian.tw_kejadian_datetime<:twAkhirKejadian and "
					+ " kejadian.tw_kejadian_datetime>:twAwalKejadian");
			sqlQuery.setParameter("kotamaops", kotamaops.getId());
			sqlQuery.setParameter("propinsi", propinsi.getId());
			sqlQuery.setParameter("kabupatenKot", kabupatenKot.getId());
			sqlQuery.setParameter("twAkhirKejadian", twAkhir.toString());
			sqlQuery.setParameter("twAwalKejadian", twAwal.toString());
			
			return sqlQuery.list().isEmpty() ? BigInteger.ZERO : (BigInteger) sqlQuery.list().get(0);
			
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
	}

	@Override
	public BigInteger countKejadian(Kotamaops kotamaops, Propinsi propinsi, Kabupaten_Kotamadya kabupatenKot,
			Kecamatan kecamatan, LocalDateTime twAwal, LocalDateTime twAkhir) {
		Session session = getSessionFactory().openSession();
		
		try {			
			SQLQuery sqlQuery = session.createSQLQuery("SELECT count(kejadian.id) FROM e010_k2_pusdalops.kejadian kejadian"
					+ " where "
					+ " kejadian.kotamaops_id_fk=:kotamaops and "
					+ " kejadian.propinsi_id_fk=:propinsi and "
					+ " kejadian.kabupaten_id_fk=:kabupatenKot and "
					+ " kejadian.kecamatan_id_fk=:kecamatan and "
					+ " kejadian.tw_kejadian_datetime<:twAkhirKejadian and "
					+ " kejadian.tw_kejadian_datetime>:twAwalKejadian");
			sqlQuery.setParameter("kotamaops", kotamaops.getId());
			sqlQuery.setParameter("propinsi", propinsi.getId());
			sqlQuery.setParameter("kabupatenKot", kabupatenKot.getId());
			sqlQuery.setParameter("kecamatan", kecamatan.getId());
			sqlQuery.setParameter("twAkhirKejadian", twAkhir.toString());
			sqlQuery.setParameter("twAwalKejadian", twAwal.toString());
			
			return sqlQuery.list().isEmpty() ? BigInteger.ZERO : (BigInteger) sqlQuery.list().get(0);
			
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
	}

	@Override
	public BigInteger countKejadian(Kotamaops kotamaops, Propinsi propinsi, Kabupaten_Kotamadya kabupatenKot,
			Kecamatan kecamatan, Kelurahan kelurahan, LocalDateTime twAwal, LocalDateTime twAkhir) {
		Session session = getSessionFactory().openSession();
		
		try {			
			SQLQuery sqlQuery = session.createSQLQuery("SELECT count(kejadian.id) FROM e010_k2_pusdalops.kejadian kejadian"
					+ " where "
					+ " kejadian.kotamaops_id_fk=:kotamaops and "
					+ " kejadian.propinsi_id_fk=:propinsi and "
					+ " kejadian.kabupaten_id_fk=:kabupatenKot and "
					+ " kejadian.kecamatan_id_fk=:kecamatan and "
					+ " kejadian.kelurahan_id_fk=:kelurahan and "
					+ " kejadian.tw_kejadian_datetime<:twAkhirKejadian and "
					+ " kejadian.tw_kejadian_datetime>:twAwalKejadian");
			sqlQuery.setParameter("kotamaops", kotamaops.getId());
			sqlQuery.setParameter("propinsi", propinsi.getId());
			sqlQuery.setParameter("kabupatenKot", kabupatenKot.getId());
			sqlQuery.setParameter("kecamatan", kecamatan.getId());
			sqlQuery.setParameter("kelurahan", kelurahan.getId());
			sqlQuery.setParameter("twAkhirKejadian", twAkhir.toString());
			sqlQuery.setParameter("twAwalKejadian", twAwal.toString());
			
			return sqlQuery.list().isEmpty() ? BigInteger.ZERO : (BigInteger) sqlQuery.list().get(0);
			
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
	}

	@Override
	public BigInteger countPelakuKejadian(KejadianPelaku kejadianPelaku, LocalDateTime twAwal, LocalDateTime twAkhir) {
		Session session = getSessionFactory().openSession();
		
		try {			
			SQLQuery sqlQuery = session.createSQLQuery("SELECT count(kejadian.motif_kejadian_id_fk) FROM e010_k2_pusdalops.kejadian kejadian"
					+ " where "
					+ " kejadian.pelaku_kejadian_id_fk=:kejadianPelaku and "
					+ " kejadian.tw_kejadian_datetime<:twAkhirKejadian and "
					+ " kejadian.tw_kejadian_datetime>:twAwalKejadian"
					);
			sqlQuery.setParameter("kejadianPelaku", kejadianPelaku.getId());
			sqlQuery.setParameter("twAkhirKejadian", twAkhir.toString());
			sqlQuery.setParameter("twAwalKejadian", twAwal.toString());
			
			return sqlQuery.list().isEmpty() ? BigInteger.ZERO : (BigInteger) sqlQuery.list().get(0);
			
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
	}

	@Override
	public BigInteger countPelakuKejadian(KejadianPelaku kejadianPelaku, Kotamaops kotamaops,
			LocalDateTime twAwal, LocalDateTime twAkhir) {
		Session session = getSessionFactory().openSession();
		
		try {			
			SQLQuery sqlQuery = session.createSQLQuery("SELECT count(kejadian.motif_kejadian_id_fk) FROM e010_k2_pusdalops.kejadian kejadian"
					+ " where "
					+ " kejadian.pelaku_kejadian_id_fk=:kejadianPelaku and "
					+ " kejadian.kotamaops_id_fk=:kotamaops and "
					+ " kejadian.tw_kejadian_datetime<:twAkhirKejadian and "
					+ " kejadian.tw_kejadian_datetime>:twAwalKejadian"
					);
			sqlQuery.setParameter("kejadianPelaku", kejadianPelaku.getId());
			sqlQuery.setParameter("kotamaops", kotamaops.getId());
			sqlQuery.setParameter("twAkhirKejadian", twAkhir.toString());
			sqlQuery.setParameter("twAwalKejadian", twAwal.toString());
			
			return sqlQuery.list().isEmpty() ? BigInteger.ZERO : (BigInteger) sqlQuery.list().get(0);
			
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
	}
	
	@Override
	public BigInteger countPelakuKejadian(KejadianPelaku kejadianPelaku, Kotamaops kotamaops, Propinsi propinsi,
			LocalDateTime twAwal, LocalDateTime twAkhir) {
		Session session = getSessionFactory().openSession();
		
		try {			
			SQLQuery sqlQuery = session.createSQLQuery("SELECT count(kejadian.motif_kejadian_id_fk) FROM e010_k2_pusdalops.kejadian kejadian"
					+ " where "
					+ " kejadian.pelaku_kejadian_id_fk=:kejadianPelaku and "
					+ " kejadian.kotamaops_id_fk=:kotamaops and "
					+ " kejadian.propinsi_id_fk=:propinsi and "
					+ " kejadian.tw_kejadian_datetime<:twAkhirKejadian and "
					+ " kejadian.tw_kejadian_datetime>:twAwalKejadian"
					);
			sqlQuery.setParameter("kejadianPelaku", kejadianPelaku.getId());
			sqlQuery.setParameter("kotamaops", kotamaops.getId());
			sqlQuery.setParameter("propinsi", propinsi.getId());
			sqlQuery.setParameter("twAkhirKejadian", twAkhir.toString());
			sqlQuery.setParameter("twAwalKejadian", twAwal.toString());
			
			return sqlQuery.list().isEmpty() ? BigInteger.ZERO : (BigInteger) sqlQuery.list().get(0);
			
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
	}	

	@Override
	public BigInteger countPelakuKejadian(KejadianPelaku kejadianPelaku, Kotamaops kotamaops, Propinsi propinsi,
			Kabupaten_Kotamadya kabupatentKot, LocalDateTime twAwal, LocalDateTime twAkhir) {
		
		Session session = getSessionFactory().openSession();
		
		try {			
			SQLQuery sqlQuery = session.createSQLQuery("SELECT count(kejadian.motif_kejadian_id_fk) FROM e010_k2_pusdalops.kejadian kejadian"
					+ " where "
					+ " kejadian.pelaku_kejadian_id_fk=:kejadianPelaku and "
					+ " kejadian.kotamaops_id_fk=:kotamaops and "
					+ " kejadian.propinsi_id_fk=:propinsi and "
					+ " kejadian.kabupaten_id_fk=:kabupatenKot and "
					+ " kejadian.tw_kejadian_datetime<:twAkhirKejadian and "
					+ " kejadian.tw_kejadian_datetime>:twAwalKejadian"
					);
			sqlQuery.setParameter("kejadianPelaku", kejadianPelaku.getId());
			sqlQuery.setParameter("kotamaops", kotamaops.getId());
			sqlQuery.setParameter("propinsi", propinsi.getId());
			sqlQuery.setParameter("kabupatenKot", kabupatentKot.getId());
			sqlQuery.setParameter("twAkhirKejadian", twAkhir.toString());
			sqlQuery.setParameter("twAwalKejadian", twAwal.toString());
			
			return sqlQuery.list().isEmpty() ? BigInteger.ZERO : (BigInteger) sqlQuery.list().get(0);
			
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
	}

	@Override
	public BigInteger countPelakuKejadian(KejadianPelaku kejadianPelaku, Kotamaops kotamaops, Propinsi propinsi,
			Kabupaten_Kotamadya kabupatentKot, Kecamatan kecamatan, LocalDateTime twAwal, LocalDateTime twAkhir) {

		Session session = getSessionFactory().openSession();
		
		try {			
			SQLQuery sqlQuery = session.createSQLQuery("SELECT count(kejadian.motif_kejadian_id_fk) FROM e010_k2_pusdalops.kejadian kejadian"
					+ " where "
					+ " kejadian.pelaku_kejadian_id_fk=:kejadianPelaku and "
					+ " kejadian.kotamaops_id_fk=:kotamaops and "
					+ " kejadian.propinsi_id_fk=:propinsi and "
					+ " kejadian.kabupaten_id_fk=:kabupatenKot and "
					+ " kejadian.kecamatan_id_fk=:kecamatan and "
					+ " kejadian.tw_kejadian_datetime<:twAkhirKejadian and "
					+ " kejadian.tw_kejadian_datetime>:twAwalKejadian"
					);
			sqlQuery.setParameter("kejadianPelaku", kejadianPelaku.getId());
			sqlQuery.setParameter("kotamaops", kotamaops.getId());
			sqlQuery.setParameter("propinsi", propinsi.getId());
			sqlQuery.setParameter("kabupatenKot", kabupatentKot.getId());
			sqlQuery.setParameter("kecamatan", kecamatan.getId());
			sqlQuery.setParameter("twAkhirKejadian", twAkhir.toString());
			sqlQuery.setParameter("twAwalKejadian", twAwal.toString());
			
			return sqlQuery.list().isEmpty() ? BigInteger.ZERO : (BigInteger) sqlQuery.list().get(0);
			
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}

	}

	@Override
	public BigInteger countPelakuKejadian(KejadianPelaku kejadianPelaku, Kotamaops kotamaops, Propinsi propinsi,
			Kabupaten_Kotamadya kabupatentKot, Kecamatan kecamatan, Kelurahan kelurahan, LocalDateTime twAwal,
			LocalDateTime twAkhir) {
		Session session = getSessionFactory().openSession();
		
		try {			
			SQLQuery sqlQuery = session.createSQLQuery("SELECT count(kejadian.motif_kejadian_id_fk) FROM e010_k2_pusdalops.kejadian kejadian"
					+ " where "
					+ " kejadian.pelaku_kejadian_id_fk=:kejadianPelaku and "
					+ " kejadian.kotamaops_id_fk=:kotamaops and "
					+ " kejadian.propinsi_id_fk=:propinsi and "
					+ " kejadian.kabupaten_id_fk=:kabupatenKot and "
					+ " kejadian.kecamatan_id_fk=:kecamatan and "
					+ " kejadian.kelurahan_id_fk=:kelurahan and "
					+ " kejadian.tw_kejadian_datetime<:twAkhirKejadian and "
					+ " kejadian.tw_kejadian_datetime>:twAwalKejadian"
					);
			sqlQuery.setParameter("kejadianPelaku", kejadianPelaku.getId());
			sqlQuery.setParameter("kotamaops", kotamaops.getId());
			sqlQuery.setParameter("propinsi", propinsi.getId());
			sqlQuery.setParameter("kabupatenKot", kabupatentKot.getId());
			sqlQuery.setParameter("kecamatan", kecamatan.getId());
			sqlQuery.setParameter("kelurahan", kelurahan.getId());
			sqlQuery.setParameter("twAkhirKejadian", twAkhir.toString());
			sqlQuery.setParameter("twAwalKejadian", twAwal.toString());
			
			return sqlQuery.list().isEmpty() ? BigInteger.ZERO : (BigInteger) sqlQuery.list().get(0);
			
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
	}

	@Override
	public BigInteger countMotifKejadian(KejadianMotif motifKejadian, LocalDateTime twAwal,
			LocalDateTime twAkhir) {
		Session session = getSessionFactory().openSession();
		
		try {			
			SQLQuery sqlQuery = session.createSQLQuery("SELECT count(kejadian.motif_kejadian_id_fk) FROM e010_k2_pusdalops.kejadian kejadian"
					+ " where "
					+ " kejadian.motif_kejadian_id_fk=:kejadianMotif and "
					+ " kejadian.tw_kejadian_datetime<:twAkhirKejadian and "
					+ " kejadian.tw_kejadian_datetime>:twAwalKejadian"
					);
			sqlQuery.setParameter("kejadianMotif", motifKejadian.getId());
			sqlQuery.setParameter("twAkhirKejadian", twAkhir.toString());
			sqlQuery.setParameter("twAwalKejadian", twAwal.toString());
			
			return sqlQuery.list().isEmpty() ? BigInteger.ZERO : (BigInteger) sqlQuery.list().get(0);
			
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
	}
	
	@Override
	public BigInteger countMotifKejadian(KejadianMotif motifKejadian, Kotamaops kotamaops, LocalDateTime twAwal,
			LocalDateTime twAkhir) {

		Session session = getSessionFactory().openSession();
		
		try {			
			SQLQuery sqlQuery = session.createSQLQuery("SELECT count(kejadian.motif_kejadian_id_fk) FROM e010_k2_pusdalops.kejadian kejadian"
					+ " where "
					+ " kejadian.motif_kejadian_id_fk=:kejadianMotif and "
					+ " kejadian.kotamaops_id_fk=:kotamaops and "
					+ " kejadian.tw_kejadian_datetime<:twAkhirKejadian and "
					+ " kejadian.tw_kejadian_datetime>:twAwalKejadian"
					);
			sqlQuery.setParameter("kejadianMotif", motifKejadian.getId());
			sqlQuery.setParameter("kotamaops", kotamaops.getId());
			sqlQuery.setParameter("twAkhirKejadian", twAkhir.toString());
			sqlQuery.setParameter("twAwalKejadian", twAwal.toString());
			
			return sqlQuery.list().isEmpty() ? BigInteger.ZERO : (BigInteger) sqlQuery.list().get(0);
			
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}

	}

	@Override
	public BigInteger countMotifKejadian(KejadianMotif motifKejadian, Kotamaops kotamaops, Propinsi propinsi,
			LocalDateTime twAwal, LocalDateTime twAkhir) {
		Session session = getSessionFactory().openSession();
		
		try {			
			SQLQuery sqlQuery = session.createSQLQuery("SELECT count(kejadian.motif_kejadian_id_fk) FROM e010_k2_pusdalops.kejadian kejadian"
					+ " where "
					+ " kejadian.motif_kejadian_id_fk=:kejadianMotif and "
					+ " kejadian.kotamaops_id_fk=:kotamaops and "
					+ " kejadian.propinsi_id_fk=:propinsi and "
					+ " kejadian.tw_kejadian_datetime<:twAkhirKejadian and "
					+ " kejadian.tw_kejadian_datetime>:twAwalKejadian"
					);
			sqlQuery.setParameter("kejadianMotif", motifKejadian.getId());
			sqlQuery.setParameter("kotamaops", kotamaops.getId());
			sqlQuery.setParameter("propinsi", propinsi.getId());
			sqlQuery.setParameter("twAkhirKejadian", twAkhir.toString());
			sqlQuery.setParameter("twAwalKejadian", twAwal.toString());
			
			return sqlQuery.list().isEmpty() ? BigInteger.ZERO : (BigInteger) sqlQuery.list().get(0);
			
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
	}

	@Override
	public BigInteger countMotifKejadian(KejadianMotif motifKejadian, Kotamaops kotamaops, Propinsi propinsi,
			Kabupaten_Kotamadya kabupatenKot, LocalDateTime twAwal, LocalDateTime twAkhir) {

		Session session = getSessionFactory().openSession();
		
		try {			
			SQLQuery sqlQuery = session.createSQLQuery("SELECT count(kejadian.motif_kejadian_id_fk) FROM e010_k2_pusdalops.kejadian kejadian"
					+ " where "
					+ " kejadian.motif_kejadian_id_fk=:kejadianMotif and "
					+ " kejadian.kotamaops_id_fk=:kotamaops and "
					+ " kejadian.propinsi_id_fk=:propinsi and "
					+ " kejadian.kabupaten_id_fk=:kabupatenKot and "
					+ " kejadian.tw_kejadian_datetime<:twAkhirKejadian and "
					+ " kejadian.tw_kejadian_datetime>:twAwalKejadian"
					);
			sqlQuery.setParameter("kejadianMotif", motifKejadian.getId());
			sqlQuery.setParameter("kotamaops", kotamaops.getId());
			sqlQuery.setParameter("propinsi", propinsi.getId());
			sqlQuery.setParameter("kabupatenKot", kabupatenKot.getId());
			sqlQuery.setParameter("twAkhirKejadian", twAkhir.toString());
			sqlQuery.setParameter("twAwalKejadian", twAwal.toString());
			
			return sqlQuery.list().isEmpty() ? BigInteger.ZERO : (BigInteger) sqlQuery.list().get(0);
			
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
	}

	@Override
	public BigInteger countMotifKejadian(KejadianMotif motifKejadian, Kotamaops kotamaops, Propinsi propinsi,
			Kabupaten_Kotamadya kabupatenKot, Kecamatan kecamatan, LocalDateTime twAwal, LocalDateTime twAkhir) {

		Session session = getSessionFactory().openSession();
		
		try {			
			SQLQuery sqlQuery = session.createSQLQuery("SELECT count(kejadian.motif_kejadian_id_fk) FROM e010_k2_pusdalops.kejadian kejadian"
					+ " where "
					+ " kejadian.motif_kejadian_id_fk=:kejadianMotif and "
					+ " kejadian.kotamaops_id_fk=:kotamaops and "
					+ " kejadian.propinsi_id_fk=:propinsi and "
					+ " kejadian.kabupaten_id_fk=:kabupatenKot and "
					+ " kejadian.kecamatan_id_fk=:kecamatan and "
					+ " kejadian.tw_kejadian_datetime<:twAkhirKejadian and "
					+ " kejadian.tw_kejadian_datetime>:twAwalKejadian"
					);
			sqlQuery.setParameter("kejadianMotif", motifKejadian.getId());
			sqlQuery.setParameter("kotamaops", kotamaops.getId());
			sqlQuery.setParameter("propinsi", propinsi.getId());
			sqlQuery.setParameter("kabupatenKot", kabupatenKot.getId());
			sqlQuery.setParameter("kecamatan", kecamatan.getId());
			sqlQuery.setParameter("twAkhirKejadian", twAkhir.toString());
			sqlQuery.setParameter("twAwalKejadian", twAwal.toString());
			
			return sqlQuery.list().isEmpty() ? BigInteger.ZERO : (BigInteger) sqlQuery.list().get(0);
			
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
	}

	@Override
	public BigInteger countMotifKejadian(KejadianMotif motifKejadian, Kotamaops kotamaops, Propinsi propinsi,
			Kabupaten_Kotamadya kabupatenKot, Kecamatan kecamatan, Kelurahan kelurahan, LocalDateTime twAwal,
			LocalDateTime twAkhir) {
		Session session = getSessionFactory().openSession();
		
		try {			
			SQLQuery sqlQuery = session.createSQLQuery("SELECT count(kejadian.motif_kejadian_id_fk) FROM e010_k2_pusdalops.kejadian kejadian"
					+ " where "
					+ " kejadian.motif_kejadian_id_fk=:kejadianMotif and "
					+ " kejadian.kotamaops_id_fk=:kotamaops and "
					+ " kejadian.propinsi_id_fk=:propinsi and "
					+ " kejadian.kabupaten_id_fk=:kabupatenKot and "
					+ " kejadian.kecamatan_id_fk=:kecamatan and "
					+ " kejadian.kelurahan_id_fk=:kelurahan and "
					+ " kejadian.tw_kejadian_datetime<:twAkhirKejadian and "
					+ " kejadian.tw_kejadian_datetime>:twAwalKejadian"
					);
			sqlQuery.setParameter("kejadianMotif", motifKejadian.getId());
			sqlQuery.setParameter("kotamaops", kotamaops.getId());
			sqlQuery.setParameter("propinsi", propinsi.getId());
			sqlQuery.setParameter("kabupatenKot", kabupatenKot.getId());
			sqlQuery.setParameter("kecamatan", kecamatan.getId());
			sqlQuery.setParameter("kelurahan", kelurahan.getId());
			sqlQuery.setParameter("twAkhirKejadian", twAkhir.toString());
			sqlQuery.setParameter("twAwalKejadian", twAwal.toString());
			
			return sqlQuery.list().isEmpty() ? BigInteger.ZERO : (BigInteger) sqlQuery.list().get(0);
			
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
	}
	


}
