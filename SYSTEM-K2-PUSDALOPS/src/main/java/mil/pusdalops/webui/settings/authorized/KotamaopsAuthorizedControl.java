package mil.pusdalops.webui.settings.authorized;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import mil.pusdalops.domain.authorization.User;
import mil.pusdalops.domain.authorization.UserRole;
import mil.pusdalops.domain.kotamaops.Kotamaops;
import mil.pusdalops.domain.kotamaops.KotamaopsType;
import mil.pusdalops.domain.settings.Settings;
import mil.pusdalops.persistence.settings.dao.SettingsDao;
import mil.pusdalops.persistence.user.dao.UserDao;
import mil.pusdalops.persistence.userrole.dao.UserRoleDao;
import mil.pusdalops.webui.common.GFCBaseController;
import mil.pusdalops.webui.dialogs.KotamaopsListDialogData;

public class KotamaopsAuthorizedControl extends GFCBaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7750780354400307518L;
	
	private SettingsDao settingsDao;
	private UserDao userDao;
	private UserRoleDao userRoleDao;
	
	private Window kotamaopsAuthorizedWin;
	private Label formTitleLabel, infoResultlabel;
	private Textbox kotamaopsPeruntukanTextbox;
	private Button selectKotamaopsButton, saveSelectedKotamaopsButton;
	private Listbox authUserListbox;
	
	private Settings currentSettings;
	private List<User> users;
	private Kotamaops selectedKotamaops;
	
	private final long SETTINGS_DEFAULT_ID = 1L;
	
	public void onCreate$kotamaopsAuthorizedWin(Event event) throws Exception {
		formTitleLabel.setValue("Settings | Kotamaops");
		
		// get the current settings and set it for this controller
		setCurrentSettings(
				getSettingsDao().findSettingsById(SETTINGS_DEFAULT_ID));
		
		setSelectedKotamaops(
				getCurrentSettings().getSelectedKotamaops());
		
		// display the current selected / peruntukan kotamaops
		kotamaopsPeruntukanTextbox.setValue(
				getSelectedKotamaops().getKotamaopsName());
		
		// display the users associated with this kotamaops
		displayUserForKotamaops(getSelectedKotamaops());
	}
	
	private void displayUserForKotamaops(Kotamaops kotamaops) throws Exception {
		setUsers(
				getUserDao().findAllUsersByKotamaops(kotamaops));
		
		authUserListbox.setModel(
				new ListModelList<User>(getUsers()));
		authUserListbox.setItemRenderer(getAuthUserListitemRenderer());
	}

	private ListitemRenderer<User> getAuthUserListitemRenderer() {

		return new ListitemRenderer<User>() {
			
			@Override
			public void render(Listitem item, User user, int index) throws Exception {
				Listcell lc;
				
				// No.
				lc = new Listcell(String.valueOf(index+1)+".");
				lc.setParent(item);
				
				// Nama Login
				lc = new Listcell(user.getUserName());
				lc.setParent(item);
				
				// Aktif
				lc = new Listcell(user.isActive()? "Aktif" : "Non-Aktif");
				lc.setParent(item);
				
				// Edit
				lc = initEdit(new Listcell(), user);
				lc.setParent(item);
			}

			private Listcell initEdit(Listcell listcell, User user) {
				Button editButton = new Button();
				editButton.setLabel("...");
				editButton.setClass("listinfoEditButton");
				editButton.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						Map<String, User> args = Collections.singletonMap("user", user);
						Window userEditWin = (Window) Executions.createComponents(
								"/settings/authorized/KotamaopsAuthorizedUserDialog.zul", kotamaopsAuthorizedWin, args);
						userEditWin.addEventListener(Events.ON_OK, new EventListener<Event>() {

							@Override
							public void onEvent(Event event) throws Exception {
								User user = (User) event.getData();
								
								// save
								getUserDao().update(user);
								
								// load and display
								displayUserForKotamaops(user.getKotamaops());
							}
						});
						userEditWin.doModal();
					}
				});
				
				Button deleteButton = new Button();
				deleteButton.setLabel(" - ");
				deleteButton.setClass("listinfoEditButton");
				deleteButton.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						Messagebox.show("Hapus User: "+user.getUserName()+" ?", "Konfirmasi", 
								Messagebox.OK | Messagebox.CANCEL, 
								Messagebox.QUESTION, new EventListener<Event>() {
									
									@Override
									public void onEvent(Event event) throws Exception {
										if (event.getName().equals("onOK")) {
											try {
												// delete
												getUserDao().delete(user);												
												
												// display
												displayUserForKotamaops(getCurrentSettings().getSelectedKotamaops());
											} catch (Exception e) {
												throw new Exception("Tidak dapat dihapus. User: "+
														user.getUserName()+
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

	public void onAfterRender$authUserListbox(Event event) throws Exception {
		infoResultlabel.setValue("Total: "+authUserListbox.getItemCount()+" pengguna (user)");
	}
	
	public void onClick$selectKotamaopsButton(Event event) throws Exception {
		KotamaopsListDialogData dialogData = new KotamaopsListDialogData();
		dialogData.setKotamaopsType(KotamaopsType.PUSDALOPS);
		dialogData.setKotamaopsToExclude(new ArrayList<Kotamaops>());
		
		Map<String, KotamaopsListDialogData> args = Collections.singletonMap("dialogData", dialogData);
		Window kotamaopsSelectWin = (Window) Executions.createComponents(
				"/dialogs/KotamaopsListDialog.zul", kotamaopsAuthorizedWin, args);
		kotamaopsSelectWin.addEventListener(Events.ON_CHANGE, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				Kotamaops selKotamaops = (Kotamaops) event.getData();
			
				kotamaopsPeruntukanTextbox.setValue(selKotamaops.getKotamaopsName());
				kotamaopsPeruntukanTextbox.setAttribute("selKotamaops", selKotamaops);
				
				// hide the pilih button
				selectKotamaopsButton.setVisible(false);
				
				// make the save button visible
				saveSelectedKotamaopsButton.setVisible(true);
			}
		});
		kotamaopsSelectWin.doModal();
	}
	
	public void onClick$saveSelectedKotamaopsButton(Event event) throws Exception {
		Messagebox.show("Simpan Peruntukan Kotamaops?", "Konfirmasi", 
				Messagebox.OK | Messagebox.CANCEL, 
				Messagebox.QUESTION, new EventListener<Event>() {
					
					@Override
					public void onEvent(Event event) throws Exception {
						if (event.getName().equals("onOK")) {
							Kotamaops selKotamaops = 
									(Kotamaops) kotamaopsPeruntukanTextbox.getAttribute("selKotamaops");
							
							// update the settings with the selected kotamaops
							getCurrentSettings().setSelectedKotamaops(selKotamaops);
							getSettingsDao().update(getCurrentSettings());
							
							// display the users for this Kotamaops
							displayUserForKotamaops(selKotamaops);
						}
						
						// hide the save button
						saveSelectedKotamaopsButton.setVisible(false);
						
						// make the pilih button visible
						selectKotamaopsButton.setVisible(true);
						
					}
				});
	}
	
	public void onClick$newButton(Event event) throws Exception {
		// determine the kotamaops for the new user
		// -- in case the kotamaops changed, need to request from db
		Settings settings = getSettingsDao().findSettingsById(SETTINGS_DEFAULT_ID);
		setSelectedKotamaops(settings.getSelectedKotamaops());
		
		User user = new User();
		user.setKotamaops(getSelectedKotamaops());
		user.setActive(true);
		
		// ONLY for NEW USER
		// index 3L in the db - 'ROLE_USER'
		UserRole userRoleUser = getUserRoleDao().findUserRoleById(3L);
		
		Set<UserRole> userRoles = new HashSet<UserRole>();
		userRoles.add(userRoleUser);
		user.setUserRoles(userRoles);
		
		Map<String, User> args = Collections.singletonMap("user", user);
		Window userCreateWin = (Window) Executions.createComponents(
				"/settings/authorized/KotamaopsAuthorizedUserDialog.zul", kotamaopsAuthorizedWin, args);
		userCreateWin.addEventListener(Events.ON_OK, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				User user = (User) event.getData();
				
				// save
				getUserDao().save(user);
				
				// load and display
				displayUserForKotamaops(getSelectedKotamaops());
			}
		});
		
		
		userCreateWin.doModal();
	}

	public SettingsDao getSettingsDao() {
		return settingsDao;
	}

	public void setSettingsDao(SettingsDao settingsDao) {
		this.settingsDao = settingsDao;
	}

	public Settings getCurrentSettings() {
		return currentSettings;
	}

	public void setCurrentSettings(Settings currentSettings) {
		this.currentSettings = currentSettings;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	/**
	 * @return the userRoleDao
	 */
	public UserRoleDao getUserRoleDao() {
		return userRoleDao;
	}

	/**
	 * @param userRoleDao the userRoleDao to set
	 */
	public void setUserRoleDao(UserRoleDao userRoleDao) {
		this.userRoleDao = userRoleDao;
	}

	public Kotamaops getSelectedKotamaops() {
		return selectedKotamaops;
	}

	public void setSelectedKotamaops(Kotamaops selectedKotamaops) {
		this.selectedKotamaops = selectedKotamaops;
	}

}
