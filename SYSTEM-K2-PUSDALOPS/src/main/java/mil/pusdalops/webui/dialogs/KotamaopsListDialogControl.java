package mil.pusdalops.webui.dialogs;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Window;

import mil.pusdalops.domain.kotamaops.Kotamaops;
import mil.pusdalops.domain.kotamaops.KotamaopsType;
import mil.pusdalops.persistence.kotamaops.dao.KotamaopsDao;
import mil.pusdalops.webui.common.GFCBaseController;

public class KotamaopsListDialogControl extends GFCBaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5939921477577060748L;

	private KotamaopsDao kotamaopsDao;
	
	private Window kotamaopsListDialogWin;
	private Listbox kotamaopsListbox;
	
	private List<Kotamaops> kotamaopsList;
	private List<Kotamaops> kotamaopsListToExclude;
	private KotamaopsListDialogData dialogData;
	private KotamaopsType kotamaopsType;
	
	// private final int KOTAMAOPS_PUST_IDX = 0;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		setDialogData(
				(KotamaopsListDialogData) arg.get("dialogData"));
	}
	
	@SuppressWarnings("unchecked")
	public void onCreate$kotamaopsListDialogWin(Event event) throws Exception {
		setKotamaopsType(
				getDialogData().getKotamaopsType());

		setKotamaopsListToExclude(
				getDialogData().getKotamaopsToExclude());
		
		if (getKotamaopsListToExclude().isEmpty()) {
			// load data
			setKotamaopsList(getKotamaopsDao().findAllKotamaops());
		} else {
			// init data
			setKotamaopsList((List<Kotamaops>) getItemsNotInTheList(getKotamaopsListToExclude(), getKotamaopsDao().findAllKotamaops()));			
			// exclude pusdalops
			Kotamaops kotamaopsPusdalops = null;
			for (Kotamaops kotamaops : kotamaopsList) {
				if (kotamaops.getKotamaopsType().compareTo(KotamaopsType.PUSDALOPS)==0) {
					kotamaopsPusdalops = kotamaops;
				}
			}
			getKotamaopsList().remove(kotamaopsPusdalops);
		}				
		displayKotamaopsList();
	}
	
	private void displayKotamaopsList() {
		kotamaopsListbox.setModel(
				new ListModelList<Kotamaops>(getKotamaopsList()));
		kotamaopsListbox.setItemRenderer(getKotamaopsListitemRenderer());
		
	}

	private ListitemRenderer<Kotamaops> getKotamaopsListitemRenderer() {

		return new ListitemRenderer<Kotamaops>() {
			
			BufferedImage buffImg = null;
			
			@Override
			public void render(Listitem item, Kotamaops kotamaops, int index) throws Exception {
				Listcell lc;
				
				item.setClass("expandedRow");

				try {
				    buffImg = ImageIO.read(new File("/pusdalops/img/logo/"+kotamaops.getImagedId()));
				} catch (IOException e) {
					throw e;
				}				
				
				// image logo
				lc = new Listcell();
				lc.setImageContent(buffImg);
				// lc.setImage("/img/logo/"+kotamaops.getImagedId());
				lc.setStyle("text-align: center;");
				lc.setParent(item);
				
				// Nama Kotamaops
				lc = new Listcell(kotamaops.getKotamaopsName());
				lc.setParent(item);
				
				item.setValue(kotamaops);
				
			}
		};
	}

	public void onClick$selectButton(Event event) throws Exception {
		if (kotamaopsListbox.getSelectedItem()==null) {
			throw new Exception("Kotamaops Belum Terpilih.");
		}
		Kotamaops selKotamaops = kotamaopsListbox.getSelectedItem().getValue();
		
		Events.sendEvent(Events.ON_CHANGE, kotamaopsListDialogWin, selKotamaops);
		
		kotamaopsListDialogWin.detach();
	}
	
	public void onClick$cancelButton(Event event) throws Exception {
		kotamaopsListDialogWin.detach();
	}

	public KotamaopsDao getKotamaopsDao() {
		return kotamaopsDao;
	}

	public void setKotamaopsDao(KotamaopsDao kotamaopsDao) {
		this.kotamaopsDao = kotamaopsDao;
	}

	public List<Kotamaops> getKotamaopsList() {
		return kotamaopsList;
	}

	public void setKotamaopsList(List<Kotamaops> kotamaopsList) {
		this.kotamaopsList = kotamaopsList;
	}

	public KotamaopsType getKotamaopsType() {
		return kotamaopsType;
	}

	public void setKotamaopsType(KotamaopsType kotamaopsType) {
		this.kotamaopsType = kotamaopsType;
	}

	public KotamaopsListDialogData getDialogData() {
		return dialogData;
	}

	public void setDialogData(KotamaopsListDialogData dialogData) {
		this.dialogData = dialogData;
	}

	public List<Kotamaops> getKotamaopsListToExclude() {
		return kotamaopsListToExclude;
	}

	public void setKotamaopsListToExclude(List<Kotamaops> kotamaopsListToExclude) {
		this.kotamaopsListToExclude = kotamaopsListToExclude;
	}
	
}
