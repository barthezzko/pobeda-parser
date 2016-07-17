package com.barthezzko.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.barthezzko.service.PeriodRequestService;
import com.barthezzko.service.SearchResultService;

/**
 * Created with IntelliJ IDEA. User: barthezzko Date: 2/27/16 Time: 9:34 PM To
 * change this template use File | Settings | File Templates.
 */
public class Handler extends HttpServlet {

	SearchResultService pobedaService = new SearchResultService();
	PeriodRequestService periodic = new PeriodRequestService();
	private static final Logger logger = Logger
			.getLogger(Handler.class.getName());

	@Override
	public void service(HttpServletRequest req, HttpServletResponse res)
			throws IOException {
		if ("interesting".equals(req.getParameter("city"))){
			periodic.invokeInteresting();
		} else {
			String[] cities = (String[]) req.getParameterValues("city");
			Map<String, Integer> res2 = new HashMap<>();
			if (cities != null && cities.length > 0) {
				for (String city : cities) {
					logger.info("Fetching info for " + city);
					res2.putAll(pobedaService.invoke(city, "VKO", 9));
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						logger.log(Level.SEVERE, "Can't sleep: "  + e.getMessage());
					}
				}
			}
		}
		if ("true".equals(req.getParameter("all"))) {
			try {
				// pobedaService.email(pobedaService.invokeAll());
			} catch (Exception e) {
				e.printStackTrace(); // To change body of catch statement use
										// File | Settings | File Templates.
			}
		} else {
			// pobedaService.email(pobedaService.invoke(req.getParameter("from"),
			// req.getParameter("to"),
			// Integer.valueOf(req.getParameter("mon"))),
			// req.getParameter("from"), req.getParameter("to"));
		}

	}
}
