package mil.pusdalops.persistence;

import java.math.BigInteger;
import java.time.LocalDateTime;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import mil.pusdalops.domain.kerugian.Pihak;
import mil.pusdalops.domain.kerugian.TipeKerugian;
import mil.pusdalops.persistence.kejadian.rekap.dao.KejadianRekapDao;

public class App_Query {

	private static ApplicationContext ctx;
	
	public static void main(String[] args) throws Exception {
		ctx = new ClassPathXmlApplicationContext("CommonContext-Dao.xml");

		KejadianRekapDao kejadianRekapDao = (KejadianRekapDao) ctx.getBean("kejadianRekapDao");

		
		BigInteger count = kejadianRekapDao.countKerugianByTipe(TipeKerugian.Material, Pihak.KITA, LocalDateTime.now(), LocalDateTime.now());
		System.out.println(count);
		System.out.println("hello!!!");
		
		// List<Kejadian> kejadianList = kejadianRekapDao.findAllKejadian(null, null);
		
		// KejadianDao kejadianDao = (KejadianDao) ctx.getBean("kejadianDao");
		// List<Kejadian> kejadianList = kejadianDao.findAllKejadian(true);
	}

}
