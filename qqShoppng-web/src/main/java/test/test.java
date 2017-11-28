package test;

import java.util.List;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.qqShopping.user.Model.User;
import com.qqShopping.user.service.UserService;

public class test {
	public static void main(String[] args) {
		ClassPathXmlApplicationContext applicationContext=new ClassPathXmlApplicationContext("spring/applicationContext-dubbo.xml");
		applicationContext.start();
		UserService service=(UserService) applicationContext.getBean("userService");
		List<String> users=service.getName();
		for (String user : users) {
			System.out.println(user);
		}
	}

}
