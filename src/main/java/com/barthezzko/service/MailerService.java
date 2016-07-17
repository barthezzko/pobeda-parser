package com.barthezzko.service;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.barthezzko.model.JobInfo;

public class MailerService {
	
	private static final Logger logger = Logger
			.getLogger(MailerService.class.getName());

	
	public static void email(Map<String, Map<String, Integer>> map, JobInfo jobInfo) {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);
		String msgBody = "Job Result";
		for (Entry<String, Map<String, Integer>> searchResult : map.entrySet()) {
			msgBody += "<tr><th>" + searchResult.getKey()
					+ "</th><td><ul>";
			for (Entry<String, Integer> en : searchResult.getValue().entrySet()) {
				msgBody += "<li>" + en.getKey() + ": " + en.getValue() + "</li>";
			}
			msgBody += "</ul></td></tr>";
		}
		msgBody += "</table>";

		try {
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(
					"mailer@pobeda-aero.appspotmail.com", "pobeda-aero mailer"));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(
					"barthezzko@gmail.com", "Mikhail Baytsurov"));
			msg.setSubject("Job " +jobInfo.jobName + "#" + jobInfo.id + " : " + jobInfo.startDate + " (" + map.size()
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
