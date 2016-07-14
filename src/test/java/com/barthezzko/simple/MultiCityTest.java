package com.barthezzko.simple;

import java.io.IOException;

import org.junit.Test;

import com.barthezzko.service.SearchResultService;

public class MultiCityTest {

	@Test
	public void fetchCities() throws IOException{
		String[] cities = {"CGN", "BGY", "FMM"};
		SearchResultService srs = new SearchResultService();
		for(String city: cities){
			srs.invoke(city, "VKO", 9);
		}
	}
	
}