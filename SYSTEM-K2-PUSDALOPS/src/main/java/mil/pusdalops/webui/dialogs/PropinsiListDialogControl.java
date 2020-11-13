package mil.pusdalops.webui.dialogs;

import java.util.List;

import org.zkoss.zk.ui.event.Event;
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
	
	private List<Propinsi> propinsiList;
	
	public void onCreate$propinsiListDialogWin(Event event) throws Exception {

		// load propinsi list
		loadPropinsiList();
		
		// display propinsi list
		displayPropinsiList();
	}

	private void loadPropinsiList() throws Exception {
		setPropinsiList(
				getPropinsiDao().findAllPropinsi());
		
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
				
				// image
				lc = new Listcell();
				lc.setParent(item);
				
				// Nama Propinsi
				lc = new Listcell(propinsi.getNamaPropinsi());
				lc.setParent(item);
				
				item.setValue(propinsi);
			}
		};
	}
	
	public void onClick$selectButton(Event event) throws Exception {
		
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
}
