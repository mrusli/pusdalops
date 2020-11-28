package mil.pusdalops.webui.kronologis;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import mil.pusdalops.domain.kejadian.Kejadian;
import mil.pusdalops.persistence.kejadian.dao.KejadianDao;
import mil.pusdalops.webui.common.GFCBaseController;

public class KejadianKronologisViewDialogControl extends GFCBaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7553460883015426866L;

	private KejadianDao kejadianDao;
	
	private Window kejadianKronologisViewDialogWin;
	private Textbox kejadianIdTextbox, twBuatTahunTextbox, twBuatTanggalJamTextbox,
		twJadiTahunTextbox, twJadiTanggalJamTextbox, twJadiTimeZoneTextbox, koordGpsTextbox,
		koordPetaTextbox, bujurLintangTextbox, kampungTextbox, jalanTextbox, keteranganPelakuTextbox,
		sasaranTextbox, kronologisTextbox;
	private Label idLabel;
	private Combobox twBuatTimeZoneCombobox, kotamaopsCombobox, propCombobox, kabupatenCombobox,
		kecamatanCombobox, kelurahanCombobox, jenisKejadianCombobox, motifKejadianCombobox,
		pelakuKejadianCombobox;
	
	private Kejadian kejadian;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		setKejadian((Kejadian) arg.get("selectedKejadian"));
	}

	public void onCreate$kejadianKronologisViewDialogWin(Event event) throws Exception {
		// display
		displayKejadianData();
	}

	private void displayKejadianData() throws Exception {
		kejadianIdTextbox.setValue(getKejadian().getSerialNumber().getSerialComp());
		idLabel.setValue(String.valueOf(getKejadian().getId()));
		twBuatTahunTextbox.setValue(
				getLocalDateTimeString(asLocalDateTime(getKejadian().getTwPembuatanDateTime()), "YYYY"));
		twBuatTanggalJamTextbox.setValue(
				getLocalDateTimeString(asLocalDateTime(getKejadian().getTwPembuatanDateTime()), "MMdd")+"."+
				getLocalDateTimeString(asLocalDateTime(getKejadian().getTwPembuatanDateTime()), "hhmm"));
		twBuatTimeZoneCombobox.setValue(getKejadian().getTwPembuatanTimezone().toString());

		twJadiTahunTextbox.setValue(
				getLocalDateTimeString(asLocalDateTime(getKejadian().getTwKejadianDateTime()), "YYYY"));
		twJadiTanggalJamTextbox.setValue(
				getLocalDateTimeString(asLocalDateTime(getKejadian().getTwKejadianDateTime()), "MMdd")+"."+
				getLocalDateTimeString(asLocalDateTime(getKejadian().getTwKejadianDateTime()), "hhmm"));
		twJadiTimeZoneTextbox.setValue(getKejadian().getTwKejadianTimezone().toString());
		
		Kejadian kejadianKotamaopsByProxy = getKejadianDao().findKejadianKotamaopsByProxy(getKejadian().getId());
		kotamaopsCombobox.setValue(kejadianKotamaopsByProxy.getKotamaops().getKotamaopsName());
		
		Kejadian kejadianProprinsiByProxy = getKejadianDao().findKejadianPropinsiByProxy(getKejadian().getId());
		propCombobox.setValue(kejadianProprinsiByProxy.getPropinsi().getNamaPropinsi());
		
		Kejadian kejadianKabKotByProxy = getKejadianDao().findKejadianKabupatenByProxy(getKejadian().getId());
		kabupatenCombobox.setValue(kejadianKabKotByProxy.getKabupatenKotamadya().getNamaKabupaten());
		
		Kejadian kejadianKecamatanByProxy = getKejadianDao().findKejadianKecamatanByProxy(getKejadian().getId());
		kecamatanCombobox.setValue(kejadianKecamatanByProxy.getKecamatan().getNamaKecamatan());
		
		Kejadian kejadianKelurahanByProxy = getKejadianDao().findKejadianKelurahanByProxy(getKejadian().getId());
		kelurahanCombobox.setValue(kejadianKelurahanByProxy.getKelurahan().getNamaKelurahan());
		
		koordGpsTextbox.setValue(getKejadian().getKoordinatGps());
		koordPetaTextbox.setValue(getKejadian().getKoordinatPeta());
		bujurLintangTextbox.setValue(getKejadian().getBujurLintang());
		
		kampungTextbox.setValue(getKejadian().getKampung());
		jalanTextbox.setValue(getKejadian().getJalan());
		
		jenisKejadianCombobox.setValue(getKejadian().getJenisKejadian().getNamaJenis());
		motifKejadianCombobox.setValue(getKejadian().getMotifKejadian().getNamaMotif());
		pelakuKejadianCombobox.setValue(getKejadian().getPelakuKejadian().getNamaPelaku());
		keteranganPelakuTextbox.setValue(getKejadian().getKeteranganPelaku());
		sasaranTextbox.setValue(getKejadian().getSasaran());
		
		kronologisTextbox.setValue(getKejadian().getKronologis());
	}

	public void onClick$closeButton(Event event) throws Exception {
		kejadianKronologisViewDialogWin.detach();
	}
	
	public Kejadian getKejadian() {
		return kejadian;
	}

	public void setKejadian(Kejadian kejadian) {
		this.kejadian = kejadian;
	}

	public KejadianDao getKejadianDao() {
		return kejadianDao;
	}

	public void setKejadianDao(KejadianDao kejadianDao) {
		this.kejadianDao = kejadianDao;
	}
}
