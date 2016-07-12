package com.barthezzko.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class PeriodRequestService {

	private static final Integer[] MONTHS_TO_SCAN = { 3, 4, 5, 6, 7, 8, 9 };
	private static final List<String> BAD_CITIES = Arrays.asList(new String[] {
			"VKO", "", "{}", "TJM", "PEE", "KUF", "SCW", "KVX", "EGO", "NJC",
			"CSY", "CEK", "UFA", "SGC", "IGT" });
	
	private static final Logger logger = Logger
			.getLogger(PeriodRequestService.class.getName());
	
	
	private SearchResultService service = new SearchResultService();
	
	public Map<String, Map<String, Integer>> invokeAll() throws Exception {
		Set<String> cities = SearchResultService.getAptList().keySet();
		cities.removeAll(BAD_CITIES);
		logger.info("looking for flights from/to " + cities);
		Map<String, Map<String, Integer>> res = new HashMap<>();
		for (String city : cities) {
			if (BAD_CITIES.contains(city))
				continue;
			for (int mon : MONTHS_TO_SCAN) {
				res.put("MOW_" + city + "_" + mon, service.invoke("VKO", city, mon));
				Thread.sleep(5001);
			}
		}
		return res;
	}

}
