package mil.pusdalops.persistence.kejadian.rekap.dao;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import mil.pusdalops.domain.kejadian.Kejadian;
import mil.pusdalops.domain.kerugian.Pihak;
import mil.pusdalops.domain.kerugian.TipeKerugian;
import mil.pusdalops.domain.kotamaops.Kotamaops;
import mil.pusdalops.domain.wilayah.Kabupaten_Kotamadya;
import mil.pusdalops.domain.wilayah.Kecamatan;
import mil.pusdalops.domain.wilayah.Kelurahan;
import mil.pusdalops.domain.wilayah.Propinsi;

public interface KejadianRekapDao {

	public BigInteger countKerugianByTipe(TipeKerugian tipeKerugian, 
			Pihak pihak, LocalDateTime twAkhir, LocalDateTime twAwal) throws Exception;

	public BigInteger countKerugianByTipe(TipeKerugian tipeKerugian, 
			Kotamaops kotamaops, Pihak pihak, LocalDateTime twAkhir, LocalDateTime twAwal) throws Exception;

	public BigInteger countKerugianByTipe(TipeKerugian tipeKerugian, 
			Kotamaops kotamaops, Propinsi propinsi, 
			Pihak pihak, LocalDateTime twAkhir, LocalDateTime twAwal) throws Exception;

	public BigInteger countKerugianByTipe(TipeKerugian tipeKerugian, 
			Kotamaops kotamaops, Propinsi propinsi, Kabupaten_Kotamadya kabupatenKot,
			Pihak pihak, LocalDateTime twAkhir, LocalDateTime twAwal) throws Exception;

	public BigInteger countKerugianByTipe(TipeKerugian tipeKerugian, 
			Kotamaops kotamaops, Propinsi propinsi,	Kabupaten_Kotamadya kabupatenKot, 
			Kecamatan kecamatan, 
			Pihak pihak, LocalDateTime twAkhir, LocalDateTime twAwal) throws Exception;

	public BigInteger countKerugianByTipe(TipeKerugian tipeKerugian, 
			Kotamaops kotamaops, Propinsi propinsi,	Kabupaten_Kotamadya kabupatenKot, 
			Kecamatan kecamatan, Kelurahan kelurahan, 
			Pihak pihak, LocalDateTime twAkhir, LocalDateTime twAwal) throws Exception;

	public List<Kejadian> findAllKejadian(Date twAkhir, Date twAwal) throws Exception;

	public List<Kejadian> findAllKejadian(
			Kotamaops kotamaops, 
			Date twAkhir, Date twAwal) throws Exception;

	public List<Kejadian> findAllKejadian(
			Kotamaops kotamaops, Propinsi propinsi, 
			Date twAkhir, Date twAwal) throws Exception;

	public List<Kejadian> findAllKejadian(
			Kotamaops kotamaops, Propinsi propinsi, Kabupaten_Kotamadya kabupatenKot,
			Date twAkhir, Date twAwal) throws Exception;

	public List<Kejadian> findAllKejadian(
			Kotamaops kotamaops, Propinsi propinsi, Kabupaten_Kotamadya kabupatenKot,
			Kecamatan kecamatan, 
			Date twAkhir, Date twAwal) throws Exception;

	public List<Kejadian> findAllKejadian(
			Kotamaops kotamaops, Propinsi propinsi, Kabupaten_Kotamadya kabupatenKot,
			Kecamatan kecamatan, Kelurahan kelurahan, 
			Date twAkhir, Date twAwal) throws Exception;


}
