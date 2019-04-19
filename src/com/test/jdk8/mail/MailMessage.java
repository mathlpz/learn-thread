package com.test.jdk8.mail;

import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeUtility;
import java.util.Properties;

/**
 * @Author: lpz
 * @Date: 2019-04-03 14:38
 */
public class MailMessage {


    public static void getMailTest() throws Exception {

        //    String smtpHost = "smtp.qq.com";
    String protocol = "pop3";
    String port = "995";
    String pop3Server = "pop.qq.com";
    String username = "670077051@qq.com";
    String password = "ojwctfnixdusbfih";

//        String pop3Server = "pop3.126.com"; // 收件服务器 
//        String protocol = "pop3";
//        String port = "995"; // 邮件服务端口号 995
//        String username = "mathlpz@126.com"; // 邮件地址 
//        String password = "mathlpz126@#"; // 授权码OR密码
        //
        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", protocol); // 使用的协议（JavaMail规范要求）
        props.setProperty("mail.smtp.host", pop3Server); // 发件人的邮箱的 SMTP服务器地址
        props.setProperty("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail.pop3.socketFactory.fallback", "true");
        props.setProperty("mail.pop3.socketFactory.port", port);
        // 获取连接
        Session session = Session.getDefaultInstance(props);
        session.setDebug(false);
        // 获取Store对象
        Store store = session.getStore(protocol);
        store.connect(pop3Server, username, password); // POP3服务器的登陆认证

        // 通过POP3协议获得Store对象调用这个方法时，邮件夹名称只能指定为"INBOX"
        Folder folder = store.getFolder("INBOX");// 获得用户的邮件帐户
        folder.open(Folder.READ_WRITE); // 设置对邮件帐户的访问权限
        // Message[] messages = search(folder);
        Message[] messages = folder.getMessages();
        // MailAnalysisUtil pmm = null;
        System.out.println("邮件大小数" + messages.length);
        for (int i = 0; i < messages.length; i++) {
            System.out.println("解析第" + i + "个邮件");
            Message msg = messages[i];

            String subject = msg.getSubject();
            System.out.println("主题：" + subject + "<br/>");

            if (msg.isMimeType("multipart/alternative")) {
                Multipart mp = (Multipart) msg.getContent();
                int bodynum = mp.getCount();
                for (int j = 0; j < bodynum; j++) {
                    if (mp.getBodyPart(j).isMimeType("text/html")) {
                        String content = (String) mp.getBodyPart(j).getContent();
                        System.out.println("邮件内容：" + content);
                    } else {
                        System.out.println("--- 邮件格式1？：" + mp.getBodyPart(j).getContentType());
                        System.out.println("--- 邮件格式2？：" + mp.getBodyPart(j).getContent());

                    }
                }
            }

            Multipart mp = (Multipart) msg.getContent();
            for (int j = 0; j < mp.getCount(); j++) {
                BodyPart bp = mp.getBodyPart(j);
                if (bp.getDisposition() != null) {
                    String fileName = bp.getFileName();
                    if (fileName.startsWith("=?")) {
                        fileName = MimeUtility.decodeText(fileName); //需要解析中文名称的文件名(将中文名称加入邮件时用encodeText)  
                    }
                    System.out.println("附件：" + fileName);
                }
            }

        }
        folder.close(false); // 关闭邮件夹对象
        store.close(); // 关闭连接对象

    }


    public static void main(String[] args) {
        try {
            getMailTest();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}


