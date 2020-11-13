package mil.pusdalops.persistence.kelurahan.dao;

import java.util.List;

import mil.pusdalops.domain.wilayah.Kelurahan;

public interface KelurahanDao {

	/**
	 * @param id
	 * @return Kelurahan
	 * @throws Exception
	 */
	public Kelurahan findKelurahanById(long id) throws Exception;
	
	/**
	 * @return List<Kelurahan>
	 * @throws Exception
	 */
	public List<Kelurahan> findAllKelurahan() throws Exception;
	
	/**
	 * @param kelurahan
	 * @return long
	 * @throws Exception
	 */
	public Long save(Kelurahan kelurahan) throws Exception;
	
	/**
	 * @param kelurahan
	 * @throws Exception
	 */
	public void update(Kelurahan kelurahan) throws Exception;
}
