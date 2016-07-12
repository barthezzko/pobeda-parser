package com.barthezzko.thresholds;

import java.util.HashMap;
import java.util.Map;

public class Thresholds {
	
	private static final Map<String, Integer> THRESHOLDS = new HashMap<>();

	
	static {
		THRESHOLDS.put("ASF", 1500);
		THRESHOLDS.put("ZLP", 2500);
		THRESHOLDS.put("QDU", 2500);
		THRESHOLDS.put("ROV", 1500);
		THRESHOLDS.put("FMM", 2500);

		THRESHOLDS.put("AER", 1500);
		THRESHOLDS.put("KGD", 1500);
		THRESHOLDS.put("ZMU", 2500);
		THRESHOLDS.put("MCX", 1500);
		THRESHOLDS.put("SVX", 1500);

		THRESHOLDS.put("KRR", 1500);
		THRESHOLDS.put("QLV", 2500);
		THRESHOLDS.put("VOG", 1500);
		THRESHOLDS.put("OGZ", 1500);
		THRESHOLDS.put("LED", 1500);

		THRESHOLDS.put("BGY", 2500);
		THRESHOLDS.put("BTS", 2500);
		THRESHOLDS.put("CGN", 2500);
		THRESHOLDS.put("NAL", 1500);

		THRESHOLDS.put("OVB", 2500);
	}

}
