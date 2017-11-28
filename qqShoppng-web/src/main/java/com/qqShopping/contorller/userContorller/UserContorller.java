package com.qqShopping.contorller.userContorller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.qqShopping.user.service.UserService;

@Controller
@RequestMapping("user")
public class UserContorller {

	@Autowired
	UserService userService;
	
	@RequestMapping("getName")
	public String getName(Model model){
		List<String> users=userService.getName();
		model.addAttribute("users",users);
		return "";
	}
}
