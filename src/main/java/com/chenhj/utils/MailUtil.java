package com.chenhj.utils;





import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.Date;
import java.util.Properties;
/**邮件发送公共类
 * @author chenhongjie
 */
public class MailUtil {
	private String[] receiveMailAccount;
	private  String[] ccMailAccount;
	private String text;//邮件内容
	private String title;
	private String file;
	public MailUtil(String[] receiveMailAccount,String[] ccMailAccount){
		this.receiveMailAccount=receiveMailAccount;
		this.ccMailAccount=ccMailAccount;
	}
	public MailUtil(String[] receiveMailAccount){
		this.receiveMailAccount=receiveMailAccount;
	}
    public static final String sendEmailAccount = "project@sinozo.com";
    public static final String sendEmailPassword ="Star000000";
    public static final String myEmailSMTPHost = "smtp.sinozo.com";
    private static final String emailName="Sinozo项目管理邮箱";
    /**
     *发送邮件
     * @param title   邮件标题
     * @param text    邮件内容
     * @param files   文件路径  附件
     */
    public void sendMail(String title,String text,String files){
    	this.title=title;
    	this.text=text;
    	this.file=files;
    	// 1. 创建参数配置, 用于连接邮件服务器的参数配置
        try {
    	Properties props = new Properties();                    // 参数配置
        props.setProperty("mail.transport.protocol", "smtp");   // 使用的协议（JavaMail规范要求）
        props.setProperty("mail.smtp.host",myEmailSMTPHost);   // 发件人的邮箱的 SMTP 服务器地址
        props.setProperty("mail.smtp.auth", "true");            // 需要请求认证
        Session session = Session.getDefaultInstance(props);
        session.setDebug(false);// 设置为debug模式, 可以查看详细的发送 log
        // 3. 创建一封邮件
         MimeMessage message = null;
		 message= createMimeMessage(session);
        // 4. 根据 Session 获取邮件传输对象
        Transport transport = session.getTransport();
        
        transport.connect(sendEmailAccount,sendEmailPassword);
        // 6. 发送邮件, 发到所有的收件地址, message.getAllRecipients() 获取到的是在创建邮件对象时添加的所有收件人, 抄送人, 密送人
        transport.sendMessage(message, message.getAllRecipients());
        // 7. 关闭连接
        transport.close();
        } catch (Exception e) {
			e.printStackTrace();
		}
    }
    /**
     * 创建一封只包含文本的简单邮件
     *
     * @param session 和服务器交互的会话
     * @param sendMail 发件人邮箱
     * @param receiveMail 收件人邮箱
     * @return
     * @throws Exception
     */
    private  MimeMessage createMimeMessage(Session session){
    	try {
        // 1. 创建一封邮件
        MimeMessage message = new MimeMessage(session);
        // 2. From: 发件人
        message.setFrom(new InternetAddress(sendEmailAccount, emailName, "UTF-8"));
        // 3. To: 收件人（可以增加多个收件人、抄送、密送）
        if(receiveMailAccount!=null){
        	for(String receive:receiveMailAccount){
            message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(receive,receive, "UTF-8"));
        	}
        }
        Multipart multipart = new MimeMultipart();  
        BodyPart contentPart = new MimeBodyPart();  
        contentPart.setContent(text,"text/html; charset=UTF-8");
        contentPart.setHeader("Content-Type", "text/html; charset=UTF-8");  
        multipart.addBodyPart(contentPart); 
        /*添加附件*/ 
        if(StringUtils.isNotBlank(file)){
        	File usFile = new File(file);  
        	MimeBodyPart fileBody = new MimeBodyPart();  
        	DataSource source = new FileDataSource(file);  
        	fileBody.setDataHandler(new DataHandler(source));  
        //	sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();  
     	   String dfileName = new String((usFile.getName()).getBytes("utf-8"),"ISO8859-1"); 

        //	fileBody.setFileName("=?UTF-8?B?"  
          //          + enc.encode(dfileName.getBytes()) + "?=");
     	  fileBody.setFileName(dfileName); 
            multipart.addBodyPart(fileBody);
        }
        // Cc: 抄送
        
        if(ccMailAccount!=null){
            Address[] ccAdresses = new InternetAddress[ccMailAccount.length];
        	int i =0;
            for(String cc:ccMailAccount){
        		ccAdresses[i]=new InternetAddress(cc);
        		i++;
        	}
    		message.setRecipients(MimeMessage.RecipientType.CC, ccAdresses);
        }
        //使用Bese64编码邮件主题,防止乱码
        // 4. Subject: 邮件主题
        message.setSubject(MimeUtility.encodeText(title, "UTF-8", "B"));
        // 5. Content: 邮件正文（可以使用html标签）
        message.addHeader("charset", "UTF-8");  
        message.setContent(multipart); 
        // 6. 设置发件时间
        message.setSentDate(new Date());
        // 7. 保存设置
        message.saveChanges();
        return message;
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return null;
    }
}
