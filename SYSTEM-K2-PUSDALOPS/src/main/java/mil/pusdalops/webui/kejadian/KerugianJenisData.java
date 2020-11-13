package mil.pusdalops.webui.kejadian;

import mil.pusdalops.domain.kerugian.TipeKerugian;

public class KerugianJenisData {
	
	private String namaKerugianJenis;
	
	private TipeKerugian tipeKerugian;

	public TipeKerugian getTipeKerugian() {
		return tipeKerugian;
	}

	public void setTipeKerugian(TipeKerugian tipeKerugian) {
		this.tipeKerugian = tipeKerugian;
	}

	public String getNamaKerugianJenis() {
		return namaKerugianJenis;
	}

	public void setNamaKerugianJenis(String namaKerugianJenis) {
		this.namaKerugianJenis = namaKerugianJenis;
	}
	
}
