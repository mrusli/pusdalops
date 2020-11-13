package mil.pusdalops.persistence;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import mil.pusdalops.persistence.kejadian.rekap.dao.KejadianRekapDao;

public class App_Query {

	private static ApplicationContext ctx;
	
	public static void main(String[] args) throws Exception {
		ctx = new ClassPathXmlApplicationContext("CommonContext-Dao.xml");

		@SuppressWarnings("unused")
		KejadianRekapDao kejadianRekapDao = (KejadianRekapDao) ctx.getBean("kejadianRekapDao");

		// BigInteger count = kejadianRekapDao.countTipeKerugianPersonil(null, null, null);
		// System.out.println(count);
		
		// List<Kejadian> kejadianList = kejadianRekapDao.findAllKejadianKerugian(null, null);
		
		// KejadianDao kejadianDao = (KejadianDao) ctx.getBean("kejadianDao");
		// List<Kejadian> kejadianList = kejadianDao.findAllKejadian(true);
	}

}
