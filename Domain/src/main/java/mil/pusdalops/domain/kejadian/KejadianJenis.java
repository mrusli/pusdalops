package mil.pusdalops.domain.kejadian;

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
@Table(name = "kejadian_jenis", schema = SchemaUtil.SCHEMA_COMMON)
public class KejadianJenis extends IdBasedObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8143331190124174052L;

	//  `jenis` varchar(255) DEFAULT NULL,
	@Column(name = "jenis")
	private String namaJenis;

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "KejadianJenis [namaJenis=" + namaJenis + "]";
	}

	public String getNamaJenis() {
		return namaJenis;
	}

	public void setNamaJenis(String namaJenis) {
		this.namaJenis = namaJenis;
	}
}
