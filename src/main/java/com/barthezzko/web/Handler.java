package com.barthezzko.web;

import com.barthezzko.service.SearchResultService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: barthezzko
 * Date: 2/27/16
 * Time: 9:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class Handler extends HttpServlet {


    SearchResultService pobedaService = new SearchResultService();

    @Override
    public void service(HttpServletRequest req, HttpServletResponse res) throws IOException {
        if ("true".equals(req.getParameter("all"))){
            try {
                pobedaService.email(pobedaService.invokeAll());
            } catch (Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        } else {
           // pobedaService.email(pobedaService.invoke(req.getParameter("from"), req.getParameter("to"), Integer.valueOf(req.getParameter("mon"))), req.getParameter("from"), req.getParameter("to"));
        }

    }
}
