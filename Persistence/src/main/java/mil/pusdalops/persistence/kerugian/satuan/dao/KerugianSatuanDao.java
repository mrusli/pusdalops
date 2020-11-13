package mil.pusdalops.persistence.kerugian.satuan.dao;

import java.util.List;

import mil.pusdalops.domain.kerugian.KerugianSatuan;

public interface KerugianSatuanDao {

	/**
	 * Find by Id
	 * 
	 * @param id
	 * @return KerugianSatuan
	 * @throws Exception
	 */
	public KerugianSatuan findKerugianSatuanById(long id) throws Exception;
	
	/**
	 * Find all -- ascending or descending
	 * 
	 * @param asc - true - ascending, false - descending
	 * @return List<KerugianSatuan>
	 * @throws Exception
	 */
	public List<KerugianSatuan> findAllKerugianSatuan(boolean asc) throws Exception;
	
	/**
	 * Save
	 * 
	 * @param kerugianSatuan
	 * @return Long
	 * @throws Exception
	 */
	public Long save(KerugianSatuan kerugianSatuan) throws Exception;
	
	/**
	 * Update
	 * 
	 * @param kerugianSatuan
	 * @throws Exception
	 */
	public void update(KerugianSatuan kerugianSatuan) throws Exception;
	
}
