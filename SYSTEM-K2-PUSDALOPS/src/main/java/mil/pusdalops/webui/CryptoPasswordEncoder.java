package mil.pusdalops.webui;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;

public class CryptoPasswordEncoder {

	public static void main(String[] args) {
		System.out.println("hello world!!! - password encoder");

		String password = "KODAM XIV";

		PasswordEncoder passwordEncoder = new PasswordEncoder() {

			public String encode(CharSequence rawPassword) {
				
				return BCrypt.hashpw(rawPassword.toString(), BCrypt.gensalt());
			}

			public boolean matches(CharSequence rawPassword, String encodedPassword) {
				
				return BCrypt.checkpw(rawPassword.toString(), encodedPassword);
			}

		};
		
		String encodedPassword = passwordEncoder.encode(password.toString());
		
		System.out.println("Password: '"+password+"' is encoded to: "+encodedPassword);
		
		boolean isMatched = passwordEncoder.matches(password.toString(), encodedPassword);
		
		System.out.println(isMatched ? "Match" : "No Match");

	}
}
