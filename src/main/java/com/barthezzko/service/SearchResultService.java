package com.barthezzko.service;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.barthezzko.model.SearchCriterion;
import com.barthezzko.valid.CommonValidators;

/**
 * Hello world!
 *
 */
public class SearchResultService {

	private static String AVL_MONTH_URL = "https://booking.pobeda.aero/AjaxMonthLowFareAvailaibility.aspx";
	private static String INITIAL_URL = "http://www.pobeda.aero/";
	private static String SEARCH_URL = "https://booking.pobeda.aero/ExternalSearch.aspx?marketType=%s&fromStation=%s&toStation=%s&beginDate=%s&endDate=%s&adultCount=1&childrenCount=0&infantCount=0&currencyCode=RUB&utm_source=pobeda&culture=ru-RU";

	private static List<String> NOT_VALID_PRICES = Arrays.asList(new String[] {
			"——", "Нет мест" });

	private static final Logger logger = Logger
			.getLogger(SearchResultService.class.getName());

	private static String getDateForMonth(int month, boolean end) {
		Date date = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.MONTH, month - 1);
		c.set(Calendar.DAY_OF_MONTH,
				end ? c.getActualMaximum(Calendar.DAY_OF_MONTH) : c
						.getActualMinimum(Calendar.DAY_OF_MONTH));
		return new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
	}

	public Map<String, Integer> invoke(String from, String to, int mon) {
		try {
			return invoke(buildCriterion(from, to, getDateForMonth(mon, false),
					getDateForMonth(mon, true)));
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
		return new HashMap<>();
	}

	public Map<String, Integer> invoke(SearchCriterion searchCriterion)
			throws Exception {
		logger.info(searchCriterion.getFrom() + " > " + searchCriterion.getTo()
				+ " " + searchCriterion.getOutboundDate() + " "
				+ searchCriterion.getInboundDate());
		String searchUrl = String.format(SEARCH_URL,
				searchCriterion.getTripType(), searchCriterion.getFrom(),
				searchCriterion.getTo(), searchCriterion.getInboundDate(),
				searchCriterion.getOutboundDate());
		logger.fine("SEARCH URL: " + searchUrl);
		Map<String, String> cookies = Jsoup
				.connect(searchUrl)
				.data("t", new Date().getTime() + "")
				.userAgent(
						"Mozilla/5.0 (Linux; U; Android 4.0.3; ko-kr; LG-L160L Build/IML74K) AppleWebkit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30")
				.method(Method.GET).execute().cookies();
		String sessionId = cookies.get("ASP.NET_SessionId");
		String skysales = cookies.get("skysales");
		String safeString = "_ga=1; _ym_uid=1; ASP.NET_SessionId=" + sessionId
				+ "; skysales=" + skysales + "; _ym_isad=1;";
		safeString += URLEncoder.encode(searchCriterion.toJson(), "UTF-8");
		logger.fine("INJECT COOKIES: " + cookies.toString());
		Map<String, Integer> rsMap = fetch(
				safeString,
				searchCriterion.getOutboundDate(),
				"VKO".equals(searchCriterion.getTo()) ? searchCriterion
						.getFrom() : searchCriterion.getTo());
		return rsMap;
	}

	public static Map<String, String> getAptList() throws IOException {
		Map<String, String> apts = new HashMap<String, String>();
		Connection.Response res = Jsoup.connect(INITIAL_URL).method(Method.GET)
				.execute();
		Document doc = res.parse();
		Elements els = doc.select("div.form-dropoutList__item");
		for (Element el : els) {
			if (CommonValidators.validIataCode(el.attr("data-iata"))) {
				apts.put(el.attr("data-iata"), el.select("span").html());
			}
		}
		return apts;
	}

	private static SearchCriterion buildCriterion(String fromApt, String toApt,
			String fromDate, String toDate) {
		SearchCriterion searchCriterion = new SearchCriterion();
		searchCriterion.setFrom(fromApt);
		searchCriterion.setTo(toApt);
		searchCriterion.setInboundDate(fromDate);
		searchCriterion.setOutboundDate(toDate);
		searchCriterion.setSelectedADT(1);
		searchCriterion.setTripType("RoundTrip");
		searchCriterion.setCurrencyCode("RUB");
		searchCriterion.setAnyFieldWithData("false");
		return searchCriterion;
	}

	private static Map<String, Integer> fetch(String searchCriterion,
			String dateSelected, String toCity) throws Exception {
		Map<String, Integer> results = new HashMap<String, Integer>();
		Connection.Response res = Jsoup
				.connect(AVL_MONTH_URL)
				.data("dateSelected", dateSelected, "indexTrip", "1")
				.userAgent(
						"Mozilla/5.0 (Linux; U; Android 4.0.3; ko-kr; LG-L160L Build/IML74K) AppleWebkit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30")
				.cookie("userSearchConfiguration", searchCriterion)
				.method(Method.GET).execute();
		Document doc = res.parse();
		logger.fine("MONTH_AVL:" + AVL_MONTH_URL + " date "
				+ dateSelected);
		Elements els = doc.select("div [data-type=dayMonth]");
		for (Element el : els) {
			String strPrice = el.select("div.price").html()
					.replace("&nbsp;", "").replace("руб.", "").trim();
			if (NOT_VALID_PRICES.contains(strPrice))
				continue;
			int price = Integer.valueOf(strPrice);
			if (price < 5000)
				results.put(el.attr("data-date"), price);
		}
		logger.info(toCity +  ": fetched " + results.size() + " entries");
		return results;
	}
}
