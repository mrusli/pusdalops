package mil.pusdalops.domain.kejadian;

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
@Table(name = "kejadian_motif", schema = SchemaUtil.SCHEMA_COMMON)
public class KejadianMotif extends IdBasedObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8882923627318897766L;

	//  `motif` varchar(255) DEFAULT NULL,
	@Column(name = "motif")
	private String namaMotif;

	public String getNamaMotif() {
		return namaMotif;
	}

	public void setNamaMotif(String namaMotif) {
		this.namaMotif = namaMotif;
	}
}
