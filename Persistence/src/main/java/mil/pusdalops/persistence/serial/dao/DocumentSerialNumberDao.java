package mil.pusdalops.persistence.serial.dao;

import java.util.List;

import mil.pusdalops.domain.serial.DocumentSerialNumber;

public interface DocumentSerialNumberDao {

	/**
	 * Find the document serial number by id.
	 * 
	 * @param id
	 * @return DocumentSerialNumber
	 * @throws Exception
	 */
	public DocumentSerialNumber findDocumentSerialNumberById(long id) throws Exception;
	
	/**
	 * Find all the document serial number.
	 * 
	 * @return List<DocumentSerialNumber>
	 * @throws Exception
	 */
	public List<DocumentSerialNumber> findAllDocumentSerialNumber() throws Exception;
	
	/**
	 * Save a new document serial number.
	 * 
	 * @param documentSerialNumber
	 * @return long
	 * @throws Exception
	 */
	public long save(DocumentSerialNumber documentSerialNumber) throws Exception;
	
	/**
	 * Update an existing document serial number.
	 * 
	 * @param documentSerialNumber
	 * @throws Exception
	 */
	public void update(DocumentSerialNumber documentSerialNumber) throws Exception;
	
	/**
	 * Find the last document serial number by document code by reversing the order list (descending), and return
	 * the 1st item in the list.
	 *   
	 * In order to insert a new DocumentSerialNumber, you must find the last serial number and 1 (one). If this
	 * return null, an initial serial number can be used (001).
	 * 
	 * @param documentCode
	 * @return DocumentSerialNumber
	 * @throws Exception
	 */
	public DocumentSerialNumber findLastDocumentSerialNumberByDocumentType(String documentCode) throws Exception;

	/**
	 * Find the last document serial number WITHOUT document code (i.e., documentCode is null) by reversing the 
	 * order list (descending), and return the 1st item in the list.
	 *   
	 * In order to insert a new DocumentSerialNumber, you must find the last serial number and 1 (one). If this
	 * return null, an initial serial number can be used (001).
	 * 
	 * @return DocumentSerialNumber
	 * @throws Exception
	 */
	public DocumentSerialNumber findLastDocumentSerialNumber() throws Exception;

}
