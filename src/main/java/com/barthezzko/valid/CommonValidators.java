package com.barthezzko.valid;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonValidators {

	public static boolean validIataCode(String userNameString) {
		Pattern p = Pattern.compile("^[A-Z]{3}$");
		Matcher m = p.matcher(userNameString);
		return m.matches();
	}
}
