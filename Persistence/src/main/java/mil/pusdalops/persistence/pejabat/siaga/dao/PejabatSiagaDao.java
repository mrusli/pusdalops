package mil.pusdalops.persistence.pejabat.siaga.dao;

import java.util.List;

import mil.pusdalops.domain.siaga.PejabatSiaga;

public interface PejabatSiagaDao {

	/**
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public PejabatSiaga findPejabatSiagaById(long id) throws Exception;
	
	/**
	 * @return
	 * @throws Exception
	 */
	public List<PejabatSiaga> findAllPejabatSiaga() throws Exception;
	
	/**
	 * @param pejabatSiaga
	 * @return
	 * @throws Exception
	 */
	public Long save(PejabatSiaga pejabatSiaga) throws Exception;
	
	/**
	 * @param pejabatSiaga
	 * @throws Exception
	 */
	public void update(PejabatSiaga pejabatSiaga) throws Exception;

	/**
	 * Pejabat is declared lazy
	 * 
	 * @param id
	 * @return PejabatSiaga
	 * @throws Exception
	 */
	public PejabatSiaga findPejabatSiagaPejabatByProxy(long id) throws Exception;
	
}
