package mil.pusdalops.persistence.kerugian.jenis.dao;

import java.util.List;

import mil.pusdalops.domain.kerugian.KerugianJenis;

public interface KerugianJenisDao {

	/**
	 * @param id
	 * @return KerugianJenis
	 * @throws Exception
	 */
	public KerugianJenis findKerugianJenisById(long id) throws Exception;
	

	/**
	 * List all kerugian jenis - sorted ascending / descending
	 * 
	 * @param asc - true - ascending, false - descending
	 * @return List<KerugianJenis>
	 * @throws Exception
	 */
	public List<KerugianJenis> findAllKerugianJenis(boolean asc) throws Exception;
	
	/**
	 * @param kerugianJenis
	 * @return Long
	 * @throws Exception
	 */
	public Long save(KerugianJenis kerugianJenis) throws Exception;
	
	/**
	 * @param kerugianJenis
	 * @throws Exception
	 */
	public void update(KerugianJenis kerugianJenis) throws Exception;
}
