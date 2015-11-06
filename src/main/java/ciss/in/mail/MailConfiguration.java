package ciss.in.mail;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
@PropertySource("classpath:mail.properties")
public class MailConfiguration {

    @Value("${mail.protocol}")
    private String protocol;
    
    @Value("${mail.host}")
    private String host;
    
    @Value("${mail.port}")
    private int port;
    
    @Value("${mail.smtp.auth}")
    private boolean auth;

/*    @Value("${mail.smtp.socketFactory.class}")
    private String socketClass;
    
    @Value("${mail.smtp.socketFactory.fallback}")
    private String socketFallback;

    @Value("${mail.smtp.socketFactory.port}")
    private String socketPort;

    @Value("${mail.smtp.startssl.enable}")
    private String socketEnable;*/

    //@Value("${mail.smtps.ssl.enable}")
    //private boolean ssl;
    
    @Value("${mail.smtp.starttls.enable}")
    private boolean starttls;
    
    @Value("${mail.from}")
    private String from;
    @Value("${mail.username}")
    private String username;
    @Value("${mail.password}")
    private String password;

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        Properties mailProperties = new Properties();
/*        mailProperties.put("mail.smtp.socketFactory.class", socketClass);
        mailProperties.put("mail.smtp.socketFactory.fallback", socketFallback);
        mailProperties.put("mail.smtp.socketFactory.port", socketPort);
        mailProperties.put("mail.smtp.startssl.enable", socketEnable);
        mailProperties.put("mail.smtp.auth", auth);*/
        //mailProperties.put("mail.smtps.ssl.enable", ssl);
        mailProperties.put("mail.smtp.starttls.enable", starttls);
        mailProperties.put("mail.from", from);
        mailSender.setJavaMailProperties(mailProperties);
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setProtocol(protocol);
        mailSender.setUsername(username);
        mailSender.setPassword(password);

        return mailSender;
    }
}