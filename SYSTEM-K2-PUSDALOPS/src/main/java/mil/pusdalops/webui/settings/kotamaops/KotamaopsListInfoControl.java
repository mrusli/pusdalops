package mil.pusdalops.webui.settings.kotamaops;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import mil.pusdalops.domain.kotamaops.Kotamaops;
import mil.pusdalops.persistence.kotamaops.dao.KotamaopsDao;
import mil.pusdalops.webui.common.GFCBaseController;

public class KotamaopsListInfoControl extends GFCBaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8194729544364348368L;

	private KotamaopsDao kotamaopsDao;
	
	private Window kotamaopsListInfoWin;
	private Label formTitleLabel, infoResultlabel;
	private Listbox kotamaopsListbox;
	
	private List<Kotamaops> kotamaopsList;
	
	private final int KOTAMAOPS_PUST_IDX = 0;
	private static final Logger log = Logger.getLogger(KotamaopsListInfoControl.class);
	
	public void onCreate$kotamaopsListInfoWin(Event event) throws Exception {
		formTitleLabel.setValue("Settings | Kotamaops");
		
		// load data
		loadKotamaops();
		
		// display
		displayKotamaops();
	}

	private void loadKotamaops() throws Exception {
		setKotamaopsList(
				getKotamaopsDao().findAllKotamaops());
		
		// remove kotamaops pusat
		getKotamaopsList().remove(KOTAMAOPS_PUST_IDX);
	}

	private void displayKotamaops() {
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
				} catch (MalformedURLException e) {
					log.error("Malformed Image URL");
				} catch (IOException e) {
					log.error("IO Image Error");
				}
								
				// logo image
				lc = new Listcell();
				lc.setImageContent(buffImg);					
				// lc.setImage("/img/logo/"+kotamaops.getImagedId());
				lc.setStyle("text-align: center;");
				lc.setParent(item);
				
				// Nama Kotamaops
				lc = new Listcell(kotamaops.getKotamaopsName());
				lc.setParent(item);
				
				// Alamat
				lc = initAddress(new Listcell(), kotamaops);
				lc.setParent(item);
				
				// Komunikasi
				lc = initKomunikasi(new Listcell(), kotamaops);
				lc.setParent(item);
				
				// Zona-Waktu
				lc = initZonaWaktu(new Listcell(), kotamaops);
				lc.setParent(item);
				
				// edit and delete
				lc = initEdit(new Listcell(), kotamaops);
				lc.setParent(item);
				
				item.setValue(kotamaops);
			}

			private Listcell initAddress(Listcell listcell, Kotamaops kotamaops) {
				Label lb1 = new Label();
				lb1.setPre(true);
				// System.out.println(kotamaops.getAddress01()==null ? " " : kotamaops.getAddress01() +" \n"+
				//		kotamaops.getAddress02());
				lb1.setValue(kotamaops.getAddress01()==null ? "" : kotamaops.getAddress01() +" \n"+
						kotamaops.getAddress02() +" \n"+
						kotamaops.getCity());
				// lb1.setValue(
				//		kotamaops.getAddress01()==null ? " " : kotamaops.getAddress01() +" \n "+
				//		kotamaops.getAddress02()==null ? " " : kotamaops.getAddress02() +" \n "+
				//		kotamaops.getCity()==null ? " " : kotamaops.getCity());
				lb1.setParent(listcell);
				
				return listcell;
			}

			private Listcell initKomunikasi(Listcell listcell, Kotamaops kotamaops) {
				Label lb1 = new Label();
				lb1.setMultiline(true);
				lb1.setValue(kotamaops.getPhone()==null ? " " : kotamaops.getPhone() + "\n" +
						kotamaops.getFax() + "\n" + 
						kotamaops.getEmail());
				lb1.setParent(listcell);

				return listcell;
			}

			private Listcell initZonaWaktu(Listcell listcell, Kotamaops kotamaops) {
				Label lb1 = new Label();
				lb1.setValue(kotamaops.getTimeZone()==null? " " : kotamaops.getTimeZone().toString());
				lb1.setParent(listcell);
				
				return listcell;
			}

			private Listcell initEdit(Listcell listcell, Kotamaops kotamaops) {
				Button editButton = new Button();
				editButton.setLabel("...");
				editButton.setClass("listinfoEditButton");
				editButton.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						Map<String, Kotamaops> args = Collections.singletonMap("kotamaops", kotamaops);
						Window kotamaopsDialogEditWin = (Window) Executions.createComponents(
								"/settings/kotamaops/KotamaopsDialog.zul", kotamaopsListInfoWin, args);
						kotamaopsDialogEditWin.addEventListener(Events.ON_OK, new EventListener<Event>() {

							@Override
							public void onEvent(Event event) throws Exception {
								// get the object
								Kotamaops editedkotamaops = (Kotamaops) event.getData();
								
								// update
								getKotamaopsDao().update(editedkotamaops);
								
								// load
								loadKotamaops();
								
								// display
								displayKotamaops();
							}
						});
						kotamaopsDialogEditWin.doModal();
					}
				});
				
				Button deleteButton = new Button();
				deleteButton.setLabel(" - ");
				deleteButton.setClass("listinfoEditButton");
				deleteButton.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						Messagebox.show("Hapus Kotamaops "+kotamaops.getKotamaopsName()+" ?", "Konfirmasi", 
								Messagebox.OK | Messagebox.CANCEL, 
								Messagebox.QUESTION, new EventListener<Event>() {
									
									@Override
									public void onEvent(Event event) throws Exception {
										if (event.getName().equals("onOK")) {
											try {
												// delete
												getKotamaopsDao().delete(kotamaops);												

												// load
												loadKotamaops();
												
												// display
												displayKotamaops();
											} catch (Exception e) {
												throw new Exception("Tidak dapat dihapus. Kotamaops "+kotamaops.getKotamaopsName()+
														" mempunyai hubungan data.");
											}
										}
									}
								}
						);
						
					}
				});
				
				// use Hlayout as parent for the buttons
				Hlayout hlayout = new Hlayout();
				editButton.setParent(hlayout);
				deleteButton.setParent(hlayout);
				
				hlayout.setParent(listcell);
				
				return listcell;
			}
		};
	}

	public void onAfterRender$kotamaopsListbox(Event event) throws Exception {
		infoResultlabel.setValue("Total: "+kotamaopsListbox.getItemCount()+" Kotamaops");
	}
		
	public void onClick$newButton(Event event) throws Exception {
		Map<String, Kotamaops> args = Collections.singletonMap("kotamaops", new Kotamaops());
		Window kotamaopsDialogWin = (Window) Executions.createComponents(
				"/settings/kotamaops/KotamaopsDialog.zul", kotamaopsListInfoWin, args);
		kotamaopsDialogWin.addEventListener(Events.ON_OK, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				// get the object
				Kotamaops kotamaops = (Kotamaops) event.getData();
				
				// save
				getKotamaopsDao().save(kotamaops);
				
				// load
				loadKotamaops();
				
				// display
				displayKotamaops();
			}
		});
		
		kotamaopsDialogWin.doModal();
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

	
	
}
