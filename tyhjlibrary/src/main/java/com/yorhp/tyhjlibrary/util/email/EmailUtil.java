package com.yorhp.tyhjlibrary.util.email;


import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

/**
 * Created by _Tyhj on 2016/7/31.
 */
public class EmailUtil {
    private static final String from = "tyhj@tyhj5.com";
    private static final String host = "smtp.tyhj5.com";
    private static final boolean isSSL = true;
    private static final int port = 25;
    private static final String username = "tyhj@tyhj5.com";
    private static final String password= "Han123456";

    public static void sendEmail(String emailnumber, String msg){
        //发送邮件
            try {
                Email email = new SimpleEmail();
                //email.setSSLOnConnect(isSSL);
                email.setHostName(host);
                email.setSmtpPort(port);
                email.setAuthentication(username, password);
                email.setFrom(from);
                email.addTo(emailnumber);
                email.setSubject("baseApp邮箱验证");
                email.setMsg(msg);
                email.send();
            } catch (EmailException e) {
                e.printStackTrace();
            }
            System.out.println("发送完毕！");
    }
}
