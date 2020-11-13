package mil.pusdalops.persistence.kerugian.kondisi.dao;

import java.util.List;

import mil.pusdalops.domain.kerugian.KerugianKondisi;

public interface KerugianKondisiDao {

	/**
	 * @param id
	 * @return KerugianKondisi
	 * @throws Exception
	 */
	public KerugianKondisi findKerugianKondisiById(long id) throws Exception;
	
	/**
	 * Find all - ascending or descending order
	 * 
	 * @param asc - true - ascending, false - descending
	 * @return List<KerugianKondisi>
	 * @throws Exception
	 */
	public List<KerugianKondisi> findAllKerugianKondisi(boolean asc) throws Exception;
	
	/**
	 * Save
	 * 
	 * @param kerugianKondisi
	 * @return Long
	 * @throws Exception
	 */
	public Long save(KerugianKondisi kerugianKondisi) throws Exception;
	
	/**
	 * Update
	 * 
	 * @param kerugianKondisi
	 * @throws Exception
	 */
	public void update(KerugianKondisi kerugianKondisi) throws Exception;
}
