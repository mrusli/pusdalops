package mil.pusdalops.domain.wilayah;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import mil.pusdalops.domain.common.IdBasedObject;
import mil.pusdalops.domain.common.SchemaUtil;

/**
 * 
 * @author rusli
 *
 */
@Entity
@Table(name = "kelurahan", schema = SchemaUtil.SCHEMA_COMMON)
public class Kelurahan extends IdBasedObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8759039594852603007L;

	@Column(name = "nama_kelurahan")
	private String namaKelurahan;

	public String getNamaKelurahan() {
		return namaKelurahan;
	}

	public void setNamaKelurahan(String namaKelurahan) {
		this.namaKelurahan = namaKelurahan;
	}
}
