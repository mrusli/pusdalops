package mil.pusdalops.domain.kejadian;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import mil.pusdalops.domain.common.IdBasedObject;
import mil.pusdalops.domain.common.SchemaUtil;

/**
 * @author rusli
 *
 */
@Entity
@Table(name = "kejadian_pelaku", schema = SchemaUtil.SCHEMA_COMMON)
public class KejadianPelaku extends IdBasedObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3678024817630632848L;

	//  `pelaku` varchar(255) DEFAULT NULL,
	@Column(name = "pelaku")
	private String namaPelaku;

	public String getNamaPelaku() {
		return namaPelaku;
	}

	public void setNamaPelaku(String namaPelaku) {
		this.namaPelaku = namaPelaku;
	}
}
