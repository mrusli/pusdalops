package mil.pusdalops.domain.kejadian;

import java.sql.Time;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

import mil.pusdalops.domain.common.IdBasedObject;
import mil.pusdalops.domain.common.SchemaUtil;
import mil.pusdalops.domain.gmt.TimezoneInd;
import mil.pusdalops.domain.kerugian.Kerugian;
import mil.pusdalops.domain.kotamaops.Kotamaops;
import mil.pusdalops.domain.serial.DocumentSerialNumber;
import mil.pusdalops.domain.wilayah.Kabupaten_Kotamadya;
import mil.pusdalops.domain.wilayah.Kecamatan;
import mil.pusdalops.domain.wilayah.Kelurahan;
import mil.pusdalops.domain.wilayah.Propinsi;

/**
 * 
 * @author rusli
 *
 */
@Entity
@Indexed
@Table(name = "kejadian", schema = SchemaUtil.SCHEMA_COMMON)
public class Kejadian extends IdBasedObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4582143025513714680L;

	//  KEY `key_document_serial_no_01` (`serial_number_id_fk`),
	//  CONSTRAINT `fk_document_serial_no_01` FOREIGN KEY (`serial_number_id_fk`) REFERENCES `document_serial_number` (`id`),
	//  `serial_number_id_fk` bigint(20) DEFAULT NULL,
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "serial_number_id_fk")
	private DocumentSerialNumber serialNumber;
	
	//  `tw_pembuatan_datetime` timestamp DEFAULT NULL,
	@Column(name = "tw_pembuatan_datetime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date twPembuatanDateTime;
	
	//	`tw_pembuatan_time` time DEFAULT NULL,
	@Column(name = "tw_pembuatan_time")
	private Time twPembuatanTime;
	
	//	`tw_pembuatan_timezone` INT(11) DEFAULT NULL
	@Column(name = "tw_pembuatan_timezone")
	@Enumerated(EnumType.ORDINAL)
	private TimezoneInd twPembuatanTimezone;
		
	//  `tw_kejadian_datetime` timestamp DEFAULT NULL,
	@Column(name = "tw_kejadian_datetime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date twKejadianDateTime;

	//	`tw_kejadian_time` time DEFAULT NULL,
	@Column(name = "tw_kejadian_time")
	private Time twKejadianTime;
	
	//	`tw_kejadian_timezone` INT(11) DEFAULT NULL
	@Column(name = "tw_kejadian_timezone")
	@Enumerated(EnumType.ORDINAL)
	private TimezoneInd twKejadianTimezone;

	//  `kotamaops_id_fk` bigint(20) DEFAULT NULL,
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "kotamaops_id_fk")
	private Kotamaops kotamaops;

	//  `propinsi_id_fk` bigint(20) DEFAULT NULL,
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "propinsi_id_fk")
	private Propinsi propinsi;
	
	//  `kabupaten_id_fk` bigint(20) DEFAULT NULL,
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "kabupaten_id_fk")
	private Kabupaten_Kotamadya kabupatenKotamadya;
	
	//	`kecamatan_id_fk` bigint(20) DEFAUL NULL,
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "kecamatan_id_fk")
	private Kecamatan kecamatan;
	
	//  `kelurahan_id_fk` bigint(20) DEFAULT NULL,
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "kelurahan_id_fk")
	private Kelurahan kelurahan;
	
	//  `koord_gps` varchar(255) DEFAULT NULL,
	@Column(name = "koord_gps")
	private String koordinatGps;
	
	//  `koor_peta` varchar(255) DEFAULT NULL,
	@Column(name = "koor_peta")
	private String koordinatPeta;
	
	//	`bujur_lintang` VARCHAR(255) NULL
	@Column(name = "bujur_lintang")
	private String bujurLintang;
	
	//  `kampung` varchar(255) DEFAULT NULL,
	@Column(name = "kampung")
	private String kampung;
	
	//  `jalan` varchar(255) DEFAULT NULL,
	@Column(name = "jalan")
	private String jalan;
		 
	//  `kronologis` varchar(1020) DEFAULT NULL,
	@Column(name = "kronologis")
	// @Convert(converter = AttributeEncryptor.class)
	@Field(index=Index.YES, analyze=Analyze.YES, store=Store.NO)
	private String kronologis;
	
	//  `jenis_kejadian_id_fk` bigint(20) DEFAULT NULL,
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "jenis_kejadian_id_fk")
	private KejadianJenis jenisKejadian;
	
	//  `motif_kejadian_id_fk` bigint(20) DEFAULT NULL,
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "motif_kejadian_id_fk")
	private KejadianMotif motifKejadian;
	
	//  `pelaku_kejadian_id_fk` bigint(20) DEFAULT NULL,
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "pelaku_kejadian_id_fk")
	private KejadianPelaku pelakuKejadian;
	
	//  `keterangan_pelaku` varchar(255) DEFAULT NULL,
	@Column(name = "keterangan_pelaku")
	private String keteranganPelaku;
	
	//  `sasaran` varchar(255) DEFAULT NULL,
	@Column(name = "sasaran")
	private String sasaran;

	// one to many Kerugian - join table
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval=true, fetch=FetchType.LAZY)
	@JoinTable(
			name = "kejadian_join_kerugian",
			joinColumns = @JoinColumn(name = "id_kejadian"),
			inverseJoinColumns = @JoinColumn(name = "id_kerugian"))
	private List<Kerugian> kerugians;	
	
	// `synch_datetime` datetime DEFAULT NULL,
	@Column(name = "synch_datetime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date synchAt;

	/*
	 * cloud ONLY
	 * 
	 */
	// `synch_by_kotamaops` bigint(20)
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "synch_by_kotamaops")
	private Kotamaops synchByKotamaops;
	
	// `kotamaops_synch_datetime` datetime
	@Column(name = "kotamaops_synch_datetime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date kotamaopsSynchAt;
	
	// `pusdalops_synch_datetime` datetime
	@Column(name = "pusdalops_synch_datetime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date pusdalopsSynchAt;
	
	/**
	 * @return the serialNumber
	 */
	public DocumentSerialNumber getSerialNumber() {
		return serialNumber;
	}

	/**
	 * @param serialNumber the serialNumber to set
	 */
	public void setSerialNumber(DocumentSerialNumber serialNumber) {
		this.serialNumber = serialNumber;
	}

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
	 * @return the kronologis
	 */
	public String getKronologis() {
		return kronologis;
	}

	/**
	 * @param kronologis the kronologis to set
	 */
	public void setKronologis(String kronologis) {
		this.kronologis = kronologis;
	}

	/**
	 * @return the jenisKejadian
	 */
	public KejadianJenis getJenisKejadian() {
		return jenisKejadian;
	}

	/**
	 * @param jenisKejadian the jenisKejadian to set
	 */
	public void setJenisKejadian(KejadianJenis jenisKejadian) {
		this.jenisKejadian = jenisKejadian;
	}

	/**
	 * @return the motifKejadian
	 */
	public KejadianMotif getMotifKejadian() {
		return motifKejadian;
	}

	/**
	 * @param motifKejadian the motifKejadian to set
	 */
	public void setMotifKejadian(KejadianMotif motifKejadian) {
		this.motifKejadian = motifKejadian;
	}

	/**
	 * @return the pelakuKejadian
	 */
	public KejadianPelaku getPelakuKejadian() {
		return pelakuKejadian;
	}

	/**
	 * @param pelakuKejadian the pelakuKejadian to set
	 */
	public void setPelakuKejadian(KejadianPelaku pelakuKejadian) {
		this.pelakuKejadian = pelakuKejadian;
	}

	/**
	 * @return the keteranganPelaku
	 */
	public String getKeteranganPelaku() {
		return keteranganPelaku;
	}

	/**
	 * @param keteranganPelaku the keteranganPelaku to set
	 */
	public void setKeteranganPelaku(String keteranganPelaku) {
		this.keteranganPelaku = keteranganPelaku;
	}

	/**
	 * @return the sasaran
	 */
	public String getSasaran() {
		return sasaran;
	}

	/**
	 * @param sasaran the sasaran to set
	 */
	public void setSasaran(String sasaran) {
		this.sasaran = sasaran;
	}

	public Propinsi getPropinsi() {
		return propinsi;
	}

	public void setPropinsi(Propinsi propinsi) {
		this.propinsi = propinsi;
	}

	public Kabupaten_Kotamadya getKabupatenKotamadya() {
		return kabupatenKotamadya;
	}

	public void setKabupatenKotamadya(Kabupaten_Kotamadya kabupatenKotamadya) {
		this.kabupatenKotamadya = kabupatenKotamadya;
	}

	public Kelurahan getKelurahan() {
		return kelurahan;
	}

	public void setKelurahan(Kelurahan kelurahan) {
		this.kelurahan = kelurahan;
	}

	public String getKoordinatGps() {
		return koordinatGps;
	}

	public void setKoordinatGps(String koordinatGps) {
		this.koordinatGps = koordinatGps;
	}

	public String getKoordinatPeta() {
		return koordinatPeta;
	}

	public void setKoordinatPeta(String koordinatPeta) {
		this.koordinatPeta = koordinatPeta;
	}

	public String getKampung() {
		return kampung;
	}

	public void setKampung(String kampung) {
		this.kampung = kampung;
	}

	public String getJalan() {
		return jalan;
	}

	public void setJalan(String jalan) {
		this.jalan = jalan;
	}

	public String getBujurLintang() {
		return bujurLintang;
	}

	public void setBujurLintang(String bujurLintang) {
		this.bujurLintang = bujurLintang;
	}

	public TimezoneInd getTwPembuatanTimezone() {
		return twPembuatanTimezone;
	}

	public void setTwPembuatanTimezone(TimezoneInd twPembuatanTimezone) {
		this.twPembuatanTimezone = twPembuatanTimezone;
	}

	public TimezoneInd getTwKejadianTimezone() {
		return twKejadianTimezone;
	}

	public void setTwKejadianTimezone(TimezoneInd twKejadianTimezone) {
		this.twKejadianTimezone = twKejadianTimezone;
	}

	public Time getTwPembuatanTime() {
		return twPembuatanTime;
	}

	public void setTwPembuatanTime(Time twPembuatanTime) {
		this.twPembuatanTime = twPembuatanTime;
	}

	public Time getTwKejadianTime() {
		return twKejadianTime;
	}

	public void setTwKejadianTime(Time twKejadianTime) {
		this.twKejadianTime = twKejadianTime;
	}

	public Date getTwPembuatanDateTime() {
		return twPembuatanDateTime;
	}

	public void setTwPembuatanDateTime(Date twPembuatanDateTime) {
		this.twPembuatanDateTime = twPembuatanDateTime;
	}

	public Date getTwKejadianDateTime() {
		return twKejadianDateTime;
	}

	public void setTwKejadianDateTime(Date twKejadianDateTime) {
		this.twKejadianDateTime = twKejadianDateTime;
	}

	public List<Kerugian> getKerugians() {
		return kerugians;
	}

	public void setKerugians(List<Kerugian> kerugians) {
		this.kerugians = kerugians;
	}

	/**
	 * @return the kecamatan
	 */
	public Kecamatan getKecamatan() {
		return kecamatan;
	}

	/**
	 * @param kecamatan the kecamatan to set
	 */
	public void setKecamatan(Kecamatan kecamatan) {
		this.kecamatan = kecamatan;
	}

	public Date getSynchAt() {
		return synchAt;
	}

	public void setSynchAt(Date synchAt) {
		this.synchAt = synchAt;
	}

	public Date getKotamaopsSynchAt() {
		return kotamaopsSynchAt;
	}

	public void setKotamaopsSynchAt(Date kotamaopsSynchAt) {
		this.kotamaopsSynchAt = kotamaopsSynchAt;
	}

	public Date getPusdalopsSynchAt() {
		return pusdalopsSynchAt;
	}

	public void setPusdalopsSynchAt(Date pusdalopsSynchAt) {
		this.pusdalopsSynchAt = pusdalopsSynchAt;
	}

	public Kotamaops getSynchByKotamaops() {
		return synchByKotamaops;
	}

	public void setSynchByKotamaops(Kotamaops synchByKotamaops) {
		this.synchByKotamaops = synchByKotamaops;
	}
	
}
