package mil.pusdalops.webui.settings.kotamaprops;

import java.util.Comparator;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Window;

import mil.pusdalops.domain.wilayah.Propinsi;
import mil.pusdalops.persistence.propinsi.dao.PropinsiDao;
import mil.pusdalops.webui.common.GFCBaseController;

public class PropinsiListDialogControl extends GFCBaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7398174681062574774L;

	private PropinsiDao propinsiDao;
	
	private Window propinsiListDialogWin;
	private Listbox propinsiListbox;
	
	private List<Propinsi> propinsiList, propinsiToExclude;
	private PropinsiListDialogData propinsiListDialogData;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		setPropinsiListDialogData(
				(PropinsiListDialogData) arg.get("dialogData"));
	}

	public void onCreate$propinsiListDialogWin(Event event) throws Exception {
		setPropinsiToExclude(
				getPropinsiListDialogData().getPropinsisToExclude());
		
		// load propinsi list
		loadPropinsiList();
		
		// display propinsi list
		displayPropinsiList();
	}

	@SuppressWarnings("unchecked")
	private void loadPropinsiList() throws Exception {
		// load propinsi to add (not listed)
		setPropinsiList((List<Propinsi>) getItemsNotInTheList(getPropinsiToExclude(), getPropinsiDao().findAllPropinsi()));
		// sort
		getPropinsiList().sort(Comparator.comparing(Propinsi::getNamaPropinsi));
	}

	private void displayPropinsiList() {
		propinsiListbox.setModel(
				new ListModelList<Propinsi>(getPropinsiList()));
		propinsiListbox.setItemRenderer(getPropinsiListitemRenderer());
		
	}

	private ListitemRenderer<Propinsi> getPropinsiListitemRenderer() {

		return new ListitemRenderer<Propinsi>() {
			
			@Override
			public void render(Listitem item, Propinsi propinsi, int index) throws Exception {
				Listcell lc;
				
				// Nama Propinsi
				lc = new Listcell(propinsi.getNamaPropinsi());
				lc.setParent(item);
				
				item.setValue(propinsi);
			}
		};
	}
	
	public void onClick$selectButton(Event event) throws Exception {
		if (propinsiListbox.getSelectedItem()==null) {
			throw new Exception("Propinsi belum terpilih");
		}
		// get selected propinsi
		Propinsi selProp = propinsiListbox.getSelectedItem().getValue();
		// send event
		Events.sendEvent(Events.ON_CHANGE, propinsiListDialogWin, selProp);
		
		propinsiListDialogWin.detach();
	}
	
	public void onClick$cancelButton(Event event) throws Exception {
		
		propinsiListDialogWin.detach();
	}

	/**
	 * @return the propinsiDao
	 */
	public PropinsiDao getPropinsiDao() {
		return propinsiDao;
	}

	/**
	 * @param propinsiDao the propinsiDao to set
	 */
	public void setPropinsiDao(PropinsiDao propinsiDao) {
		this.propinsiDao = propinsiDao;
	}

	/**
	 * @return the propinsiList
	 */
	public List<Propinsi> getPropinsiList() {
		return propinsiList;
	}

	/**
	 * @param propinsiList the propinsiList to set
	 */
	public void setPropinsiList(List<Propinsi> propinsiList) {
		this.propinsiList = propinsiList;
	}

	public List<Propinsi> getPropinsiToExclude() {
		return propinsiToExclude;
	}

	public void setPropinsiToExclude(List<Propinsi> propinsiToExclude) {
		this.propinsiToExclude = propinsiToExclude;
	}

	public PropinsiListDialogData getPropinsiListDialogData() {
		return propinsiListDialogData;
	}

	public void setPropinsiListDialogData(PropinsiListDialogData propinsiListDialogData) {
		this.propinsiListDialogData = propinsiListDialogData;
	}
}
