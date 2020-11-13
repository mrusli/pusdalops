package mil.pusdalops.webui.pejabat;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import mil.pusdalops.domain.kotamaops.Kotamaops;
import mil.pusdalops.domain.kotamaops.KotamaopsType;
import mil.pusdalops.domain.pejabat.Pejabat;
import mil.pusdalops.persistence.kotamaops.dao.KotamaopsDao;
import mil.pusdalops.persistence.pejabat.dao.PejabatDao;
import mil.pusdalops.webui.common.GFCBaseController;

public class PejabatDialogWinControl extends GFCBaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4930947116597866526L;

	private KotamaopsDao kotamaopsDao;
	private PejabatDao pejabatDao;
	
	private Window pejabatDialogWin;
	private Combobox kotamaopsCombobox;
	private Textbox namaTextbox, pangkatTextbox, jabatanTextbox, 
		nrpTextbox;
	
	private PejabatData pejabatData;
	private Pejabat pejabat;
	private Kotamaops kotamaops;
	private ZoneId zoneId;
	private LocalDateTime currentLocalDateTime;
	
	private static final Logger log = Logger.getLogger(PejabatDialogWinControl.class);
	
	/* (non-Javadoc)
	 * @see org.zkoss.zk.ui.util.GenericForwardComposer#doAfterCompose(org.zkoss.zk.ui.Component)
	 */
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		setPejabatData(
				(PejabatData) arg.get("pejabatData"));
	}

	public void onCreate$pejabatDialogWin(Event event) throws Exception {
		log.info("PejabatDialogWinControl create...");
		
		setPejabat(
				getPejabatData().getPejabat());
		setKotamaops(
				getPejabatData().getKotamaops());
		
		// set the timezone for this kotamaops
		int timezoneOrdinal = getKotamaops().getTimeZone().getValue();
		setZoneId(getKotamaops().getTimeZone().toZoneId(timezoneOrdinal));							
		
		setCurrentLocalDateTime(getLocalDateTime(getZoneId()));
		
		loadKotamaopsCombobox();
		
		displayDataPejabat();
	}

	private void loadKotamaopsCombobox() throws Exception {
		Comboitem comboitem;

		if (getKotamaops().getKotamaopsType().compareTo(KotamaopsType.PUSDALOPS)==0) {
			// load all kotamaops
			List<Kotamaops> kotamaopsList = getKotamaopsDao().findAllKotamaops();	
			for (Kotamaops kotamaops : kotamaopsList) {
				comboitem = new Comboitem();
				comboitem.setLabel(kotamaops.getKotamaopsName());
				comboitem.setValue(kotamaops);
				comboitem.setParent(kotamaopsCombobox);
			}
			kotamaopsCombobox.setSelectedIndex(0);
		} else {
			// use this kotamaops -- nothing to select
			comboitem = new Comboitem();
			comboitem.setLabel(getKotamaops().getKotamaopsName());
			comboitem.setValue(getKotamaops());
			comboitem.setParent(kotamaopsCombobox);
			
			kotamaopsCombobox.setSelectedItem(comboitem);
			kotamaopsCombobox.setDisabled(true);
		}
	}

	private void displayDataPejabat() throws Exception {
		if (getPejabat().getId().compareTo(Long.MIN_VALUE)==0) {
			// new
			// do nothing
		} else {
			// existing data -- display
			
			// kotamaops
			Pejabat pejabatKotamaopsByProxy = 
					getPejabatDao().findPejabatKotamaopsByProxy(getPejabat().getId());
			for (Comboitem comboitem : kotamaopsCombobox.getItems()) {
				if (pejabatKotamaopsByProxy.getKotamaops().getKotamaopsName().compareTo(comboitem.getLabel())==0) {
					kotamaopsCombobox.setSelectedItem(comboitem);
					break;
				}
			}
			namaTextbox.setValue(getPejabat().getNama());
			pangkatTextbox.setValue(getPejabat().getPangkat());
			jabatanTextbox.setValue(getPejabat().getJabatan());
			nrpTextbox.setValue(getPejabat().getNrp());
		}
	}

	public void onClick$saveButton(Event event) throws Exception {
		if (namaTextbox.getValue().isEmpty()) {
			throw new Exception("Nama belum diisi");
		}
		if (pangkatTextbox.getValue().isEmpty()) {
			throw new Exception("Pangkat belum diisi");
		}
		if (jabatanTextbox.getValue().isEmpty()) {
			throw new Exception("Jabatan belum diisi");
		}
		if (nrpTextbox.getValue().isEmpty()) {
			throw new Exception("NRP belum diisi");
		}
		
		Pejabat modPejabat = getPejabat();
		
		Comboitem comboitem = kotamaopsCombobox.getSelectedItem();
		modPejabat.setKotamaops(comboitem.getValue());
		modPejabat.setCreatedAt(asDate(getCurrentLocalDateTime()));
		modPejabat.setEditedAt(asDate(getCurrentLocalDateTime()));
		modPejabat.setNama(namaTextbox.getValue());
		modPejabat.setPangkat(pangkatTextbox.getValue());
		modPejabat.setJabatan(jabatanTextbox.getValue());
		modPejabat.setNrp(nrpTextbox.getValue());
		
		// send event
		Events.sendEvent(Events.ON_OK, pejabatDialogWin, modPejabat);
		
		// detach
		pejabatDialogWin.detach();
	}
	
	public void onClick$cancelButton(Event event) throws Exception {
		// detach
		pejabatDialogWin.detach();
	}
	
	public PejabatData getPejabatData() {
		return pejabatData;
	}

	public void setPejabatData(PejabatData pejabatData) {
		this.pejabatData = pejabatData;
	}

	public Pejabat getPejabat() {
		return pejabat;
	}

	public void setPejabat(Pejabat pejabat) {
		this.pejabat = pejabat;
	}

	public Kotamaops getKotamaops() {
		return kotamaops;
	}

	public void setKotamaops(Kotamaops kotamaops) {
		this.kotamaops = kotamaops;
	}

	public KotamaopsDao getKotamaopsDao() {
		return kotamaopsDao;
	}

	public void setKotamaopsDao(KotamaopsDao kotamaopsDao) {
		this.kotamaopsDao = kotamaopsDao;
	}

	public PejabatDao getPejabatDao() {
		return pejabatDao;
	}

	public void setPejabatDao(PejabatDao pejabatDao) {
		this.pejabatDao = pejabatDao;
	}

	public ZoneId getZoneId() {
		return zoneId;
	}

	public void setZoneId(ZoneId zoneId) {
		this.zoneId = zoneId;
	}

	public LocalDateTime getCurrentLocalDateTime() {
		return currentLocalDateTime;
	}

	public void setCurrentLocalDateTime(LocalDateTime currentLocalDateTime) {
		this.currentLocalDateTime = currentLocalDateTime;
	}

}
