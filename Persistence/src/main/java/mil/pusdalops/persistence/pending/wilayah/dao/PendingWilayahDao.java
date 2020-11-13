package mil.pusdalops.persistence.pending.wilayah.dao;

import java.util.List;

import mil.pusdalops.domain.pending.PendingWilayah;

public interface PendingWilayahDao {

	/**
	 * Find by id
	 * 
	 * @param id
	 * @return PendingWilayah
	 * @throws Exception
	 */
	public PendingWilayah findPendingWilayahById(long id) throws Exception; 
	
	/**
	 * Find all
	 * 
	 * @return List<PendingWilayah>
	 * @throws Exception
	 */
	public List<PendingWilayah> findAllPendingWilayah() throws Exception;
	
	/**
	 * Save a pendingWilayah object
	 * 
	 * @param pendingWilayah
	 * @return Long
	 * @throws Exception
	 */
	public Long save(PendingWilayah pendingWilayah) throws Exception;
	
	/**
	 * Update an existing pendingWilayah object
	 * 
	 * @param pendingWilayah
	 * @throws Exception
	 */
	public void update(PendingWilayah pendingWilayah) throws Exception;
	
}
