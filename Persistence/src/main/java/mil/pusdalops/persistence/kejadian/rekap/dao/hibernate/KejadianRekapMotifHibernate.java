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
import mil.pusdalops.domain.kotamaops.Kotamaops;
import mil.pusdalops.domain.wilayah.Kabupaten_Kotamadya;
import mil.pusdalops.domain.wilayah.Kecamatan;
import mil.pusdalops.domain.wilayah.Kelurahan;
import mil.pusdalops.domain.wilayah.Propinsi;
import mil.pusdalops.persistence.kejadian.rekap.dao.KejadianRekapMotifDao;

public class KejadianRekapMotifHibernate implements KejadianRekapMotifDao {
	private SessionFactory sessionFactory;
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public BigInteger countAllKejadian(LocalDateTime twAwal, LocalDateTime twAkhir) throws Exception {
		
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
	public BigInteger countKejadianInKotamaopsList(List<Kotamaops> kotamaopsList, LocalDateTime twAwal, LocalDateTime twAkhir) throws Exception {
		Session session = getSessionFactory().openSession();
		
		try {			
			SQLQuery sqlQuery = session.createSQLQuery("SELECT count(kejadian.id) FROM e010_k2_pusdalops.kejadian kejadian"
					+ " where "
					+ " kejadian.kotamaops_id_fk in (:kotamaops) and "
					+ " kejadian.tw_kejadian_datetime<:twAkhirKejadian and "
					+ " kejadian.tw_kejadian_datetime>:twAwalKejadian");
			sqlQuery.setParameterList("kotamaops", kotamaopsList);
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
	public BigInteger countKejadian(Kotamaops kotamaops, Propinsi propinsi, 
			LocalDateTime twAwal, LocalDateTime twAkhir) {
		
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

	@SuppressWarnings("unchecked")
	@Override
	public List<Kejadian> findDistinctKejadianByJenisKejadianInKotamaopsList(List<Kotamaops> kotamaopsList, Date twAwal,
			Date twAkhir) throws Exception {

		Session session = getSessionFactory().openSession();		

		Criteria criteria = session.createCriteria(Kejadian.class);
		criteria.add(Restrictions.ge("twKejadianDateTime", twAwal));
		criteria.add(Restrictions.le("twKejadianDateTime", twAkhir));
		criteria.add(Restrictions.in("kotamaops", kotamaopsList));
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

	@SuppressWarnings("unchecked")
	@Override
	public List<Kejadian> findDistinctKejadianByJenisKejadian(Kotamaops kotamaops, Propinsi propinsi, Date twAwal,
			Date twAkhir) {
		Session session = getSessionFactory().openSession();		

		Criteria criteria = session.createCriteria(Kejadian.class);
		criteria.add(Restrictions.eq("kotamaops", kotamaops));
		criteria.add(Restrictions.eq("propinsi",propinsi));
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

	@SuppressWarnings("unchecked")
	@Override
	public List<Kejadian> findDistinctKejadianByJenisKejadian(Kotamaops kotamaops, Propinsi propinsi,
			Kabupaten_Kotamadya kabupatenKot, Date twAwal, Date twAkhir) {
		Session session = getSessionFactory().openSession();		

		Criteria criteria = session.createCriteria(Kejadian.class);
		criteria.add(Restrictions.eq("kotamaops", kotamaops));
		criteria.add(Restrictions.eq("propinsi",propinsi));
		criteria.add(Restrictions.eq("kabupatenKotamadya", kabupatenKot));
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

	@SuppressWarnings("unchecked")
	@Override
	public List<Kejadian> findDistinctKejadianByJenisKejadian(Kotamaops kotamaops, Propinsi propinsi,
			Kabupaten_Kotamadya kabupatenKot, Kecamatan kecamatan, Date twAwal, Date twAkhir) {

		Session session = getSessionFactory().openSession();		

		Criteria criteria = session.createCriteria(Kejadian.class);
		criteria.add(Restrictions.eq("kotamaops", kotamaops));
		criteria.add(Restrictions.eq("propinsi",propinsi));
		criteria.add(Restrictions.eq("kabupatenKotamadya", kabupatenKot));
		criteria.add(Restrictions.eq("kecamatan", kecamatan));
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
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Kejadian> findDistinctKejadianByJenisKejadian(Kotamaops kotamaops, Propinsi propinsi,
			Kabupaten_Kotamadya kabupatenKot, Kecamatan kecamatan, Kelurahan kelurahan, Date twAwal, Date twAkhir) {

		Session session = getSessionFactory().openSession();		

		Criteria criteria = session.createCriteria(Kejadian.class);
		criteria.add(Restrictions.eq("kotamaops", kotamaops));
		criteria.add(Restrictions.eq("propinsi",propinsi));
		criteria.add(Restrictions.eq("kabupatenKotamadya", kabupatenKot));
		criteria.add(Restrictions.eq("kecamatan", kecamatan));
		criteria.add(Restrictions.eq("kelurahan", kelurahan));
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
	public BigInteger countJenisKejadian(KejadianJenis jenisKejadian, LocalDateTime twAwal,
			LocalDateTime twAkhir) {

		Session session = getSessionFactory().openSession();
		
		try {			
			SQLQuery sqlQuery = session.createSQLQuery("SELECT count(kejadian.jenis_kejadian_id_fk) FROM e010_k2_pusdalops.kejadian kejadian"
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

	@Override
	public BigInteger countJenisKejadianInKotamaops(List<Kotamaops> kotamaopsList, KejadianJenis jenisKejadian,
			LocalDateTime twAwal, LocalDateTime twAkhir) throws Exception {
		Session session = getSessionFactory().openSession();
		
		try {			
			SQLQuery sqlQuery = session.createSQLQuery("SELECT count(kejadian.jenis_kejadian_id_fk) FROM e010_k2_pusdalops.kejadian kejadian"
					+ " where "
					+ " kejadian.kotamaops_id_fk in (:kotamaops) and "
					+ " kejadian.jenis_kejadian_id_fk=:kejadianJenis and "
					+ " kejadian.tw_kejadian_datetime<:twAkhirKejadian and "
					+ " kejadian.tw_kejadian_datetime>:twAwalKejadian"
					);
			sqlQuery.setParameterList("kotamaops", kotamaopsList);
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
	
	@Override
	public BigInteger countJenisKejadian(KejadianJenis jenisKejadian, Kotamaops kotamaops, LocalDateTime twAwal,
			LocalDateTime twAkhir) {

		Session session = getSessionFactory().openSession();
		
		try {			
			SQLQuery sqlQuery = session.createSQLQuery("SELECT count(kejadian.jenis_kejadian_id_fk) FROM e010_k2_pusdalops.kejadian kejadian"
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
	public BigInteger countJenisKejadian(KejadianJenis jenisKejadian, Kotamaops kotamaops, Propinsi propinsi,
			LocalDateTime twAwal, LocalDateTime twAkhir) {

		Session session = getSessionFactory().openSession();
		
		try {			
			SQLQuery sqlQuery = session.createSQLQuery("SELECT count(kejadian.jenis_kejadian_id_fk) FROM e010_k2_pusdalops.kejadian kejadian"
					+ " where "
					+ " kejadian.jenis_kejadian_id_fk=:kejadianJenis and "
					+ " kejadian.kotamaops_id_fk=:kotamaops and "
					+ " kejadian.propinsi_id_fk=:propinsi and "
					+ " kejadian.tw_kejadian_datetime<:twAkhirKejadian and "
					+ " kejadian.tw_kejadian_datetime>:twAwalKejadian"
					);
			sqlQuery.setParameter("kejadianJenis", jenisKejadian.getId());
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
	public BigInteger countJenisKejadian(KejadianJenis jenisKejadian, Kotamaops kotamaops, Propinsi propinsi,
			Kabupaten_Kotamadya kabupatenKot, LocalDateTime twAwal, LocalDateTime twAkhir) {

		Session session = getSessionFactory().openSession();
		
		try {			
			SQLQuery sqlQuery = session.createSQLQuery("SELECT count(kejadian.jenis_kejadian_id_fk) FROM e010_k2_pusdalops.kejadian kejadian"
					+ " where "
					+ " kejadian.jenis_kejadian_id_fk=:kejadianJenis and "
					+ " kejadian.kotamaops_id_fk=:kotamaops and "
					+ " kejadian.propinsi_id_fk=:propinsi and "
					+ " kejadian.kabupaten_id_fk=:kabupatenKot and "
					+ " kejadian.tw_kejadian_datetime<:twAkhirKejadian and "
					+ " kejadian.tw_kejadian_datetime>:twAwalKejadian"
					);
			sqlQuery.setParameter("kejadianJenis", jenisKejadian.getId());
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
	public BigInteger countJenisKejadian(KejadianJenis jenisKejadian, Kotamaops kotamaops, Propinsi propinsi,
			Kabupaten_Kotamadya kabupatenKot, Kecamatan kecamatan, LocalDateTime twAwal, LocalDateTime twAkhir) {

		Session session = getSessionFactory().openSession();
		
		try {			
			SQLQuery sqlQuery = session.createSQLQuery("SELECT count(kejadian.jenis_kejadian_id_fk) FROM e010_k2_pusdalops.kejadian kejadian"
					+ " where "
					+ " kejadian.jenis_kejadian_id_fk=:kejadianJenis and "
					+ " kejadian.kotamaops_id_fk=:kotamaops and "
					+ " kejadian.propinsi_id_fk=:propinsi and "
					+ " kejadian.kabupaten_id_fk=:kabupatenKot and "
					+ " kejadian.kecamatan_id_fk=:kecamatan and "
					+ " kejadian.tw_kejadian_datetime<:twAkhirKejadian and "
					+ " kejadian.tw_kejadian_datetime>:twAwalKejadian"
					);
			sqlQuery.setParameter("kejadianJenis", jenisKejadian.getId());
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
	public BigInteger countJenisKejadian(KejadianJenis jenisKejadian, Kotamaops kotamaops, Propinsi propinsi,
			Kabupaten_Kotamadya kabupatenKot, Kecamatan kecamatan, Kelurahan kelurahan, LocalDateTime twAwal,
			LocalDateTime twAkhir) {

		Session session = getSessionFactory().openSession();
		
		try {			
			SQLQuery sqlQuery = session.createSQLQuery("SELECT count(kejadian.jenis_kejadian_id_fk) FROM e010_k2_pusdalops.kejadian kejadian"
					+ " where "
					+ " kejadian.jenis_kejadian_id_fk=:kejadianJenis and "
					+ " kejadian.kotamaops_id_fk=:kotamaops and "
					+ " kejadian.propinsi_id_fk=:propinsi and "
					+ " kejadian.kabupaten_id_fk=:kabupatenKot and "
					+ " kejadian.kecamatan_id_fk=:kecamatan and "
					+ " kejadian.kelurahan_id_fk=:kelurahan and "
					+ " kejadian.tw_kejadian_datetime<:twAkhirKejadian and "
					+ " kejadian.tw_kejadian_datetime>:twAwalKejadian"
					);
			sqlQuery.setParameter("kejadianJenis", jenisKejadian.getId());
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
	public BigInteger countMotifKejadian(KejadianMotif motifKejadian, LocalDateTime twAwal, LocalDateTime twAkhir) {
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
