package mil.pusdalops.persistence.kejadian.rekap.dao.hibernate;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import mil.pusdalops.domain.kejadian.Kejadian;
import mil.pusdalops.domain.kejadian.KejadianJenis;
import mil.pusdalops.domain.kejadian.KejadianMotif;
import mil.pusdalops.domain.kejadian.KejadianPelaku;
import mil.pusdalops.domain.kotamaops.Kotamaops;
import mil.pusdalops.domain.wilayah.Propinsi;

import mil.pusdalops.persistence.kejadian.rekap.dao.KejadianRekapIntensitasDao;

public class KejadianRekapIntensitasHibernate implements KejadianRekapIntensitasDao {

	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public BigInteger countKejadianByPropinsi(Propinsi propinsi, Date twAwal, Date twAkhir) {
		Session session = getSessionFactory().openSession();
		
		try {			
			SQLQuery sqlQuery = session.createSQLQuery("SELECT count(kejadian.id) FROM e010_k2_pusdalops.kejadian kejadian"
					+ " where "
					+ " kejadian.propinsi_id_fk=:propinsi and "
					+ " kejadian.tw_kejadian_datetime<:twAkhirKejadian and "
					+ " kejadian.tw_kejadian_datetime>:twAwalKejadian "
					);
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

	@SuppressWarnings("unchecked")
	@Override
	public List<Kejadian> findDistinctKejadianByJenisKejadian(Date twAwal, Date twAkhir) {
		Session session = getSessionFactory().openSession();		

		Criteria criteria = session.createCriteria(Kejadian.class);
		criteria.add(Restrictions.ge("twKejadianDateTime", twAwal));
		criteria.add(Restrictions.le("twKejadianDateTime", twAkhir));
		criteria.setProjection(Projections.distinct(Projections.projectionList().add(Projections.property("jenisKejadian"), "jenisKejadian")));
		criteria.setResultTransformer(Transformers.aliasToBean(Kejadian.class));
		
		try {

			return criteria.list();
			
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
	}

	@Override
	public BigInteger countJenisKejadian(KejadianJenis jenisKejadian, LocalDateTime twAwal, LocalDateTime twAkhir) {
		Session session = getSessionFactory().openSession();
		
		try {			
			SQLQuery sqlQuery = session.createSQLQuery("SELECT count(kejadian.id) FROM e010_k2_pusdalops.kejadian kejadian"
					+ " where "
					+ " kejadian.jenis_kejadian_id_fk=:kejadianJenis and "
					+ " kejadian.tw_kejadian_datetime<:twAkhirKejadian and "
					+ " kejadian.tw_kejadian_datetime>:twAwalKejadian"
					);
			sqlQuery.setParameter("kejadianJenis", jenisKejadian.getId());
			sqlQuery.setParameter("twAkhirKejadian", twAkhir.toString());
			sqlQuery.setParameter("twAwalKejadian", twAwal.toString());
			
			return sqlQuery.list().isEmpty() ? BigInteger.ZERO : (BigInteger) sqlQuery.list().get(0);
			
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Kejadian> findDistinctKejadianByJenisKejadian(Kotamaops kotamaops, Date twAwal, Date twAkhir) {
		Session session = getSessionFactory().openSession();		

		Criteria criteria = session.createCriteria(Kejadian.class);
		criteria.add(Restrictions.eq("kotamaops", kotamaops));
		criteria.add(Restrictions.ge("twKejadianDateTime", twAwal));
		criteria.add(Restrictions.le("twKejadianDateTime", twAkhir));
		criteria.setProjection(Projections.distinct(Projections.projectionList().add(Projections.property("jenisKejadian"), "jenisKejadian")));
		criteria.setResultTransformer(Transformers.aliasToBean(Kejadian.class));
		
		try {

			return criteria.list();
			
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}

	}

	@Override
	public BigInteger countJenisKejadian(KejadianJenis jenisKejadian, Kotamaops kotamaops, LocalDateTime twAwal,
			LocalDateTime twAkhir) {
		Session session = getSessionFactory().openSession();
		
		try {			
			SQLQuery sqlQuery = session.createSQLQuery("SELECT count(kejadian.id) FROM e010_k2_pusdalops.kejadian kejadian"
					+ " where "
					+ " kejadian.jenis_kejadian_id_fk=:kejadianJenis and "
					+ " kejadian.kotamaops_id_fk=:kotamaops and "
					+ " kejadian.tw_kejadian_datetime<:twAkhirKejadian and "
					+ " kejadian.tw_kejadian_datetime>:twAwalKejadian"
					);
			sqlQuery.setParameter("kejadianJenis", jenisKejadian.getId());
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
	public BigInteger countMotifKejadian(KejadianMotif motifKejadian, LocalDateTime twAwal, LocalDateTime twAkhir) {
		Session session = getSessionFactory().openSession();
		
		try {			
			SQLQuery sqlQuery = session.createSQLQuery("SELECT count(kejadian.id) FROM e010_k2_pusdalops.kejadian kejadian"
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
			SQLQuery sqlQuery = session.createSQLQuery("SELECT count(kejadian.id) FROM e010_k2_pusdalops.kejadian kejadian"
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
	public BigInteger countPelakuKejadian(KejadianPelaku kejadianPelaku, LocalDateTime twAwal, LocalDateTime twAkhir) {
		Session session = getSessionFactory().openSession();
		
		try {			
			SQLQuery sqlQuery = session.createSQLQuery("SELECT count(kejadian.id) FROM e010_k2_pusdalops.kejadian kejadian"
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
	public BigInteger countPelakuKejadian(KejadianPelaku kejadianPelaku, Kotamaops kotamaops, LocalDateTime twAwal,
			LocalDateTime twAkhir) {
		Session session = getSessionFactory().openSession();
		
		try {			
			SQLQuery sqlQuery = session.createSQLQuery("SELECT count(kejadian.id) FROM e010_k2_pusdalops.kejadian kejadian"
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
	
}
