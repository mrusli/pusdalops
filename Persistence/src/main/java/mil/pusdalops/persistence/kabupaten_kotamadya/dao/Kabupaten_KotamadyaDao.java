package mil.pusdalops.persistence.kabupaten_kotamadya.dao;

import java.util.List;

import mil.pusdalops.domain.wilayah.Kabupaten_Kotamadya;

public interface Kabupaten_KotamadyaDao {

	/**
	 * @param id
	 * @return Kabupaten_Kotamadya
	 * @throws Exception
	 */
	public Kabupaten_Kotamadya findKabupaten_KotamadyaById(long id) throws Exception;
	
	/**
	 * @return List<Kabupaten_Kotamadya>
	 * @throws Exception
	 */
	public List<Kabupaten_Kotamadya> findAllKabupaten_Kotamadya() throws Exception;
	
	/**
	 * @param kabupaten_kotamadya
	 * @return long
	 * @throws Exception
	 */
	public Long save(Kabupaten_Kotamadya kabupaten_kotamadya) throws Exception;
	
	/**
	 * @param kabupaten_kotamadya
	 * @throws Exception
	 */
	public void update(Kabupaten_Kotamadya kabupaten_kotamadya) throws Exception;

	/**
	 * Kecamatan list is declared lazy.
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Kabupaten_Kotamadya findKecamatanByProxy(long id) throws Exception;
}
