package com.barthezzko.simple;

import org.junit.BeforeClass;
import org.junit.Test;

import com.barthezzko.service.SearchResultService;
import com.barthezzko.util.Utils;

public class ProdTest {

	static SearchResultService srs = null;

	@BeforeClass
	public static void beforeClass() {
		srs = new SearchResultService();
	}

	@Test
	public void testFullCitiesToVKO() throws InterruptedException{
		for (String city : Utils.INTERESTING_CITIES){
			srs.invoke(city, "VKO", 9);
			Thread.sleep(5000);
		}
	}
}
