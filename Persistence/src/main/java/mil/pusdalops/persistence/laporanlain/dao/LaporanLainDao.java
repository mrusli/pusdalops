package mil.pusdalops.persistence.laporanlain.dao;

import java.util.List;

import mil.pusdalops.domain.kotamaops.Kotamaops;
import mil.pusdalops.domain.laporanlain.LaporanLain;

public interface LaporanLainDao {

	/**
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public LaporanLain findLaporanLainById(long id) throws Exception;
	
	/**
	 * @return
	 * @throws Exception
	 */
	public List<LaporanLain> findAllLaporanLain() throws Exception;
	
	/**
	 * @param laporanLain
	 * @return
	 * @throws Exception
	 */
	public Long save(LaporanLain laporanLain) throws Exception;
	
	/**
	 * @param laporanLain
	 * @throws Exception
	 */
	public void update(LaporanLain laporanLain) throws Exception;

	/**
	 * Kotamaops is declared lazy
	 * 
	 * @param id
	 * @return LaporanLain
	 * @throws Exception
	 */
	public LaporanLain findLaporanLainKotamaopsByProxy(long id) throws Exception;

	/**
	 * find LaporanLain by Kotamaops
	 * 
	 * @param kotamaops
	 * @return
	 * @throws Exception
	 */
	public List<LaporanLain> findAllLaporanLainByKotamaops(Kotamaops kotamaops) throws Exception;
	
}
