package mil.pusdalops.persistence.kejadian.rekap.dao;

import java.math.BigInteger;
import java.time.LocalDateTime;

import mil.pusdalops.domain.kejadian.KejadianMotif;
import mil.pusdalops.domain.kejadian.KejadianPelaku;
import mil.pusdalops.domain.kotamaops.Kotamaops;
import mil.pusdalops.domain.wilayah.Kabupaten_Kotamadya;
import mil.pusdalops.domain.wilayah.Kecamatan;
import mil.pusdalops.domain.wilayah.Kelurahan;
import mil.pusdalops.domain.wilayah.Propinsi;

public interface KejadianRekapPelakuMotifDao {

	public BigInteger countKejadian(LocalDateTime twAwal, LocalDateTime twAkhir);

	public BigInteger countKejadian(Kotamaops kotamaops, LocalDateTime twAwal, LocalDateTime twAkhir);

	public BigInteger countKejadian(Kotamaops value, Propinsi propinsi, LocalDateTime twAwal,
			LocalDateTime twAkhir);

	public BigInteger countKejadian(Kotamaops kotamaops, Propinsi propinsi, Kabupaten_Kotamadya kabupatenKot,
			LocalDateTime twAwal, LocalDateTime twAkhir);

	public BigInteger countKejadian(Kotamaops kotamaops, Propinsi propinsi, Kabupaten_Kotamadya kabupatenKot, 
			Kecamatan kecamatan, LocalDateTime twAwal, LocalDateTime twAkhir);

	public BigInteger countKejadian(Kotamaops kotamaops, Propinsi propinsi, Kabupaten_Kotamadya kabupatenKot, 
			Kecamatan kecamatan, Kelurahan kelurahan, LocalDateTime twAwal, LocalDateTime twAkhir);

	public BigInteger countPelakuKejadian(KejadianPelaku kejadianPelaku, 
			LocalDateTime twAwal, LocalDateTime twAkhir);

	public BigInteger countPelakuKejadian(KejadianPelaku kejadianPelaku, Kotamaops kotamaops,
			LocalDateTime twAwal, LocalDateTime twAkhir);

	public BigInteger countPelakuKejadian(KejadianPelaku kejadianPelaku, Kotamaops kotamaops,
			Propinsi propinsi, LocalDateTime twAwal, LocalDateTime twAkhir);
	
	public BigInteger countPelakuKejadian(KejadianPelaku kejadianPelaku, Kotamaops kotamaops,
			Propinsi propinsi, Kabupaten_Kotamadya kabupatentKot, LocalDateTime twAwal, LocalDateTime twAkhir);
	
	public BigInteger countPelakuKejadian(KejadianPelaku kejadianPelaku, Kotamaops kotamaops,
			Propinsi propinsi, Kabupaten_Kotamadya kabupatentKot, Kecamatan kecamatan, 
			LocalDateTime twAwal, LocalDateTime twAkhir);
	
	public BigInteger countPelakuKejadian(KejadianPelaku kejadianPelaku, Kotamaops kotamaops,
			Propinsi propinsi, Kabupaten_Kotamadya kabupatentKot, Kecamatan kecamatan, 
			Kelurahan kelurahan, LocalDateTime twAwal, LocalDateTime twAkhir);

	public BigInteger countMotifKejadian(KejadianMotif motifKejadian, LocalDateTime twAwal,
			LocalDateTime twAkhir);

	public BigInteger countMotifKejadian(KejadianMotif motifKejadian, Kotamaops kotamaops,
			LocalDateTime twAwal, LocalDateTime twAkhir);

	public BigInteger countMotifKejadian(KejadianMotif motifKejadian, Kotamaops kotamaops,
			Propinsi propinsi, LocalDateTime twAwal, LocalDateTime twAkhir);

	public BigInteger countMotifKejadian(KejadianMotif motifKejadian, Kotamaops kotamaops,
			Propinsi propinsi, Kabupaten_Kotamadya kabupatenKot, LocalDateTime twAwal, 
			LocalDateTime twAkhir);

	public BigInteger countMotifKejadian(KejadianMotif motifKejadian, Kotamaops kotamaops,
			Propinsi propinsi, Kabupaten_Kotamadya kabupatenKot, Kecamatan kecamatan, 
			LocalDateTime twAwal, LocalDateTime twAkhir);
	
	public BigInteger countMotifKejadian(KejadianMotif motifKejadian, Kotamaops kotamaops,
			Propinsi propinsi, Kabupaten_Kotamadya kabupatenKot, Kecamatan kecamatan, 
			Kelurahan kelurahan, LocalDateTime twAwal, LocalDateTime twAkhir);

}
