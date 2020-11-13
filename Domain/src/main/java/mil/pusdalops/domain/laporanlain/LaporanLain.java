package mil.pusdalops.domain.laporanlain;

import java.sql.Time;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import mil.pusdalops.domain.common.IdBasedObject;
import mil.pusdalops.domain.common.SchemaUtil;
import mil.pusdalops.domain.gmt.TimezoneInd;
import mil.pusdalops.domain.kotamaops.Kotamaops;
import mil.pusdalops.domain.serial.DocumentSerialNumber;

@Entity
@Table(name = "laporan_lain", schema = SchemaUtil.SCHEMA_COMMON)
public class LaporanLain extends IdBasedObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5431323808114304426L;

	//  `serial_number_id_fk` bigint(20) DEFAULT NULL,
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "serial_number_id_fk")
	private DocumentSerialNumber serialNumber;
	
	//  `tw_pembuatan_datetime` timestamp NULL DEFAULT NULL,
	@Column(name = "tw_pembuatan_datetime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date twPembuatanDateTime;

	//  `tw_pembuatan_time` time DEFAULT NULL,
	@Column(name = "tw_pembuatan_time")
	private Time twPembuatanTime;
	
	//  `tw_pembuatan_timezone` int(11) DEFAULT NULL,
	@Column(name = "tw_pembuatan_timezone")
	@Enumerated(EnumType.ORDINAL)
	private TimezoneInd twPembuatanTimezone;
	
	//  `tw` char(9) DEFAULT NULL,
	@Column(name = "tw")
	private String tw;
	
	//  `tahun` char(4) DEFAULT NULL,
	@Column(name = "tahun")
	private String tahun;
	
	//  `kotamaops_id_fk` bigint(20) DEFAULT NULL,
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "kotamaops_id_fk")
	private Kotamaops kotamaops;
	
	//  `judul_laporan` varchar(255) DEFAULT NULL,
	@Column(name = "judul_laporan")
	private String judulLaporan;
	
	//  `laporan` varchar(1020) DEFAULT NULL,
	@Column(name = "laporan")
	private String isiLaporan;
	
	//  `klasifikasi` varchar(255) DEFAULT NULL,
	@Column(name = "klasifikasi")
	private String klasifikasi;

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
	 * @return the twPembuatanDateTime
	 */
	public Date getTwPembuatanDateTime() {
		return twPembuatanDateTime;
	}

	/**
	 * @param twPembuatanDateTime the twPembuatanDateTime to set
	 */
	public void setTwPembuatanDateTime(Date twPembuatanDateTime) {
		this.twPembuatanDateTime = twPembuatanDateTime;
	}

	/**
	 * @return the twPembuatanTime
	 */
	public Time getTwPembuatanTime() {
		return twPembuatanTime;
	}

	/**
	 * @param twPembuatanTime the twPembuatanTime to set
	 */
	public void setTwPembuatanTime(Time twPembuatanTime) {
		this.twPembuatanTime = twPembuatanTime;
	}

	/**
	 * @return the twPembuatanTimezone
	 */
	public TimezoneInd getTwPembuatanTimezone() {
		return twPembuatanTimezone;
	}

	/**
	 * @param twPembuatanTimezone the twPembuatanTimezone to set
	 */
	public void setTwPembuatanTimezone(TimezoneInd twPembuatanTimezone) {
		this.twPembuatanTimezone = twPembuatanTimezone;
	}

	/**
	 * @return the tw
	 */
	public String getTw() {
		return tw;
	}

	/**
	 * @param tw the tw to set
	 */
	public void setTw(String tw) {
		this.tw = tw;
	}

	/**
	 * @return the tahun
	 */
	public String getTahun() {
		return tahun;
	}

	/**
	 * @param tahun the tahun to set
	 */
	public void setTahun(String tahun) {
		this.tahun = tahun;
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
	 * @return the judulLaporan
	 */
	public String getJudulLaporan() {
		return judulLaporan;
	}

	/**
	 * @param judulLaporan the judulLaporan to set
	 */
	public void setJudulLaporan(String judulLaporan) {
		this.judulLaporan = judulLaporan;
	}

	/**
	 * @return the isiLaporan
	 */
	public String getIsiLaporan() {
		return isiLaporan;
	}

	/**
	 * @param isiLaporan the isiLaporan to set
	 */
	public void setIsiLaporan(String isiLaporan) {
		this.isiLaporan = isiLaporan;
	}

	/**
	 * @return the klasifikasi
	 */
	public String getKlasifikasi() {
		return klasifikasi;
	}

	/**
	 * @param klasifikasi the klasifikasi to set
	 */
	public void setKlasifikasi(String klasifikasi) {
		this.klasifikasi = klasifikasi;
	}
}
