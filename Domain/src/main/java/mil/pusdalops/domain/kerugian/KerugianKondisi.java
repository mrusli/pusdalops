package mil.pusdalops.domain.kerugian;

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
@Table(name = "kerugian_kondisi", schema = SchemaUtil.SCHEMA_COMMON)
public class KerugianKondisi extends IdBasedObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6801157821320676570L;

	//  `kondisi` varchar(255) DEFAULT NULL,
	@Column(name = "kondisi")
	private String namaKondisi;
	
	public String getNamaKondisi() {
		return namaKondisi;
	}

	public void setNamaKondisi(String namaKondisi) {
		this.namaKondisi = namaKondisi;
	}

}
