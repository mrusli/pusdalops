package mil.pusdalops.persistence.kecamatan.dao;

import java.util.List;

import mil.pusdalops.domain.wilayah.Kecamatan;

public interface KecamatanDao {

	/**
	 * @param id
	 * @return Kecamatan
	 * @throws Exception
	 */
	public Kecamatan findKecamatanById(long id) throws Exception;
	
	/**
	 * @return List<Kecamatan>
	 * @throws Exception
	 */
	public List<Kecamatan> findAllKecamatan() throws Exception;
	
	/**
	 * @param kecamatan
	 * @return long
	 * @throws Exception
	 */
	public Long save(Kecamatan kecamatan) throws Exception;
	
	/**
	 * @param kecamatan
	 * @throws Exception
	 */
	public void update(Kecamatan kecamatan) throws Exception;

	/**
	 * Kelurahan List is declared fetched lazy.
	 * 
	 * @param id
	 * @return Kelurahan
	 * @throws Exception
	 */
	public Kecamatan findKelurahanByProxy(long id) throws Exception;
}
