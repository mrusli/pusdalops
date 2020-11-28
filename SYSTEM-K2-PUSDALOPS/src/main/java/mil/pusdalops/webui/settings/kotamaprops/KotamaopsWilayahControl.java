package mil.pusdalops.webui.settings.kotamaprops;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.Window;

import mil.pusdalops.domain.kotamaops.Kotamaops;
import mil.pusdalops.domain.kotamaops.KotamaopsType;
import mil.pusdalops.domain.settings.Settings;
import mil.pusdalops.domain.wilayah.Propinsi;
import mil.pusdalops.persistence.kotamaops.dao.KotamaopsDao;
import mil.pusdalops.persistence.settings.dao.SettingsDao;
import mil.pusdalops.webui.common.GFCBaseController;
import mil.pusdalops.webui.dialogs.KotamaopsListDialogData;

public class KotamaopsWilayahControl extends GFCBaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6726903864395985253L;

	private SettingsDao settingsDao;
	private KotamaopsDao kotamaopsDao;
	
	private Window kotamaopsWilayahWin;
	private Label formTitleLabel, infoResultProplabel, 
		infoResultKotamalabel;
	private Textbox kotamaopsTerpilihTextbox;
	private Listbox kotamaopsPropinsiListbox, kotamaopsListbox;
	private Hlayout propHLayout, kotamaHLayout;
	
	private Settings defaultSettings;
	private Kotamaops defaultKotamaops;
	
	private final long SETTINGS_DEFAULT_ID = 1L;
	
	public void onCreate$kotamaopsWilayahWin(Event event) throws Exception {
		// set the default settings from settings DB
		setDefaultSettings(
				getSettingsDao().findSettingsById(SETTINGS_DEFAULT_ID));

		// set the kotamaops from settings
		setDefaultKotamaops(
				getDefaultSettings().getSelectedKotamaops());
		
		if (getDefaultKotamaops().getKotamaopsType().compareTo(KotamaopsType.PUSDALOPS)==0) {
			formTitleLabel.setValue("Settings | Kotamaops : ");

			// display the kotamaops
			kotamaopsTerpilihTextbox.setValue(
					getDefaultKotamaops().getKotamaopsName());

			// by proxy
			Kotamaops kotamaopsByProxy = 
					getKotamaopsDao().findKotamaopsKotamaopsByProxy(getDefaultKotamaops().getId());
			
			// display the kotamaops under PUSDALOPS
			displayKotamaopsKotamaops(kotamaopsByProxy.getKotamaops());
		} else {	
			formTitleLabel.setValue("Settings | Kotamaops - Propinsi | Kotamaops : ");		

			// display the kotamaops
			kotamaopsTerpilihTextbox.setValue(
					getDefaultKotamaops().getKotamaopsName());
			
			// display the wilayah / propinsi
			displayKotamaopsWilayah();
		}
	
	}

	private void displayKotamaopsKotamaops(List<Kotamaops> kotamaopsList) {
		kotamaopsPropinsiListbox.setVisible(false);
		kotamaopsListbox.setVisible(true);
		propHLayout.setVisible(false);
		kotamaHLayout.setVisible(true);
		
		kotamaopsListbox.setModel(new ListModelList<Kotamaops>(kotamaopsList));
		kotamaopsListbox.setItemRenderer(getKotamaopsListitemRenderer());
	}

	private ListitemRenderer<Kotamaops> getKotamaopsListitemRenderer() {

		return new ListitemRenderer<Kotamaops>() {
			
			BufferedImage buffImg = null;
			
			@Override
			public void render(Listitem item, Kotamaops kotamaops, int index) throws Exception {
				Listcell lc;
				
				item.setClass("expandedRowProp");

				try {
				    buffImg = ImageIO.read(new File("/pusdalops/img/logo/"+kotamaops.getImagedId()));
				} catch (IOException e) {
					throw e;
				}				
				
				// Image
				lc = new Listcell();
				lc.setImageContent(buffImg);
				// lc.setImage("/img/logo/"+kotamaops.getImagedId());
				lc.setStyle("text-align: center;");
				lc.setParent(item);
				
				// Kotamaops
				lc = initKotamaops(new Listcell(), kotamaops);
				lc.setParent(item);
				
				item.setValue(kotamaops);
			}

			private Listcell initKotamaops(Listcell listcell, Kotamaops kotamaops) {
				Vlayout vlayout = new Vlayout();
				
				// label for kotamaops name
				Label kotamaopsNameLabel = new Label();
				kotamaopsNameLabel.setValue(kotamaops.getKotamaopsName());
				kotamaopsNameLabel.setParent(vlayout);
				
				// button to remove kotamaops from Pusdalops
				Button removeKotamaopsButton = new Button();
				removeKotamaopsButton.setLabel("Hapus");
				removeKotamaopsButton.setSclass("listinfoEditButton");
				removeKotamaopsButton.setParent(vlayout);
				removeKotamaopsButton.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						Messagebox.show("Hapus Kotamaops "+kotamaops.getKotamaopsName()+" dari Pusdalops ?", 
								"Konfirmasi", 
								Messagebox.OK | Messagebox.CANCEL, 
								Messagebox.QUESTION, new EventListener<Event>() {

									@Override
									public void onEvent(Event event) throws Exception {
										if (event.getName().equals("onOK")) {
											// is this kotamaops used in Kejadian ?
											
											try {
												// get the kotamaops from pusdalops by proxy
												Kotamaops kotamaopsByProxy;
												kotamaopsByProxy = 
														getKotamaopsDao().findKotamaopsKotamaopsByProxy(getDefaultKotamaops().getId());
												// list of kotamaops under pusdalops
												List<Kotamaops> kotamaopsList = kotamaopsByProxy.getKotamaops();
												// remove
												kotamaopsList.remove(kotamaops);
												// update
												getKotamaopsDao().update(kotamaopsByProxy);
												// display
												kotamaopsByProxy = 
														getKotamaopsDao().findKotamaopsKotamaopsByProxy(getDefaultKotamaops().getId());
												displayKotamaopsKotamaops(kotamaopsByProxy.getKotamaops());
												
											} catch (Exception e) {
												throw new Exception("Tidak dapat dihapus...");
											}
										}
									}
								});
						
					}
				});
				
				vlayout.setParent(listcell);
				
				return listcell;
			}
		};
	}

	public void onAfterRender$kotamaopsListbox(Event event) throws Exception {
		infoResultKotamalabel.setValue("Total: " + kotamaopsListbox.getItemCount() + " kotamaops");
	}
	
	public void onClick$newKotamaopsButton(Event event) throws Exception {
		KotamaopsListDialogData dialogData = new KotamaopsListDialogData();
		dialogData.setKotamaopsType(null);
		Kotamaops kotamaopsByProxy = 
				getKotamaopsDao().findKotamaopsKotamaopsByProxy(getDefaultKotamaops().getId());
		dialogData.setKotamaopsToExclude(kotamaopsByProxy.getKotamaops());
		
		Map<String, KotamaopsListDialogData> args = Collections.singletonMap("dialogData", dialogData);
		Window kotamaopsSelectWin = (Window) Executions.createComponents(
				"/dialogs/KotamaopsListDialog.zul", kotamaopsWilayahWin, args);
		kotamaopsSelectWin.addEventListener(Events.ON_CHANGE, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				Kotamaops selKotamaops = (Kotamaops) event.getData();
			
				Kotamaops kotamaopsByProxy = 
						getKotamaopsDao().findKotamaopsKotamaopsByProxy(getDefaultKotamaops().getId());
				kotamaopsByProxy.getKotamaops().add(selKotamaops);
				
				getKotamaopsDao().update(kotamaopsByProxy);
				
				displayKotamaopsKotamaops(kotamaopsByProxy.getKotamaops());				
			}
		});
		kotamaopsSelectWin.doModal();		
	}

	private void displayKotamaopsWilayah() throws Exception {
		kotamaopsPropinsiListbox.setVisible(true); 
		kotamaopsListbox.setVisible(false);
		propHLayout.setVisible(true);
		kotamaHLayout.setVisible(false);
		
		Kotamaops kotamaopsByProxy = 
				getKotamaopsDao().findKotamaopsPropinsiByProxy(getDefaultKotamaops().getId());
		
		kotamaopsPropinsiListbox.setModel(
				new ListModelList<Propinsi>(kotamaopsByProxy.getPropinsis()));
		kotamaopsPropinsiListbox.setItemRenderer(getKotamaopsPropinsiListitemRenderer());
		
	}

	private ListitemRenderer<Propinsi> getKotamaopsPropinsiListitemRenderer() {

		return new ListitemRenderer<Propinsi>() {

			BufferedImage buffImg = null;
			
			@Override
			public void render(Listitem item, Propinsi propinsi, int index) throws Exception {
				Listcell lc;
				
				item.setClass("expandedRowProp");
				
				// image
				lc = initImage(new Listcell(), propinsi);
				lc.setStyle("text-align: center;");
				lc.setParent(item);
				
				// Propinsi
				lc = initPropinsi(new Listcell(), propinsi);
				lc.setParent(item);
				
				item.setValue(propinsi);
			}

			private Listcell initImage(Listcell listcell, Propinsi propinsi) throws IOException {
				Image img = new Image();
				
				try {
				    buffImg = ImageIO.read(new File("/pusdalops/img/propinsi/"+propinsi.getImageId_01_100()));
				} catch (IOException e) {
					throw e;
				}				
				
				img.setContent(buffImg);
				// img.setSrc("/img/prop/"+propinsi.getImageId_01_100());
				img.setParent(listcell);
				img.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						Map<String, Propinsi> arg = 
								Collections.singletonMap("propinsi", propinsi);
						
						Window propImgDisplayWin = (Window) Executions.createComponents(
								"/settings/kotamaopsprops/PropinsiImgDisplayDialog.zul", kotamaopsWilayahWin, arg);
						
						propImgDisplayWin.doModal();						
					}
				});

				return listcell;
			}
			
			private Listcell initPropinsi(Listcell listcell, Propinsi propinsi) {
				Vlayout vlayout = new Vlayout();
				
				// label for propinsi name
				Label propNameLabel = new Label();
				propNameLabel.setValue(propinsi.getNamaPropinsi());
				propNameLabel.setParent(vlayout);
				
				// button to remove this propinsi
				Button propDelButton = new Button();
				propDelButton.setLabel("Hapus");
				propDelButton.setSclass("listinfoEditButton");
				propDelButton.setParent(vlayout);
				propDelButton.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						Messagebox.show("Hapus Propinsi "+propinsi.getNamaPropinsi()+" dari Kotamaops "+
								getDefaultKotamaops().getKotamaopsName()+" ?", 
								"Konfirmasi", 
								Messagebox.OK | Messagebox.CANCEL, 
								Messagebox.QUESTION, new EventListener<Event>() {
									
									@Override
									public void onEvent(Event event) throws Exception {
										if (event.getName().equals("onOK")) {
											// is the propinsi (under this kotamaops) used in Kejadian?
											
											
											try {
												// proxy propinsi
												Kotamaops kotamaopsPropinsiByProxy = 
														getKotamaopsDao().findKotamaopsPropinsiByProxy(getDefaultKotamaops().getId());
												
												// get the propinsi from proxy
												List<Propinsi> propinsiList = kotamaopsPropinsiByProxy.getPropinsis();
												
												// remove
												propinsiList.remove(propinsi);
												
												// update
												getKotamaopsDao().update(kotamaopsPropinsiByProxy);
												
												// load and display -- from kotamaops
												displayKotamaopsWilayah();
												
											} catch (Exception e) {
												throw new Exception("Tidak Dapat Dihapus...");
											}
										}
										
									}
								});
						
					}
				});
				
				vlayout.setParent(listcell);
				
				return listcell;
			}
			
		};
	}
	
	public void onAfterRender$kotamaopsPropinsiListbox(Event event) throws Exception {
		infoResultProplabel.setValue("Total: " + kotamaopsPropinsiListbox.getItemCount() + " propinsi");
	}
		
	public void onClick$newPropinsiButton(Event event) throws Exception {
		PropinsiListDialogData dialogData = new PropinsiListDialogData();

		Kotamaops kotamaopsByProxy = 
				getKotamaopsDao().findKotamaopsPropinsiByProxy(getDefaultKotamaops().getId());
		dialogData.setPropinsisToExclude(kotamaopsByProxy.getPropinsis());
		
		Map<String, PropinsiListDialogData> args = Collections.singletonMap("dialogData", dialogData);
		Window propinsiSelectWin = (Window) Executions.createComponents(				
				"/settings/kotamaopsprops/PropinsiListDialog.zul", kotamaopsWilayahWin, args);
		propinsiSelectWin.addEventListener(Events.ON_CHANGE, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				// get the data
				Propinsi selPropinsi = (Propinsi) event.getData();
				// add to the kotamaops
				kotamaopsByProxy.getPropinsis().add(selPropinsi);
				// update
				getKotamaopsDao().update(kotamaopsByProxy);
				// load and display -- from kotamaops
				displayKotamaopsWilayah();
			}
		});
		
		propinsiSelectWin.doModal();
	}
	
	
	public SettingsDao getSettingsDao() {
		return settingsDao;
	}

	public void setSettingsDao(SettingsDao settingsDao) {
		this.settingsDao = settingsDao;
	}

	public Settings getDefaultSettings() {
		return defaultSettings;
	}

	public void setDefaultSettings(Settings defaultSettings) {
		this.defaultSettings = defaultSettings;
	}

	public Kotamaops getDefaultKotamaops() {
		return defaultKotamaops;
		
	}

	public void setDefaultKotamaops(Kotamaops defaultKotamaops) {
		this.defaultKotamaops = defaultKotamaops;
	}

	public KotamaopsDao getKotamaopsDao() {
		return kotamaopsDao;
	}

	public void setKotamaopsDao(KotamaopsDao kotamaopsDao) {
		this.kotamaopsDao = kotamaopsDao;
	}
}
