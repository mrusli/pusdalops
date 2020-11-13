package mil.pusdalops.webui.settings;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.Window;

import mil.pusdalops.domain.kejadian.KejadianJenis;
import mil.pusdalops.domain.kejadian.KejadianMotif;
import mil.pusdalops.domain.kerugian.KerugianJenis;
import mil.pusdalops.domain.kerugian.KerugianKondisi;
import mil.pusdalops.domain.kerugian.KerugianSatuan;
import mil.pusdalops.domain.kotamaops.Kotamaops;
import mil.pusdalops.domain.settings.Settings;
import mil.pusdalops.persistence.kejadian.jenis.dao.KejadianJenisDao;
import mil.pusdalops.persistence.kejadian.motif.dao.KejadianMotifDao;
import mil.pusdalops.persistence.kerugian.jenis.dao.KerugianJenisDao;
import mil.pusdalops.persistence.kerugian.kondisi.dao.KerugianKondisiDao;
import mil.pusdalops.persistence.kerugian.satuan.dao.KerugianSatuanDao;
import mil.pusdalops.persistence.settings.dao.SettingsDao;
import mil.pusdalops.webui.common.GFCBaseController;
import mil.pusdalops.webui.kejadian.KerugianJenisData;

public class KejadianSettingsControl extends GFCBaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5765801355529942485L;

	private KejadianJenisDao kejadianJenisDao;
	private KejadianMotifDao kejadianMotifDao;
	private KerugianJenisDao kerugianJenisDao;
	private KerugianKondisiDao kerugianKondisiDao;
	private SettingsDao settingsDao;
	private KerugianSatuanDao kerugianSatuanDao;
	
	private Label formTitleLabel;
	private Listbox jenisKejadianListbox, motifKejadianListbox,
		jenisKerugianListbox, kondisiKerugianListbox,
		satuanKerugianListbox;
	private Tabbox kejadianTabbox, kerugianTabbox;
	private Vlayout jenisKejadianVlayout, motifKejadianVlayout,
		jenisKerugianVlayout, kondisiKerugianVlayout,
		satuanKerugianVlayout;
	
	private List<KejadianJenis> kejadianJenisList;
	private List<KejadianMotif> kejadianMotifList;
	private List<KerugianJenis> kerugianJenisList;
	private List<KerugianKondisi> kerugianKondisiList;
	private List<KerugianSatuan> kerugianSatuanList;
	private LocalDateTime currentLocalDateTime;
	private Settings settings;
	private Kotamaops kotamaops;
	private ZoneId zoneId;
	
	private final long SETTINGS_DEFAULT_ID = 1L;
	
	public void onCreate$kejadianSettingsWin(Event event) throws Exception {
		setSettings(
				getSettingsDao().findSettingsById(SETTINGS_DEFAULT_ID));
		setKotamaops(
				getSettings().getSelectedKotamaops());
		
		formTitleLabel.setValue("Settings | Kejadian dan Kerugian");
		
		// set the timezone for this kotamaops
		int timezoneOrdinal = getKotamaops().getTimeZone().getValue();
		setZoneId(getKotamaops().getTimeZone().toZoneId(timezoneOrdinal));
		
		setCurrentLocalDateTime(getLocalDateTime(getZoneId()));
		
		// display Jenis Kejadian
		displayKejadianJenis();
		
		// display Motif Kejadian
		displayKejadianMotif();
		
		// display Kerugian Jenis
		displayKerugianJenis();
		
		// display Kondisi Kerugian
		displayKondisiKerugian();
		
		// display Satuan Kerugian
		displaySatuanKerugian();
	}

	public void onSelect$kejadianTabbox(Event event) throws Exception {
		int seltabIndex = kejadianTabbox.getSelectedIndex();
		switch (seltabIndex) {
		case 0:
			// jenisKejadianVlayout
			jenisKejadianVlayout.setVisible(true);
			motifKejadianVlayout.setVisible(false);
			
			break;
		case 1:
			// motifKejadianVlayout
			jenisKejadianVlayout.setVisible(false);
			motifKejadianVlayout.setVisible(true);
			
			break;
		default:
			break;
		}
	}

	public void onSelect$kerugianTabbox(Event event) throws Exception {
		int seltabIndex = kerugianTabbox.getSelectedIndex();
		switch (seltabIndex) {
		case 0:
			// jenisKerugianVlayout
			jenisKerugianVlayout.setVisible(true); 
			kondisiKerugianVlayout.setVisible(false);
			satuanKerugianVlayout.setVisible(false);
			break;
		case 1:
			// kondisiKerugianVlayout
			jenisKerugianVlayout.setVisible(false); 
			kondisiKerugianVlayout.setVisible(true);
			satuanKerugianVlayout.setVisible(false);
			break;
		case 2:
			// satuanKerugianVlayout
			jenisKerugianVlayout.setVisible(false); 
			kondisiKerugianVlayout.setVisible(false);
			satuanKerugianVlayout.setVisible(true);
		default:
			break;
		}
	}
	
	private void displayKejadianJenis() throws Exception {
		// load data kejadianJenis
		setKejadianJenisList(
				getKejadianJenisDao().findAllKejadianJenis(true));
		
		// display
		jenisKejadianListbox.setModel(
				new ListModelList<KejadianJenis>(getKejadianJenisList()));
		jenisKejadianListbox.setItemRenderer(getKejadianJenisListiitemRenderer());
	}
	
	private ListitemRenderer<KejadianJenis> getKejadianJenisListiitemRenderer() {
		
		return new ListitemRenderer<KejadianJenis>() {
			
			@Override
			public void render(Listitem item, KejadianJenis kejadianJenis, int index) throws Exception {
				Listcell lc;
				
				// Jenis Kejadian
				lc = initNamaJenisKejadian(new Listcell(), kejadianJenis);
				lc.setStyle("white-space: nowrap;");
				lc.setParent(item);
				
				item.setValue(kejadianJenis);
			}

			private Listcell initNamaJenisKejadian(Listcell listcell, KejadianJenis kejadianJenis) {
				Textbox namaJenisKejTextbox = new Textbox();
				namaJenisKejTextbox.setValue(kejadianJenis.getNamaJenis());
				namaJenisKejTextbox.setWidth("420px");
				namaJenisKejTextbox.setInplace(true);
				namaJenisKejTextbox.setSubmitByEnter(true);
				namaJenisKejTextbox.setParent(listcell);
				namaJenisKejTextbox.addEventListener(Events.ON_OK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						// Messagebox.show("Are you sure to save?", "Confirm Dialog", Messagebox.OK | Messagebox.IGNORE  | Messagebox.CANCEL, Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener() {
						Messagebox.show("Simpan Perubahan Jenis Kejadian?", "Konfirmasi", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener<Event>() {
							
							@Override
							public void onEvent(Event event) throws Exception {
								if (event.getName().equals("onOK")) {
									Listitem item = (Listitem) listcell.getParent();
									
									// get the object
									KejadianJenis modJenisKej = item.getValue();
									
									// assign the attribute value
									modJenisKej.setEditedAt(asDate(getCurrentLocalDateTime()));
									modJenisKej.setNamaJenis(namaJenisKejTextbox.getValue());
									
									// update
									getKejadianJenisDao().update(modJenisKej);
									
									// show notification
									Clients.showNotification("Perubahan Jenis Kejadian Sudah Disimpan",
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

	public void onClick$jenisKejadianAddButton(Event event) throws Exception {
		Map<String, String> args = Collections.singletonMap("name", "Jenis Kejadian:");
		Window textEntryWindow = (Window) Executions.createComponents("/common/TextEntry.zul", null, args);
		textEntryWindow.addEventListener(Events.ON_CHANGE, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				String jenisKej = (String) event.getData();
				
				// create a new object
				KejadianJenis kejadianJenis = new KejadianJenis();
				kejadianJenis.setCreatedAt(asDate(getCurrentLocalDateTime()));
				kejadianJenis.setEditedAt(asDate(getCurrentLocalDateTime()));
				kejadianJenis.setNamaJenis(jenisKej);
				
				// save object
				getKejadianJenisDao().save(kejadianJenis);
				
				// display
				displayKejadianJenis();
			}
		});

		textEntryWindow.doModal();
	}

	private void displayKejadianMotif() throws Exception {
		// load data Motif Kejadian
		setKejadianMotifList(
				getKejadianMotifDao().findAllKejadianMotif());
		
		// display Motif Kejadian
		motifKejadianListbox.setModel(
				new ListModelList<KejadianMotif>(getKejadianMotifList()));
		motifKejadianListbox.setItemRenderer(getMotifKejadianListitemRenderer());
	}

	
	private ListitemRenderer<KejadianMotif> getMotifKejadianListitemRenderer() {
		
		return new ListitemRenderer<KejadianMotif>() {
			
			@Override
			public void render(Listitem item, KejadianMotif kejadianMotif, int index) throws Exception {
				Listcell lc;
				
				// Motif Kejadian
				lc = initMotifKejadian(new Listcell(), kejadianMotif);
				lc.setStyle("white-space: nowrap;");
				lc.setParent(item);
				
				item.setValue(kejadianMotif);
			}

			private Listcell initMotifKejadian(Listcell listcell, KejadianMotif kejadianMotif) {
				Textbox motifKejTextbox = new Textbox();
				motifKejTextbox.setValue(kejadianMotif.getNamaMotif());
				motifKejTextbox.setWidth("420px");
				motifKejTextbox.setInplace(true);
				motifKejTextbox.setSubmitByEnter(true);
				motifKejTextbox.setParent(listcell);
				motifKejTextbox.addEventListener(Events.ON_OK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						// Messagebox.show("Are you sure to save?", "Confirm Dialog", Messagebox.OK | Messagebox.IGNORE  | Messagebox.CANCEL, Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener() {
						Messagebox.show("Simpan Perubahan Motif Kejadian?", "Konfirmasi", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener<Event>() {
							
							@Override
							public void onEvent(Event event) throws Exception {
								if (event.getName().equals("onOK")) {
									Listitem item = (Listitem) listcell.getParent();
									
									// get the object
									KejadianMotif modMotifKej = item.getValue();
									
									// assign the attribute value
									modMotifKej.setNamaMotif(motifKejTextbox.getValue());
									modMotifKej.setEditedAt(asDate(getCurrentLocalDateTime()));
									
									// update
									getKejadianMotifDao().update(modMotifKej);
									
									// show notification
									Clients.showNotification("Perubahan Motif Kejadian Sudah Disimpan",
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

	public void onClick$motifAddButton(Event event) throws Exception {
		Map<String, String> args = Collections.singletonMap("name", "Motif Kejadian:");
		Window textEntryWindow = (Window) Executions.createComponents("/common/TextEntry.zul", null, args);
		textEntryWindow.addEventListener(Events.ON_CHANGE, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				String motifKej = (String) event.getData();
				
				// create a new object
				KejadianMotif kejadianMotif = new KejadianMotif();
				kejadianMotif.setNamaMotif(motifKej);
				kejadianMotif.setCreatedAt(asDate(getCurrentLocalDateTime()));
				kejadianMotif.setEditedAt(asDate(getCurrentLocalDateTime()));
				
				// save object
				getKejadianMotifDao().save(kejadianMotif);
				
				// display
				displayKejadianMotif();
			}
		});

		textEntryWindow.doModal();		
	}
	
	private void displayKerugianJenis() throws Exception {
		setKerugianJenisList(
				getKerugianJenisDao().findAllKerugianJenis(true));
		jenisKerugianListbox.setModel(
				new ListModelList<KerugianJenis>(getKerugianJenisList()));
		jenisKerugianListbox.setItemRenderer(getKerugianJenisListitemRenderer());
	}

	private ListitemRenderer<KerugianJenis> getKerugianJenisListitemRenderer() {

		return new ListitemRenderer<KerugianJenis>() {
			
			@Override
			public void render(Listitem item, KerugianJenis kerugianJenis, int index) throws Exception {
				Listcell lc;
				
				// Jenis Kerugian
				lc = initKerugianJenis(new Listcell(), kerugianJenis);
				lc.setParent(item);
				
				item.setValue(kerugianJenis);
			}

			private Listcell initKerugianJenis(Listcell listcell, KerugianJenis kerugianJenis) {
				Textbox jenisKerTextbox = new Textbox();
				jenisKerTextbox.setValue(kerugianJenis.getNamaJenis());
				jenisKerTextbox.setWidth("420px");
				jenisKerTextbox.setInplace(true);
				jenisKerTextbox.setSubmitByEnter(true);
				jenisKerTextbox.setParent(listcell);
				jenisKerTextbox.addEventListener(Events.ON_OK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						Messagebox.show("Simpan Perubahan Jenis Kerugian?", "Konfirmasi", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener<Event>() {
							
							@Override
							public void onEvent(Event event) throws Exception {
								if (event.getName().equals("onOK")) {
									Listitem item = (Listitem) listcell.getParent();
									
									// get the object
									KerugianJenis modJenisKer = item.getValue();
									
									// assign the attribute value
									modJenisKer.setNamaJenis(jenisKerTextbox.getValue());
									modJenisKer.setEditedAt(asDate(getCurrentLocalDateTime()));
									
									// update
									getKerugianJenisDao().update(modJenisKer);
									
									// show notification
									Clients.showNotification("Perubahan Jenis Kerugian Sudah Disimpan",
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

	public void onClick$jenisKerugianAddButton(Event event) throws Exception {
		// Map<String, String> args = Collections.singletonMap("name", "Jenis Kerugian:");
		Window textEntryWindow = (Window) Executions.createComponents("/kejadian/KerugianJenisTextEntry.zul", null, null);
		textEntryWindow.addEventListener(Events.ON_CHANGE, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				KerugianJenisData kerugianJenisData = (KerugianJenisData) event.getData();
				
				// create a new object
				KerugianJenis kerugianJenis = new KerugianJenis();
				kerugianJenis.setCreatedAt(asDate(getCurrentLocalDateTime()));
				kerugianJenis.setEditedAt(asDate(getCurrentLocalDateTime()));
				kerugianJenis.setNamaJenis(kerugianJenisData.getNamaKerugianJenis());
				kerugianJenis.setTipeKerugian(kerugianJenisData.getTipeKerugian());
				
				// save object
				getKerugianJenisDao().save(kerugianJenis);
				
				// display
				displayKerugianJenis();
			}
		});

		textEntryWindow.doModal();		
		
	}
	
	private void displayKondisiKerugian() throws Exception {
		setKerugianKondisiList(
				getKerugianKondisiDao().findAllKerugianKondisi(true));
		kondisiKerugianListbox.setModel(
				new ListModelList<KerugianKondisi>(getKerugianKondisiList()));
		kondisiKerugianListbox.setItemRenderer(getKerugianKondisiListitemRenderer());
	}
	
	private ListitemRenderer<KerugianKondisi> getKerugianKondisiListitemRenderer() {

		return new ListitemRenderer<KerugianKondisi>() {
			
			@Override
			public void render(Listitem item, KerugianKondisi kerugianKondisi, int index) throws Exception {
				Listcell lc;
				
				lc = initKerugianKondisi(new Listcell(), kerugianKondisi);
				lc.setParent(item);
				
				item.setValue(kerugianKondisi);
				
			}

			private Listcell initKerugianKondisi(Listcell listcell, KerugianKondisi kerugianKondisi) {
				Textbox kondisiKerTextbox = new Textbox();
				kondisiKerTextbox.setValue(kerugianKondisi.getNamaKondisi());
				kondisiKerTextbox.setWidth("420px");
				kondisiKerTextbox.setInplace(true);
				kondisiKerTextbox.setSubmitByEnter(true);
				kondisiKerTextbox.setParent(listcell);
				kondisiKerTextbox.addEventListener(Events.ON_OK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						Messagebox.show("Simpan Perubahan Kondisi Kerugian?", "Konfirmasi", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener<Event>() {
							
							@Override
							public void onEvent(Event event) throws Exception {
								if (event.getName().equals("onOK")) {
									Listitem item = (Listitem) listcell.getParent();
									
									// get the object
									KerugianKondisi modKondisiKer = item.getValue();
									
									// assign the attribute value
									modKondisiKer.setNamaKondisi(kondisiKerTextbox.getValue());
									modKondisiKer.setEditedAt(asDate(getCurrentLocalDateTime()));
									
									// update
									getKerugianKondisiDao().update(modKondisiKer);
									
									// show notification
									Clients.showNotification("Perubahan Kondisi Kerugian Sudah Disimpan",
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

	public void onClick$kondisiKerugianAddButton(Event event) throws Exception {
		Map<String, String> args = Collections.singletonMap("name", "Kondisi Kerugian:");
		Window textEntryWindow = (Window) Executions.createComponents("/common/TextEntry.zul", null, args);
		textEntryWindow.addEventListener(Events.ON_CHANGE, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				String kondisiKerugianStr = (String) event.getData();
				
				// create a new object
				KerugianKondisi kondisiKerugian = new KerugianKondisi();
				kondisiKerugian.setEditedAt(asDate(getCurrentLocalDateTime()));
				kondisiKerugian.setCreatedAt(asDate(getCurrentLocalDateTime()));
				kondisiKerugian.setNamaKondisi(kondisiKerugianStr);
				
				// save object
				getKerugianKondisiDao().save(kondisiKerugian);
				
				// display
				// displayKejadianMotif();
				displayKondisiKerugian();
			}
		});

		textEntryWindow.doModal();		
		
	}
	
	private void displaySatuanKerugian() throws Exception {
		setKerugianSatuanList(getKerugianSatuanDao().findAllKerugianSatuan(true));
		satuanKerugianListbox.setModel(
				new ListModelList<KerugianSatuan>(getKerugianSatuanList()));
		satuanKerugianListbox.setItemRenderer(
				getKerugianSatuanListitemRenderer());
		
	}

	private ListitemRenderer<KerugianSatuan> getKerugianSatuanListitemRenderer() {
		
		return new ListitemRenderer<KerugianSatuan>() {
			
			@Override
			public void render(Listitem item, KerugianSatuan kerugianSatuan, int index) throws Exception {
				Listcell lc;
				
				lc = initKerugianSatuan(new Listcell(), kerugianSatuan);
				lc.setParent(item);
				
				item.setValue(kerugianSatuan);
				
			}

			private Listcell initKerugianSatuan(Listcell listcell, KerugianSatuan kerugianSatuan) {
				Textbox satuanKerTextbox = new Textbox();
				satuanKerTextbox.setValue(kerugianSatuan.getSatuan());
				satuanKerTextbox.setWidth("420px");
				satuanKerTextbox.setInplace(true);
				satuanKerTextbox.setSubmitByEnter(true);
				satuanKerTextbox.setParent(listcell);
				satuanKerTextbox.addEventListener(Events.ON_OK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						Messagebox.show("Simpan Perubahan Satuan Kerugian?", "Konfirmasi", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener<Event>() {
							
							@Override
							public void onEvent(Event event) throws Exception {
								if (event.getName().equals("onOK")) {
									Listitem item = (Listitem) listcell.getParent();
									
									// get the object
									KerugianSatuan modSatuanKer = item.getValue();
									
									// assign the attribute value
									modSatuanKer.setSatuan(satuanKerTextbox.getValue());
									modSatuanKer.setEditedAt(asDate(getCurrentLocalDateTime()));
									
									// update
									// getKerugianKondisiDao().update(modKondisiKer);
									getKerugianSatuanDao().update(modSatuanKer);
									
									// show notification
									Clients.showNotification("Perubahan Satuan Kerugian Sudah Disimpan",
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
	
	public void onClick$satuanKerugianAddButton(Event event) throws Exception {
		Map<String, String> args = Collections.singletonMap("name", "Satuan Kerugian:");
		Window textEntryWindow = (Window) Executions.createComponents("/common/TextEntry.zul", null, args);
		textEntryWindow.addEventListener(Events.ON_CHANGE, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				String satuanKerugianStr = (String) event.getData();
				
				// create a new object
				KerugianSatuan satuanKerugian = new KerugianSatuan();
				satuanKerugian.setEditedAt(asDate(getCurrentLocalDateTime()));
				satuanKerugian.setCreatedAt(asDate(getCurrentLocalDateTime()));
				satuanKerugian.setSatuan(satuanKerugianStr);
				
				// save object
				getKerugianSatuanDao().save(satuanKerugian);
				
				// display
				displaySatuanKerugian();
			}
		});

		textEntryWindow.doModal();				
	}

	public KejadianJenisDao getKejadianJenisDao() {
		return kejadianJenisDao;
	}


	public void setKejadianJenisDao(KejadianJenisDao kejadianJenisDao) {
		this.kejadianJenisDao = kejadianJenisDao;
	}


	public KejadianMotifDao getKejadianMotifDao() {
		return kejadianMotifDao;
	}


	public void setKejadianMotifDao(KejadianMotifDao kejadianMotifDao) {
		this.kejadianMotifDao = kejadianMotifDao;
	}

	public List<KejadianJenis> getKejadianJenisList() {
		return kejadianJenisList;
	}


	public void setKejadianJenisList(List<KejadianJenis> kejadianJenisList) {
		this.kejadianJenisList = kejadianJenisList;
	}


	public List<KejadianMotif> getKejadianMotifList() {
		return kejadianMotifList;
	}


	public void setKejadianMotifList(List<KejadianMotif> kejadianMotifList) {
		this.kejadianMotifList = kejadianMotifList;
	}

	/**
	 * @return the kerugianJenisDao
	 */
	public KerugianJenisDao getKerugianJenisDao() {
		return kerugianJenisDao;
	}

	/**
	 * @param kerugianJenisDao the kerugianJenisDao to set
	 */
	public void setKerugianJenisDao(KerugianJenisDao kerugianJenisDao) {
		this.kerugianJenisDao = kerugianJenisDao;
	}

	/**
	 * @return the kerugianKondisiDao
	 */
	public KerugianKondisiDao getKerugianKondisiDao() {
		return kerugianKondisiDao;
	}

	/**
	 * @param kerugianKondisiDao the kerugianKondisiDao to set
	 */
	public void setKerugianKondisiDao(KerugianKondisiDao kerugianKondisiDao) {
		this.kerugianKondisiDao = kerugianKondisiDao;
	}

	public List<KerugianJenis> getKerugianJenisList() {
		return kerugianJenisList;
	}

	public void setKerugianJenisList(List<KerugianJenis> kerugianJenisList) {
		this.kerugianJenisList = kerugianJenisList;
	}

	public LocalDateTime getCurrentLocalDateTime() {
		return currentLocalDateTime;
	}

	public void setCurrentLocalDateTime(LocalDateTime currentLocalDateTime) {
		this.currentLocalDateTime = currentLocalDateTime;
	}

	public SettingsDao getSettingsDao() {
		return settingsDao;
	}

	public void setSettingsDao(SettingsDao settingsDao) {
		this.settingsDao = settingsDao;
	}

	public Kotamaops getKotamaops() {
		return kotamaops;
	}

	public void setKotamaops(Kotamaops kotamaops) {
		this.kotamaops = kotamaops;
	}

	public Settings getSettings() {
		return settings;
	}

	public void setSettings(Settings settings) {
		this.settings = settings;
	}

	public ZoneId getZoneId() {
		return zoneId;
	}

	public void setZoneId(ZoneId zoneId) {
		this.zoneId = zoneId;
	}

	public List<KerugianKondisi> getKerugianKondisiList() {
		return kerugianKondisiList;
	}

	public void setKerugianKondisiList(List<KerugianKondisi> kerugianKondisiList) {
		this.kerugianKondisiList = kerugianKondisiList;
	}

	public KerugianSatuanDao getKerugianSatuanDao() {
		return kerugianSatuanDao;
	}

	public void setKerugianSatuanDao(KerugianSatuanDao kerugianSatuanDao) {
		this.kerugianSatuanDao = kerugianSatuanDao;
	}

	public List<KerugianSatuan> getKerugianSatuanList() {
		return kerugianSatuanList;
	}

	public void setKerugianSatuanList(List<KerugianSatuan> kerugianSatuanList) {
		this.kerugianSatuanList = kerugianSatuanList;
	}

}
