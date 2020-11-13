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
@Table(name = "kerugian_jenis", schema = SchemaUtil.SCHEMA_COMMON)
public class KerugianJenis extends IdBasedObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9074830316062043405L;

	//  `tipe_kerugian` varchar(255) DEFAULT NULL,
	@Column(name = "tipe_kerugian")
	private TipeKerugian tipeKerugian;
	
	//  `nama_jenis_kerugian` varchar(255) DEFAULT NULL,
	@Column(name = "nama_jenis_kerugian")
	private String namaJenis;
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "KerugianJenis [tipeKerugian=" + tipeKerugian + ", namaJenis=" + namaJenis + "]";
	}

	public String getNamaJenis() {
		return namaJenis;
	}

	public void setNamaJenis(String namaJenis) {
		this.namaJenis = namaJenis;
	}

	public TipeKerugian getTipeKerugian() {
		return tipeKerugian;
	}

	public void setTipeKerugian(TipeKerugian tipeKerugian) {
		this.tipeKerugian = tipeKerugian;
	}
}
