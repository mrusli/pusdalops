package mil.pusdalops.webui.security;

import java.io.Serializable;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordEncoderImpl implements PasswordEncoder, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -39253251195714178L;

	@Override
	public String encode(CharSequence rawPassword) {

		return BCrypt.hashpw(rawPassword.toString(), BCrypt.gensalt());
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		
		return BCrypt.checkpw(rawPassword.toString(), encodedPassword);
	}

}
