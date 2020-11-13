package mil.pusdalops.persistence.kejadian.pelaku.dao;

import java.util.List;

import mil.pusdalops.domain.kejadian.KejadianPelaku;

public interface KejadianPelakuDao {
	
	/**
	 * @param id
	 * @return KejadianPelaku
	 * @throws Exception
	 */
	public KejadianPelaku findKejadianPelakuById(long id) throws Exception;
	
	/**
	 * @return List<KejadianPelaku>
	 * @throws Exception
	 */
	public List<KejadianPelaku> findAllKejadianPelaku() throws Exception;
	
	/**
	 * @param kejadianPelaku
	 * @return Long
	 * @throws Exception
	 */
	public Long save(KejadianPelaku kejadianPelaku) throws Exception;
	
	/**
	 * @param kejadianPelaku
	 * @throws Exception
	 */
	public void update(KejadianPelaku kejadianPelaku) throws Exception;


}
