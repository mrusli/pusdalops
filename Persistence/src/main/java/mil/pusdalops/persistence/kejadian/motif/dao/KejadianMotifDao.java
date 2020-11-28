package mil.pusdalops.persistence.kejadian.motif.dao;

import java.util.List;

import mil.pusdalops.domain.kejadian.KejadianMotif;

public interface KejadianMotifDao {

	/**
	 * @param id
	 * @return KejadianMotif
	 * @throws Exception
	 */
	public KejadianMotif findKejadianMotifById(long id) throws Exception;
	
	/**
	 * @return List<KejadianMotif>
	 * @throws Exception
	 */
	public List<KejadianMotif> findAllKejadianMotif() throws Exception;
		
	/**
	 * @param kejadianMotif
	 * @return Long
	 * @throws Exception
	 */
	public Long save(KejadianMotif kejadianMotif) throws Exception;
	
	/**
	 * @param kejadianMotif
	 * @throws Exception
	 */
	public void update(KejadianMotif kejadianMotif) throws Exception;

	/**
	 * Find all with ascending / descending order.
	 * 
	 * @param ascending - pass in a true boolean value
	 * @return List<Kejadian>
	 * @throws Exception
	 */
	public List<KejadianMotif> findAllKejadianMotifOrderBy(boolean ascending) throws Exception;
	
}
