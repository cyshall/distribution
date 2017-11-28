package com.qqShopping.user.starter;

import java.io.IOException;

import org.springframework.context.support.ClassPathXmlApplicationContext;


public class starterMain {
	
	public static void main(String[] args) throws IOException {
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(new String[]{"spring/applicationContext-dao.xml","spring/applicationContext-dubbo.xml"});
		applicationContext.start();
		System.in.read();
	}
}
	 