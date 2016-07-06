package com.barthezzko.simple;

import org.junit.Test;

import com.barthezzko.service.SearchResultService;

public class SimpleRequestCase {
	
	@Test
	public void request(){
		SearchResultService srs = new SearchResultService();
		srs.invoke("VKO", "CGN", 8);
	}
	
}
