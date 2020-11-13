package mil.pusdalops.webui.settings.kotamaops;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import mil.pusdalops.domain.gmt.TimezoneInd;
import mil.pusdalops.domain.kotamaops.Kotamaops;
import mil.pusdalops.domain.kotamaops.KotamaopsType;
import mil.pusdalops.webui.common.GFCBaseController;

public class KotamaopsDialogControl extends GFCBaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8781989113391554204L;

	private Window kotamaopsDialogWin;
	private Combobox zonaWaktuCombobox;
	private Textbox namaKotamaopsTextbox, alamat01Textbox, alamat02Textbox,
		kotaTextbox, postalCodeTextbox, phoneTextbox, emailTextbox, faxTextbox,
		kodeTextbox;
	
	private Kotamaops kotamaops;
	
	/* (non-Javadoc)
	 * @see org.zkoss.zk.ui.util.GenericForwardComposer#doAfterCompose(org.zkoss.zk.ui.Component)
	 */
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		// kotamaops
		setKotamaops(
				(Kotamaops) Executions.getCurrent().getArg().get("kotamaops"));
	}	
	
	public void onCreate$kotamaopsDialogWin(Event event) throws Exception {
		if (getKotamaops().getId().compareTo(Long.MIN_VALUE)==0) {
			// new - create
			kotamaopsDialogWin.setTitle("Membuat Kotamaops");
		} else {
			// existing - edit
			kotamaopsDialogWin.setTitle("Merubah Kotamaops");
		}
		
		// load TimezoneInd
		loadTimezoneInd();
		
		// display
		displayKotamaopsInfo();
	}

	private void loadTimezoneInd() {
		Comboitem comboitem;
		for (TimezoneInd timezoneInd : TimezoneInd.values()) {
			comboitem = new Comboitem();
			comboitem.setLabel(timezoneInd.toString());
			comboitem.setValue(timezoneInd);
			comboitem.setParent(zonaWaktuCombobox);
		}
		
	}

	private void displayKotamaopsInfo() {
		if (getKotamaops().getId().compareTo(Long.MIN_VALUE)==0) {
			// new - do nothing
		} else {
			// existing - display data
			namaKotamaopsTextbox.setValue(getKotamaops().getKotamaopsName()); 
			alamat01Textbox.setValue(getKotamaops().getAddress01());
			alamat02Textbox.setValue(getKotamaops().getAddress02());
			kotaTextbox.setValue(getKotamaops().getCity());
			postalCodeTextbox.setValue(getKotamaops().getPostalCode());
			phoneTextbox.setValue(getKotamaops().getPhone());
			emailTextbox.setValue(getKotamaops().getEmail());
			faxTextbox.setValue(getKotamaops().getFax());
			kodeTextbox.setValue(getKotamaops().getDocumentCode());
			for (Comboitem comboitem : zonaWaktuCombobox.getItems()) {
				if (comboitem.getValue().equals(getKotamaops().getTimeZone())) {
					zonaWaktuCombobox.setSelectedItem(comboitem);
				}
			}			
		}
	}

	public void onClick$saveButton(Event event) throws Exception {
		// get the changes
		Kotamaops modKotamaops = getKotamaops();
		modKotamaops.setKotamaopsName(namaKotamaopsTextbox.getValue());
		modKotamaops.setAddress01(alamat01Textbox.getValue());
		modKotamaops.setAddress02(alamat02Textbox.getValue());
		modKotamaops.setCity(kotaTextbox.getValue());
		modKotamaops.setPostalCode(postalCodeTextbox.getValue());
		modKotamaops.setPhone(phoneTextbox.getValue());
		modKotamaops.setEmail(emailTextbox.getValue());
		modKotamaops.setFax(faxTextbox.getValue());
		modKotamaops.setDocumentCode(kodeTextbox.getValue());
		modKotamaops.setTimeZone(zonaWaktuCombobox.getSelectedItem().getValue());
		// created in PUSAT ONLY
		modKotamaops.setCreatedAt(asDate(getLocalDateTime()));
		modKotamaops.setEditedAt(asDate(getLocalDateTime()));
		// PUSAT creates OTHERS kotamaops
		modKotamaops.setKotamaopsType(KotamaopsType.OTHERS);
		
		// send event with changes in the object
		Events.sendEvent(Events.ON_OK, kotamaopsDialogWin, modKotamaops);
		
		kotamaopsDialogWin.detach();
	}
	
	public void onClick$cancelButton(Event event) throws Exception {
		
		kotamaopsDialogWin.detach();
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

}
