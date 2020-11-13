package mil.pusdalops.domain.kerugian;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import mil.pusdalops.domain.common.IdBasedObject;
import mil.pusdalops.domain.common.SchemaUtil;

/**
 * 
 * @author rusli
 *
 */
@Entity
@Table(name = "kerugian", schema = SchemaUtil.SCHEMA_COMMON)
public class Kerugian extends IdBasedObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2548912212324758014L;

	//  `nama_mat` varchar(255) DEFAULT NULL,
	@Column(name = "nama_mat")
	private String namaMaterial;
	
	//  `pihak` int(11) DEFAULT NULL,
	@Column(name = "pihak")
	@Enumerated(EnumType.ORDINAL)
	private Pihak paraPihak;
	
	//	`lembaga_terkait` int(11) DEFAULT NULL,
	@Column(name = "lembaga_terkait")
	@Enumerated(EnumType.ORDINAL)
	private Lembaga lembagaTerkait;

	//	`tipe_kerugian` int(11) DEFAULT NULL,
	@Column(name = "tipe_kerugian")
	@Enumerated(EnumType.ORDINAL)
	private TipeKerugian tipeKerugian;
	
	//  `jenis_kerugian_id_fk` bigint(20) DEFAULT NULL,
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "jenis_kerugian_id_fk")
	private KerugianJenis kerugianJenis;
	
	//  `kondisi_kerugian_id_fk` bigint(20) DEFAULT NULL,
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "kondisi_kerugian_id_fk")
	private KerugianKondisi kerugianKondisi;
	
	//  `jumlah` int(11) DEFAULT NULL,
	@Column(name = "jumlah")
	private int jumlah;
	
	//  `satuan_kerugian_id_fk` bigint(20) DEFAULT NULL
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "satuan_kerugian_id_fk")
	private KerugianSatuan kerugianSatuan;
	
	//  `keterangan` varchar(255) DEFAULT NULL,
	@Column(name = "keterangan")
	private String keterangan;
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Kerugian [namaMaterial=" + namaMaterial + ", paraPihak=" + paraPihak + ", lembagaTerkait="
				+ lembagaTerkait + ", kerugianJenis=" + kerugianJenis + ", kerugianKondisi=" + kerugianKondisi
				+ ", jumlah=" + jumlah + ", keterangan=" + keterangan + "]";
	}

	public String getNamaMaterial() {
		return namaMaterial;
	}

	public void setNamaMaterial(String namaMaterial) {
		this.namaMaterial = namaMaterial;
	}

	public Pihak getParaPihak() {
		return paraPihak;
	}

	public void setParaPihak(Pihak paraPihak) {
		this.paraPihak = paraPihak;
	}

	public KerugianJenis getKerugianJenis() {
		return kerugianJenis;
	}

	public void setKerugianJenis(KerugianJenis kerugianJenis) {
		this.kerugianJenis = kerugianJenis;
	}

	public KerugianKondisi getKerugianKondisi() {
		return kerugianKondisi;
	}

	public void setKerugianKondisi(KerugianKondisi kerugianKondisi) {
		this.kerugianKondisi = kerugianKondisi;
	}

	public int getJumlah() {
		return jumlah;
	}

	public void setJumlah(int jumlah) {
		this.jumlah = jumlah;
	}

	public String getKeterangan() {
		return keterangan;
	}

	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}

	public Lembaga getLembagaTerkait() {
		return lembagaTerkait;
	}

	public void setLembagaTerkait(Lembaga lembagaTerkait) {
		this.lembagaTerkait = lembagaTerkait;
	}

	public TipeKerugian getTipeKerugian() {
		return tipeKerugian;
	}

	public void setTipeKerugian(TipeKerugian tipeKerugian) {
		this.tipeKerugian = tipeKerugian;
	}

	public KerugianSatuan getKerugianSatuan() {
		return kerugianSatuan;
	}

	public void setKerugianSatuan(KerugianSatuan kerugianSatuan) {
		this.kerugianSatuan = kerugianSatuan;
	}


}
