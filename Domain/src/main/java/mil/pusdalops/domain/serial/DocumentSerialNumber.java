package mil.pusdalops.domain.serial;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import mil.pusdalops.domain.common.IdBasedObject;
import mil.pusdalops.domain.common.SchemaUtil;

/**
 * HAND202008310909078
 * - 4 char document type - toCode
 * - 4 char current year
 * - 2 char current month
 * - 2 char current date
 * - 2 char current hour
 * - 2 char current minutes
 * - 3 char serial number 
 * 
 * @author rusli
 *
 */
@Entity
@Table(name = "document_serial_number", schema = SchemaUtil.SCHEMA_COMMON)
public class DocumentSerialNumber extends IdBasedObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7990934547843160953L;
	
	//  document_code varchar(4) DEFAULT NULL,
	@Column(name = "document_code")
	private String documentCode;
	
	//  `serial_date` date DEFAULT NULL,
	@Column(name = "serial_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date serialDate;
	
	//  `serial_no` int(11) NOT NULL,
	@Column(name = "serial_no")
	private int serialNo;
	
	//  `serial_comp` varchar(255) DEFAULT NULL,
	@Column(name = "serial_comp")
	private String serialComp;

	/**
	 * @return the serialDate
	 */
	public Date getSerialDate() {
		return serialDate;
	}

	/**
	 * @param serialDate the serialDate to set
	 */
	public void setSerialDate(Date serialDate) {
		this.serialDate = serialDate;
	}

	/**
	 * @return the serialNo
	 */
	public int getSerialNo() {
		return serialNo;
	}

	/**
	 * @param serialNo the serialNo to set
	 */
	public void setSerialNo(int serialNo) {
		this.serialNo = serialNo;
	}

	/**
	 * @return the serialComp
	 */
	public String getSerialComp() {
		return serialComp;
	}

	/**
	 * @param serialComp the serialComp to set
	 */
	public void setSerialComp(String serialComp) {
		this.serialComp = serialComp;
	}

	public String getDocumentCode() {
		return documentCode;
	}

	public void setDocumentCode(String documentCode) {
		this.documentCode = documentCode;
	}
	
}
