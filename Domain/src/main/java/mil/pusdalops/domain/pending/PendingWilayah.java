package mil.pusdalops.domain.pending;

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
import mil.pusdalops.domain.wilayah.Propinsi;

@Entity
@Table(name = "pending_wilayah", schema = SchemaUtil.SCHEMA_COMMON)
public class PendingWilayah extends IdBasedObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9073931353060080581L;

	// `serial_number_id_fk` bigint(20) DEFAULT NULL,
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "serial_number_id_fk")
	private DocumentSerialNumber serialNumber;
	
	// `tw_pembuatan_datetime` timestamp NULL DEFAULT NULL,
	@Column(name = "tw_pembuatan_datetime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date twPembuatanDateTime;

	// `tw_pembuatan_timezone` int(11) DEFAULT NULL,
	@Column(name = "tw_pembuatan_timezone")
	@Enumerated(EnumType.ORDINAL)
	private TimezoneInd twPembuatanTimezone;
	
	// `status` int(11) DEFAULT NULL,
	@Column(name = "status")
	@Enumerated(EnumType.ORDINAL)
	private PendingStatus pendingStatus;
	
	// kejadian_id bigint(20)
	@Column(name = "kejadian_id")
	private Long kejadianId;
	
	// `kotamaops_id_fk` bigint(20) DEFAULT NULL,
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "kotamaops_id_fk")	
	private Kotamaops kotamaops;
	
	// `propinsi_id_fk` bigint(20) DEFAULT NULL,
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "propinsi_id_fk")	
	private Propinsi propinsi;
	
	// `nama_kabupaten_kotamadya` varchar(255) DEFAULT NULL,
	@Column(name = "nama_kabupaten_kotamadya")
	private String namaKabupatenKotamadya;
	
	// `nama_kecamatan` varchar(255) DEFAULT NULL,
	@Column(name = "nama_kecamatan")
	private String namaKecamatan;
	
	// `nama_kelurahan` varchar(255) DEFAULT NULL,
	@Column(name = "nama_kelurahan")
	private String namaKelurahan;

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PendingWilayah [twPembuatanDateTime=" + twPembuatanDateTime + ", twPembuatanTimezone="
				+ twPembuatanTimezone + ", namaKabupatenKotamadya=" + namaKabupatenKotamadya + ", namaKecamatan="
				+ namaKecamatan + ", namaKelurahan=" + namaKelurahan + "]";
	}

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
	 * @return the pendingStatus
	 */
	public PendingStatus getPendingStatus() {
		return pendingStatus;
	}

	/**
	 * @param pendingStatus the pendingStatus to set
	 */
	public void setPendingStatus(PendingStatus pendingStatus) {
		this.pendingStatus = pendingStatus;
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
	 * @return the propinsi
	 */
	public Propinsi getPropinsi() {
		return propinsi;
	}

	/**
	 * @param propinsi the propinsi to set
	 */
	public void setPropinsi(Propinsi propinsi) {
		this.propinsi = propinsi;
	}

	/**
	 * @return the namaKabupatenKotamadya
	 */
	public String getNamaKabupatenKotamadya() {
		return namaKabupatenKotamadya;
	}

	/**
	 * @param namaKabupatenKotamadya the namaKabupatenKotamadya to set
	 */
	public void setNamaKabupatenKotamadya(String namaKabupatenKotamadya) {
		this.namaKabupatenKotamadya = namaKabupatenKotamadya;
	}

	/**
	 * @return the namaKecamatan
	 */
	public String getNamaKecamatan() {
		return namaKecamatan;
	}

	/**
	 * @param namaKecamatan the namaKecamatan to set
	 */
	public void setNamaKecamatan(String namaKecamatan) {
		this.namaKecamatan = namaKecamatan;
	}

	/**
	 * @return the namaKelurahan
	 */
	public String getNamaKelurahan() {
		return namaKelurahan;
	}

	/**
	 * @param namaKelurahan the namaKelurahan to set
	 */
	public void setNamaKelurahan(String namaKelurahan) {
		this.namaKelurahan = namaKelurahan;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();		
		result = prime * result + ((namaKabupatenKotamadya == null) ? 0 : namaKabupatenKotamadya.hashCode());
		result = prime * result + ((namaKecamatan == null) ? 0 : namaKecamatan.hashCode());
		result = prime * result + ((namaKelurahan == null) ? 0 : namaKelurahan.hashCode());
		result = prime * result + ((twPembuatanDateTime == null) ? 0 : twPembuatanDateTime.hashCode());
		result = prime * result + ((twPembuatanTimezone == null) ? 0 : twPembuatanTimezone.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		PendingWilayah other = (PendingWilayah) obj;
		if (this.getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!this.getId().equals(other.getId()))
			return false;
		if (this.getVersion() == null) {
			if (other.getVersion() != null)
				return false;
		} else if (!this.getVersion().equals(other.getVersion()))
			return false;
		if (namaKabupatenKotamadya == null) {
			if (other.namaKabupatenKotamadya != null)
				return false;
		} else if (!namaKabupatenKotamadya.equals(other.namaKabupatenKotamadya))
			return false;
		if (namaKecamatan == null) {
			if (other.namaKecamatan != null)
				return false;
		} else if (!namaKecamatan.equals(other.namaKecamatan))
			return false;
		if (namaKelurahan == null) {
			if (other.namaKelurahan != null)
				return false;
		} else if (!namaKelurahan.equals(other.namaKelurahan))
			return false;
		if (twPembuatanDateTime == null) {
			if (other.twPembuatanDateTime != null)
				return false;
		} else if (!twPembuatanDateTime.equals(other.twPembuatanDateTime))
			return false;
		if (twPembuatanTimezone != other.twPembuatanTimezone)
			return false;
		return true;
	}

	public Long getKejadianId() {
		return kejadianId;
	}

	public void setKejadianId(Long kejadianId) {
		this.kejadianId = kejadianId;
	}
	
}
