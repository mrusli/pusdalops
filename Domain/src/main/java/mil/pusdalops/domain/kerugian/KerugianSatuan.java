package mil.pusdalops.domain.kerugian;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import mil.pusdalops.domain.common.IdBasedObject;
import mil.pusdalops.domain.common.SchemaUtil;

@Entity
@Table(name = "kerugian_satuan", schema = SchemaUtil.SCHEMA_COMMON)
public class KerugianSatuan extends IdBasedObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7373570231414921881L;

	// `satuan` varchar(255) NOT NULL
	@Column(name = "satuan")
	private String satuan;

	public String getSatuan() {
		return satuan;
	}

	public void setSatuan(String satuan) {
		this.satuan = satuan;
	}
	
}
