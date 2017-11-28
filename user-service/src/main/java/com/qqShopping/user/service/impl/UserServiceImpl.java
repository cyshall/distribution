package com.qqShopping.user.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qqShopping.user.Dao.UserDao;
import com.qqShopping.user.Model.User;
import com.qqShopping.user.service.UserService;
import com.qqShopping.user.util.RedisUtil;

import redis.clients.jedis.Jedis;

public class UserServiceImpl implements UserService{

	@Autowired
	private UserDao userdao;
	
	public List<String> getName() {
		List<String> list=new ArrayList<String>();
		list.add("1");
		list.add("2");
		list.add("3");
//		List<String> list=RedisUtil.getList("userList");
//		if (list.size()==0) {
//			list=userdao.getUser();
//			System.out.println("-----数据来自数据库--------");
//			for (String string : list) {
//				RedisUtil.setList("userList", string);
//				System.out.println(string);
//			}
//		}else{
//			System.out.println("------数据来自redis-------");
//			for (String string : list) {
//				System.out.println(string);
//			}
//		}
		return list;
	}

}
