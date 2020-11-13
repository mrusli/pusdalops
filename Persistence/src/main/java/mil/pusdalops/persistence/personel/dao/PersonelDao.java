package mil.pusdalops.persistence.personel.dao;

import java.util.List;

import mil.pusdalops.domain.personel.Personel;

public interface PersonelDao {

	/**
	 * @param id
	 * @return Personel
	 * @throws Exception
	 */
	public Personel findPersonelById(long id) throws Exception;
	
	/**
	 * @return List<Personel>
	 * @throws Exception
	 */
	public List<Personel> findAllPersonel() throws Exception;
	
	/**
	 * @param personel
	 * @return Long
	 * @throws Exception
	 */
	public Long save(Personel personel) throws Exception;
	
	/**
	 * @param personel
	 * @throws Exception
	 */
	public void update(Personel personel) throws Exception;
}
