package mil.pusdalops.persistence.kejadian.jenis.dao;

import java.util.List;

import mil.pusdalops.domain.kejadian.KejadianJenis;

public interface KejadianJenisDao {

	/**
	 * @param id
	 * @return KejadianJenis
	 * @throws Exception
	 */
	public KejadianJenis findKejadianJenisById(long id) throws Exception;
	
	/**
	 * Find all - with ascending or descening order
	 * 
	 * @param asc - true - ascending; false - descending
	 * @return List<KejadianJenis>
	 * @throws Exception
	 */
	public List<KejadianJenis> findAllKejadianJenis(boolean asc) throws Exception;
	
	/**
	 * @param kejadianJenis
	 * @return Long
	 * @throws Exception
	 */
	public Long save(KejadianJenis kejadianJenis) throws Exception;
	
	/**
	 * @param kejadianJenis
	 * @throws Exception
	 */
	public void update(KejadianJenis kejadianJenis) throws Exception;
	
	/**
	 * Find all with ascending / descending order.
	 * 
	 * @param ascending - pass in a true boolean value
	 * @return List<Kejadian>
	 * @throws Exception
	 */
	public List<KejadianJenis> findAllKejadianJenisOrderBy(boolean ascending) throws Exception;
	
}
