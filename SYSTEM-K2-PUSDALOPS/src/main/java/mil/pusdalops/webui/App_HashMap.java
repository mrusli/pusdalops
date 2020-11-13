package mil.pusdalops.webui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mil.pusdalops.domain.kerugian.KerugianJenis;

public class App_HashMap {

	public static void main(String[] args) {
		System.out.println("Hello World!!!");

		Map<Boolean, List<KerugianJenis>> kerugianJenisRefMap = new HashMap<Boolean, List<KerugianJenis>>();
		
		@SuppressWarnings("unused")
		List<KerugianJenis> kerugianJenisRefMapList = kerugianJenisRefMap.put(Boolean.TRUE, new ArrayList<KerugianJenis>());
		kerugianJenisRefMap.get(Boolean.TRUE).add(new KerugianJenis());
		kerugianJenisRefMap.get(Boolean.TRUE).add(new KerugianJenis());
		kerugianJenisRefMap.get(Boolean.TRUE).add(new KerugianJenis());
		
		System.out.println(kerugianJenisRefMap.get(Boolean.TRUE));	
	}
}
