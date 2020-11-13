package mil.pusdalops.domain;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

import mil.pusdalops.domain.authorization.User;

public class App_User {

	public static void main(String[] args) throws Exception {
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().configure("hibernate-cloud.cfg.xml").build();

		Metadata metadata = new MetadataSources(serviceRegistry).getMetadataBuilder().build();

		SessionFactory sessionFactory = metadata.getSessionFactoryBuilder().build();

		try {
			Session session = sessionFactory.openSession();
			
			User user = session.get(User.class, 1L);
			
			System.out.println(user.getUserName());
			
		} catch (Exception e) {
			throw e;
		} finally {
			sessionFactory.close();
		}

		
	}

}
