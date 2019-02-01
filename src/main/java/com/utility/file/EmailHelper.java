/*
 *
 */
package com.utility.file;

import java.io.IOException;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;

import com.utility.selenium.ApplicationProperties;
import com.utility.selenium.BaseTestScript;

/**
 * The Class EmailHelper.
 * This class is use for Email Sending purpose all methods and variables are use for same
 *
 * @author Deepak.Rathod
 */
public class EmailHelper
{

	/** The pass. */
	String pass = "Automation@123";

	/** The logger. */
	protected Logger logger = Logger.getLogger(this.getClass());


	/**
	 * The Full Test case Retry Set.
	 *
	 * @author Deepak.Rathod
	 */

	/**
	 * Mail configuration.
	 *
	 * @throws IOException
	 *         Signals that an I/O exception has occurred.
	 * @throws MessagingException
	 *         the messaging exception
	 */
	public void mailConfiguration() throws Exception
	{
		if (!("No").equalsIgnoreCase(ApplicationProperties.getInstance().getProperty("report.with.mail")))
		{
			if (!"smtp.gmail.com".equalsIgnoreCase(ApplicationProperties.getInstance().getProperty("mail.server")))
			{
				sendMail();
			}
			else
			{
				sendMailViaGmail();
			}
		}
	}


	/**
	 * Send mail.
	 *
	 * @throws IOException
	 *         Signals that an I/O exception has occurred.
	 * @throws MessagingException
	 *         the messaging exception
	 */
	public void sendMail() throws Exception
	{
		Properties properties = System.getProperties();
		properties.setProperty("mail.smtp.host", "localhost");
		Session session = Session.getDefaultInstance(properties);
		MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress("deepak.rathod@automation.com"));
		InternetAddress[] iAdressArray = InternetAddress.parse(BaseTestScript.MAILTO);
		message.setRecipients(Message.RecipientType.CC, iAdressArray);
		message.setSubject(BaseTestScript.MAILSUBPREFIX+" " + BaseTestScript.MAILSUBJECT + " at" + BaseTestScript.dateAndSystemTime("dd:MMM:YYYY:hh:ss"));
		message.setContent("This email is send by automation, Automation will execute this mail after finished all test cases."
				+ "Purpose of this email is for notification", "text/html");
		Transport.send(message);
		logger.info("Sent message successfully....");
	}

	/**
	 * Send mail via gmail.
	 *
	 * @throws IOException
	 *         Signals that an I/O exception has occurred.
	 * @throws MessagingException
	 *         the messaging exception
	 */
	public void sendMailViaGmail() throws Exception
	{
		// Get session Objects and set properties.
		Properties properties = System.getProperties();
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.host", BaseTestScript.MAILHOST);
		properties.put("mail.smtp.user", BaseTestScript.MAILFROM);
		properties.put("mail.smtp.password", pass);
		properties.put("mail.smtp.port", "465");
		properties.put("mail.smtp.socketFactory.port", "465");
		properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		properties.put("mail.smtp.auth", "true");

		Session session = Session.getDefaultInstance(properties,
				new javax.mail.Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(BaseTestScript.MAILFROM,pass);
			}
		});

		MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress(BaseTestScript.MAILFROM));
		InternetAddress[] iAdressArray = InternetAddress.parse(BaseTestScript.MAILTO);
		message.setRecipients(Message.RecipientType.TO, iAdressArray);
		message.setSubject(BaseTestScript.MAILSUBPREFIX+" - "+ BaseTestScript.MAILSUBJECT);

		//3) create MimeBodyPart object and set your message text
		BodyPart messageBodyPart1 = new MimeBodyPart();
		messageBodyPart1.setContent("<b>This is Auto Generated Mail.</b> You receive this mail because your email id is in recipient list. "
				+ "We notify you that CME EDB Automation Execution is finshed."
				+ "<br> Along with this email 'Framework' it self <b>attached one zip</b> file "
				+ "(CME-EDB-Automation-Report.zip).<br><br> Which contains <b>Report.html</b> file as part of execution report.<br>"
				+ "Inside this report we can view steps of automation execution such as how many feature files are execute, how many scenaiores are executes and we are also monitor that how many steps executed and thoase ar logged into result.<br>"
				+ "<br><b>PFA and Unzip it for more details.</b><br>"
				+ "<br>"
				+ "<br>"
				+ "Thank you,<br>"
				+ "CME-EDB Automation Team,<br>"
				+ "GSLab.","text/html");

		//4) create new MimeBodyPart object and set DataHandler object to this object
		MimeBodyPart messageBodyPart2 = new MimeBodyPart();

		DataSource source = new FileDataSource(BaseTestScript.REPORTLOCATION+"\\CME-EDB-Automation-Report.zip");
		messageBodyPart2.setDataHandler(new DataHandler(source));
		messageBodyPart2.setFileName("CME-EDB-Automation-Report.zip");

		//5) create Multipart object and add MimeBodyPart objects to this object
		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(messageBodyPart1);
		multipart.addBodyPart(messageBodyPart2);

		//6) set the multiplart object to the message object
		message.setContent(multipart );

		//7) send message
		Transport.send(message);
		logger.info("Sent message successfully....");

	}

}
