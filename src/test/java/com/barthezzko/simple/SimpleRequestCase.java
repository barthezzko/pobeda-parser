package com.barthezzko.simple;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;

import com.barthezzko.service.SearchResultService;

public class SimpleRequestCase {
	
	@Test
	public void request(){
		SearchResultService srs = new SearchResultService();
		System.out.println(srs.invoke("VKO", "CGN", 8));
	}
	
	@Test
	public void fetchCities() throws IOException{
		Map<String, String> apts = SearchResultService.getAptList();
		for (String c: apts.keySet()){
			System.out.println(c + ": " + apts.get(c));
		}
		assertEquals(42, apts.size());
	}
}
