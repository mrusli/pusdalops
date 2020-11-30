package mil.pusdalops.persistence.laporanrutin.dao;

import java.util.Date;
import java.util.List;

import mil.pusdalops.domain.kejadian.Kejadian;
import mil.pusdalops.domain.kejadian.KejadianJenis;
import mil.pusdalops.domain.kotamaops.Kotamaops;

public interface LaporanRutinDao {

	public List<Kejadian> findAllKejadian(Date twAwal, Date twAkhir);

	public List<Kejadian> findAllKejadianByKotamaops(Kotamaops kotamaops, Date twAwal, Date twAkhir);

	public List<Kejadian> findAllKejadianByKejadianJenis(KejadianJenis kejadianJenis, Date twAwal, Date twAkhir);

	public List<Kejadian> findAllKejadianByKotamaopsByKejadianJenis(Kotamaops kotamaops, KejadianJenis kejadianJenis, Date twAwal, Date twAkhir);

	public Kejadian findKejadianKotamaopsByProxy(Long id) throws Exception;

	public Kejadian findKejadianPropinsiByProxy(Long id) throws Exception;

	public Kejadian findKejadianKabupatenKotByProxy(Long id) throws Exception;

	public List<Kejadian> findAllKejadian() throws Exception;

	public List<Kejadian> findAllKejadianInKotamaops(List<Kotamaops> kotamaopsList, Date twAwal, Date twAkhir) throws Exception;

}
