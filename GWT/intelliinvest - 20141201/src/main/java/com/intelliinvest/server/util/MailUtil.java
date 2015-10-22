package com.intelliinvest.server.util;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.apache.log4j.Logger;

import com.intelliinvest.client.EncryptUtil;
import com.sun.mail.util.MailSSLSocketFactory;


/**
 * Utility class for sending email messages.
 * 
 * @author kijain
 */
public class MailUtil {

	private static Logger logger = Logger.getLogger(MailUtil.class);

	static String ERR_INVALID_EMAIL_ADDRESS = "Invalid Email Address: ";
	static String ERR_VALID_BUT_UNSENT_EMAIL_ADDRESS = "Valid but Unsent Email Address: ";
	static String SENT_EMAILED_ADDRESS = "Sent Email Address";

	/**
	 * Send an email message (with no attachment) to one or more recipients.
	 */
	public static boolean sendMail(String mailHost, String sender, String password, 
			String[] recipients, String subject, String message) {
		return sendMail(mailHost, sender, password, recipients, null, subject, message,
				null);
	}

	/**
	 * Send an email message (with attachment(s)) to one or more recipients without
	 * cc or bcc.
	 */
	public static boolean sendMail(String mailHost, String sender, String password, 
			String[] recipients, String subject, String message,
			String[] attachment) {
		return sendMail(mailHost, sender, password, recipients, null, subject, message,
				attachment);
	}

	/**
	 * Send an email with an attachment to one or more recipients. Returns false
	 * if mail is rejected by mail server.
	 */
	@SuppressWarnings("static-access")
	public static boolean sendMail(String mailHost, final String sender, final String password, 
			String[] recipients, Message.RecipientType[] rcptstypes,
			String subject, String message, String[] attachment) {
		boolean returnValue = true;
		try {
			logger.info("Mail Details : sender " + sender + " recipients " + recipients + " subject " + subject + " message " + message );
			logger.info("Started Sending Email");

			MailSSLSocketFactory sf = new MailSSLSocketFactory();
			sf.setTrustAllHosts(true);
			
			Properties props = System.getProperties();
			props.put("mail.smtp.host", mailHost);
			props.put("mail.smtp.auth", true);
			props.put("mail.smtp.port", "465");
			props.put("mail.transport.protocol", "smtp");
			props.put("mail.smtp.ssl.enable", true);
			props.put("mail.smtp.ssl.socketFactory", sf);

			Session session = Session.getInstance(props, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					try{
						return new PasswordAuthentication(sender, EncryptUtil.decrypt(password));
					}catch(Exception e){
						throw new RuntimeException("Error while decrypting " + e.getMessage());
					}
				}
			});

			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(sender));
			for (int i = 0; i < recipients.length; i++) {
				if ((rcptstypes != null) && (i < rcptstypes.length))
					msg.addRecipients(rcptstypes[i], recipients[i]);
				else
					msg.addRecipients(Message.RecipientType.TO, recipients[i]);
			}
			msg.setSubject(subject);

			// create the Multipart and add its parts to it
			Multipart mp = new MimeMultipart();
			
			// create and fill the first message part
			BodyPart mbp1 = new MimeBodyPart();			
			mbp1.setContent(message,"text/html;charset=UTF-8");
			mp.addBodyPart(mbp1);
			if (attachment != null) {
				for (int attachNo=0;attachNo<attachment.length;attachNo++){
					if (attachment[attachNo]!=null && !attachment[attachNo].equals("")){
						BodyPart mbp2=new MimeBodyPart();
						FileDataSource fds = new FileDataSource(attachment[attachNo]);
						int sep=attachment[attachNo].lastIndexOf('/');
						String fname=attachment[attachNo].substring(sep + 1);
						mbp2.setDataHandler(new DataHandler(fds));					
						int i=0;
						String fNameEncoded="";
						String encodingStr="=?UTF-8?Q?";
						boolean encFlag=false;
						int txtLen=1;
						while (i<fname.length()){
							String fnameTruncated=(fname.length()>=i+txtLen?fname.substring(i,i+txtLen):fname.substring(i));
							String fEncTruncated=MimeUtility.encodeText(fnameTruncated, "UTF-8", "Q");
							if (fEncTruncated.contains(encodingStr)){
								fNameEncoded+=fEncTruncated.substring(encodingStr.length(),fEncTruncated.length()-2);
								encFlag=true;
							}
							else
								fNameEncoded+=fEncTruncated;
							i+=txtLen;
						}
						if (encFlag)
							fNameEncoded=encodingStr+fNameEncoded.replaceAll("_","=5F")+"?=";
						mbp2.setFileName(fNameEncoded);
						mp.addBodyPart(mbp2);
					}
				}
			}
			msg.setContent(mp);
			msg.setSentDate(new Date());
			Transport trans = session.getTransport("smtp");
			trans.connect();
			if (!trans.isConnected()) {
				logger.error("Not able to connect to mail server");
				return false;
			}
			trans.send(msg);
			trans.close();
			logger.info("Finished Sending Email");

		} catch (MessagingException mex) {
			logger.error("Exception occurred while sending mail ..." + mex.getMessage());
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
			}
			Exception ex = mex;
			do {
				if (ex instanceof SendFailedException) {
					SendFailedException sfex = (SendFailedException) ex;
					Address[] invalid = sfex.getInvalidAddresses();
					if (invalid != null) {
						returnValue = false;
						StringBuffer invalidAddress = new StringBuffer();
						for (int i = 0; i < invalid.length; i++) {
							if (i > 0)
								invalidAddress.append(",");
							invalidAddress.append(invalid[i]);
						}
						logger.error(ERR_INVALID_EMAIL_ADDRESS + ":"
								+ invalidAddress.toString());
					}
					Address[] validUnsent = sfex.getValidUnsentAddresses();
					if (validUnsent != null) {
						returnValue = false;
						StringBuffer unsentAddress = new StringBuffer();
						for (int i = 0; i < validUnsent.length; i++) {
							if (i > 0)
								unsentAddress.append(",");
							unsentAddress.append(validUnsent[i]);
						}
						logger.error(ERR_VALID_BUT_UNSENT_EMAIL_ADDRESS + ":"
								+ unsentAddress.toString());
					}
					Address[] validSent = sfex.getValidSentAddresses();
					if (validSent != null) {
						StringBuffer sentAddress = new StringBuffer();
						for (int i = 0; i < validSent.length; i++) {
							if (i > 0)
								sentAddress.append(",");
							sentAddress.append(validSent[i]);
						}
						logger.info("Finished Sending Email");						
					}
				}
				if (ex instanceof MessagingException)
					ex = ((MessagingException) ex).getNextException();
				else
					ex = null;
			} while (ex != null);
		} catch (UnsupportedEncodingException e) {
			logger.error("Exception occurred while sending mail ... " + e.getMessage());
			logger.error(ExceptionUtil.getErrorStack(e));		
		}catch (Exception e) {
			logger.error("Error sending mail" + e.getMessage());
		}
		return returnValue;

	}
	
	public static void main(String[] args) throws Exception {
		MailUtil.sendMail("java.cloudserverdns.com", "intellii@intelliinvest.com", "e0202b9bd284497bb5e9709cdf77f68a", new String[]{"raja.rengasamy@gmail.com"}, "test from java", "test from java");
	}

}
