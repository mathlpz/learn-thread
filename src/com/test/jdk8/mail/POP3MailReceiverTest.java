package com.test.jdk8.mail;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeUtility;
import javax.mail.search.AndTerm;
import javax.mail.search.ComparisonTerm;
import javax.mail.search.FromStringTerm;
import javax.mail.search.SearchTerm;
import javax.mail.search.SentDateTerm;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 邮件接受测试
 */
public class POP3MailReceiverTest {

    private Logger logger = LoggerFactory.getLogger(POP3MailReceiverTest.class);


//    String FILE_PATH = "G:/email/";
    String FILE_PATH = "/home/tempFile/email/";

    // 执行日期标识
    String dateTimeFlag;

    // 搜索有误差
//    String hostServer = "******"; // 收件服务器 
//    String protocol = "pop3";
//    String port = "995"; // 邮件服务端口号 995
//    String username = "***"; // 邮件地址 
//    String password = "******"; // 授权码OR密码

    String hostServer = "******"; // 收件服务器 
    String protocol = "imap";
    String port = "993"; //
    String username = "******"; // 邮件地址 
    String password = "******"; // 授权码OR密码

    String fromEmail = "************"; // 发件人


    /**
     * 获取SMTP默认配置，发件
     *
     * @return
     */
    public Properties getSMTP() {
        Properties p = new Properties();
        p.setProperty("mail.smtp.host", "smtp.qq.com"); // 按需要更改
        p.setProperty("mail.smtp.protocol", "smtp");
        p.setProperty("mail.smtp.port", "465");
        p.setProperty("mail.smtp.auth", "true");
        // SSL安全连接参数
        p.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        p.setProperty("mail.smtp.socketFactory.fallback", "false");
        p.setProperty("mail.smtp.socketFactory.port", "465");
        return p;
    }

    /**
     * 获取POP3收信配置 995
     *
     * @return
     */
    public Properties getPOP3() {
        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", protocol);
        props.setProperty("mail.pop3.host", hostServer); // 按需要更改
        props.setProperty("mail.pop3.port", port);
        // SSL安全连接参数
        props.setProperty("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail.pop3.socketFactory.fallback", "false");
        props.setProperty("mail.pop3.socketFactory.port", port);
        // 解决DecodingException: BASE64Decoder: but only got 0 before padding character (=)
        props.setProperty("mail.mime.base64.ignoreerrors", "true");
        return props;
    }

    /**
     * 获取IMAP收信配置 993
     *
     * @return
     */
    public Properties getIMAP() {
        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", protocol);
        props.setProperty("mail.imap.host", hostServer); // 按需要更改
        props.setProperty("mail.imap.port", port);
        // SSL安全连接参数
        props.setProperty("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail.imap.socketFactory.fallback", "false");
        props.setProperty("mail.imap.socketFactory.port", port);
        props.setProperty("mail.mime.base64.ignoreerrors", "true");
        return props;
    }


