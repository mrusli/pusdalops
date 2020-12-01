package mil.pusdalops.persistence.kejadian.rekap.dao;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import mil.pusdalops.domain.kejadian.Kejadian;
import mil.pusdalops.domain.kejadian.KejadianJenis;
import mil.pusdalops.domain.kejadian.KejadianMotif;
import mil.pusdalops.domain.kotamaops.Kotamaops;
import mil.pusdalops.domain.wilayah.Kabupaten_Kotamadya;
import mil.pusdalops.domain.wilayah.Kecamatan;
import mil.pusdalops.domain.wilayah.Kelurahan;
import mil.pusdalops.domain.wilayah.Propinsi;

public interface KejadianRekapMotifDao {

	public BigInteger countAllKejadian(LocalDateTime twAwal, LocalDateTime twAkhir) throws Exception;

	public BigInteger countKejadianInKotamaopsList(List<Kotamaops> kotamaopsList, LocalDateTime twAwal, LocalDateTime twAkhir) throws Exception;
	
	public BigInteger countKejadian(Kotamaops kotamaops, 
			LocalDateTime twAwal, LocalDateTime twAkhir);

	public BigInteger countKejadian(Kotamaops kotamaops, Propinsi propinsi, 
			LocalDateTime twAwal, LocalDateTime twAkhir);

	public BigInteger countKejadian(Kotamaops kotamaops, Propinsi propinsi, 
			Kabupaten_Kotamadya kabupatenKot,
			LocalDateTime twAwal, LocalDateTime twAkhir);

	public BigInteger countKejadian(Kotamaops kotamaops, Propinsi propinsi, 
			Kabupaten_Kotamadya kabupatenKot, Kecamatan kecamatan,
			LocalDateTime twAwal, LocalDateTime twAkhir);

	public BigInteger countKejadian(Kotamaops kotamaops, Propinsi propinsi, 
			Kabupaten_Kotamadya kabupatenKot, Kecamatan kecamatan,
			Kelurahan kelurahan,
			LocalDateTime twAwal, LocalDateTime twAkhir);

	public List<Kejadian> findDistinctKejadianByJenisKejadian(Date twAwal,
			Date twAkhir);

	public List<Kejadian> findDistinctKejadianByJenisKejadianInKotamaopsList(
			List<Kotamaops> kotamaopsList, Date twAwal, Date twAkhir) throws Exception;
	
	public List<Kejadian> findDistinctKejadianByJenisKejadian(Kotamaops kotamaops, 
			Date twAwal, Date twAkhir);
	
	public List<Kejadian> findDistinctKejadianByJenisKejadian(Kotamaops kotamaops, 
			Propinsi propinsi, Date twAwal, Date twAkhir);

	public List<Kejadian> findDistinctKejadianByJenisKejadian(Kotamaops kotamaops, 
			Propinsi propinsi, Kabupaten_Kotamadya kabupatenKot, Date twAwal, Date twAkhir);
	
	public List<Kejadian> findDistinctKejadianByJenisKejadian(Kotamaops kotamaops, 
			Propinsi propinsi, Kabupaten_Kotamadya kabupatenKot, Kecamatan kecamatan,
			Date twAwal, Date twAkhir);
	
	public List<Kejadian> findDistinctKejadianByJenisKejadian(Kotamaops kotamaops, 
			Propinsi propinsi, Kabupaten_Kotamadya kabupatenKot, Kecamatan kecamatan,
			Kelurahan kelurahan, Date twAwal, Date twAkhir);
	
	public BigInteger countJenisKejadian(KejadianJenis jenisKejadian, LocalDateTime twAwal,
			LocalDateTime twAkhir);

	public BigInteger countJenisKejadian(KejadianJenis jenisKejadian, Kotamaops kotamaops,
			LocalDateTime twAwal, LocalDateTime twAkhir);
	
	public BigInteger countJenisKejadian(KejadianJenis jenisKejadian, Kotamaops kotamaops,
			Propinsi propinsi, LocalDateTime twAwal, LocalDateTime twAkhir);
	
	public BigInteger countJenisKejadian(KejadianJenis jenisKejadian, Kotamaops kotamaops,
			Propinsi propinsi, Kabupaten_Kotamadya kabupatenKot, LocalDateTime twAwal, 
			LocalDateTime twAkhir);
	
	public BigInteger countJenisKejadian(KejadianJenis jenisKejadian, Kotamaops kotamaops,
			Propinsi propinsi, Kabupaten_Kotamadya kabupatenKot, Kecamatan kecamatan,
			LocalDateTime twAwal, LocalDateTime twAkhir);

	public BigInteger countJenisKejadian(KejadianJenis jenisKejadian, Kotamaops kotamaops,
			Propinsi propinsi, Kabupaten_Kotamadya kabupatenKot, Kecamatan kecamatan,
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

	public BigInteger countJenisKejadianInKotamaops(List<Kotamaops> kotamaopsList, KejadianJenis jenisKejadian,
			LocalDateTime twAwal, LocalDateTime twAkhir) throws Exception;
	
}
