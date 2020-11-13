package mil.pusdalops.persistence.pejabat.dao;

import java.util.List;

import mil.pusdalops.domain.kotamaops.Kotamaops;
import mil.pusdalops.domain.pejabat.Pejabat;

public interface PejabatDao {

	/**
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Pejabat findPejabatById(long id) throws Exception;
	
	/**
	 * @param asc
	 * @return
	 * @throws Exception
	 */
	public List<Pejabat> findAllPejabat() throws Exception;
	
	/**
	 * @param pejabat
	 * @return
	 * @throws Exception
	 */
	public Long save(Pejabat pejabat) throws Exception;
	
	/**
	 * @param pejabat
	 * @throws Exception
	 */
	public void update(Pejabat pejabat) throws Exception;

	/**
	 * Find all with Order - asc - true, ascending order, 
	 * false, descending order
	 * 
	 * @param asc
	 * @return List<Pejabat>
	 * @throws Exception
	 */
	public List<Pejabat> findAllPejabat(boolean asc) throws Exception;
	
	/**
	 * Find by kotamaops
	 * 
	 * @param asc
	 * @param kotamaops
	 * @return List<Pejabat>
	 */
	public List<Pejabat> findAllPejabatByKotamaops(boolean asc, Kotamaops kotamaops) throws Exception;

	public Pejabat findPejabatKotamaopsByProxy(long id) throws Exception;
}
