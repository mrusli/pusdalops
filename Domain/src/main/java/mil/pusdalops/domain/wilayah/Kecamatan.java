package mil.pusdalops.domain.wilayah;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import mil.pusdalops.domain.common.IdBasedObject;
import mil.pusdalops.domain.common.SchemaUtil;

/**
 * 
 *  @author rusli
 *
 */
@Entity
@Table(name = "kecamatan", schema = SchemaUtil.SCHEMA_COMMON)
public class Kecamatan extends IdBasedObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4571923372448411112L;

	@Column(name = "nama_kecamatan")
	private String namaKecamatan;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval=true, fetch = FetchType.LAZY)
	@JoinTable(
			name = "kecamatan_join_kelurahan",
			joinColumns = @JoinColumn(name = "id_kecamatan"),
			inverseJoinColumns = @JoinColumn(name = "id_kelurahan"))
	private List<Kelurahan> kelurahans;
	
	@Override
	public String toString() {
		return "Kecamatan [namaKecamatan=" + namaKecamatan + "]";
	}

	public String getNamaKecamatan() {
		return namaKecamatan;
	}

	public void setNamaKecamatan(String namaKecamatan) {
		this.namaKecamatan = namaKecamatan;
	}

	public List<Kelurahan> getKelurahans() {
		return kelurahans;
	}

	public void setKelurahans(List<Kelurahan> kelurahans) {
		this.kelurahans = kelurahans;
	}
	
}
