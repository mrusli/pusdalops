package mil.pusdalops.persistence;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import mil.pusdalops.domain.authorization.User;
import mil.pusdalops.persistence.user.dao.UserDao;

public class App_User {

	private static ApplicationContext ctx;

	public static void main(String[] args) throws Exception {
		User user;
		String usernameToFind = "PUSAT";
		
		ctx = new ClassPathXmlApplicationContext("CommonContext-Cloud-Dao.xml");

		UserDao userDao = (UserDao) ctx.getBean("userDao");

		user = userDao.findUserByUsername(usernameToFind);

		System.out.println("findUserByUsername: "+user);
		
		user = userDao.findUserById(1L);
		
		System.out.println("findUserById: "+user);
		
		List<User> userList = userDao.findAllUsers();
		
		System.out.println("findAllUsers: "+userList);
	}

}
