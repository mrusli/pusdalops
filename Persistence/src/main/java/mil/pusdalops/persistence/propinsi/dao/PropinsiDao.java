package mil.pusdalops.persistence.propinsi.dao;

import java.util.List;

import mil.pusdalops.domain.wilayah.Propinsi;

public interface PropinsiDao {

	/**
	 * @param id
	 * @return Propinsi
	 * @throws Exception
	 */
	public Propinsi findPropinsiById(long id) throws Exception;
	
	/**
	 * @return List<Propinsi>
	 * @throws Exception
	 */
	public List<Propinsi> findAllPropinsi() throws Exception;
	
	/**
	 * @param propinsi
	 * @return long
	 * @throws Exception
	 */
	public Long save(Propinsi propinsi) throws Exception;
	
	/**
	 * @param propinsi
	 * @throws Exception
	 */
	public void update(Propinsi propinsi) throws Exception;

	/**
	 * KabupatenKotamadays list is declared fetch lazy.
	 * @param id 
	 * 
	 * @return Propinsi
	 * @throws Exception
	 */
	public Propinsi findKabupatenKotamadyaByProxy(long id) throws Exception;
}
