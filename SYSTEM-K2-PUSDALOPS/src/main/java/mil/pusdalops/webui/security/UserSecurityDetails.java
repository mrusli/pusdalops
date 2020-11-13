package mil.pusdalops.webui.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class UserSecurityDetails extends User {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3057935641728931129L;

	private mil.pusdalops.domain.authorization.User loginUser;
	
	public UserSecurityDetails(String username, String password, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
	}

	public UserSecurityDetails(String username, String password, Collection<? extends GrantedAuthority> authorities, 
			mil.pusdalops.domain.authorization.User dbUser) {

		super(username, password, authorities);

		setLoginUser(dbUser);
	}

	public mil.pusdalops.domain.authorization.User getLoginUser() {
		return loginUser;
	}

	public void setLoginUser(mil.pusdalops.domain.authorization.User loginUser) {
		this.loginUser = loginUser;
	}
	
}
