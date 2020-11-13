package mil.pusdalops.webui.common;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.apache.log4j.Logger;

import mil.pusdalops.domain.serial.DocumentSerialNumber;
import mil.pusdalops.persistence.serial.dao.DocumentSerialNumberDao;

public class SerialNumberGenerator extends GFCBaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5203520684708567842L;
	
	private DocumentSerialNumberDao documentSerialNumberDao;
	
	private static final Logger log = Logger.getLogger(SerialNumberGenerator.class);
	
	public int getSerialNumber(String documentCode, ZoneId zoneId, LocalDateTime currentDate) throws Exception {
		int serialNum = 1;
		DocumentSerialNumber documentSerNum = null;
		
		if (documentCode==null) {
			documentSerNum =
					getDocumentSerialNumberDao().findLastDocumentSerialNumber();
		} else {
			documentSerNum =
					getDocumentSerialNumberDao().findLastDocumentSerialNumberByDocumentType(documentCode);			
		}

		if (documentSerNum!=null) {
			Date lastDate = documentSerNum.getSerialDate();

			// compare year
			int lastYearValue = asLocalDateTime(lastDate).getYear();
			int currYearValue = currentDate.getYear();
			log.info("year: last: "+lastYearValue+" current: "+currYearValue);
			
			// compare month
			int lastMonthValue = asLocalDateTime(lastDate).getMonthValue();
			int currMonthValue = currentDate.getMonthValue();
			log.info("month: last: "+lastMonthValue+" current: "+currMonthValue);
			
			if (lastYearValue==currYearValue) {

				if (lastMonthValue==currMonthValue) {

					serialNum = documentSerNum.getSerialNo()+1;
					
				}
			}
		}
		
		return serialNum;
	}
	
	
	public DocumentSerialNumberDao getDocumentSerialNumberDao() {
		return documentSerialNumberDao;
	}

	public void setDocumentSerialNumberDao(DocumentSerialNumberDao documentSerialNumberDao) {
		this.documentSerialNumberDao = documentSerialNumberDao;
	}

}
