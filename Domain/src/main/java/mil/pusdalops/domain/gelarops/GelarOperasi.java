package mil.pusdalops.domain.gelarops;

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
@Table(name = "gelar_operasi", schema = SchemaUtil.SCHEMA_COMMON)
public class GelarOperasi extends IdBasedObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7130935702106807253L;

	//  `serial_number_id_fk` bigint(20) DEFAULT NULL,
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "serial_number_id_fk")	
	private DocumentSerialNumber serialNumber;
	
	//  `tw_awal_ops` timestamp NULL DEFAULT NULL,
	@Column(name = "tw_awal_ops")
	@Temporal(TemporalType.TIMESTAMP)
	private Date twAwalOps;
	
	//  `tahun_awal_ops` char(4) DEFAULT NULL,
	@Column(name = "tahun_awal_ops")
	private String tahunAwalOps;
	
	//  `tw_awal` char(9) DEFAULT NULL,
	@Column(name = "tw_awal")
	private String twAwal;
	
	//  `tw_akhir_ops` timestamp NULL DEFAULT NULL,
	@Column(name = "tw_akhir_ops")
	@Temporal(TemporalType.TIMESTAMP)
	private Date twAkhirOps;
	
	//  `tahun_akhir_ops` char(4) DEFAULT NULL,
	@Column(name = "tahun_akhir_ops")
	private String tahunAkhirOps;
	
	//  `tw_akhir` char(9) DEFAULT NULL,
	@Column(name = "tw_akhir")
	private String twAkhir;
	
	//  `tw_timezone` int(11) DEFAULT NULL,
	@Column(name = "tw_timezone")
	@Enumerated(EnumType.ORDINAL)
	private TimezoneInd twTimezone;
	
	//  `disposisi` varchar(255) DEFAULT NULL,
	@Column(name = "disposisi")
	private String disposisi;
	
	//  `satuan` varchar(255) DEFAULT NULL,
	@Column(name = "satuan")
	private String satuan;
	
	//  `brigade` varchar(255) DEFAULT NULL,
	@Column(name = "brigade")
	private String brigade;
	
	//  `batalyon` varchar(255) DEFAULT NULL,
	@Column(name = "batalyon")
	private String batalyon;
	
	//  `kotamaops_id_fk` bigint(20) DEFAULT NULL,
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "kotamaops_id_fk")
	private Kotamaops kotamaops;
	
	//  `kolak` varchar(255) DEFAULT NULL,
	@Column(name = "kolak")
	private String kolak;
	
	//  `bujur` varchar(255) DEFAULT NULL,
	@Column(name = "bujur")
	private String bujur;
	
	//  `lintang` varchar(255) DEFAULT NULL,
	@Column(name = "lintang")
	private String lintang;
	
	//  `kv` varchar(255) DEFAULT NULL,
	@Column(name = "kv")
	private String kv;
	
	//  `co_pilot` varchar(255) DEFAULT NULL,
	@Column(name = "co_pilot")
	private String coPilot;
	
	//  `visi` varchar(255) DEFAULT NULL,
	@Column(name = "visi")
	private String visi;
	
	//  `misi` varchar(255) DEFAULT NULL,
	@Column(name = "misi")
	private String misi;
	
	//  `komandan` varchar(255) DEFAULT NULL,
	@Column(name = "komandan")
	private String komandan;
	
	//  `abk` varchar(255) DEFAULT NULL,
	@Column(name = "abk")
	private String abk;
	
	//  `jumlah_personil` varchar(255) DEFAULT NULL,
	@Column(name = "jumlah_personil")
	private String jumlahPersonil;
	
	//  `jumlah_senjata` varchar(255) DEFAULT NULL,
	@Column(name = "jumlah_senjata")
	private String jumlahSenjata;
	
	//  `jenis_senjata` varchar(255) DEFAULT NULL,
	@Column(name = "jenis_senjata")
	private String jenisSenjata;
	
	//  `jumlah_amunisi` varchar(255) DEFAULT NULL,
	@Column(name = "jumlah_amunisi")
	private String jumlahAmunisi;
	
	//  `jenis_amunisi` varchar(255) DEFAULT NULL,
	@Column(name = "jenis_amunisi")
	private String jenisAmunisi;
	
	//  `bahan_bakar` varchar(255) DEFAULT NULL,
	@Column(name = "bahan_bakar")
	private String bahanBakar;
	
	//  `air_tawar` varchar(255) DEFAULT NULL,
	@Column(name = "air_tawar")
	private String airTawar;
	
	//  `minyak_lincir` varchar(255) DEFAULT NULL,
	@Column(name = "minyak_lincir")
	private String minyakLincir;
	
	//  `jenis_ops` varchar(255) DEFAULT NULL,
	@Column(name = "jenis_ops")
	private String jenisOps;
	
	//  `nama` varchar(255) DEFAULT NULL,
	@Column(name = "nama")
	private String nama;
	
	//  `alkom` varchar(255) DEFAULT NULL,
	@Column(name = "alkom")
	private String alkom;
	
	//  `ran` varchar(255) DEFAULT NULL,
	@Column(name = "ran")
	private String ran;
	
	//  `halu` varchar(255) DEFAULT NULL,
	@Column(name = "halu")
	private String halu;
	
	//  `cepat` varchar(255) DEFAULT NULL,
	@Column(name = "cepat")
	private String cepat;
	
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
	 * @return the twAwalOps
	 */
	public Date getTwAwalOps() {
		return twAwalOps;
	}

	/**
	 * @param twAwalOps the twAwalOps to set
	 */
	public void setTwAwalOps(Date twAwalOps) {
		this.twAwalOps = twAwalOps;
	}

	/**
	 * @return the tahunAwalOps
	 */
	public String getTahunAwalOps() {
		return tahunAwalOps;
	}

	/**
	 * @param tahunAwalOps the tahunAwalOps to set
	 */
	public void setTahunAwalOps(String tahunAwalOps) {
		this.tahunAwalOps = tahunAwalOps;
	}

	/**
	 * @return the twAwal
	 */
	public String getTwAwal() {
		return twAwal;
	}

	/**
	 * @param twAwal the twAwal to set
	 */
	public void setTwAwal(String twAwal) {
		this.twAwal = twAwal;
	}

	/**
	 * @return the twAkhirOps
	 */
	public Date getTwAkhirOps() {
		return twAkhirOps;
	}

	/**
	 * @param twAkhirOps the twAkhirOps to set
	 */
	public void setTwAkhirOps(Date twAkhirOps) {
		this.twAkhirOps = twAkhirOps;
	}

	/**
	 * @return the tahunAkhirOps
	 */
	public String getTahunAkhirOps() {
		return tahunAkhirOps;
	}

	/**
	 * @param tahunAkhirOps the tahunAkhirOps to set
	 */
	public void setTahunAkhirOps(String tahunAkhirOps) {
		this.tahunAkhirOps = tahunAkhirOps;
	}

	/**
	 * @return the twAkhir
	 */
	public String getTwAkhir() {
		return twAkhir;
	}

	/**
	 * @param twAkhir the twAkhir to set
	 */
	public void setTwAkhir(String twAkhir) {
		this.twAkhir = twAkhir;
	}

	/**
	 * @return the twTimezone
	 */
	public TimezoneInd getTwTimezone() {
		return twTimezone;
	}

	/**
	 * @param twTimezone the twTimezone to set
	 */
	public void setTwTimezone(TimezoneInd twTimezone) {
		this.twTimezone = twTimezone;
	}

	/**
	 * @return the disposisi
	 */
	public String getDisposisi() {
		return disposisi;
	}

	/**
	 * @param disposisi the disposisi to set
	 */
	public void setDisposisi(String disposisi) {
		this.disposisi = disposisi;
	}

	/**
	 * @return the satuan
	 */
	public String getSatuan() {
		return satuan;
	}

	/**
	 * @param satuan the satuan to set
	 */
	public void setSatuan(String satuan) {
		this.satuan = satuan;
	}

	/**
	 * @return the brigade
	 */
	public String getBrigade() {
		return brigade;
	}

	/**
	 * @param brigade the brigade to set
	 */
	public void setBrigade(String brigade) {
		this.brigade = brigade;
	}

	/**
	 * @return the batalyon
	 */
	public String getBatalyon() {
		return batalyon;
	}

	/**
	 * @param batalyon the batalyon to set
	 */
	public void setBatalyon(String batalyon) {
		this.batalyon = batalyon;
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
	 * @return the kolak
	 */
	public String getKolak() {
		return kolak;
	}

	/**
	 * @param kolak the kolak to set
	 */
	public void setKolak(String kolak) {
		this.kolak = kolak;
	}

	/**
	 * @return the bujur
	 */
	public String getBujur() {
		return bujur;
	}

	/**
	 * @param bujur the bujur to set
	 */
	public void setBujur(String bujur) {
		this.bujur = bujur;
	}

	/**
	 * @return the lintang
	 */
	public String getLintang() {
		return lintang;
	}

	/**
	 * @param lintang the lintang to set
	 */
	public void setLintang(String lintang) {
		this.lintang = lintang;
	}

	/**
	 * @return the kv
	 */
	public String getKv() {
		return kv;
	}

	/**
	 * @param kv the kv to set
	 */
	public void setKv(String kv) {
		this.kv = kv;
	}

	/**
	 * @return the coPilot
	 */
	public String getCoPilot() {
		return coPilot;
	}

	/**
	 * @param coPilot the coPilot to set
	 */
	public void setCoPilot(String coPilot) {
		this.coPilot = coPilot;
	}

	/**
	 * @return the visi
	 */
	public String getVisi() {
		return visi;
	}

	/**
	 * @param visi the visi to set
	 */
	public void setVisi(String visi) {
		this.visi = visi;
	}

	/**
	 * @return the misi
	 */
	public String getMisi() {
		return misi;
	}

	/**
	 * @param misi the misi to set
	 */
	public void setMisi(String misi) {
		this.misi = misi;
	}

	/**
	 * @return the komandan
	 */
	public String getKomandan() {
		return komandan;
	}

	/**
	 * @param komandan the komandan to set
	 */
	public void setKomandan(String komandan) {
		this.komandan = komandan;
	}

	/**
	 * @return the abk
	 */
	public String getAbk() {
		return abk;
	}

	/**
	 * @param abk the abk to set
	 */
	public void setAbk(String abk) {
		this.abk = abk;
	}

	/**
	 * @return the jumlahPersonil
	 */
	public String getJumlahPersonil() {
		return jumlahPersonil;
	}

	/**
	 * @param jumlahPersonil the jumlahPersonil to set
	 */
	public void setJumlahPersonil(String jumlahPersonil) {
		this.jumlahPersonil = jumlahPersonil;
	}

	/**
	 * @return the jumlahSenjata
	 */
	public String getJumlahSenjata() {
		return jumlahSenjata;
	}

	/**
	 * @param jumlahSenjata the jumlahSenjata to set
	 */
	public void setJumlahSenjata(String jumlahSenjata) {
		this.jumlahSenjata = jumlahSenjata;
	}

	/**
	 * @return the jenisSenjata
	 */
	public String getJenisSenjata() {
		return jenisSenjata;
	}

	/**
	 * @param jenisSenjata the jenisSenjata to set
	 */
	public void setJenisSenjata(String jenisSenjata) {
		this.jenisSenjata = jenisSenjata;
	}

	/**
	 * @return the jumlahAmunisi
	 */
	public String getJumlahAmunisi() {
		return jumlahAmunisi;
	}

	/**
	 * @param jumlahAmunisi the jumlahAmunisi to set
	 */
	public void setJumlahAmunisi(String jumlahAmunisi) {
		this.jumlahAmunisi = jumlahAmunisi;
	}

	/**
	 * @return the jenisAmunisi
	 */
	public String getJenisAmunisi() {
		return jenisAmunisi;
	}

	/**
	 * @param jenisAmunisi the jenisAmunisi to set
	 */
	public void setJenisAmunisi(String jenisAmunisi) {
		this.jenisAmunisi = jenisAmunisi;
	}

	/**
	 * @return the bahanBakar
	 */
	public String getBahanBakar() {
		return bahanBakar;
	}

	/**
	 * @param bahanBakar the bahanBakar to set
	 */
	public void setBahanBakar(String bahanBakar) {
		this.bahanBakar = bahanBakar;
	}

	/**
	 * @return the airTawar
	 */
	public String getAirTawar() {
		return airTawar;
	}

	/**
	 * @param airTawar the airTawar to set
	 */
	public void setAirTawar(String airTawar) {
		this.airTawar = airTawar;
	}

	/**
	 * @return the minyakLincir
	 */
	public String getMinyakLincir() {
		return minyakLincir;
	}

	/**
	 * @param minyakLincir the minyakLincir to set
	 */
	public void setMinyakLincir(String minyakLincir) {
		this.minyakLincir = minyakLincir;
	}

	/**
	 * @return the jenisOps
	 */
	public String getJenisOps() {
		return jenisOps;
	}

	/**
	 * @param jenisOps the jenisOps to set
	 */
	public void setJenisOps(String jenisOps) {
		this.jenisOps = jenisOps;
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
	 * @return the alkom
	 */
	public String getAlkom() {
		return alkom;
	}

	/**
	 * @param alkom the alkom to set
	 */
	public void setAlkom(String alkom) {
		this.alkom = alkom;
	}

	/**
	 * @return the ran
	 */
	public String getRan() {
		return ran;
	}

	/**
	 * @param ran the ran to set
	 */
	public void setRan(String ran) {
		this.ran = ran;
	}

	/**
	 * @return the halu
	 */
	public String getHalu() {
		return halu;
	}

	/**
	 * @param halu the halu to set
	 */
	public void setHalu(String halu) {
		this.halu = halu;
	}

	/**
	 * @return the cepat
	 */
	public String getCepat() {
		return cepat;
	}

	/**
	 * @param cepat the cepat to set
	 */
	public void setCepat(String cepat) {
		this.cepat = cepat;
	}
}
