package mil.pusdalops.webui.settings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.context.SecurityContextHolder;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import mil.pusdalops.domain.authorization.User;
import mil.pusdalops.domain.kotamaops.KotamaopsType;
import mil.pusdalops.domain.wilayah.Kabupaten_Kotamadya;
import mil.pusdalops.domain.wilayah.Kecamatan;
import mil.pusdalops.domain.wilayah.Kelurahan;
import mil.pusdalops.domain.wilayah.Propinsi;
import mil.pusdalops.persistence.kabupaten_kotamadya.dao.Kabupaten_KotamadyaDao;
import mil.pusdalops.persistence.kecamatan.dao.KecamatanDao;
import mil.pusdalops.persistence.kelurahan.dao.KelurahanDao;
import mil.pusdalops.persistence.propinsi.dao.PropinsiDao;
import mil.pusdalops.persistence.user.dao.UserDao;
import mil.pusdalops.webui.common.GFCBaseController;
import mil.pusdalops.webui.security.UserSecurityDetails;

public class WilayahSettingsControl extends GFCBaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8303496428208392289L;

	private PropinsiDao propinsiDao;
	private Kabupaten_KotamadyaDao kabupaten_KotamadyaDao;
	private KecamatanDao kecamatanDao;
	private KelurahanDao kelurahanDao;
	private UserDao userDao;
	
	private Label formTitleLabel;
	private Listbox propinsiListbox, kabupatenListbox, kecamatanListbox,
		kelurahanListbox;
	
	private List<Propinsi> propinsiList;
	private UserSecurityDetails userSecurityDetails;	
	
	public void onCreate$wilayahSettingsWin(Event event) throws Exception {
		formTitleLabel.setValue("Settings | Wilayah");
		
		setUserSecurityDetails(
				(UserSecurityDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal());

		User loginUser = getUserSecurityDetails().getLoginUser();
		User loginUserByProxy = getUserDao().findUserKotamaopsByProxy(loginUser.getId());
		
		// check whether the Kotamaops is PUSAT or others -- if pusdalops - all propinsi, else only the selected propinsi
		if (loginUserByProxy.getKotamaops().getKotamaopsType().compareTo(KotamaopsType.PUSDALOPS)==0) {
			setPropinsiList(
					getPropinsiDao().findAllPropinsi());
			
		} else {
			// TODO: propinsi for selected kotamaops
		}
		
		// display
		displayPropinsiListInfo();
	}

	private void displayPropinsiListInfo() {
		propinsiListbox.setModel(
				new ListModelList<Propinsi>(getPropinsiList()));
		propinsiListbox.setItemRenderer(
				getPropinsiListitemRenderer());
	}

	private ListitemRenderer<Propinsi> getPropinsiListitemRenderer() {

		return new ListitemRenderer<Propinsi>() {
			
			@Override
			public void render(Listitem item, Propinsi propinsi, int index) throws Exception {
				Listcell lc;
				
				// Propinsi
				lc = initNamaPropinsi(new Listcell(), propinsi);
				lc.setStyle("white-space: nowrap;");
				lc.setParent(item);
				
				// to select
				lc = initSelect(new Listcell(), propinsi);
				lc.setParent(item);
				
				item.setValue(propinsi);				
			}

			private Listcell initNamaPropinsi(Listcell listcell, Propinsi propinsi) {
				Textbox namapropinsiTextbox = new Textbox();
				namapropinsiTextbox.setValue(propinsi.getNamaPropinsi());
				namapropinsiTextbox.setWidth("260px");
				namapropinsiTextbox.setInplace(true);
				namapropinsiTextbox.setSubmitByEnter(true);
				namapropinsiTextbox.addEventListener(Events.ON_OK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						// Messagebox.show("Are you sure to save?", "Confirm Dialog", Messagebox.OK | Messagebox.IGNORE  | Messagebox.CANCEL, Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener() {
						Messagebox.show("Simpan Perubahan nama Propinsi?", "Konfirmasi", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener<Event>() {
							
							@Override
							public void onEvent(Event event) throws Exception {
								if (event.getName().equals("onOK")) {
									Listitem item = (Listitem) listcell.getParent();
									
									// get the object
									Propinsi modPropinsi = (Propinsi)item.getValue();
									
									// assign the attribute value
									modPropinsi.setNamaPropinsi(namapropinsiTextbox.getValue());
									
									// update
									getPropinsiDao().update(modPropinsi);
									
									// show notification
									Clients.showNotification("Perubahan Nama Propinsi Sudah Disimpan",
											"info", null, "middle_center", 0);
								}
								
							}
						});
					}
				});
				namapropinsiTextbox.setParent(listcell);

				return listcell;
			}

			private Listcell initSelect(Listcell listcell, Propinsi propinsi) {
				Button selButton = new Button();
				selButton.setLabel("");
				selButton.setImage("/img/arrow-right-16x16.png");
				selButton.setClass("listinfoEditButton");
				selButton.setParent(listcell);
				selButton.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						
						// kecamatanListbox.getItems().clear();
						initKecamatanListbox();
						
						// kelurahanListbox.getItems().clear();
						initKelurahanListbox();
						
						Listitem item = (Listitem) listcell.getParent();
						
						// item is highlighted (selected)
						item.setSelected(true);
						
						// propinsi object by proxy
						Propinsi propByProxy = getPropinsiDao().findKabupatenKotamadyaByProxy(propinsi.getId());
						
						// display kabupaten-kotamadyas
						displayKabupatenKotamadyas(propByProxy.getKabupatenkotamadyas());
					}

				});
				
				return listcell;
			}
		};
	}

	private void displayKabupatenKotamadyas(List<Kabupaten_Kotamadya> kabupatenkotamadyas) {
		kabupatenListbox.setModel(
				new ListModelList<Kabupaten_Kotamadya>(kabupatenkotamadyas));
		kabupatenListbox.setItemRenderer(getKabupatenListitemRenderer());
		
	}
	
	
	private ListitemRenderer<Kabupaten_Kotamadya> getKabupatenListitemRenderer() {
		
		return new ListitemRenderer<Kabupaten_Kotamadya>() {
			
			@Override
			public void render(Listitem item, Kabupaten_Kotamadya kabupaten, int index) throws Exception {
				Listcell lc;
				
				// Kabupaten
				lc = initKotamadya(new Listcell(), kabupaten);
				lc.setStyle("white-space: nowrap;");
				lc.setParent(item);

				// select button
				lc = initSelectKabupaten(new Listcell(), kabupaten);
				lc.setParent(item);
				
				item.setValue(kabupaten);
				
			}

			private Listcell initKotamadya(Listcell listcell, Kabupaten_Kotamadya kabupaten) {
				Textbox namaKotamadyaTextbox = new Textbox();
				namaKotamadyaTextbox.setValue(kabupaten.getNamaKabupaten());
				namaKotamadyaTextbox.setWidth("260px");
				namaKotamadyaTextbox.setInplace(true);
				namaKotamadyaTextbox.setSubmitByEnter(true);
				namaKotamadyaTextbox.addEventListener(Events.ON_OK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						// Messagebox.show("Are you sure to save?", "Confirm Dialog", Messagebox.OK | Messagebox.IGNORE  | Messagebox.CANCEL, Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener() {
						Messagebox.show("Simpan Perubahan nama Kabupaten / Kotamadya?", "Konfirmasi", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener<Event>() {
							
							@Override
							public void onEvent(Event event) throws Exception {
								if (event.getName().equals("onOK")) {
									Listitem item = (Listitem) listcell.getParent();
									
									// get the object
									Kabupaten_Kotamadya modKabupaten = item.getValue();
									
									// assign the attribute value
									modKabupaten.setNamaKabupaten(namaKotamadyaTextbox.getValue());
									
									// update
									getKabupaten_KotamadyaDao().update(modKabupaten);
									
									// show notification
									Clients.showNotification("Perubahan Nama Kabupaten / Kotamadya Sudah Disimpan",
											"info", null, "middle_center", 0);
								}
							}
						});
						
					}
				});
				namaKotamadyaTextbox.setParent(listcell);

				return listcell;
			}

			private Listcell initSelectKabupaten(Listcell listcell, Kabupaten_Kotamadya kabupaten) {
				Button selButton = new Button();
				selButton.setLabel("");
				selButton.setImage("/img/arrow-right-16x16.png");
				selButton.setClass("listinfoEditButton");
				selButton.setParent(listcell);
				selButton.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						// kelurahanListbox.getItems().clear();
						initKelurahanListbox();
						
						Listitem item = (Listitem) listcell.getParent();
						
						// item is highlighted (selected)
						item.setSelected(true);
						
						// Kabupaten_Kotamadya object by proxy
						Kabupaten_Kotamadya kabByProxy = getKabupaten_KotamadyaDao().findKecamatanByProxy(kabupaten.getId());
						
						// display kecamatan
						displayKecamatans(kabByProxy.getKecamatans());
					}
				});
				
				return listcell;
			}
		};
	}

	private void displayKecamatans(List<Kecamatan> kecamatans) {
		kecamatanListbox.setModel(
				new ListModelList<Kecamatan>(kecamatans));
		kecamatanListbox.setItemRenderer(getKecamatanListitemRenderer());
	}
	
	
	private ListitemRenderer<Kecamatan> getKecamatanListitemRenderer() {

		return new ListitemRenderer<Kecamatan>() {
			
			@Override
			public void render(Listitem item, Kecamatan kecamatan, int index) throws Exception {
				Listcell lc;
				
				// Kecamatan
				lc = initKecamatan(new Listcell(), kecamatan);
				lc.setParent(item);
				
				// Select
				lc = initSelectKecamatan(new Listcell(), kecamatan);
				lc.setParent(item);
				
				item.setValue(kecamatan);				
			}

			private Listcell initKecamatan(Listcell listcell, Kecamatan kecamatan) {
				Textbox namaKecamatanTextbox = new Textbox();
				namaKecamatanTextbox.setValue(kecamatan.getNamaKecamatan());
				namaKecamatanTextbox.setWidth("260px");
				namaKecamatanTextbox.setInplace(true);
				namaKecamatanTextbox.setSubmitByEnter(true);
				namaKecamatanTextbox.setParent(listcell);
				namaKecamatanTextbox.addEventListener(Events.ON_OK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						// Messagebox.show("Are you sure to save?", "Confirm Dialog", Messagebox.OK | Messagebox.IGNORE  | Messagebox.CANCEL, Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener() {
						Messagebox.show("Simpan Perubahan nama Kabupaten / Kotamadya?", "Konfirmasi", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener<Event>() {
							
							@Override
							public void onEvent(Event event) throws Exception {																
								if (event.getName().equals("onOK")) {
									Listitem item = (Listitem) listcell.getParent();
									
									// get the object
									Kecamatan modKecamatan = item.getValue();
									
									// assign the attribute value
									modKecamatan.setNamaKecamatan(namaKecamatanTextbox.getValue());
									
									// update
									getKecamatanDao().update(modKecamatan);
									
									// show notification
									Clients.showNotification("Perubahan Nama Kecamatan Sudah Disimpan",
										"info", null, "middle_center", 0);
								}
							}
						}); 

					}
				});

				return listcell;
			}

			private Listcell initSelectKecamatan(Listcell listcell, Kecamatan kecamatan) {
				Button selButton = new Button();
				selButton.setLabel("");
				selButton.setImage("/img/arrow-right-16x16.png");
				selButton.setClass("listinfoEditButton");
				selButton.setParent(listcell);
				selButton.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						Listitem item = (Listitem) listcell.getParent();
						
						// item is highlighted (selected)
						item.setSelected(true);
						
						// Kecamatan object by proxy
						Kecamatan kecByProxy = getKecamatanDao().findKelurahanByProxy(kecamatan.getId());
						
						// display kelurahan
						displayKelurahan(kecByProxy.getKelurahans());
					}

				});
					
				return listcell;
			}
		};
	}

	private void displayKelurahan(List<Kelurahan> kelurahans) {
		kelurahanListbox.setModel(
				new ListModelList<Kelurahan>(kelurahans));
		kelurahanListbox.setItemRenderer(getKelurahanListitemRenderer());		
	}
	
	private ListitemRenderer<Kelurahan> getKelurahanListitemRenderer() {
		
		return new ListitemRenderer<Kelurahan>() {
			
			@Override
			public void render(Listitem item, Kelurahan kelurahan, int index) throws Exception {
				Listcell lc;
				
				// nama Kelurahan
				lc = initNamaKelurahan(new Listcell(), kelurahan);
				lc.setParent(item);
				
				item.setValue(kelurahan);				
			}

			private Listcell initNamaKelurahan(Listcell listcell, Kelurahan kelurahan) {
				Textbox namaKelurahanTextbox = new Textbox();
				namaKelurahanTextbox.setValue(kelurahan.getNamaKelurahan());
				namaKelurahanTextbox.setWidth("260px");
				namaKelurahanTextbox.setInplace(true);
				namaKelurahanTextbox.setSubmitByEnter(true);
				namaKelurahanTextbox.setParent(listcell);
				namaKelurahanTextbox.addEventListener(Events.ON_OK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						// Messagebox.show("Are you sure to save?", "Confirm Dialog", Messagebox.OK | Messagebox.IGNORE  | Messagebox.CANCEL, Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener() {
						Messagebox.show("Simpan Perubahan nama Kabupaten / Kotamadya?", "Konfirmasi", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener<Event>() {
							
							@Override
							public void onEvent(Event event) throws Exception {
								if (event.getName().equals("onOK")) {
									Listitem item = (Listitem) listcell.getParent();
									
									// get the object
									Kelurahan modKelurahan= item.getValue();
									
									// assign the attribute value
									modKelurahan.setNamaKelurahan(namaKelurahanTextbox.getValue());
									
									// update																					
									getKelurahanDao().update(modKelurahan);
									
									// show notification
									Clients.showNotification("Perubahan Nama Kelurahan Sudah Disimpan",
										"info", null, "middle_center", 0);
								}																			
							}
						});
					}
				});

				return listcell;
			}
		};
	}

	private void initKabupatenListbox() {
		kabupatenListbox.setModel(
				new ListModelList<Kabupaten_Kotamadya>(
						new ArrayList<Kabupaten_Kotamadya>()));
	}
	
	private void initKecamatanListbox() {
		kecamatanListbox.setModel(
				new ListModelList<Kecamatan>(new ArrayList<Kecamatan>()));
	}
	
	private void initKelurahanListbox() {
		kelurahanListbox.setModel(
				new ListModelList<Kelurahan>(new ArrayList<Kelurahan>()));
	}
	
	public void onClick$kelurahanAddButton(Event event) throws Exception {
		// a kecamatan MUST BE SELECTED -- if null, throw an exception and exit
		if (kecamatanListbox.getSelectedItem()==null) {
			throw new Exception("Belum memilih Kecamatan.");
		}

		Kecamatan selKecamatan = kecamatanListbox.getSelectedItem().getValue();
		
		Map<String, String> args = Collections.singletonMap("name", "Kelurahan:");
		Window textEntryWindow = (Window) Executions.createComponents("/common/TextEntry.zul", null, args);
		textEntryWindow.addEventListener(Events.ON_CHANGE, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				String namaKelurahan = (String) event.getData();
				
				// new kelurahan
				Kelurahan kelurahan = new Kelurahan();
				kelurahan.setNamaKelurahan(namaKelurahan);
				
				Kecamatan kecByProxy = getKecamatanDao().findKelurahanByProxy(selKecamatan.getId());
				
				// add to kecamatan
				kecByProxy.getKelurahans().add(kelurahan);
				
				// update
				getKecamatanDao().update(kecByProxy);
				
				// display
				displayKelurahan(kecByProxy.getKelurahans());
			}
		});
		
		textEntryWindow.doModal();
	}
	
	public void onClick$kecamatanAddButton(Event event) throws Exception {
		// a kabupaten MUST BE SELECTED -- if null, throw an exception and exit
		if (kabupatenListbox.getSelectedItem()==null) {
			throw new Exception("Belum memilih Kabupaten.");
		}
		
		Kabupaten_Kotamadya selKabupaten = kabupatenListbox.getSelectedItem().getValue();
		
		Map<String, String> args = Collections.singletonMap("name", "Kecamatan:");
		Window textEntryWindow = (Window) Executions.createComponents("/common/TextEntry.zul", null, args);
		textEntryWindow.addEventListener(Events.ON_CHANGE, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				String namaKecamatan = (String) event.getData();
				
				// new kecamatan
				Kecamatan kecamatan = new Kecamatan();
				kecamatan.setNamaKecamatan(namaKecamatan);
				
				Kabupaten_Kotamadya kabByProxy = 
						getKabupaten_KotamadyaDao().findKecamatanByProxy(selKabupaten.getId()); 
				
				// add to kabupaten
				kabByProxy.getKecamatans().add(kecamatan);
				
				// update
				getKabupaten_KotamadyaDao().update(kabByProxy);
				
				// display
				displayKecamatans(kabByProxy.getKecamatans());
				
				// init kecamatan, kelurahan
				initKelurahanListbox();
			}
		});
		
		textEntryWindow.doModal();
		
	}
	
	public void onClick$kabupatenAddButton(Event event) throws Exception {
		// a propinsi MUST BE SELECTED -- if null, throw an exception and exit
		if (propinsiListbox.getSelectedItem()==null) {
			throw new Exception("Belum memilih Kabupaten.");
		}
		
		Propinsi selPropinsi = propinsiListbox.getSelectedItem().getValue();
		
		Map<String, String> args = Collections.singletonMap("name", "Kabupaten/Kot:");
		Window textEntryWindow = (Window) Executions.createComponents("/common/TextEntry.zul", null, args);
		textEntryWindow.addEventListener(Events.ON_CHANGE, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				String namaKabupaten = (String) event.getData();
				
				// new kabupaten
				Kabupaten_Kotamadya kabupaten = new Kabupaten_Kotamadya();
				kabupaten.setNamaKabupaten(namaKabupaten);
				
				Propinsi propByProxy = 
						getPropinsiDao().findKabupatenKotamadyaByProxy(selPropinsi.getId());
						
				// add to kabupaten
				propByProxy.getKabupatenkotamadyas().add(kabupaten);
				
				// update
				getPropinsiDao().update(propByProxy);
				
				// display
				displayKabupatenKotamadyas(propByProxy.getKabupatenkotamadyas());
				
				// init kecamatan, kelurahan
				initKecamatanListbox();
				initKelurahanListbox();
			}
		});
		
		textEntryWindow.doModal();
		
	}
	
	public void onClick$propinsiAddButton(Event event) throws Exception {
		Map<String, String> args = Collections.singletonMap("name", "Propinsi:");
		Window textEntryWindow = (Window) Executions.createComponents("/common/TextEntry.zul", null, args);
		textEntryWindow.addEventListener(Events.ON_CHANGE, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				String namaPropinsi = (String) event.getData();
				
				// new propinsi
				Propinsi propinsi = new Propinsi();
				propinsi.setNamaPropinsi(namaPropinsi);
				
				// save to propinsi
				getPropinsiDao().save(propinsi);
				
				// re-load the list
				setPropinsiList(
						getPropinsiDao().findAllPropinsi());
				
				// display
				displayPropinsiListInfo();
				
				// init kabupaten, kecamatan, kelurahan
				initKabupatenListbox();
				initKecamatanListbox();
				initKelurahanListbox();
			}
		});
		
		textEntryWindow.doModal();
	}
	
	public PropinsiDao getPropinsiDao() {
		return propinsiDao;
	}

	public void setPropinsiDao(PropinsiDao propinsiDao) {
		this.propinsiDao = propinsiDao;
	}

	public List<Propinsi> getPropinsiList() {
		return propinsiList;
	}

	public void setPropinsiList(List<Propinsi> propinsiList) {
		this.propinsiList = propinsiList;
	}

	public UserSecurityDetails getUserSecurityDetails() {
		return userSecurityDetails;
	}

	public void setUserSecurityDetails(UserSecurityDetails userSecurityDetails) {
		this.userSecurityDetails = userSecurityDetails;
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public Kabupaten_KotamadyaDao getKabupaten_KotamadyaDao() {
		return kabupaten_KotamadyaDao;
	}

	public void setKabupaten_KotamadyaDao(Kabupaten_KotamadyaDao kabupaten_KotamadyaDao) {
		this.kabupaten_KotamadyaDao = kabupaten_KotamadyaDao;
	}

	public KecamatanDao getKecamatanDao() {
		return kecamatanDao;
	}

	public void setKecamatanDao(KecamatanDao kecamatanDao) {
		this.kecamatanDao = kecamatanDao;
	}

	public KelurahanDao getKelurahanDao() {
		return kelurahanDao;
	}

	public void setKelurahanDao(KelurahanDao kelurahanDao) {
		this.kelurahanDao = kelurahanDao;
	}
}
