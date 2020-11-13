package mil.pusdalops.webui.settings.authorized;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import mil.pusdalops.domain.authorization.User;
import mil.pusdalops.persistence.user.dao.UserDao;
import mil.pusdalops.webui.common.GFCBaseController;
import mil.pusdalops.webui.security.PasswordEncoderImpl;

public class KotamaopsAuthorizedUserDialogControl extends GFCBaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9203549074075872034L;

	private UserDao userDao;
	
	private Window kotamaopsAuthorizedUserDialogWin;
	private Textbox namaKotamaopsTextbox, usernameTextbox, passwordTextbox;
	private Checkbox activeCheckbox;
	
	private User user;
	
	/* (non-Javadoc)
	 * @see org.zkoss.zk.ui.util.GenericForwardComposer#doAfterCompose(org.zkoss.zk.ui.Component)
	 */
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		// user
		setUser((User) arg.get("user"));
	}

	public void onCreate$kotamaopsAuthorizedUserDialogWin(Event event) throws Exception {
		if (getUser().getId().compareTo(Long.MIN_VALUE)==0) {
			// new user
			kotamaopsAuthorizedUserDialogWin.setTitle("Tambah User");
		} else {
			// existing user
			kotamaopsAuthorizedUserDialogWin.setTitle("Rubah User Info");			
		}
		
		// displayInfo
		displayUserInfo();
		
	}

	private void displayUserInfo() throws Exception {		
		if (getUser().getId().compareTo(Long.MIN_VALUE)==0) {
			// do nothing
		} else {
			User userByProxy = 
					getUserDao().findUserKotamaopsByProxy(getUser().getId());
			
			namaKotamaopsTextbox.setValue(userByProxy.getKotamaops().getKotamaopsName());
			activeCheckbox.setChecked(getUser().isActive());
			usernameTextbox.setValue(getUser().getUserName());
			passwordTextbox.setValue(getUser().getUserPassword());
		}
	}

	public void onClick$ecryptPasswordButton(Event event) throws Exception {
		// create PasswordEncoderImpl
		PasswordEncoderImpl passwordEncoder = new PasswordEncoderImpl();
		
		// encode the password
		String encodedPassword = passwordEncoder.encode(passwordTextbox.getValue());
		
		// display the encoded password
		passwordTextbox.setValue(encodedPassword);
		passwordTextbox.setAttribute("encrypted", true);
	}
	
	public void onClick$saveButton(Event event) throws Exception {
		if (usernameTextbox.getValue().isEmpty()) {
			throw new Exception("Nama user belum diisi.");
		}
		boolean isEncrypted = (boolean) passwordTextbox.getAttribute("encrypted");
		if (! isEncrypted) {
			throw new Exception("Password belum diisi dan/atau di-encrypt.");
		}
		
		// get the mod user data
		User modUser = getUser();
		modUser.setActive(activeCheckbox.isChecked());
		modUser.setUserName(usernameTextbox.getValue());
		modUser.setUserPassword(passwordTextbox.getValue());
		
		// send event
		Events.sendEvent(Events.ON_OK, kotamaopsAuthorizedUserDialogWin, modUser);
		
		// detach
		kotamaopsAuthorizedUserDialogWin.detach();
	}
	
	public void onClick$cancelButton(Event event) throws Exception {
		kotamaopsAuthorizedUserDialogWin.detach();
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	
}
