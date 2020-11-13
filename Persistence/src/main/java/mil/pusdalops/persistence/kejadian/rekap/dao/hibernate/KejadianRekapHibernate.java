package mil.pusdalops.persistence.kejadian.rekap.dao.hibernate;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import mil.pusdalops.domain.kejadian.Kejadian;
import mil.pusdalops.domain.kerugian.Pihak;
import mil.pusdalops.domain.kerugian.TipeKerugian;
import mil.pusdalops.domain.kotamaops.Kotamaops;
import mil.pusdalops.domain.wilayah.Kabupaten_Kotamadya;
import mil.pusdalops.domain.wilayah.Kecamatan;
import mil.pusdalops.domain.wilayah.Kelurahan;
import mil.pusdalops.domain.wilayah.Propinsi;
import mil.pusdalops.persistence.kejadian.rekap.dao.KejadianRekapDao;

public class KejadianRekapHibernate implements KejadianRekapDao {

	private SessionFactory sessionFactory;
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Kejadian> findAllKejadian(
			Date twAkhir, Date twAwal) throws Exception {
		
		Session session = getSessionFactory().openSession();
		
		Criteria criteria = session.createCriteria(Kejadian.class);
		criteria.add(Restrictions.le("twKejadianDateTime", twAkhir));
		criteria.add(Restrictions.ge("twKejadianDateTime", twAwal));
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
	public List<Kejadian> findAllKejadian(Kotamaops kotamaops, 
			Date twAkhir, Date twAwal) throws Exception {
		
		Session session = getSessionFactory().openSession();
		
		Criteria criteria = session.createCriteria(Kejadian.class);
		criteria.add(Restrictions.eq("kotamaops", kotamaops));			
		criteria.add(Restrictions.le("twKejadianDateTime", twAkhir));
		criteria.add(Restrictions.ge("twKejadianDateTime", twAwal));
		
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
	public List<Kejadian> findAllKejadian(Kotamaops kotamaops, Propinsi propinsi, 
			Date twAkhir, Date twAwal) throws Exception {
		
		Session session = getSessionFactory().openSession();
		
		Criteria criteria = session.createCriteria(Kejadian.class);
		criteria.add(Restrictions.eq("kotamaops", kotamaops));			
		criteria.add(Restrictions.eq("propinsi", propinsi));
		criteria.add(Restrictions.le("twKejadianDateTime", twAkhir));
		criteria.add(Restrictions.ge("twKejadianDateTime", twAwal));
		
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
	public List<Kejadian> findAllKejadian(Kotamaops kotamaops, Propinsi propinsi, Kabupaten_Kotamadya kabupatenKot,
			Date twAkhir, Date twAwal) throws Exception {

		Session session = getSessionFactory().openSession();
		
		Criteria criteria = session.createCriteria(Kejadian.class);
		criteria.add(Restrictions.eq("kotamaops", kotamaops));			
		criteria.add(Restrictions.eq("propinsi", propinsi));
		criteria.add(Restrictions.eq("kabupatenKotamadya", kabupatenKot));
		criteria.add(Restrictions.le("twKejadianDateTime", twAkhir));
		criteria.add(Restrictions.ge("twKejadianDateTime", twAwal));
		
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
	public List<Kejadian> findAllKejadian(Kotamaops kotamaops, Propinsi propinsi, Kabupaten_Kotamadya kabupatenKot,
			Kecamatan kecamatan, Date twAkhir, Date twAwal) throws Exception {

		Session session = getSessionFactory().openSession();
		
		Criteria criteria = session.createCriteria(Kejadian.class);
		criteria.add(Restrictions.eq("kotamaops", kotamaops));			
		criteria.add(Restrictions.eq("propinsi", propinsi));
		criteria.add(Restrictions.eq("kabupatenKotamadya", kabupatenKot));
		criteria.add(Restrictions.eq("kecamatan", kecamatan));
		criteria.add(Restrictions.le("twKejadianDateTime", twAkhir));
		criteria.add(Restrictions.ge("twKejadianDateTime", twAwal));
		
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
	public List<Kejadian> findAllKejadian(Kotamaops kotamaops, Propinsi propinsi, Kabupaten_Kotamadya kabupatenKot,
			Kecamatan kecamatan, Kelurahan kelurahan, Date twAkhir, Date twAwal) throws Exception {

		Session session = getSessionFactory().openSession();
		
		Criteria criteria = session.createCriteria(Kejadian.class);
		criteria.add(Restrictions.eq("kotamaops", kotamaops));			
		criteria.add(Restrictions.eq("propinsi", propinsi));
		criteria.add(Restrictions.eq("kabupatenKotamadya", kabupatenKot));
		criteria.add(Restrictions.eq("kecamatan", kecamatan));
		criteria.add(Restrictions.eq("kelurahan", kelurahan));
		criteria.add(Restrictions.le("twKejadianDateTime", twAkhir));
		criteria.add(Restrictions.ge("twKejadianDateTime", twAwal));
		
		try {
			
			return criteria.list();
			
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
	}
	
	@Override
	public BigInteger countKerugianByTipe(TipeKerugian tipeKerugian, Pihak pihak, LocalDateTime twAkhir, LocalDateTime twAwal) throws Exception {
		Session session = getSessionFactory().openSession();
		
		try {			
			SQLQuery sqlQuery = session.createSQLQuery("SELECT count(kerugian.tipe_kerugian) FROM e010_k2_pusdalops.kejadian kejadian"
					+ " left outer join kejadian_join_kerugian on kejadian_join_kerugian.id_kejadian=kejadian.id"
					+ " left outer join kerugian kerugian on kejadian_join_kerugian.id_kerugian=kerugian.id"
					+ " where "
					+ " kerugian.pihak=:pihak and "
					+ " kejadian.tw_kejadian_datetime<:twAkhirKejadian and "
					+ " kejadian.tw_kejadian_datetime>:twAwalKejadian and "
					+ " kerugian.tipe_kerugian=:tipeKerugian"
					);
			sqlQuery.setParameter("pihak", pihak.ordinal());
			sqlQuery.setParameter("twAkhirKejadian", twAkhir.toString());
			sqlQuery.setParameter("twAwalKejadian", twAwal.toString());
			sqlQuery.setParameter("tipeKerugian", tipeKerugian.ordinal());
			
			return sqlQuery.list().isEmpty() ? BigInteger.ZERO : (BigInteger) sqlQuery.list().get(0);
			
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
	}		

	@Override
	public BigInteger countKerugianByTipe(TipeKerugian tipeKerugian, Kotamaops kotamaops, Pihak pihak, LocalDateTime twAkhir, LocalDateTime twAwal) throws Exception {
		Session session = getSessionFactory().openSession();
				
		try {			
			SQLQuery sqlQuery = session.createSQLQuery("SELECT count(kerugian.tipe_kerugian) FROM e010_k2_pusdalops.kejadian kejadian"
					+ " left outer join kejadian_join_kerugian on kejadian_join_kerugian.id_kejadian=kejadian.id"
					+ " left outer join kerugian kerugian on kejadian_join_kerugian.id_kerugian=kerugian.id"
					+ " where "
					+ " kejadian.kotamaops_id_fk=:kotamops and "
					+ " kerugian.pihak=:pihak and "
					+ " kejadian.tw_kejadian_datetime<:twAkhirKejadian and "
					+ " kejadian.tw_kejadian_datetime>:twAwalKejadian and "
					+ " kerugian.tipe_kerugian=:tipeKerugian"
					);
			sqlQuery.setParameter("kotamops", kotamaops.getId());
			sqlQuery.setParameter("pihak", pihak.ordinal());
			sqlQuery.setParameter("twAkhirKejadian", twAkhir.toString());
			sqlQuery.setParameter("twAwalKejadian", twAwal.toString());
			sqlQuery.setParameter("tipeKerugian", tipeKerugian.ordinal());
			
			return sqlQuery.list().isEmpty() ? BigInteger.ZERO : (BigInteger) sqlQuery.list().get(0);
			
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
		
	}

	@Override
	public BigInteger countKerugianByTipe(TipeKerugian tipeKerugian, Kotamaops kotamaops, Propinsi propinsi, Pihak pihak, LocalDateTime twAkhir, LocalDateTime twAwal) throws Exception {
		Session session = getSessionFactory().openSession();
		
		try {			
			SQLQuery sqlQuery = session.createSQLQuery("SELECT count(kerugian.tipe_kerugian) FROM e010_k2_pusdalops.kejadian kejadian"
					+ " left outer join kejadian_join_kerugian on kejadian_join_kerugian.id_kejadian=kejadian.id"
					+ " left outer join kerugian kerugian on kejadian_join_kerugian.id_kerugian=kerugian.id"
					+ " where "
					+ " kejadian.kotamaops_id_fk=:kotamops and "
					+ " kejadian.propinsi_id_fk=:propinsi and"
					+ " kerugian.pihak=:pihak and "
					+ " kejadian.tw_kejadian_datetime<:twAkhirKejadian and "
					+ " kejadian.tw_kejadian_datetime>:twAwalKejadian and "					
					+ " kerugian.tipe_kerugian=:tipeKerugian"
					);
			sqlQuery.setParameter("kotamops", kotamaops.getId());
			sqlQuery.setParameter("propinsi", propinsi.getId());
			sqlQuery.setParameter("pihak", pihak.ordinal());
			sqlQuery.setParameter("twAkhirKejadian", twAkhir.toString());
			sqlQuery.setParameter("twAwalKejadian", twAwal.toString());
			sqlQuery.setParameter("tipeKerugian", tipeKerugian.ordinal());
			
			return sqlQuery.list().isEmpty() ? BigInteger.ZERO : (BigInteger) sqlQuery.list().get(0);
			
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
	}

	@Override
	public BigInteger countKerugianByTipe(TipeKerugian tipeKerugian, Kotamaops kotamaops, Propinsi propinsi, Kabupaten_Kotamadya kabupatenKot, 
			Pihak pihak, LocalDateTime twAkhir, LocalDateTime twAwal) throws Exception {

		Session session = getSessionFactory().openSession();
		
		try {			
			SQLQuery sqlQuery = session.createSQLQuery("SELECT count(kerugian.tipe_kerugian) FROM e010_k2_pusdalops.kejadian kejadian"
					+ " left outer join kejadian_join_kerugian on kejadian_join_kerugian.id_kejadian=kejadian.id"
					+ " left outer join kerugian kerugian on kejadian_join_kerugian.id_kerugian=kerugian.id"
					+ " where "
					+ " kejadian.kotamaops_id_fk=:kotamops and "
					+ " kejadian.propinsi_id_fk=:propinsi and "
					+ " kejadian.kabupaten_id_fk=:kabupatenKot and "
					+ " kerugian.pihak=:pihak and "
					+ " kejadian.tw_kejadian_datetime<:twAkhirKejadian and "
					+ " kejadian.tw_kejadian_datetime>:twAwalKejadian and "						
					+ " kerugian.tipe_kerugian=:tipeKerugian"
					);
			sqlQuery.setParameter("kotamops", kotamaops.getId());
			sqlQuery.setParameter("propinsi", propinsi.getId());
			sqlQuery.setParameter("kabupatenKot", kabupatenKot.getId());
			sqlQuery.setParameter("pihak", pihak.ordinal());
			sqlQuery.setParameter("twAkhirKejadian", twAkhir.toString());
			sqlQuery.setParameter("twAwalKejadian", twAwal.toString());			
			sqlQuery.setParameter("tipeKerugian", tipeKerugian.ordinal());
			
			return sqlQuery.list().isEmpty() ? BigInteger.ZERO : (BigInteger) sqlQuery.list().get(0);
			
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
	}

	@Override
	public BigInteger countKerugianByTipe(TipeKerugian tipeKerugian, Kotamaops kotamaops, Propinsi propinsi, Kabupaten_Kotamadya kabupatenKot, 
			Kecamatan kecamatan, Pihak pihak, LocalDateTime twAkhir, LocalDateTime twAwal) throws Exception {

		Session session = getSessionFactory().openSession();
		
		try {			
			SQLQuery sqlQuery = session.createSQLQuery("SELECT count(kerugian.tipe_kerugian) FROM e010_k2_pusdalops.kejadian kejadian"
					+ " left outer join kejadian_join_kerugian on kejadian_join_kerugian.id_kejadian=kejadian.id"
					+ " left outer join kerugian kerugian on kejadian_join_kerugian.id_kerugian=kerugian.id"
					+ " where "
					+ " kejadian.kotamaops_id_fk=:kotamops and "
					+ " kejadian.propinsi_id_fk=:propinsi and "
					+ " kejadian.kabupaten_id_fk=:kabupatenKot and "
					+ " kejadian.kecamatan_id_fk=:kecamatan and "
					+ " kerugian.pihak=:pihak and "
					+ " kejadian.tw_kejadian_datetime<:twAkhirKejadian and "
					+ " kejadian.tw_kejadian_datetime>:twAwalKejadian and "						
					+ " kerugian.tipe_kerugian=:tipeKerugian"
					);
			sqlQuery.setParameter("kotamops", kotamaops.getId());
			sqlQuery.setParameter("propinsi", propinsi.getId());
			sqlQuery.setParameter("kabupatenKot", kabupatenKot.getId());
			sqlQuery.setParameter("kecamatan", kecamatan.getId());
			sqlQuery.setParameter("pihak", pihak.ordinal());
			sqlQuery.setParameter("twAkhirKejadian", twAkhir.toString());
			sqlQuery.setParameter("twAwalKejadian", twAwal.toString());					
			sqlQuery.setParameter("tipeKerugian", tipeKerugian.ordinal());
			
			return sqlQuery.list().isEmpty() ? BigInteger.ZERO : (BigInteger) sqlQuery.list().get(0);
			
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}

	}

	@Override
	public BigInteger countKerugianByTipe(TipeKerugian tipeKerugian, Kotamaops kotamaops, Propinsi propinsi, Kabupaten_Kotamadya kabupatenKot, 
			Kecamatan kecamatan, Kelurahan kelurahan, Pihak pihak, LocalDateTime twAkhir, LocalDateTime twAwal) {

		Session session = getSessionFactory().openSession();
		
		try {			
			SQLQuery sqlQuery = session.createSQLQuery("SELECT count(kerugian.tipe_kerugian) FROM e010_k2_pusdalops.kejadian kejadian"
					+ " left outer join kejadian_join_kerugian on kejadian_join_kerugian.id_kejadian=kejadian.id"
					+ " left outer join kerugian kerugian on kejadian_join_kerugian.id_kerugian=kerugian.id"
					+ " where "
					+ " kejadian.kotamaops_id_fk=:kotamops and "
					+ " kejadian.propinsi_id_fk=:propinsi and "
					+ " kejadian.kabupaten_id_fk=:kabupatenKot and "
					+ " kejadian.kecamatan_id_fk=:kecamatan and "
					+ " kejadian.kelurahan_id_fk=:kelurahan and "
					+ " kerugian.pihak=:pihak and "
					+ " kejadian.tw_kejadian_datetime<:twAkhirKejadian and "
					+ " kejadian.tw_kejadian_datetime>:twAwalKejadian and "						
					+ " kerugian.tipe_kerugian=:tipeKerugian"
					);
			sqlQuery.setParameter("kotamops", kotamaops.getId());
			sqlQuery.setParameter("propinsi", propinsi.getId());
			sqlQuery.setParameter("kabupatenKot", kabupatenKot.getId());
			sqlQuery.setParameter("kecamatan", kecamatan.getId());
			sqlQuery.setParameter("kelurahan", kelurahan.getId());
			sqlQuery.setParameter("pihak", pihak.ordinal());
			sqlQuery.setParameter("twAkhirKejadian", twAkhir.toString());
			sqlQuery.setParameter("twAwalKejadian", twAwal.toString());					
			sqlQuery.setParameter("tipeKerugian", tipeKerugian.ordinal());
			
			return sqlQuery.list().isEmpty() ? BigInteger.ZERO : (BigInteger) sqlQuery.list().get(0);
			
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
	}

}
