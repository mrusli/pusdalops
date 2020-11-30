package mil.pusdalops.webui.laporanrutin;

public class KejadianPrintData {
	
	private String namaKotamaops;
	
	private int noUrt;
	
	private int noKejadianKotamaops;
	
	private String tw;
	
	private String uraianKejadian;
	
	private String jenisKejadian;
	
	@Override
	public String toString() {
		return "KejadianPrintData [namaKotamaops=" + namaKotamaops + ", noUrt=" + noUrt + ", noKejadianKotamaops="
				+ noKejadianKotamaops + ", tw=" + tw + ", uraianKejadian=" + uraianKejadian + ", jenisKejadian="
				+ jenisKejadian + "]";
	}

	public String getNamaKotamaops() {
		return namaKotamaops;
	}

	public void setNamaKotamaops(String namaKotamaops) {
		this.namaKotamaops = namaKotamaops;
	}

	public int getNoUrt() {
		return noUrt;
	}

	public void setNoUrt(int noUrt) {
		this.noUrt = noUrt;
	}

	public int getNoKejadianKotamaops() {
		return noKejadianKotamaops;
	}

	public void setNoKejadianKotamaops(int noKejadianKotamaops) {
		this.noKejadianKotamaops = noKejadianKotamaops;
	}

	public String getTw() {
		return tw;
	}

	public void setTw(String tw) {
		this.tw = tw;
	}

	public String getUraianKejadian() {
		return uraianKejadian;
	}

	public void setUraianKejadian(String uraianKejadian) {
		this.uraianKejadian = uraianKejadian;
	}

	public String getJenisKejadian() {
		return jenisKejadian;
	}

	public void setJenisKejadian(String jenisKejadian) {
		this.jenisKejadian = jenisKejadian;
	}

}
