package com.gdu.cashbook;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@SpringBootApplication
// @SpringBootApplication == @Configuration + @EnableAutoConfiguration + @ComponetScan
@PropertySource("classpath:google.properties")

public class CashbookApplication {
	@Value("${google.username}")
	public String username;
	@Value("${google.password}")
	public String password;
	
	public static void main(String[] args) {
		SpringApplication.run(CashbookApplication.class, args);
	}
	
	@Bean
	public JavaMailSender getJavaMailSender() {
		JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
		javaMailSender.setHost("smtp.gmail.com"); // 메일서버이름을 적는다  구글 stmp사용법을 검색하면 설명서가나온다
		javaMailSender.setPort(587);
		//System.out.println(username);
		//System.out.println(password);
		javaMailSender.setUsername(username);
		javaMailSender.setPassword(password);
		
		Properties prop = new Properties(); // properties ==HashMap 과 같지만 <String, String> 둘다 String으로 들어오게만들수있다 
		prop.setProperty("mail.smtp.auth", "true");
		prop.setProperty("mail.smtp.starttls.enable", "true");
		javaMailSender.setJavaMailProperties(prop);
		return javaMailSender;
	}

}
