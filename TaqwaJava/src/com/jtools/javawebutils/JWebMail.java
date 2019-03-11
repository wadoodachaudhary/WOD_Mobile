package com.jtools.javawebutils;

import java.util.Date;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class JWebMail {

	public static boolean DEBUG = false;
	static final String CLASS_NAME = "TestMail";

	public JWebMail() {

		String from = "rhino@tonge.ca";
		String to = "rhino@tonge.ca";
		String cc = null;
		String bcc = null;
		String subject = "Test Message";
		String body = "This is a test message. Please ignore it.";
		String mailHost = "mail-hub.optonline.net";

		boolean msgSent = sendMessage(mailHost, from, to, cc, bcc, subject, body);
		System.out.println("msgSent = " + msgSent);
	}

	/**
	 * Method sendMessage() sends an email message using JavaMail.
	 * 
	 * @param fromAddress
	 *            the email address of the composer of the message
	 * @param toAddress
	 *            the email address(es) of the main recipient(s) of the message
	 * @param ccAddress
	 *            the email address(es) of the cc recipient(s) of the message
	 * @param bccAddress
	 *            the email address(es) of the bcc recipient(s) of the message
	 * @param subject
	 *            the subject of the email
	 * @param body
	 *            the body of the email
	 * @return true if the message was sent; false otherwise
	 */
	public static boolean sendMessage(String mailHost, String fromAddress, String toAddress, String ccAddress, String bccAddress, String subject, String body) {

		String METHOD_NAME = "sendMessage()";
		String mailer = "msgsend";

		try {

			Properties props = System.getProperties();
			// XXX - could use Session.getTransport() and Transport.connect()
			// XXX - assume we're using SMTP
			if (JWebUtils.isNotNull(mailHost))
				props.put("mail.smtp.host", mailHost);

			// Get a Session object
			Session session = Session.getDefaultInstance(props, null);
			if (DEBUG) {
				session.setDebug(true);
			}

			// construct the message
			Message msg = new MimeMessage(session);
			if (fromAddress != null) {
				msg.setFrom(new InternetAddress(fromAddress));
			} else {
				msg.setFrom();
			}

			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toAddress, false));
			if (ccAddress != null) {
				msg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(ccAddress, false));
			}
			if (bccAddress != null) {
				msg.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(bccAddress, false));
			}

			msg.setSubject(subject);

			msg.setText(body);

			msg.setHeader("X-Mailer", mailer);
			msg.setSentDate(new Date());

			// Try to send the email.
			try {
				Transport.send(msg);
			} catch (SendFailedException sf_excp) {
				sf_excp.printStackTrace();
				System.out.println(CLASS_NAME + "." + METHOD_NAME + " - Failed to send subscription due to error: " + sf_excp);
				return (false);
			}

			System.out.println(CLASS_NAME + "." + METHOD_NAME + " - Mail was sent successfully.");
		} catch (Exception excp) {
			excp.printStackTrace();
		}

		return (true);
	} // end sendMessage
} // end class