    /**
     *
     */
    public POP3MailReceiverTest() {
        try {
            // 1. 设置连接信息, 生成一个 Session
            // 获取连接
            Session session;
            if ("pop3".equalsIgnoreCase(protocol)) {
                session = Session.getDefaultInstance(getPOP3());
            } else {
                session = Session.getDefaultInstance(getIMAP());
            }
            session.setDebug(false);
            // 2. 获取Store, 并连接到服务器
            Store store = session.getStore(protocol);
            store.connect(hostServer, username, password); // POP3服务器的登陆认证
            //
            Folder defaultFolder = store.getDefaultFolder();// 默认父目录
            if (defaultFolder == null) {
                logger.error("服务器不可用");
                return;
            }
            //
            Folder folder = defaultFolder.getFolder("INBOX");// 获取收件箱
            folder.open(Folder.READ_WRITE);// 可读邮件,可以删邮件的模式打开目录

            // 取出来邮件数
            int msgCountAll = folder.getMessageCount();
            int newMessageCount = folder.getNewMessageCount();
            int unreadMessageCount = folder.getUnreadMessageCount();
            logger.info("共有邮件:" + msgCountAll + "封, 共有新邮件:" + newMessageCount + "封,,, 未读邮件：" + unreadMessageCount);
            URLName urlName = folder.getURLName();
            logger.info("folder urlName...: " + urlName.toString());
            //
            searchMails(folder);
            // 7. 关闭 Folder 会真正删除邮件, false 不删除
            folder.close(false);
            // 8. 关闭 store, 断开网络连接
            store.close();
        } catch (NoSuchProviderException e) {
            logger.error(e.getMessage(), e);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }


    private void searchMails(Folder folder) throws Exception {
        // 4. 列出来收件箱 下所有邮件
//            Message[] messages = folder.getMessages();

        //建立搜索条件FlagTerm，这里FlagTerm继承自SearchTerm，也就是说除了获取未读邮件的条件还有很多其他条件同样继承了SearchTerm的条件类，像根据发件人，主题搜索等，
        // 还有复杂的逻辑搜索类似：
//            SearchTerm orTerm = new AndTerm(new SearchTerm[] {new FromStringTerm("mathlpz@126.com"),
//                    new SubjectTerm("把AI带回家-小荷音箱商用体验官征集活动")});
//            FlagTerm ft = new FlagTerm(new Flags(Flags.Flag.SEEN), false); //false代表未读，true代表已读
        // 搜索昨天收到的的所有邮件
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date start = calendar.getTime();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date end = calendar.getTime();
        System.out.println(DateFormatUtils.format(start, "yyyy-MM-dd HH:mm:ss"));
        System.out.println(DateFormatUtils.format(end, "yyyy-MM-dd HH:mm:ss"));
        SearchTerm comparisonTermGe = new SentDateTerm(ComparisonTerm.GE, start);
        SearchTerm comparisonTermLe = new SentDateTerm(ComparisonTerm.LE, end);
        FromStringTerm fromStringTerm = new FromStringTerm(fromEmail);
//            FromStringTerm fromStringTerm = new FromStringTerm("hy_jira_1@no-reply.com");
        SearchTerm andTerm = new AndTerm(new SearchTerm[]{comparisonTermGe, comparisonTermLe, fromStringTerm});
//            SearchTerm andTerm = new AndTerm(comparisonTermGe, comparisonTermLe);

        //
        dateTimeFlag = DateFormatUtils.format(new Date(), "yyyyMMdd");
        Message[] messages = folder.search(andTerm); //根据设置好的条件获取message
        logger.info("search邮件: " + messages.length + "封, SearchTerm:" + andTerm.getClass());
        // FetchProfile fProfile = new FetchProfile();// 选择邮件的下载模式,
        // 根据网速选择不同的模式
        // fProfile.add(FetchProfile.Item.ENVELOPE);
        // folder.fetch(messages, fProfile);// 选择性的下载邮件
        // 5. 循环处理每个邮件并实现邮件转为新闻的功能
        for (int i = 0; i < messages.length; i++) {
            // 单个邮件
            logger.info("第" + i + "邮件开始------------");
            mailReceiver(messages[i]);
            logger.info("第" + i + "邮件结束------------");
            //  邮件读取用来校验
//            messages[i].writeTo(new FileOutputStream(FILE_PATH + "pop3Mail_" + messages[i].getMessageNumber() + ".eml"));
        }
    }


    /**
     * 解析邮件
     *
     * @param msg 邮件对象
     * @return
     * @throws IOException
     * @throws MessagingException
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     */
    private void mailReceiver(Message msg) throws Exception {
        // 发件人信息
        Address[] froms = msg.getFrom();
        String mailSubject = transferChinese(msg.getSubject());
        if (froms != null) {
            InternetAddress addr = (InternetAddress) froms[0];
            logger.info("发件人地址:" + addr.getAddress() + ", 发件人显示名:" + transferChinese(addr.getPersonal()));
        } else {
            logger.error("msg.getFrom() is null... subject:" + mailSubject);
        }
        Date sentDate = msg.getSentDate();
        logger.info("邮件主题:" + mailSubject + ", sentDate:" + (sentDate == null ? null : DateFormatUtils.format(sentDate, "yyyy-MM-dd HH:mm:ss")));

        // getContent() 是获取包裹内容, Part相当于外包装
        Object content = msg.getContent();
        if (content instanceof Multipart) {
            Multipart multipart = (Multipart) content;
            reMultipart(multipart);
        } else if (content instanceof Part) {
            Part part = (Part) content;
            rePart(part);
        } else {
            String contentType = msg.getContentType();
            if (contentType != null && contentType.startsWith("text/html")) {
                logger.warn("---类型:" + contentType);
            } else {
                logger.warn("---类型:" + contentType);
                logger.warn("---内容:" + msg.getContent());
            }
        }
    }

    /**
     * 把邮件主题转换为中文.
     *
     * @param strText the str text
     * @return the string
     */
    public String transferChinese(String strText) {
        try {
            if (StringUtils.isBlank(strText)) {
                return null;
            }
            strText = MimeUtility.encodeText(new String(strText.getBytes(),
                    "UTF-8"), "UTF-8", "B");
            strText = MimeUtility.decodeText(strText);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return strText;
    }


    /**
     * @param part 解析内容
     * @throws Exception
     */
    private void rePart(Part part) {
        String tempFilePath = "";
        try {
            // 附件
            if (part.getDisposition() != null) {
                // 邮件附件
                String strFileName = MimeUtility.decodeText(part.getFileName()); //MimeUtility.decodeText解决附件名乱码问题
                logger.info("发现附件: {}, 内容类型: {} ", strFileName, MimeUtility.decodeText(part.getContentType()));
                // 读取附件字节并存储到文件中. xls/xlsx
                String fileType = strFileName.substring(strFileName.lastIndexOf(".") + 1);
                if ((fileType.equals("xlsx") || fileType.equals("xls")) &&
                        (strFileName.contains("浙江重复未报备加黑端口明细") || strFileName.contains("加黑"))) {
                    InputStream in = part.getInputStream();// 打开附件的输入流
                    tempFilePath = FILE_PATH + dateTimeFlag + strFileName;
                    FileOutputStream out = new FileOutputStream(tempFilePath);
                    int data;
                    while ((data = in.read()) != -1) {
                        out.write(data);
                    }
                    in.close();
                    out.close();
                } else {
                    logger.info("not what we need file, discard it: {}", strFileName);
                }
            } else {
                // 邮件内容
                if (part.getContentType().startsWith("text/plain") || part.getContentType().startsWith("Text/Plain")) {
                    logger.info("文本内容：" + part.getContent());
                } else if (part.getContentType().startsWith("text/html")) {
//                    logger.info("HTML内容：" + part.getContent());
                    logger.debug("HTML内容，不记录日志展示。。");
                } else {
                    logger.debug("!其它ContentType:" + part.getContentType() + ",, ?内容：" + part.getContent());
                }
            }
        } catch (MessagingException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (StringUtils.isNotBlank(tempFilePath)) {
                logger.info("哈哈哈哈，，，处理下载好的Excel文件：{}", tempFilePath);
            }
        }
    }


    /**
     * @param multipart // 接卸包裹（含所有邮件内容(包裹+正文+附件)）
     * @throws Exception
     */
    private void reMultipart(Multipart multipart) throws Exception {
        logger.info("邮件共有" + multipart.getCount() + "部分组成");
        // 依次处理各个部分
        for (int j = 0, n = multipart.getCount(); j < n; j++) {
            //System.out.println("处理第" + j + "部分");
            Part part = multipart.getBodyPart(j);
            // 解包, 取出 MultiPart的各个部分, 每部分可能是邮件内容, 也可能是另一个小包裹(MultipPart)
            // 判断此包裹内容是不是一个小包裹, 一般这一部分是 正文 Content-Type: multipart/alternative
            if (part.getContent() instanceof Multipart) {
                logger.info("部分" + j + "的ContentType:" + part.getContentType() + ", to reMultipart() ");
                Multipart p = (Multipart) part.getContent();// 转成小包裹
                //递归迭代
                reMultipart(p);
            } else {
                logger.info("部分" + j + "的ContentType:" + part.getContentType() + ", to rePart() ");
                rePart(part);
            }
        }
    }

    public static void dealStringTest(){
        int availProcessors = Runtime.getRuntime().availableProcessors();
        System.out.println("avail processors count: " + availProcessors);

        String strFileNmae = "浙江重复未报备加黑端口明细4.10.xlsx";
        String fileType = strFileNmae.substring(strFileNmae.lastIndexOf(".") + 1);
        System.out.println(fileType);
        System.out.println(strFileNmae);

        String strFileNmae2 = "浙江重复未报备加黑端口明细";
        String fileType2 = strFileNmae2.substring(strFileNmae2.lastIndexOf(".") + 1);
        System.out.println(fileType2);

        //
        List<String> list = new ArrayList<>();
        list.add("111");
        list.add("222");
        list.add("333");
        Cache<String, Object> xinAnBlacklistCache = CacheBuilder.newBuilder().build();
        xinAnBlacklistCache.put("key", list);
        System.out.println(xinAnBlacklistCache.asMap());

        list.add("444");
        list.add("555");
        System.out.println(xinAnBlacklistCache.asMap());

    }


    public static void dealSetTest(){
        Set<String> resultSet = new HashSet<>();
        resultSet.add("111");
        resultSet.add("222");
        resultSet.add("333");
        resultSet.add("444");

        resultSet.add("1112");
        resultSet.add("2223");
        resultSet.add("22234");

        // 1、判断自己内部是否有扩展号
        Set<String> remove1 = new HashSet<>();
//        Iterator<String> iterator = resultSet.iterator();
        resultSet.forEach(code -> {
            for (String result : resultSet) {
                // 扩展号
                if (!code.equals(result) && code.startsWith(result)) {
                    remove1.add(code);
                    break;
                }
            }
        });
        System.out.println(remove1);
        resultSet.removeAll(remove1);
        System.out.println(resultSet);

        AtomicInteger atomicInteger = new AtomicInteger();
        CountDownLatch countDownLatch = new CountDownLatch(3);

    }


    public static void returnTest(){
        Set<String> resultSet = new HashSet<>();
        resultSet.add("111");
        resultSet.add("222");
        for (String result : resultSet) {
            // 扩展号
            if (result.endsWith("11")) {
                break;
//                return;
            }
        }
        System.out.println("hai you shei???");
    }


    /**
     * @param args
     */
    public static void main(String[] args) {
        //
//        new POP3MailReceiverTest();

        System.out.println("--------------------------------------------------");

//        dealStringTest();

        dealSetTest();

        System.out.println("--------------------------------------------------");

        returnTest();

        System.out.println("--------------------------------------------------");



        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}