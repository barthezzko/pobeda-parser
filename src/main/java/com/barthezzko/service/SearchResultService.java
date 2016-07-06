package com.barthezzko.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

import com.barthezzko.model.SearchCriterion;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.logging.Logger;

/**
 * Hello world!
 *
 */
public class SearchResultService {

	private final static SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd");
	private static String AVL_MONTH_URL = "https://booking.pobeda.aero/AjaxMonthLowFareAvailaibility.aspx";
	private static String INITIAL_URL = "http://www.pobeda.aero/";
	private static String SEARCH_URL = "https://booking.pobeda.aero/ExternalSearch.aspx?marketType=%s&fromStation=%s&toStation=%s&beginDate=%s&endDate=%s&adultCount=1&childrenCount=0&infantCount=0&currencyCode=RUB&utm_source=pobeda&culture=ru-RU";

	private static List<String> NOT_VALID_PRICES = Arrays.asList(new String[] {
			"——", "Нет мест" });
	private static final Integer[] MONTHS_TO_SCAN = { 3, 4, 5, 6, 7, 8, 9 };
	private static final List<String> BAD_CITIES = Arrays.asList(new String[] {
			"VKO", "", "{}", "TJM", "PEE", "KUF", "SCW", "KVX", "EGO", "NJC",
			"CSY", "CEK", "UFA", "SGC", "IGT" });
	private static final Logger logger = Logger
			.getLogger(SearchResultService.class.getName());
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

	private static String getDateForMonth(int month, boolean end) {
		Date date = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.MONTH, month - 1);
		c.set(Calendar.DAY_OF_MONTH,
				end ? c.getActualMaximum(Calendar.DAY_OF_MONTH) : c
						.getActualMinimum(Calendar.DAY_OF_MONTH));
		return sdf.format(c.getTime());
	}

	public Map<String, Map<String, Integer>> invokeAll() throws Exception {
		Set<String> cities = getAptList().keySet();
		cities.removeAll(BAD_CITIES);
		logger.info("looking for flights from/to " + cities);
		Map<String, Map<String, Integer>> res = new HashMap<>();
		for (String city : cities) {
			if (BAD_CITIES.contains(city))
				continue;
			for (int mon : MONTHS_TO_SCAN) {
				res.put("MOW_" + city + "_" + mon, invoke("VKO", city, mon));
				Thread.sleep(5001);
			}
		}
		return res;
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

	public static void main(String[] args) throws IOException {
		logger.info(getAptList().toString());
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
		logger.info("SEARCH URL: " + searchUrl);
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
		logger.info("INJECT COOKIES: " + cookies.toString());
		Map<String, Integer> rsMap = fetch(
				safeString,
				searchCriterion.getOutboundDate(),
				"VKO".equals(searchCriterion.getTo()) ? searchCriterion
						.getFrom() : searchCriterion.getTo());
		return rsMap;

	}

	private static Map<String, String> getAptList() throws IOException {
		Map<String, String> apts = new HashMap<String, String>();
		Connection.Response res = Jsoup.connect(INITIAL_URL).method(Method.GET)
				.execute();
		Document doc = res.parse();
		Elements els = doc.select(".form-dropoutList__item");
		for (Element el : els) {
			apts.put(el.attr("data-iata"), el.select("span").html());
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
		System.out.println(searchCriterion.toJson());
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
		System.out.println("MONTH_AVL:" + AVL_MONTH_URL + " date " + dateSelected);
		System.out.println(doc.html());
		Elements els = doc.select("div [data-type=dayMonth]");
		for (Element el : els) {
			String strPrice = el.select("div.price").html()
					.replace("&nbsp;", "").replace("руб.", "").trim();
			if (NOT_VALID_PRICES.contains(strPrice))
				continue;
			int price = Integer.valueOf(strPrice);
			if (THRESHOLDS.get(toCity) != null
					&& THRESHOLDS.get(toCity) <= price)
				continue;
			results.put(el.attr("data-date"), price);
		}
		logger.info("fetched " + results.size() + " entries");
		return results;
	}

	public static void email(Map<String, Map<String, Integer>> map) {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);
		String msgBody = "Job Result";
		for (String setKey : map.keySet()) {
			msgBody += "<br/>----------------------<br/>" + setKey
					+ "<br/>-------------------";
			Map<String, Integer> flights = map.get(setKey);
			for (String date : flights.keySet()) {
				msgBody += "<br/><b>" + date + "</b> " + flights.get(date);
			}
		}

		try {
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(
					"mailer@pobeda-aero.appspotmail.com", "pobeda-aero mailer"));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(
					"barthezzko@gmail.com", "Mikhail Baytsurov"));
			msg.setSubject("Results from: " + new Date() + " (" + map.size()
					+ ")");
			msg.setContent(msgBody, "text/html; charset=utf-8");
			Transport.send(msg);

		} catch (AddressException e) {
			logger.info(e.getMessage());
		} catch (MessagingException e) {
			logger.info(e.getMessage());
		} catch (UnsupportedEncodingException e) {
			logger.info(e.getMessage());
		}

	}
}
