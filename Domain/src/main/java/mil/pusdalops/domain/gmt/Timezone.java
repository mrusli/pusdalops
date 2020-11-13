package mil.pusdalops.domain.gmt;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import mil.pusdalops.domain.common.IdBasedObject;
import mil.pusdalops.domain.common.SchemaUtil;

/**
 * 
 *  @author rusli
 *
 */
@Entity
@Table(name = "gmt_timezone", schema = SchemaUtil.SCHEMA_COMMON)
public class Timezone extends IdBasedObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5114091075432974377L;

	//  `name` varchar(255) DEFAULT NULL,
	@Column(name = "name")
	private String timezone;

	public String getTimezone() {
		return timezone;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}
}
