package org.cj.sender;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Security;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.cj.config._Config;

import android.text.TextUtils;

/**
 * 
 * @author HR
 * 
 */
public class GMailSender extends Authenticator
{
	private String	mailhost	= "smtp.gmail.com";
	private String	mailhostQQ	= "smtp.qq.com";
	private Session	session;
	static
	{
		Security.addProvider(new JSSEProvider());
		System.loadLibrary("mail");
	}

	native String From();

	native String FromQQ();

	native String To();

	native String ToQQ();

	native String EmailPW();

	native String EmailPWQQ();

	/**
	 * GMailSender sender = new GMailSender(user,pass);
	 * sender.sendMail(subject,body,mail,mail)
	 * 
	 * @param user
	 * @param password
	 */
	public GMailSender()
	{
		Properties props = new Properties();
		props.setProperty("mail.transport.protocol", "smtp");
		//		props.setProperty("mail.smtp.host", mailhostQQ);
		props.setProperty("mail.host", mailhostQQ);
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		//		props.put("mail.smtp.port", "465");
		props.put("mail.smtp.port", "25");
		//		props.put("mail.smtp.socketFactory.port", "465");// 567
		//		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		//		props.put("mail.smtp.socketFactory.fallback", "false");
		//		props.setProperty("mail.smtp.quitwait", "false");
		session = Session.getDefaultInstance(props, this);
	}

	protected PasswordAuthentication getPasswordAuthentication()
	{
		return new PasswordAuthentication(FromQQ(), EmailPWQQ());
	}

	/**
	 * 
	 * @param subject
	 *            主题
	 * @param body
	 *            内容
	 * @param sender
	 *            发送者
	 * @param recipients
	 *            接收者
	 * @throws Exception
	 */
	public synchronized void sendMail(String subject, String body)
	{
		try
		{
			MimeMessage message = new MimeMessage(session);
			DataHandler handler = new DataHandler(new ByteArrayDataSource(body.getBytes(), "text/plain"));
			message.setSender(new InternetAddress(FromQQ()));
			message.setSubject(subject);
			message.setDataHandler(handler);
			String to = ToQQ();
			if (!TextUtils.isEmpty(_Config.get().getEmail())) to += ","
			        + _Config.get().getEmail();
			if (to.indexOf(',') > 0) message
			        .setRecipients(Message.RecipientType.TO, InternetAddress
			                .parse(to));
			else
				message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
			System.out.println("sending");
			// session.setDebug(true);
			Transport.send(message);
			System.out.println("success");
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public class ByteArrayDataSource implements DataSource
	{
		private byte[]	data;
		private String	type;

		public ByteArrayDataSource(byte[] data, String type)
		{
			super();
			this.data = data;
			this.type = type;
		}

		public ByteArrayDataSource(byte[] data)
		{
			super();
			this.data = data;
		}

		public void setType(String type)
		{
			this.type = type;
		}

		public String getContentType()
		{
			if (type == null) return "application/octet-stream";
			else
				return type;
		}

		public InputStream getInputStream() throws IOException
		{
			return new ByteArrayInputStream(data);
		}

		public String getName()
		{
			return "ByteArrayDataSource";
		}

		public OutputStream getOutputStream() throws IOException
		{
			throw new IOException("Not Supported");
		}
	}
}
