package com.barthezzko.web;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: barthezzko
 * Date: 2/28/16
 * Time: 8:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class Queuer extends HttpServlet {



    @Override
    public void service(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String key = req.getParameter("all");
        if ("true".equals(key)){
            Queue queue = QueueFactory.getDefaultQueue();
            queue.add(TaskOptions.Builder.withUrl("/req").param("all", key));
            res.getWriter().write("done ");
            return;
        }
        res.getWriter().write("nothing to do");
    }
}
