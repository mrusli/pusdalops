package mil.pusdalops.persistence.kerugian.dao;

import java.util.List;

import mil.pusdalops.domain.kerugian.Kerugian;

public interface KerugianDao {

	/**
	 * @param id
	 * @return Kerugian
	 * @throws Exception
	 */
	public Kerugian findKerugianById(long id) throws Exception;
	
	/**
	 * @return List<Kerugian>
	 * @throws Exception
	 */
	public List<Kerugian> findAllKerugian() throws Exception;
	
	/**
	 * @param kerugian
	 * @return Long
	 * @throws Exception
	 */
	public Long save(Kerugian kerugian) throws Exception;
	
	/**
	 * @param kerugian
	 * @throws Exception
	 */
	public void update(Kerugian kerugian) throws Exception;
}
