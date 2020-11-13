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
 * @author rusli
 *
 */
@Entity
@Table(name = "kabupaten_kotamadya", schema = SchemaUtil.SCHEMA_COMMON)
public class Kabupaten_Kotamadya extends IdBasedObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1078657466190122877L;

	@Column(name = "nama_kabupaten")
	private String namaKabupaten;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval=true, fetch = FetchType.LAZY)
	@JoinTable(
			name = "kabupaten_join_kecamatan",
			joinColumns = @JoinColumn(name = "id_kabupaten"),
			inverseJoinColumns = @JoinColumn(name = "id_kecamatan"))
	private List<Kecamatan> kecamatans;
	
	public String getNamaKabupaten() {
		return namaKabupaten;
	}

	public void setNamaKabupaten(String namaKabupaten) {
		this.namaKabupaten = namaKabupaten;
	}

	public List<Kecamatan> getKecamatans() {
		return kecamatans;
	}

	public void setKecamatans(List<Kecamatan> kecamatans) {
		this.kecamatans = kecamatans;
	}
}
