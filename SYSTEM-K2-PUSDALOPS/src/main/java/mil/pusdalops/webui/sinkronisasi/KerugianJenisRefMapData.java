package mil.pusdalops.webui.sinkronisasi;

import mil.pusdalops.domain.kerugian.KerugianJenis;

public class KerugianJenisRefMapData {

	private KerugianJenis kerugianJenis;
	
	private int rowIndex;

	public KerugianJenis getKerugianJenis() {
		return kerugianJenis;
	}

	public void setKerugianJenis(KerugianJenis kerugianJenis) {
		this.kerugianJenis = kerugianJenis;
	}

	public int getRowIndex() {
		return rowIndex;
	}

	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
	}
	
}
