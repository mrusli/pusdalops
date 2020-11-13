package mil.pusdalops.domain.pejabat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import mil.pusdalops.domain.common.IdBasedObject;
import mil.pusdalops.domain.common.SchemaUtil;
import mil.pusdalops.domain.kotamaops.Kotamaops;

@Entity
@Table(name = "pejabat", schema = SchemaUtil.SCHEMA_COMMON)
public class Pejabat extends IdBasedObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3206243758711091113L;

	//  `kotamaops_id_fk` bigint(20) DEFAULT NULL,
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "kotamaops_id_fk")
	private Kotamaops kotamaops;
	
	//  `nama` varchar(255) DEFAULT NULL,
	@Column(name = "nama")
	private String nama;
	
	//  `pangkat` varchar(255) DEFAULT NULL,
	@Column(name = "pangkat")
	private String pangkat;
	
	//  `jabatan` varchar(255) DEFAULT NULL,
	@Column(name = "jabatan")
	private String jabatan;
	
	//  `nrp` varchar(255) DEFAULT NULL,
	@Column(name = "nrp")
	private String nrp;

	/**
	 * @return the kotamaops
	 */
	public Kotamaops getKotamaops() {
		return kotamaops;
	}

	/**
	 * @param kotamaops the kotamaops to set
	 */
	public void setKotamaops(Kotamaops kotamaops) {
		this.kotamaops = kotamaops;
	}

	/**
	 * @return the nama
	 */
	public String getNama() {
		return nama;
	}

	/**
	 * @param nama the nama to set
	 */
	public void setNama(String nama) {
		this.nama = nama;
	}

	/**
	 * @return the pangkat
	 */
	public String getPangkat() {
		return pangkat;
	}

	/**
	 * @param pangkat the pangkat to set
	 */
	public void setPangkat(String pangkat) {
		this.pangkat = pangkat;
	}

	/**
	 * @return the jabatan
	 */
	public String getJabatan() {
		return jabatan;
	}

	/**
	 * @param jabatan the jabatan to set
	 */
	public void setJabatan(String jabatan) {
		this.jabatan = jabatan;
	}

	/**
	 * @return the nrp
	 */
	public String getNrp() {
		return nrp;
	}

	/**
	 * @param nrp the nrp to set
	 */
	public void setNrp(String nrp) {
		this.nrp = nrp;
	}

}
