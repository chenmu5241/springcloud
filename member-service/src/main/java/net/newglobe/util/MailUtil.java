package net.newglobe.util;

import java.util.Date;
import java.util.Properties;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class MailUtil {

	@Autowired
	private SysConfig sysConfig;
	@Autowired
	private JavaMailSender mailSender;
	
	
	public boolean sendSms(String receiveMail, String title, String content) {
		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom(sysConfig.getMailFrom());
			message.setTo(receiveMail);
			message.setSubject(title);
			message.setText(content);
			mailSender.send(message);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean sendSms2(String receiveMail, String title, String content) {
		try {
			// 1. 创建参数配置, 用于连接邮件服务器的参数配置
			Properties props = new Properties(); // 参数配置
			props.setProperty("mail.transport.protocol", "smtp"); // 使用的协议（JavaMail规范要求）
			props.setProperty("mail.smtp.host", sysConfig.getMailSmtp()); // 发件人的邮箱的SMTP 服务器地址
			props.setProperty("mail.smtp.auth", "true"); // 需要请求认证

			// 2. 根据配置创建会话对象, 用于和邮件服务器交互
			Session session = Session.getDefaultInstance(props);
			// session.setDebug(true); // 设置为debug模式, 可以查看详细的发送 log
			// 1. 创建一封邮件
			MimeMessage message = new MimeMessage(session);
			// 2. From: 发件人
			message.setFrom(new InternetAddress(sysConfig.getMailAccount(), "熊先生", "UTF-8"));
			// 3. To: 收件人（可以增加多个收件人、抄送、密送）
			message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(receiveMail, "", "UTF-8"));
			// 4. Subject: 邮件主题
			message.setSubject(title, "UTF-8");
			// 5. Content: 邮件正文（可以使用html标签）
			message.setContent(content, "text/html;charset=UTF-8");
			// 6. 设置发件时间
			message.setSentDate(new Date());
			// 7. 保存设置
			message.saveChanges();

			// 4. 根据 Session 获取邮件传输对象
			Transport transport = session.getTransport();
			transport.connect(sysConfig.getMailAccount(), sysConfig.getMailPassword());
			// 6. 发送邮件, 发到所有的收件地址, message.getAllRecipients()
			// 获取到的是在创建邮件对象时添加的所有收件人, 抄送人, 密送人
			transport.sendMessage(message, message.getAllRecipients());
			// 7. 关闭连接
			transport.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}


}
